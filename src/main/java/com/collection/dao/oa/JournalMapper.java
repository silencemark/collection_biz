package com.collection.dao.oa;
import java.util.List;
import java.util.Map;
/**
 * 
 * 通知Mapper
 *
 */
public interface JournalMapper {
	
	/**
	 * app 日志列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getJournalList(Map<String,Object> map);
	
	/**
	 * app 日志总记录数
	 * @param map
	 * @return
	 */
	int getJournalListCount(Map<String,Object> map);
	
	/**
	 * 新增日报
	 * @param map
	 * @return
	 */
	int insertDaily(Map<String,Object> map);
	
 
	/**
	 * 新增周报
	 * @param map
	 * @return
	 */
	int insertWeekly(Map<String,Object> map);
	
	/**
	 * 新增月报
	 * @param map
	 * @return
	 */
	int insertMonthly(Map<String,Object> map);
	
	/**
	 * 查询日志详情
	 * @param map
	 * @return
	 */
	Map<String,Object> getLogDetailInfo(Map<String,Object> map);
	
	/**
	 * 查询日志的时间列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getJournalTimesList(Map<String,Object> map);
	
	/**
	 * 查询日志的时间列表总数
	 * @param map
	 * @return
	 */
	int getJournalTimesListCount(Map<String,Object> map);
	
	/**
	 * 提交抄送人意见
	 * @param map
	 */
	void updateJournalInfo(Map<String,Object> map);
	
}
