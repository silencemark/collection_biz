package com.collection.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.collection.dao.IAppUserCenterMapper;
import com.collection.dao.IAppVipCardMapper;
import com.collection.dao.ISystemMapper;
import com.collection.service.IAppUserCenterService;
/**
 * 个人中心相关
 * @author silence
 *
 */
public class AppUserCenterServiceImpl implements IAppUserCenterService{

	@Autowired IAppUserCenterMapper appUserCenterMapper;
	
	@Autowired IAppVipCardMapper appVipCardMapper;
	
	@Autowired ISystemMapper systemMapper;

	private Logger logger = Logger.getLogger(AppUserCenterServiceImpl.class);
	
	@Override
	public Map<String, Object> getMyCenter(Map<String, Object> data) {
		Map<String, Object> result = appUserCenterMapper.getMyCenter(data);
		//判断当天是否已经签到
		Map<String, Object> signMap = this.appUserCenterMapper.getSignTodays(data);
		if (signMap != null && signMap.size() > 0 ){
			result.put("isSign", 1);
		} else {
			result.put("isSign", 0);
		}
		return result;
	}

	@Override
	public Map<String, Object> signIn(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		//判断当天是否已经签到
		Map<String, Object> signMap = this.appUserCenterMapper.getSignTodays(data);
		if (signMap != null && signMap.size() > 0 ){
			result.put("status", 1);
			result.put("message", "今天已经签到过了");
			return result;
		}
		Map<String, Object> userinfo = this.appUserCenterMapper.getMyCenter(data);
		//普通用户只能签到送xgo
		if (userinfo!= null && "0".equals(userinfo.get("levelenum").toString())) {
			//签到送一个 xgo
			data.put("growthvalue", 0);
			this.appUserCenterMapper.signIn(data);
			//新增到签到记录表
			data.put("xgocoin", 1);
			data.put("createtime", new Date());
			this.appUserCenterMapper.insertSign(data);
			//新增到xgo记录表
			data.put("type", 1);
			data.put("remark", "恭喜您签到获得1个xgo币");
			this.appUserCenterMapper.addXgoRecord(data);
			
			result.put("status", 0);
			result.put("remark", data.get("remark"));
			return result;
		} else {
			//签到送1-50成长值 和一个 xgo
			int value = new Random().nextInt(50) + 1;
			data.put("growthvalue", value);
			this.appUserCenterMapper.signIn(data);
			//新增到签到记录表
			data.put("xgocoin", 1);
			data.put("createtime", new Date());
			this.appUserCenterMapper.insertSign(data);
			//新增到xgo记录表
			data.put("type", 1);
			data.put("remark", "恭喜您获得1个xgo币和"+value+"点成长值");
			this.appUserCenterMapper.addXgoRecord(data);
			//如果成长值达到下一个值改变会员等级
			Map<String, Object> levelMap = appUserCenterMapper.getUserNewOldLevel(data);
			if (!levelMap.get("levelid").toString().equals(levelMap.get("oldlevelid").toString())){
				appUserCenterMapper.updateUserInfoLevel(levelMap);
				//新增买家通知
	            Map<String, Object> notice = new HashMap<String, Object>();
	    		notice.put("title", "会员等级通知");
	    		notice.put("message", "恭喜您，您的会员等级升级为"+levelMap.get("levelname"));
	    		notice.put("userid", levelMap.get("userid"));
	    		notice.put("createtime", new Date());
	    		this.systemMapper.insertUserNotice(notice);
			}
			result.put("status", 0);
			result.put("remark", data.get("remark"));
			return result;
		}
	}

	@Override
	public Map<String, Object> myGrowthValue(Map<String, Object> data) {
		//查询当前用户的成长值
		Map<String, Object> result = this.appUserCenterMapper.myGrowthValue(data);
		//查询所有会员等级
		List<Map<String, Object>> memberGrowList= this.appUserCenterMapper.getMemberGrowList();
		result.put("memberGrowList", memberGrowList);
		return result;
	}

	@Override
	public void certification(Map<String, Object> data) {
		//更改用户状态
		data.put("status", 1);
		this.appUserCenterMapper.updateUserCertification(data);
		data.put("createtime", new Date());
		this.appUserCenterMapper.certification(data);
	}

	@Override
	public Map<String, Object> getCertification(Map<String, Object> data) {
		return this.appUserCenterMapper.getCertification(data);
	}

	@Override
	public Map<String, Object> myTeam(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取子集
		List<Map<String, Object>> sonlist = this.appUserCenterMapper.getSonProfit(data);
		int allnum = sonlist.size();
		for(Map<String, Object> son:sonlist){
			//获取孙子集
			son.put("grandid", data.get("userid"));
			List<Map<String, Object>> grandsonlist = this.appUserCenterMapper.getGrandSonProfit(son);
			if(grandsonlist != null && grandsonlist.size() > 0){
				allnum += grandsonlist.size();
			}
			son.put("grandsonlist", grandsonlist);
		}
		result.put("sonlist", sonlist);
		result.put("sonnum", sonlist.size());
		result.put("alllowernum", allnum);
		return result;
	}

