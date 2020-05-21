package com.collection.dao.worksheet;

import java.util.List;
import java.util.Map;

public interface RewardInfoMapper {

	/**
	 * 查询奖励单列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getRewardListInfo(Map<String,Object> map);
	
	/**
	 * 查询奖励单列表总数
	 * @param map
	 * @return
	 */
	int getRewardListCount(Map<String,Object> map);
	
	/**
	 * 查询已完成的奖励单时间列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getRewardListTimes(Map<String,Object> map);
	
	/**
	 * 查询已完成的奖励单的时间的总数
	 * @param map
	 * @return
	 */
	int getRewardListTimesCount(Map<String,Object> map);
	
	/**
	 * 查询奖励单详情
	 * @param map
	 * @return
	 */
	Map<String,Object> getRewardDetailInfo(Map<String,Object> map);
	
	/**
	 * 添加奖励单信息
	 * @param map
	 */
	void insertRewardInfo(Map<String,Object> map);
	
	/**
	 * 提交奖励单的审核信息
	 * @param map
	 */
	void updateRewarddInfo(Map<String,Object> map);
	
	/**
	 * 查询奖励类型
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getDictionListInfo(Map<String,Object> map);	
	
/*--------------------------------------------------------------------------处罚单------------------------------------------------------------------------*/

	/**
	 * 查询处罚单信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getPunishListInfo(Map<String,Object> map);
	
	/**
	 * 查询处罚单的总数
	 * @param map
	 * @return
	 */
	int getPunishListCount(Map<String,Object> map);
	
	/**
	 * 查询处罚单时间列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getPunishListTimesInfo(Map<String,Object> map);
	
	/**
	 * 查询惩罚单列表的总数
	 * @param map
	 * @return
	 */
	int getPunishListTimesCount(Map<String,Object> map);
	
	/**
	 * 查询处罚单详细信息
	 * @param map
	 * @return
	 */
	Map<String,Object> getPunishDetailInfo(Map<String,Object> map);
	
	/**
	 * 修改处罚单信息
	 * @param map
	 */
	void updatePunishInfo(Map<String,Object> map);
	
	/**
	 * 添加处罚单信息
	 * @param map
	 */
	void insertPunishInfo(Map<String,Object> map);
	
/*----------------------------------------------------------------------报修单-------------------------------------------------------------------------*/
	
	/**
	 * 查询报修单信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getRepairListInfo(Map<String,Object> map);
	
	/**
	 * 查询报修单的总数
	 * @param map
	 * @return
	 */
	int getRepairListCount(Map<String,Object> map);
	
	/**
	 * 查询报修单时间列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getRepairTimesListInfo(Map<String,Object> map);
	
	/**
	 * 查询报修单时间列表总数
	 * @param map
	 */
	int getRepairTimesListCount(Map<String,Object> map);
	
	/**
	 * 查询报修单详细信息
	 * @param map
	 * @return
	 */
	Map<String,Object> getRepairDetailInfo(Map<String,Object> map);
	
	/**
	 * 提交抄送人意见
	 * @param map
	 */
	void updateRepairInfo(Map<String,Object> map);
	
	/**
	 * 添加报修单信息
	 * @param map
	 */
	void insertRepairInfo(Map<String,Object> map);
	
	
	
	
/*-----------------------------------------------------------------菜品成本---------------------------------------------------------------------------------*/
	
	/**
	 * 查询菜品成本列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getDishesListInfo(Map<String,Object> map);
	
	/**
	 * 根据公司id获取菜品类型列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getDishestypeListInfoByOrganizeId(Map<String,Object> map);
	
	/**
	 * 查询菜品成本列表总数
	 * @param map
	 * @return
	 */
	int getDishesListCount(Map<String,Object> map);
	
	/**
	 * 查询菜品成本时间列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getDishesListTimesInfo(Map<String,Object> map);
	
	/**
	 * 查询菜品成本时间列表信息的总数
	 * @param map
	 * @return
	 */
	int getDishesListTimesCount(Map<String,Object> map);
	
	/**
	 * 查询菜品详情信息
	 * @param map
	 * @return
	 */
	Map<String,Object> getDishesDetailInfo(Map<String,Object> map);
	
	/**
	 * 提交抄送人意见
	 * @param map
	 */
	void updateDishesInfo(Map<String,Object> map);
	
	/**
	 * 添加菜品成本信息
	 * @param map
	 */
	void insertDishesInfo(Map<String,Object> map);
	
	/**
	 * 查询用户的职位信息
	 * @param map
	 * @return
	 */
	Map<String,Object> getUserPosition(Map<String,Object> map);
	
	
	
	
	/**
	 * 修改转发表中的阅读状态
	 * @param map
	 */
	void updateForwardReadStatus(Map<String,Object> map);
	
	/**
	 * 修改发布范围表中的阅读状态
	 * @param map
	 */
	void updateRangeReadStatus(Map<String,Object> map);
}
