package com.collection.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IManageBackStageMapper;
import com.collection.service.IManageBackStageService;
/**
 * 后台管理相关功能
 * @author silence
 *
 */
public class ManageBackStageServiceImpl implements IManageBackStageService{

	@Autowired IManageBackStageMapper manageBackStageMapper ;
	
	@Override
	public Map<String, Object> getUserInfo(Map<String, Object> data) {
		return this.manageBackStageMapper.getUserInfo(data);
	}

	@Override
	public int getUserListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getUserListCount(data);
	}

	@Override
	public List<Map<String, Object>> getUserList(Map<String, Object> data) {
		return this.manageBackStageMapper.getUserList(data);
	}

	@Override
	public List<Map<String, Object>> getBannerList(Map<String, Object> data) {
		return this.manageBackStageMapper.getBannerList(data);
	}

	@Override
	public int getBannerListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getBannerListCount(data);
	}

	@Override
	public Map<String, Object> getBannerInfo(Map<String, Object> data) {
		return this.manageBackStageMapper.getBannerInfo(data);
	}

	@Override
	public void insertBanner(Map<String, Object> data) {
		this.manageBackStageMapper.insertBanner(data);
	}

	@Override
	public void updateBanner(Map<String, Object> data) {
		this.manageBackStageMapper.updateBanner(data);
	}

	@Override
	public void updateUserInfo(Map<String, Object> data) {
		this.manageBackStageMapper.updateUserInfo(data);
	}
	
}