	@Override
	public Map<String, Object> myAssets(Map<String, Object> data) {
		return this.appUserCenterMapper.myAssets(data);
	}

	@Override
	public List<Map<String, Object>> getExchangeList(Map<String, Object> data) {
		return this.appUserCenterMapper.getExchangeList(data);
	}

	@Override
	public Map<String, Object> exchangeVipCard(Map<String, Object> data) {
		//1、根据价格生成一张会员卡（规则是按在价格区间，持有时限正序第一个） 算出一个隔日的待出售时间
		Map<String, Object> cardMap = this.appVipCardMapper.getMemberCardByPrice(data);
		if(cardMap!= null) {
			cardMap.put("cardprice", data.get("cardprice"));
			cardMap.put("selluserid", data.get("userid"));
			//查询一个当天的订单数据加一
			int num = appVipCardMapper.getOrderNum();
			//生成订单号
			String ordernum = AppVipCardServiceImpl.generateUniqueKey() + num;
			cardMap.put("ordernum", ordernum);
			cardMap.put("ordertype", 2);
			cardMap.put("createtime", new Date());
			//2、生成待出售的订单
			this.appVipCardMapper.insertOrder(cardMap);
			//3、个人中心可兑换资产减少
			data.put("overprofit", Double.parseDouble(data.get("cardprice").toString()) * -1);
			this.appVipCardMapper.addUserInfoOverProfit(data);
			//4、新增兑换记录
			data.put("amount", data.get("cardprice"));
			data.put("ordernum", ordernum);
			this.appUserCenterMapper.insertExchange(data);
			data = new HashMap<String, Object>();
			data.put("status", 0);
			data.put("message", "兑换成功");
			return data;
		} else {
			data = new HashMap<String, Object>();
			data.put("status", 1);
			data.put("message", "没有符合的会员卡，请输入合适的价格");
			return data;
		}
	}

	@Override
	public Map<String, Object> myInviteCode(Map<String, Object> data) {
		return this.appUserCenterMapper.myInviteCode(data);
	}

	@Override
	public void updateQrcode(Map<String, Object> data) {
		this.appUserCenterMapper.updateQrcode(data);
	}

	@Override
	public Map<String, Object> getMyUserInfo(Map<String, Object> data) {
		return this.appUserCenterMapper.getMyUserInfo(data);
	}

	@Override
	public Map<String, Object> updateHeadImg(Map<String, Object> data) {
		//拿出headimage的base64文件流
		String base64Data = String.valueOf(data.get("headimage"));
		Map<String, Object> result = new HashMap<String, Object>();
        try{  
            logger.debug("上传文件的数据："+base64Data);
            logger.debug("对数据进行判断");
            if(base64Data == null || "".equals(base64Data)){
                logger.info("上传失败，上传图片数据为空");
                result.put("status", 1);
        		result.put("message", "上传失败，上传图片数据为空");
        		return result;
            }
            String tempFileName = System.currentTimeMillis()/1000l+"_app" + ".jpg";
            logger.debug("生成文件名为："+tempFileName);
 
            //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
            byte[] bs = Base64Utils.decodeFromString(base64Data);
            try{
                //使用apache提供的工具类操作流
                FileUtils.writeByteArrayToFile(new File("/home/silence/collection_web/upload/images/"+ tempFileName), bs);  
            }catch(Exception ee){
            	logger.info("上传失败，写入文件失败"+ee.getMessage());
            	result.put("status", 1);
        		result.put("message", "上传失败，写入文件失败");
        		return result;
            }
            data.put("headimage", "/upload/images/"+tempFileName);
        }catch (Exception e) {  
        	logger.info("上传失败"+ e.getMessage());
        	result.put("status", 1);
    		result.put("message", "上传失败");
    		return result;
        }
		this.appUserCenterMapper.updateUserInfo(data);
		result.put("status", 0);
		result.put("message", "头像修改成功");
		return result;
	}

	@Override
	public void updateNickName(Map<String, Object> data) {
		this.appUserCenterMapper.updateUserInfo(data);
	}

	@Override
	public Map<String, Object> updatePassWord(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		//1、判断旧密码是否正确
		result = this.appUserCenterMapper.getMyUserInfo(data);
		if (result != null && result.size() > 0 ){
			this.appUserCenterMapper.updateUserInfo(data);
			result = new HashMap<String, Object>();
			result.put("status", 0);
			result.put("message", "登录密码修改成功");
		} else {
			result = new HashMap<String, Object>();
			result.put("status", 1);
			result.put("message", "旧密码输入错误");
		}
		return result;
	}

	@Override
	public void setPayPassWord(Map<String, Object> data) {
		this.appUserCenterMapper.updateUserInfo(data);
	}

	@Override
	public Map<String, Object> getPaymentMethod(Map<String, Object> data) {
		Map<String, Object> result = this.appUserCenterMapper.getPaymentMethod(data);
		if (result == null) {
			this.appUserCenterMapper.addPaymentMethod(data);
			result = new HashMap<String, Object>();
		}
		return result;
	}

