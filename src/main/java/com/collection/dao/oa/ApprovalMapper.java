package com.collection.dao.oa;
import java.util.List;
import java.util.Map;
/**
 * 
 * 通用审批Mapper
 *
 */
public interface ApprovalMapper {
	
	/**
	 * app 通用审批 列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getCurrencyExamineList(Map<String,Object> map);
	
	/**
	 * app 通用审批总记录数
	 * @param map
	 * @return
	 */
	int getCurrencyExamineListCount(Map<String,Object> map);
	
	/**
	 * 查询通用审批的时间列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getCurrencyExamineTimesList(Map<String,Object> map);
	
	/**
	 * 查询通用审批的时间列表的总数
	 * @param map
	 * @return
	 */
	int getCurrencyExamineTimesCount(Map<String,Object> map);
	
	/**
	 * 查询通用审批信息的详情
	 * @param map
	 */
	Map<String,Object> getCurrencyExamineDetailInfo(Map<String,Object> map);
	
	/**
	 * 新增通用审批
	 * @param map
	 * @return
	 */
	int inserCurrencyExamine(Map<String,Object> map);
	
	/**
	 * 修改通用审批信息
	 * @param map
	 */
	void updateCurrencyExamineInfo(Map<String,Object> map);
	
 
	
}
