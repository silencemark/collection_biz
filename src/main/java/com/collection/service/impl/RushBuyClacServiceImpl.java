package com.collection.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IAppVipCardMapper;
import com.collection.dao.IRushBuyClacMapper;
import com.collection.dao.ISystemMapper;
import com.collection.service.IRushBuyClacService;
import com.collection.util.Constants;

/**
 * 定时抢购功能相关操作
 * @author silence
 *
 */
public class RushBuyClacServiceImpl implements IRushBuyClacService{
	
	@Autowired IRushBuyClacMapper rushBuyClacMapper;
	
	@Autowired IAppVipCardMapper appVipCardMapper;
	
	@Autowired ISystemMapper systemMapper;
	

	private Logger logger = Logger.getLogger(RushBuyClacServiceImpl.class);

	/**
	 * 新增通知信息
	 */
	public void insertNotice(String title, String message, int status){
		Map<String, Object> notice = new HashMap<String, Object>();
		notice.put("title", title);
		notice.put("message", message);
		notice.put("status", status);
		notice.put("createtime", new Date());
		this.systemMapper.insertNotice(notice);
	}
	
	@Override
	public void rushBuyCalc(Map<String, Object> data) {
		//获取会员卡信息，且计算当前计算时间距离抢购开始时间的分钟数
		Map<String, Object> cardInfo = this.rushBuyClacMapper.getCardInfo(data);
		logger.info("当前抢购的会员卡信息：" + cardInfo.toString());
		//获取抢购的人数
		List<Map<String, Object>> userlist = this.rushBuyClacMapper.getRushBuyUser(data);
		if (userlist == null || userlist.isEmpty()) {
			insertNotice("抢购"+cardInfo.get("typename")+"分配失败", data.get("calctime")+"获取参与抢购的人数为0：此次会员卡抢购失败", Constants.FAILED);
			return;
		}
		insertNotice("抢购"+cardInfo.get("typename"), data.get("calctime")+"此次共有"+userlist.size()+"用户参与分配抢购"+cardInfo.get("typename"), Constants.FAILED);
		//获取待出售的会员卡订单数量
		List<Map<String, Object>> orderlist = this.rushBuyClacMapper.getWaitSellCardOrder(data);
		logger.info("获取待出售的会员卡订单数量：" + orderlist.toString());
		insertNotice("抢购"+cardInfo.get("typename"), cardInfo.get("typename")+":"+data.get("calctime")+"获取待出售订单数量为"+orderlist.size(), Constants.SUCCESS);
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
		//参与抢购人数小于等于3人且为最后一次结算时时 ，直接全部抢购成功 前面两次还会各抢购成功一个 所以总低于5个会总抢购成功
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
					List<Map<String, Object>> sysUserList = this.rushBuyClacMapper.getSysUser();
					//从系统用户中随机取一个用户新增一张待出售的会员卡
					int index = random.nextInt(sysUserList.size());
					Map<String, Object> orderInfo = new HashMap<String, Object>();
					orderInfo.put("selluserid", sysUserList.get(index).get("userid"));
					orderInfo.put("type", 1);
					//查询一个当天的订单数据加一
					int ordernum = appVipCardMapper.getOrderNum();
					//生成订单号
					orderInfo.put("ordernum", AppVipCardServiceImpl.generateUniqueKey() + ordernum);
					orderInfo.put("cardid", cardInfo.get("cardid"));
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
					orderMap.put("profitprice", profitprice);
					//根据抢购时间和出售所需天数计算一个到期时间duetime
					orderMap.put("commentstartdays", cardInfo.get("commentstartdays"));
					orderMap.put("rushtime", new Date());
					this.appVipCardMapper.insertOrder(orderInfo);
					
					//用户抢购记录置为抢购成功
					Map<String, Object> rushToBuyMap = new HashMap<String, Object>();
					rushToBuyMap.put("cardid", cardInfo.get("cardid"));
					rushToBuyMap.put("userid", indexlist.get(i).get("userid"));
					rushToBuyMap.put("aftstatus", 2);
					rushToBuyMap.put("befstatus", 1);
					this.rushBuyClacMapper.updateRushToBuy(rushToBuyMap);
					insertNotice("抢购"+cardInfo.get("typename"), cardInfo.get("typename")+":"+data.get("calctime")+"恭喜用户抢购价值"+cardprice+"元的"+cardInfo.get("typename")+"成功", Constants.SUCCESS);
					allcount ++;
				} catch (Exception e) {
					insertNotice("抢购"+cardInfo.get("typename"), cardInfo.get("typename")+":"+data.get("calctime")+"用户"+cardInfo.get("nickname")+"抢购失败："+e.getMessage(), Constants.FAILED);
					continue;
				}
			} else {
				try {
					//分配待出售会员卡给用户的情况
					orderMap.put("orderid", orderlist.get(i).get("orderid"));
					//当抢购人员分配到自己正在出售的会员卡此等极端情况 直接不给他分配了 当做他没抢到
					if(indexlist.get(i).get("userid").equals(orderlist.get(i).get("selluserid"))) {
						logger.info("当抢购时分配到到自己正在出售的会员卡，就不分配了 userid:"+indexlist.get(i).get("userid"));
						insertNotice("抢购"+cardInfo.get("typename"), cardInfo.get("typename")+":"+data.get("calctime")+"-"+indexlist.get(i).get("nickname")+"抢购时分配到到自己正在出售的会员卡：该用户抢购失败", Constants.FAILED);
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
					insertNotice("抢购成功", cardInfo.get("typename")+":"+data.get("calctime")+"恭喜用户抢购价值"+cardprice+"元的"+cardInfo.get("typename")+"成功", Constants.SUCCESS);
					allcount++;
				} catch (Exception e) {
					insertNotice("抢购"+cardInfo.get("typename"), cardInfo.get("typename")+":"+data.get("calctime")+"用户"+cardInfo.get("nickname")+"抢购失败："+e.getMessage(), Constants.FAILED);
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
			this.rushBuyClacMapper.updateRushToBuy(rushMap);
		}
		insertNotice("抢购"+cardInfo.get("typename"), cardInfo.get("typename")+":"+data.get("calctime")+"此次会员卡分配完成，共"+allcount+"个用户抢到会员卡", Constants.SUCCESS);
	}
}
