package com.collection.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IAppUserCenterMapper;
import com.collection.dao.IAppVipCardMapper;
import com.collection.dao.IManageBackStageMapper;
import com.collection.dao.IRushBuyClacMapper;
import com.collection.dao.ISystemMapper;
import com.collection.service.IRushBuyClacService;
import com.collection.sms.sendSms;
import com.collection.util.Constants;

/**
 * 定时抢购功能相关操作
 * @author silence
 *
 */
public class RushBuyClacServiceImpl implements IRushBuyClacService{
	
	@Autowired IRushBuyClacMapper rushBuyClacMapper;
	
	@Autowired IAppUserCenterMapper appUserCenterMapper;
	
	@Autowired IAppVipCardMapper appVipCardMapper;
	
	@Autowired ISystemMapper systemMapper;
	
	@Autowired IManageBackStageMapper manageBackStageMapper ;
	
	private Logger logger = Logger.getLogger(RushBuyClacServiceImpl.class);

	/**
	 * 新增系统通知信息
	 */
	public void insertNotice(String title, String message, int status){
		Map<String, Object> notice = new HashMap<String, Object>();
		notice.put("title", title);
		notice.put("message", message);
		notice.put("status", status);
		notice.put("createtime", new Date());
		this.systemMapper.insertNotice(notice);
	}
	
	/**
	 * 新增用户通知信息（系统不发短信）
	 */
	public void insertSystemUserNotice(String title, String message, String userid){
		Map<String, Object> notice = new HashMap<String, Object>();
		notice.put("title", title);
		notice.put("message", message);
		notice.put("userid", userid);
		notice.put("createtime", new Date());
		this.systemMapper.insertUserNotice(notice);
	}
	
	/**
	 * 新增用户通知信息
	 */
	public void insertUserNotice(String title, String sysMessage, String message, String userid, String phone){
		Map<String, Object> notice = new HashMap<String, Object>();
		notice.put("title", title);
		notice.put("message", sysMessage);
		notice.put("userid", userid);
		notice.put("createtime", new Date());
		this.systemMapper.insertUserNotice(notice);
		// 发送短信通知
		try {
			sendSms.sendSms(phone, message);
		} catch (Exception e) {
			logger.info("短信发送错误"+e.getMessage());
		}
	}
	
