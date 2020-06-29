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
}
