package com.collection.dao.managebackstage;

import java.util.List;
import java.util.Map;

public interface SystemCloudMapper {

	/**
	 * 查询管理方文件列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getSystemCloudListInfo(Map<String,Object> map);
	
	/**
	 * 查询管理方文件列表的总数
	 * @param map
	 * @return
	 */
	int getSystemCloudListCount(Map<String,Object> map);
	
	/**
	 * 查询子文件夹的父文件夹的parentid
	 * @param map
	 * @return
	 */
	String getFolderParentid(Map<String,Object> map);
	
	/**
	 * 修改管理方文件信息
	 * @param map
	 */
	void updateSystemCloudInfo(Map<String,Object> map);
	
	/**
	 * 添加文件
	 * @param map
	 */
	void insertSystemCloudInfo(Map<String,Object> map);
	
	/**
	 * 查询文件的详细信息
	 * @param map
	 * @return
	 */
	Map<String,Object> getSystemCloudDetailInfo(Map<String,Object> map);
	
}
