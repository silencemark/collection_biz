package com.collection.dao.starassess;
import java.util.List;
import java.util.Map;
/**
 * KPI星值审核管理
 * @author silence
 *
 */
public interface StarAssessMapper {
	
	/**
	 * app 查询自评项目列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> appGetEvaluateTemplateList(Map<String,Object> map);
	
	/**
	 * 查询自评项目列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getEvaluateTemplateList(Map<String,Object> map);
	
	/**
	 * 获取星值项目列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getStarProjectList(Map<String, Object> data);
	/**
	 * 添加自评
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertEvaluate(Map<String, Object> data);
	/**
	 * 自评打分添加
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertEvaluateStar(Map<String, Object> data);
	/**
	 * 自评详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getEvaluateInfo(Map<String, Object> data);
	/**
	 * 自评打分记录列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getEvaluateStarList(Map<String, Object> data);
	/**
	 * 修改自评（审核）
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateEvaluate(Map<String, Object> data);
	/**
	 * 岗位星值列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getEvaluateList(Map<String, Object> data);
	/**
	 * 岗位星值列表数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getEvaluateListNum(Map<String, Object> data);
	
	/*---------------------------------------综合自评-------------------------------------------------------*/
	
	/**
	 * 添加综合自评
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertOverallvaluate(Map<String, Object> data);
	/**
	 * 综合自评打分添加
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertOverallvaluateStar(Map<String, Object> data);
	/**
	 * 综合自评详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getOverallvaluateInfo(Map<String, Object> data);
	/**
	 * 综合自评打分记录列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getOverallvaluateStarList(Map<String, Object> data);
	/**
	 * 修改综合自评（审核）
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateOverallvaluate(Map<String, Object> data);
	/**
	 * 综合星值列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getOverallvaluateList(Map<String, Object> data);
	/**
	 * 综合星值列表数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getOverallvaluateListNum(Map<String, Object> data);
	
	/**
	 * 查询所有用户的总星值
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUserStarList(Map<String, Object> data);
	/**
	 * 查询所有用户数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getUserStarListNum(Map<String, Object> data);
	
	/**
	 * 查询所有规则
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getStarRuleList(Map<String, Object> data);
	/**
	 * 查询所有规则数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getStarRuleListNum(Map<String, Object> data);
	/**
	 * 查询规则详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getStarRuleInfo(Map<String, Object> data);
	
	/**
	 * 查询综合星值的已处理的时间列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getOverallvaluateListTime(Map<String,Object> map);
	
	/**
	 * 查询综合星值的已处理的时间列表 的总数
	 * @param map
	 * @return
	 */
	int getOverallvaluateListTimeCount(Map<String,Object> map);
	
	/**
	 * 查询岗位星值时间列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getEvaluateListTime(Map<String,Object> map);
	
	/**
	 * 查询岗位星值时间列表总数
	 * @param map
	 * @return
	 */
	int getEvaluateListTimeCount(Map<String,Object> map);
	
	/**
	 * 添加kpi星级考核规则
	 * @param map
	 */
	void insertStarRankingRuleInfo(Map<String,Object> map);
	
	/**
	 * 修改kpi星级考核规则
	 * @param map
	 */
	void updateStarRankingRuleInfo(Map<String,Object> map);
	
	/**
	 * 查询规则详情
	 * @param map
	 * @return
	 */
	Map<String,Object> getStarRankingRuleDetailInfo(Map<String,Object> map);
	
	/**
	 * 查询自评模板总数
	 * @param map
	 * @return
	 */
	int getEvaluateTemplateListCount(Map<String,Object> map);
	
	/**
	 * 添加自评模板
	 * @param map
	 */
	void insertEvaluateTemplateInfo(Map<String,Object> map);
	
	/**
	 * 修改自评模板信息
	 * @param map
	 */
	void updateEvaluateTemplateInfo(Map<String,Object> map);
	
	/**
	 * 添加检查项目
	 * @param map
	 */
	void insertEvaluateProjectInfo(Map<String,Object> map);
	
	/**
	 * 修改检查项目信息
	 * @param map
	 */
	void updateEvaluateProjectInfo(Map<String,Object> map);
	
	/**
	 * 查询单个模板信息
	 * @param map
	 * @return
	 */
	Map<String,Object> getEvaluateDetailInfo(Map<String,Object> map);
	
	/**
	 * 修改岗位自评星值信息
	 * @param map
	 */
	void updateEvaluateStarInfo(Map<String,Object> map);
	
	/**
	 * 修改综合自评星值信息
	 * @param map
	 */
	void updateOverAllaluateStarInfo(Map<String,Object> map);

	/**
	 * 查询公司的组织id
	 * @param map
	 * @return
	 */
	Map<String,Object> getOrganizeByCompanyId(Map<String,Object> map);
	
	/**
	 * 查询模板名称是否重复
	 * @param map
	 * @return
	 */
	int getEValuateTemplateNameIsExists(Map<String,Object> map);
	
	/**
	 * 查询评论信息中的转发人信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getCommentForwardUserids(Map<String,Object> map);
}
