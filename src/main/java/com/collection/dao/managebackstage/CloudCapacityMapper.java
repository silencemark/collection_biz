package com.collection.dao.managebackstage;

import java.util.List;
import java.util.Map;

public interface CloudCapacityMapper {

	/**
	 * 查询扩容申请记录
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getCloudCapacityList(Map<String,Object> map);
	
	/**
	 * 查询扩容申请的总数
	 * @param map
	 * @return
	 */
	int getCloudCapacityListCount(Map<String,Object> map);
	
	/**
	 * 查询扩容申请详情
	 * @param map
	 * @return
	 */
	Map<String,Object> getCloudCapacityDetail(Map<String,Object> map);
	
	/**
	 * 审核扩容申请
	 * @param map
	 */
	void updateCloudCapacityInfo(Map<String,Object> map);
	
	/**
	 * 修改公司的最大的空间数量
	 * @param map
	 */
	void updateCompanyCloudInfo(Map<String,Object> map);
	
}
