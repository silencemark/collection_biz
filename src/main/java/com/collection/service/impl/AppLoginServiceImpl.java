package com.collection.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IAppLoginMapper;
import com.collection.dao.IAppUserCenterMapper;
import com.collection.dao.IAppVipCardMapper;
import com.collection.dao.ISystemMapper;
import com.collection.service.IAppLoginService;
/**
 * 前端用户相关
 * @author silence
 *
 */
public class AppLoginServiceImpl implements IAppLoginService{

	@Autowired IAppLoginMapper appLoginMapper;

	@Autowired IAppUserCenterMapper appUserCenterMapper;
	
	@Autowired IAppVipCardMapper appVipCardMapper;
	
	@Autowired ISystemMapper systemMapper;
	
	private Logger logger = Logger.getLogger(AppLoginServiceImpl.class);
	
	@Override
	public Map<String, Object> login(Map<String, Object> data) {
		return appLoginMapper.login(data);
	}

	@Override
	public boolean checkPhone(Map<String, Object> data) {
		Map<String, Object> result = appLoginMapper.checkPhone(data);
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Object> insertUserInfo(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		int userid= this.appLoginMapper.getMaxUserid();
		data.put("userid", userid);
		data.put("nickname", data.get("phone"));
		data.put("createtime", new Date());
		//根据邀请码invitecode查询上级父ID
		logger.info("注册传入参数："+data.toString());
		Map<String, Object> parent = this.appLoginMapper.getParentIdByInviteCode(data);
		if (parent != null && parent.size() > 0 ) {
			/**
			 * 父级加两块钱邀请奖励 且 入库奖励记录表 累计50便不再奖励
			 */
			//1、查询累计奖励金额总数
			double rewardprice = this.appVipCardMapper.getSumRewardPrice(parent);
			//2、小于50可以奖励、直接累加可提现资产
			if (rewardprice < 50) {
				//累加可提现资产
				parent.put("profitprice", 2);
				this.appVipCardMapper.addParentsAndGrandPa(parent);
				//3、入库记录表
				parent.put("rewardprice", 2);
				parent.put("type", 1);
				this.appVipCardMapper.addRewardRecord(parent);
				//4、系统通知
				Map<String, Object> notice = new HashMap<String, Object>();
				notice.put("title", "邀请通知");
				notice.put("message", "恭喜你，您邀请的好友已经注册成功，您获得2元可兑换资产奖励，请注意查收");
				notice.put("userid", parent.get("userid"));
				notice.put("createtime", new Date());
				this.systemMapper.insertUserNotice(notice);
			}
			data.put("parentid", parent.get("userid"));
			//生成当前用户邀请码 生成方式
			String invitecode = generateInvitationCodeTwo(userid + "");
			data.put("invitecode", invitecode);
			data.put("invitecodehttpurl", "app/video/index.html?invitecode="+invitecode);
			//新增用户
			appLoginMapper.insertUserInfo(data);
			
			//注册成功 送xgo记录
			Map<String, Object> xgoMap = new HashMap<String, Object>();
			xgoMap.put("userid", userid);
			xgoMap.put("xgocoin", 10);
			xgoMap.put("createtime", new Date());
			xgoMap.put("type", 1);
			xgoMap.put("remark", "恭喜您注册成功，系统赠送您10个xgo币");
			this.appUserCenterMapper.addXgoRecord(xgoMap);
			result.put("status", 0);
			result.put("message", "注册成功");
			return result;
		} else {
			result.put("status", 1);
			result.put("message", "邀请码无效，请确认后再输入");
			return result;
		}
		
	}

	
	 
	@Override
	public void updateUserInfo(Map<String, Object> data) {
		this.appLoginMapper.updateUserInfo(data);
	}

	@Override
	public Map<String, Object> getUserInfo(Map<String, Object> data) {
		return this.appLoginMapper.getUserInfo(data);
	}
	
	//生成验证码逻辑
    private static final String[] storeInvitationChars={"a","c","b","d","f","e","h","i","j","k","l","m","n","o","p"
,"q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9"};
    /**
     * @author silence
     * @desc  生成邀请码
     * 格式： abcd12
     * */
    public static String generateInvitationCodeTwo(String userIdStr){
        int forSize=6-userIdStr.length();
        String randomStr="";
        for(int i=0;i<forSize;i++){
            Random random=new Random();
            int randomIndex=random.nextInt(35);
            randomStr=randomStr+storeInvitationChars[randomIndex];
        }
        return randomStr+userIdStr;
    }
}
