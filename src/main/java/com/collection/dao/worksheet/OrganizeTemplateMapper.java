package com.collection.dao.worksheet;

import java.util.List;
import java.util.Map;

public interface OrganizeTemplateMapper {

	/**
	 * 添加组织的检查模板信息 
	 * @param map
	 */
	void insertOrganizeTemplateInfo(Map<String,Object> map);
	
	/**
	 * 添加组织的检查的项目名称
	 * @param map
	 */
	void insertOrganizeTemplateProjectInfo(Map<String,Object> map);
	
	/**
	 * 修改检查模板信息
	 * @param map
	 */
	void updateOrganizeTemplateInfo(Map<String,Object> map);
	
	/**
	 * 修改组织检查项目信息
	 * @param map
	 */
	void updateOrganizeTemplateProjectInfo(Map<String,Object> map);
	
	/**
	 * 查询组织模板列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getOrganizeTemplateListInfo(Map<String,Object> map);
	
	/**
	 * 查询组织检查项目信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getOrganizeTemplateProjectListInfo(Map<String,Object> map);
	
	/**
	 * 查询检查模板的排序值的最大值 
	 * @param map
	 * @return
	 */
	int getOrganizeTemplateMaxPriority(Map<String,Object> map);
	
	/**
	 * 查询检查项目的最大的排序值
	 * @param map
	 * @return
	 */
	int getOrganizeTemplateProjectMaxPriority(Map<String,Object> map);
	
	/**
	 * 查询模板名称是否重复
	 * @param map
	 * @return
	 */
	int getOrganizeTemplateNameIsExsit(Map<String,Object> map);
	
}
