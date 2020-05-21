package com.collection.dao.managebackstage;

import java.util.Map;

public interface CustomerFileMapper {

	/**
	 * 添加文件上传记录
	 * @param map
	 */
	void insertCustomerFileInfo(Map<String,Object> map);
	
}
