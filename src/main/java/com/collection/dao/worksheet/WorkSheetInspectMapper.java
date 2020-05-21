package com.collection.dao.worksheet;

import java.util.List;
import java.util.Map;

public interface WorkSheetInspectMapper {

	/**
	 * 查询检查的模板信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getInspectTemplateList(Map<String,Object> map);
	
	/**
	 * 查询检查的模板信息的总数
	 * @param map
	 * @return
	 */
	int getInspectTemplateCount(Map<String,Object> map);
	
	/**
	 * 查询模板对应的检查项目信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getTemplateProjectList(Map<String,Object> map);
	
	/**
	 * 查询模板对应的检查项目信息的总数
	 * @param map
	 * @return
	 */
	int getTemplateProjectCount(Map<String,Object> map);
	
	/**
	 * 添加检查模板信息
	 * @param map
	 */
	void insertInspectTemplate(Map<String,Object> map);
	
	/**
	 * 添加检查项目信息
	 * @param map
	 */
	void insertTemplateProject(Map<String,Object> map);
	
	/**
	 * 修改检查模板信息
	 * @param map
	 */
	void updateInspectTemplate(Map<String,Object> map);
	
	/**
	 * 修改检查项目信息
	 * @param map
	 */
	void updateTemplateProject(Map<String,Object> map);
	
	/**
	 * 查询组织架构信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getOrganizeList(Map<String,Object> map);
	
	/**
	 * 查询用户所拥有的权限
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getOrganizeListByUserid(Map<String,Object> map);
	
	/**
	 * 查询组织下的人员
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getUserInfoByOrganizeid(Map<String,Object> map);
	
	/**
	 * 删除发布范围信息
	 * @param map
	 */
	void deleteReleaseRangeInfo(Map<String,Object> map);
	
	/**
	 * 查询组织下的人员的总数
	 * @param map
	 * @return
	 */
	int getUserInfoCountByOrganizeid(Map<String,Object> map);
	
	/**
	 * 删除检查项目
	 * @param map
	 */
	void deleteTemplateProjectInfo(Map<String,Object> map);
	
	/**
	 * 查询默认的检查模板信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getDefaultTemplateList(Map<String,Object> map);
	
	/**
	 * 查询默认的检查项目信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getDefaultProjectList(Map<String,Object> map);
	
	/**
	 * 查询组织架构的datacode
	 * @param map
	 * @return
	 */
	Map<String,Object> getOrganizeDataCodeInfo(Map<String,Object> map);

	/**
	 * 查询组织机构下面模板名称是否重复
	 * @param map
	 * @return
	 */
	int getTemplateNameIsExists(Map<String,Object> map);
}
