package com.collection.dao;

import java.util.List;
import java.util.Map;

/**
 * 统计相关
 * @author silence
 *
 */
public interface IManageStatisticsMapper {
	/**
	 * 获取订单量统计
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getNewOrderStatistics(Map<String, Object> data);
	
	/**
	 * 获取每日订单金额统计
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getNewMoneyStatistics(Map<String, Object> data);
	
	/**
	 * 获取每日总用户统计
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getSumUserStatistics(Map<String, Object> data);
	
	/**
	 * 获取每日新增用户统计
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getNewUserStatistics(Map<String, Object> data);
	
	/**
	 * 获取每日活跃用户统计
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getActivityStatistics(Map<String, Object> data);
}
