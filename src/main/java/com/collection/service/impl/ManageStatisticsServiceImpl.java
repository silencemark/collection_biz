package com.collection.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IManageStatisticsMapper;
import com.collection.service.IManageStatisticsService;
/**
 * 后台管理相关功能
 * @author silence
 *
 */
public class ManageStatisticsServiceImpl implements IManageStatisticsService{

	@Autowired IManageStatisticsMapper manageStatisticsMapper ;

	@Override
	public List<Map<String, Object>> getNewOrderStatistics(
			Map<String, Object> data) {
		return manageStatisticsMapper.getNewOrderStatistics(data);
	}

	@Override
	public List<Map<String, Object>> getNewMoneyStatistics(
			Map<String, Object> data) {
		return manageStatisticsMapper.getNewMoneyStatistics(data);
	}
	
}
