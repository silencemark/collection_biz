package com.collection.dao.storefront;
import java.util.List;
import java.util.Map;
/**
 * 店面管理
 * @author silence
 *
 */
public interface StoreFrontMapper {
	
	/**
	 * 添加巡店日志
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertTourStore(Map<String, Object> data);
	/**
	 * 巡店日志列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getTourStoreList(Map<String, Object> data);
	/**
	 * 巡店日志列表数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getTourStoreListNum(Map<String, Object> data);
	/**
	 * 巡店日志详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getTourStoreInfo(Map<String, Object> data);
	/**
	 * 巡店日志修改
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateTourStore(Map<String, Object> data);
	
	/**
	 * 查询Kpi星值审核 列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getKpiStarList(Map<String, Object> data);
	/**
	 * 查询Kpi星值审核 列表 数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getKpiStarListNum(Map<String, Object> data);
	/**
	 * 自评星值数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getSelfStarNum(Map<String, Object> data);
	/**
	 * 综合自评星值数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getOverallStarNum(Map<String, Object> data);
	/**
	 * 查询人事轨迹  奖励 列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getPathRewardList(Map<String, Object> data);
	/**
	 * 查询人事轨迹  奖励 列表数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getPathRewardListNum(Map<String, Object> data);
	/**
	 * 查询人事轨迹  奖励 列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getPathPunishList(Map<String, Object> data);
	/**
	 * 查询人事轨迹  奖励 列表数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getPathPunishListNum(Map<String, Object> data);
	/**
	 * 添加顾客满意 表
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertStoreEvaluate(Map<String, Object> data);

	/**
	 * 查询顾客满意列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getStoreEvaluateList(Map<String, Object> data);
	/**
	 * 查询顾客满意数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getStoreEvaluateListNum(Map<String, Object> data);
	
	/**
	 * 新增关注记录
	 */
	void insertEvaluateFollow(Map<String, Object> data);
	
	/**
	 * 修改关注状态
	 */
	
	void updateFollowStatus(Map<String, Object> data);
	
	/**
	 * 查询是否关注过
	 * @param data
	 * @return
	 */
	Map<String, Object> getFollowInfo(Map<String, Object> data);
	
	/**
	 * 巡店日志分组时间查询时间 
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getTourDateList(Map<String, Object> data);
	
	/**
	 * 巡店日志分组时间查询时间总记录数
	 * @param data
	 * @return
	 */
	int getTourDateListCount(Map<String, Object> data);
	
}