	@Override
	public void rushBuyCalc(Map<String, Object> data) {
		//获取会员卡信息，且计算当前计算时间距离抢购开始时间的分钟数
		Map<String, Object> cardInfo = this.rushBuyClacMapper.getCardInfo(data);
		//查询当前会员卡对应的所有手办
		List<Map<String, Object>> garageKitList = this.rushBuyClacMapper.getGarageKitListByCard(cardInfo);
		logger.info("当前抢购的会员卡信息：" + cardInfo.toString());
		//获取抢购的人数
		List<Map<String, Object>> userlist = this.rushBuyClacMapper.getRushBuyUser(data);
		if (userlist == null || userlist.isEmpty()) {
			insertNotice("抢购"+cardInfo.get("typename")+"分配失败", data.get("calctime")+"获取参与抢购的人数为0：此次会员卡抢购失败", Constants.FAILED);
			return;
		}
		insertNotice("抢购"+cardInfo.get("typename"), data.get("calctime")+"此次共有"+userlist.size()+"用户参与分配抢购"+cardInfo.get("typename"), Constants.SUCCESS);
		//获取待出售的会员卡订单数量
		List<Map<String, Object>> orderlist = this.rushBuyClacMapper.getWaitSellCardOrder(data);
		logger.info("获取待出售的会员卡订单数量：" + orderlist.toString());
		insertNotice("抢购"+cardInfo.get("typename"), cardInfo.get("typename")+":"+data.get("calctime")+"获取待出售订单数量为"+orderlist.size(), Constants.SUCCESS);
		//定义查询所有系统用户集合
		List<Map<String, Object>> sysUserList = new ArrayList<Map<String,Object>>();
		//获取多次结算的每次结算的抢购成功率
		List<Map<String, Object>> ratelist = this.rushBuyClacMapper.getConfigRate();
		//根据当前计算时间匹配当前计算是第第几批结算以及抢购成功率
		double rushrate = 0d;
		//是否是最后一次结算时间：1 是 0：否
		int lastflag = 0;
		for (Map<String, Object> r: ratelist) {
			if(r.get("minute").toString().equals(cardInfo.get("minutenum").toString())){
				logger.info("当前计算为"+cardInfo.get("typename")+"抢购的第"+r.get("forder")+"次结算，成功几率为："+r.get("rate")+"%");
				insertNotice("抢购"+cardInfo.get("typename"), cardInfo.get("typename")+":"+data.get("calctime")+"当前计算为"+cardInfo.get("typename")+"抢购的第"+r.get("forder")+"次结算，成功几率为："+r.get("rate")+"%", Constants.SUCCESS);
				rushrate = Double.parseDouble(r.get("rate").toString());
				lastflag = Integer.parseInt(r.get("lastflag").toString());
				break;
			}
		}
		if (rushrate == 0) {
			logger.info("当前计算时间非当日结算时间 不参与抢购分配业务，当前时间："+new Date()+",距会员卡抢购时间分钟数："+cardInfo.get("minutenum"));
			insertNotice("抢购"+cardInfo.get("typename"), cardInfo.get("typename")+":"+data.get("calctime")+"当前计算时间非当日结算时间 不参与抢购分配业务，当前时间："+new Date(), Constants.FAILED);
			return;
		}
		//定义随机发会员卡的用户集合
		List<Map<String, Object>> indexlist = new ArrayList<Map<String, Object>>();
		//定义随机发会员卡的用户个数
		int rushnum = 0;
		//参与抢购人数小于等于3人且为最后一次结算时时 ，直接全部抢购成功 前面两次还会各抢购成功一个 所以总数低于5个会总抢购成功
		if (userlist != null && userlist.size() <= 3 && lastflag == 1) {
			rushnum = userlist.size();
		} else {
			/**
			 * 抢购会员卡
			 */
			//随机按成功率 筛选出 幸运的 用户发放会员卡
			rushnum = (int) (userlist.size() * rushrate / 100d);
			//为0时至少有一个用户抢中
			if(rushnum == 0) {
				rushnum = 1;
			}
		}
		logger.info("算出的能抢到会员卡的用户个数："+rushnum);
		Random random = new Random();
		for (int i = 0; i < rushnum; i++){
			//从参与抢购的集合中取随机的一个下标
			int index = random.nextInt(userlist.size());
			indexlist.add(userlist.get(index));
			userlist.remove(index);
		}
		logger.info("算出的能抢到会员卡的用户："+indexlist.toString());
		int allcount = 0;
		//循环遍历发放会员卡
		for (int i=0; i < indexlist.size(); i++){
			Map<String, Object> orderMap = new HashMap<String, Object>();
			orderMap.put("buyuserid", indexlist.get(i).get("userid"));
			if(i >= orderlist.size()) {
				try {
					/**
					 * 出售系统会员卡
					 */
					//查询所有系统用户
					sysUserList = this.rushBuyClacMapper.getSysUser(indexlist.get(i));
					//从系统用户中随机取一个用户新增一张待出售的会员卡
					int index = random.nextInt(sysUserList.size());
					Map<String, Object> orderInfo = new HashMap<String, Object>();
					orderInfo.put("buyuserid", indexlist.get(i).get("userid"));
					orderInfo.put("selluserid", sysUserList.get(index).get("userid"));
					orderInfo.put("type", 1);
					//查询一个当天的订单数据加一
					int ordernum = appVipCardMapper.getOrderNum();
					//生成订单号
					orderInfo.put("ordernum", AppVipCardServiceImpl.generateUniqueKey() + ordernum);
					orderInfo.put("cardid", cardInfo.get("cardid"));
					//随机获取一个 当前价位的手办
					int kitindex = random.nextInt(garageKitList.size());
					orderInfo.put("kitid", garageKitList.get(kitindex).get("kitid"));
					//获取最大最小价格的价差spread 和最小价格
					double minprice = Double.parseDouble(cardInfo.get("minprice").toString());
					double spread = Double.parseDouble(cardInfo.get("spread").toString());
					//最小价格加一个随机价差作为会员卡价格
					double cardprice = minprice + random.nextInt((int)spread);
					orderInfo.put("cardprice", cardprice);
					orderInfo.put("selltime", cardInfo.get("selltime"));
					
					//买家信息如下
					orderInfo.put("buyuserid", indexlist.get(i).get("userid"));
					orderInfo.put("status", 1);
					//根据买家等级和会员卡收益率和会员卡价格计算一个收益价格profitprice
					double yield = Double.parseDouble(cardInfo.get("yield").toString());
					double interesttimes = Double.parseDouble(indexlist.get(i).get("interesttimes").toString());
					double profitprice = cardprice * yield * interesttimes / 100;
					orderInfo.put("profitprice", profitprice);
					//根据抢购时间和出售所需天数计算一个到期时间duetime
					orderInfo.put("commentstartdays", cardInfo.get("commentstartdays"));
					orderInfo.put("rushtime", new Date());
					orderInfo.put("createtime", new Date());
					this.appVipCardMapper.insertOrder(orderInfo);
					
					//用户抢购记录置为抢购成功
					Map<String, Object> rushToBuyMap = new HashMap<String, Object>();
					rushToBuyMap.put("cardid", cardInfo.get("cardid"));
					rushToBuyMap.put("userid", indexlist.get(i).get("userid"));
					rushToBuyMap.put("aftstatus", 2);
					rushToBuyMap.put("befstatus", 1);
					this.rushBuyClacMapper.updateRushToBuy(rushToBuyMap);
					//用户扣除抢购所需XGO币
					rushToBuyMap.put("xgocoin", cardInfo.get("xgocoin"));
					this.rushBuyClacMapper.deductionXgo(rushToBuyMap);
					//新增到xgo记录表
					rushToBuyMap.put("createtime", new Date());
					rushToBuyMap.put("type", 2);
					rushToBuyMap.put("remark", "恭喜您抢购"+garageKitList.get(kitindex).get("title")+"成功，使用"+cardInfo.get("xgocoin")+"个xgo币");
					this.appUserCenterMapper.addXgoRecord(rushToBuyMap);
					//给买家提示通知
					insertUserNotice("抢购"+garageKitList.get(kitindex).get("title"), Constants.sysSmsTranslate3.replace("price", cardprice+"").replace("typename", garageKitList.get(kitindex).get("title").toString()), Constants.smsTranslate3.replace("typename", garageKitList.get(kitindex).get("title").toString()), indexlist.get(i).get("userid").toString(), indexlist.get(i).get("phone").toString());
					//给卖家提示通知
					insertSystemUserNotice("出售系统"+garageKitList.get(kitindex).get("title"), "恭喜您出售价值"+cardprice+"元的"+garageKitList.get(kitindex).get("title")+"成功", sysUserList.get(index).get("userid").toString());
					//系统提示
					insertNotice("抢购"+garageKitList.get(kitindex).get("title"), garageKitList.get(kitindex).get("title")+":"+data.get("calctime")+"恭喜用户"+indexlist.get(i).get("nickname")+"抢购价值"+cardprice+"元的"+garageKitList.get(kitindex).get("title")+"成功", Constants.SUCCESS);
					allcount ++;
				} catch (Exception e) {
					logger.error(e.getMessage());
					insertNotice("抢购"+cardInfo.get("typename"), cardInfo.get("typename")+":"+data.get("calctime")+"用户"+indexlist.get(i).get("nickname")+"抢购失败", Constants.FAILED);
					continue;
				}
			} else {
				try {
					//分配待出售会员卡给用户的情况
					orderMap.put("orderid", orderlist.get(i).get("orderid"));
					//当抢购人员分配到自己正在出售的会员卡此等极端情况 直接不给他分配了 当做他没抢到
					if(indexlist.get(i).get("userid").equals(orderlist.get(i).get("selluserid"))) {
						logger.info("当抢购时分配到到自己正在出售的会员卡，就不分配了 userid:"+indexlist.get(i).get("userid"));
						insertNotice("抢购"+cardInfo.get("typename"), orderlist.get(i).get("title")+":"+data.get("calctime")+"-"+indexlist.get(i).get("nickname")+"抢购时分配到到自己正在出售的会员卡：该用户抢购失败", Constants.FAILED);
						continue;
					}
					orderMap.put("status", 1);
					//根据买家等级和会员卡收益率和会员卡价格计算一个收益价格profitprice
					double cardprice = Double.parseDouble(orderlist.get(i).get("cardprice").toString());
					double yield = Double.parseDouble(cardInfo.get("yield").toString());
					double interesttimes = Double.parseDouble(indexlist.get(i).get("interesttimes").toString());
					double profitprice = cardprice * yield * interesttimes / 100;
					orderMap.put("profitprice", profitprice);
					//根据抢购时间和出售所需天数计算一个到期时间duetime
					orderMap.put("commentstartdays", cardInfo.get("commentstartdays"));
					orderMap.put("rushtime", new Date());
					this.rushBuyClacMapper.updateOrder(orderMap);
					
					//用户抢购记录置为抢购成功
					Map<String, Object> rushToBuyMap = new HashMap<String, Object>();
					rushToBuyMap.put("cardid", cardInfo.get("cardid"));
					rushToBuyMap.put("userid", indexlist.get(i).get("userid"));
					rushToBuyMap.put("aftstatus", 2);
					rushToBuyMap.put("befstatus", 1);
					this.rushBuyClacMapper.updateRushToBuy(rushToBuyMap);
					//用户扣除抢购所需XGO币
					rushToBuyMap.put("xgocoin", cardInfo.get("xgocoin"));
					this.rushBuyClacMapper.deductionXgo(rushToBuyMap);
					//新增到xgo记录表
					rushToBuyMap.put("createtime", new Date());
					rushToBuyMap.put("type", 2);
					rushToBuyMap.put("remark", "恭喜您抢购"+orderlist.get(i).get("title")+"成功，使用"+cardInfo.get("xgocoin")+"个xgo币");
					this.appUserCenterMapper.addXgoRecord(rushToBuyMap);
					//给买家提示通知
					insertUserNotice("抢购"+orderlist.get(i).get("title"), Constants.sysSmsTranslate3.replace("price", cardprice+"").replace("typename", orderlist.get(i).get("title").toString()), Constants.smsTranslate3.replace("typename", orderlist.get(i).get("title").toString()), indexlist.get(i).get("userid").toString(), indexlist.get(i).get("phone").toString());
					//给卖家提示通知
					insertUserNotice("出售"+orderlist.get(i).get("title"), Constants.sysSmsTranslate1.replace("price", cardprice+"").replace("typename", orderlist.get(i).get("title").toString()), Constants.smsTranslate1.replace("typename", orderlist.get(i).get("title").toString()), orderlist.get(i).get("selluserid").toString(), orderlist.get(i).get("phone").toString());
					//系统通知
					insertNotice("抢购成功", cardInfo.get("typename")+":"+data.get("calctime")+"恭喜用户"+indexlist.get(i).get("nickname")+"抢购价值"+cardprice+"元的"+orderlist.get(i).get("title")+"成功", Constants.SUCCESS);
					allcount++;
				} catch (Exception e) {
					continue;
				}
			}
		}
		//最后一次结算时间把剩余的此次会员卡的所有的抢购记录置为抢购失败
		if (lastflag == 1) {
			Map<String, Object> rushMap = new HashMap<String, Object>();
			rushMap.put("cardid", cardInfo.get("cardid"));
			rushMap.put("aftstatus", 3);
			rushMap.put("befstatus", 1);
			//查询所有抢购中的用户
			List<Map<String, Object>> alluserlist = this.rushBuyClacMapper.selectRushToBuyUserid(rushMap);
			Date createtime = new Date();
			for (Map<String, Object> user: alluserlist){
				user.put("title", "抢购"+cardInfo.get("typename"));
				user.put("message", "很抱歉，本次参与抢购人数过多，"+cardInfo.get("typename")+"抢购失败，请再接再厉");
				user.put("type", 0 );
				user.put("createtime", createtime);
			}
			this.manageBackStageMapper.sendSysNotice(alluserlist);
			//全部状态置为抢购失败
			this.rushBuyClacMapper.updateRushToBuy(rushMap);
			
			
			/**
			 * 最后一次结算包含一个系统回收订单逻辑 以下几个条件时系统回收
			 * 1、待出售订单数量orderlist大于分配能抢购成功人数indexlist（总人数的20%-30%）
			 * 2、待出售订单数量orderlist小于参与抢购的总人数userlist
			 * 3、随机分配给系统用户回收订单
			 */
			if (orderlist.size() > indexlist.size() && orderlist.size() <= userlist.size()) {
				for(int i = indexlist.size(); i < orderlist.size(); i++) {
					//查询所有系统用户
					sysUserList = this.rushBuyClacMapper.getSysUser(indexlist.get(i));
					//从系统用户中随机取一个用户回收手办
					int index = random.nextInt(sysUserList.size());
					Map<String, Object> orderMap = new HashMap<String, Object>();
					orderMap.put("buyuserid", sysUserList.get(index).get("userid"));
					orderMap.put("orderid", orderlist.get(i).get("orderid"));
					orderMap.put("status", 1);
					//根据买家等级和会员卡收益率和会员卡价格计算一个收益价格profitprice
					double cardprice = Double.parseDouble(orderlist.get(i).get("cardprice").toString());
					double yield = Double.parseDouble(cardInfo.get("yield").toString());
					double interesttimes = Double.parseDouble(sysUserList.get(index).get("interesttimes").toString());
					double profitprice = cardprice * yield * interesttimes / 100;
					orderMap.put("profitprice", profitprice);
					//根据抢购时间和出售所需天数计算一个到期时间duetime
					orderMap.put("commentstartdays", cardInfo.get("commentstartdays"));
					orderMap.put("rushtime", new Date());
					this.rushBuyClacMapper.updateOrder(orderMap);
					
					//给买家提示通知
					insertSystemUserNotice("抢购"+cardInfo.get("typename"), Constants.sysSmsTranslate3.replace("price", cardprice+"").replace("typename", orderlist.get(i).get("title").toString()), sysUserList.get(index).get("userid").toString());
					//给卖家提示通知
					insertUserNotice("出售"+cardInfo.get("typename"), Constants.sysSmsTranslate1.replace("price", cardprice+"").replace("typename", orderlist.get(i).get("title").toString()), Constants.smsTranslate1.replace("typename", orderlist.get(i).get("title").toString()), orderlist.get(i).get("selluserid").toString(), orderlist.get(i).get("phone").toString());
					//系统通知
					insertNotice("回收抢购", cardInfo.get("typename")+":"+data.get("calctime")+"系统用户【"+sysUserList.get(index).get("nickname")+"】回收分配价值"+cardprice+"元的"+orderlist.get(i).get("title")+"成功", Constants.SUCCESS);
					allcount++;
				}
			}
		}
		insertNotice("抢购"+cardInfo.get("typename"), cardInfo.get("typename")+":"+data.get("calctime")+"此次会员卡分配完成，共"+allcount+"个用户抢到会员卡", Constants.SUCCESS);
	}
}
