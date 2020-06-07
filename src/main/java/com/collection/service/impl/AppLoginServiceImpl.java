package com.collection.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IAppLoginMapper;
import com.collection.service.IAppLoginService;
/**
 * 前端用户相关
 * @author silence
 *
 */
public class AppLoginServiceImpl implements IAppLoginService{

	@Autowired IAppLoginMapper appLoginMapper;

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
	public void insertUserInfo(Map<String, Object> data) {
		int userid= this.appLoginMapper.getMaxUserid();
		data.put("userid", userid);
		data.put("nickname", data.get("phone"));
		data.put("createtime", new Date());
		//根据邀请码invitecode查询上级父ID
		data.put("parentid", appLoginMapper.getParentIdByInviteCode(data));
		//生成当前用户邀请码 生成方式
		data.put("invitecode", generateInvitationCodeTwo(userid + ""));
		//新增用户
		appLoginMapper.insertUserInfo(data);
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
