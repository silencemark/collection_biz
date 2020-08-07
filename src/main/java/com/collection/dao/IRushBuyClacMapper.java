package com.collection.dao;

import java.util.List;
import java.util.Map;

/**
 * 定时抢购分配功能实现
 * @author silence
 *
 */
public interface IRushBuyClacMapper {
	
	/**
	 * 获取此次参与抢购的用户
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getRushBuyUser(Map<String, Object> data);
	
	/**
	 * 获取会员卡信息 且计算当前计算时间 距 抢购开始的分钟数
	 * @param data
	 * @return
	 */
	Map<String, Object> getCardInfo(Map<String, Object> data);
	
	/**
	 * 获取手办信息
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getGarageKitListByCard(Map<String, Object> data);
	
	/**
	 * 获取等待出售的会员卡订单
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getWaitSellCardOrder(Map<String, Object> data);
	
	/**
	 * 查询配置的抢购三次计算的几率
	 * @return
	 */
	List<Map<String, Object>> getConfigRate();
	
	/**
	 * 分配会员卡出售订单给用户
	 * @param data
	 */
	void updateOrder(Map<String, Object> data);
	
	/**
	 * 修改抢购记录
	 * @param data
	 */
	void updateRushToBuy(Map<String, Object> data);
	
	/**
	 * 扣除抢购所需xgo币
	 * @param data
	 */
	void deductionXgo(Map<String, Object> data);
	
	/**
	 * 获取所有系统用户(除了正在分配的用户 防止抢到自己的)
	 * @return
	 */
	List<Map<String, Object>> getSysUser(Map<String, Object> user); 
	
	/**
	 * 获取所有未抢购成功的用户id
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> selectRushToBuyUserid(Map<String, Object> data);
}