	@Override
	public Map<String, Object> updatePaymentMethod(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> paymap = this.appUserCenterMapper.getPaymentMethod(data);
		if (paymap == null) {
			this.appUserCenterMapper.addPaymentMethod(data);
		} 
		String weixinqrcode = String.valueOf(data.get("weixinqrcode"));
		String alipayqrcode = String.valueOf(data.get("alipayqrcode"));
		//上传微信二维码
        if(weixinqrcode != null && !"".equals(weixinqrcode)){
        	String tempFileName = System.currentTimeMillis()/1000l+"_weixin" + ".jpg";
            logger.debug("微信二维码生成文件名为："+tempFileName);
            //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
            byte[] bs = Base64Utils.decodeFromString(weixinqrcode);
            try{
                //使用apache提供的工具类操作流
                FileUtils.writeByteArrayToFile(new File("/home/silence/collection_web/upload/images/"+ tempFileName), bs);  
            }catch(Exception ee){
            	logger.info("上传失败，写入微信二维码失败"+ee.getMessage());
            	result.put("status", 1);
        		result.put("message", "上传失败，写入文件失败");
        		return result;
            }
            data.put("weixinqrcode", "/upload/images/"+tempFileName);
        }
        //上传支付宝二维码
        if (alipayqrcode != null && !"".equals(alipayqrcode)){
        	String tempFileName = System.currentTimeMillis()/1000l+"_alipay" + ".jpg";
            logger.debug("支付宝二维码生成文件名为："+tempFileName);
            //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
            byte[] bs = Base64Utils.decodeFromString(alipayqrcode);
            try{
                //使用apache提供的工具类操作流
                FileUtils.writeByteArrayToFile(new File("/home/silence/collection_web/upload/images/"+ tempFileName), bs);  
            }catch(Exception ee){
            	logger.info("上传失败，写入支付宝二维码失败"+ee.getMessage());
            	result.put("status", 1);
        		result.put("message", "上传失败，写入文件失败");
        		return result;
            }
            data.put("alipayqrcode", "/upload/images/"+tempFileName);
        }
		this.appUserCenterMapper.updatePaymentMethod(data);
		result.put("status", 0);
		result.put("message", "修改成功");
		return data;
	}

	@Override
	public List<Map<String, Object>> getMyQuestion(Map<String, Object> data) {
		return this.appUserCenterMapper.getMyQuestion(data);
	}

	@Override
	public void addMyQuestion(Map<String, Object> data) {
		data.put("createtime", new Date());
		this.appUserCenterMapper.addMyQuestion(data);
	}

	@Override
	public List<Map<String, Object>> getUserNotice(Map<String, Object> data) {
		return this.appUserCenterMapper.getUserNotice(data);
	}

	@Override
	public Map<String, Object> getNoticeUnreadNum(Map<String, Object> data) {
		return this.appUserCenterMapper.getNoticeUnreadNum(data);
	}

	@Override
	public void updateNoticeStatus(Map<String, Object> data) {
		this.appUserCenterMapper.updateNoticeStatus(data);
	}

	@Override
	public List<Map<String, Object>> getXgoRecord(Map<String, Object> data) {
		return this.appUserCenterMapper.getXgoRecord(data);
	}

	@Override
	@Transactional
	public Map<String, Object> giveXgoToOther(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		//查询当前用户信息
		Map<String, Object> userinfo = new HashMap<String, Object>();
		userinfo.put("userid", data.get("userid"));
		Map<String, Object> myinfo = this.appUserCenterMapper.getMyCenter(userinfo);
		//查询对方用户信息，判断输入的对方账户是否存在
		userinfo = new HashMap<String, Object>();
		userinfo.put("phone", data.get("phone"));
		userinfo = this.appUserCenterMapper.getMyCenter(userinfo);
		if (userinfo == null || userinfo.isEmpty()) {
			result.put("status", 1);
    		result.put("message", "转赠失败，输入手机号错误，系统没有此用户");
    		return result;
		}
		//当前用户扣除xgo
		Map<String, Object> xgoMap = new HashMap<String, Object>();
		xgoMap.put("userid", data.get("userid"));
		xgoMap.put("xgocoin", data.get("xgocoin"));
		this.appUserCenterMapper.reduceUserXgo(xgoMap);
		//新增到xgo记录表
		xgoMap.put("createtime", new Date());
		xgoMap.put("type", 2);
		xgoMap.put("remark", "您转赠给用户"+userinfo.get("nickname")+"："+data.get("xgocoin")+"个xgo币");
		this.appUserCenterMapper.addXgoRecord(xgoMap);
		
		//对方用户新增xgo
		xgoMap.put("userid", userinfo.get("userid"));
		xgoMap.put("xgocoin", data.get("xgocoin"));
		this.appUserCenterMapper.addUserXgo(xgoMap);
		//新增到xgo记录表
		xgoMap.put("type", 1);
		xgoMap.put("remark", "用户"+myinfo.get("nickname")+"转赠给您："+data.get("xgocoin")+"个xgo币");
		this.appUserCenterMapper.addXgoRecord(xgoMap);
		
		result.put("status", 0);
		result.put("message", "转赠成功");
		return result;
	}
	
	
}
