package com.collection.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.ISystemMapper;
import com.collection.service.ISystemService;
/**
 * 用户管理
 * @author silence
 *
 */
public class SystemServiceImpl implements ISystemService{
	@Autowired ISystemMapper systemMapper;
	
	@Override
	public Map<String, Object> getUserInfo(Map<String, Object> data) {
		return this.systemMapper.getUserInfo(data);
	}

	@Override
	public void updateUserInfo(Map<String, Object> data) {
		data.put("updatetime", new Date());
		this.systemMapper.updateUserInfo(data);
	}

	@Override
	public void insertUserNotice(Map<String, Object> data) {
		this.systemMapper.insertUserNotice(data);
	}
}
