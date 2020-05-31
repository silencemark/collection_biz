package com.collection.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

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
		String userid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("userid", userid);
		data.put("nickname", data.get("phone"));
		data.put("createtime", new Date());
		//根据邀请码invitecode查询上级父ID
		data.put("parentid", appLoginMapper.getParentIdByInviteCode(data));
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
}
