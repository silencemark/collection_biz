package com.collection.dao;
import java.util.List;
import java.util.Map;
/**
 * VIP会员相关
 * @author silence
 *
 */
public interface IAppVipCardMapper {
	
	/**
	 * 获取会员卡列表
	 * 当前能抢购的 优先展示
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getVipCardList(Map<String, Object> data);
	
	/**
	 * 获取待支付会员卡列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getWaitPayCard(Map<String, Object> data);
	
	/**
	 * 进入立即支付信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getPayVipCardInfo(Map<String, Object> data);
	
	/**
	 * 上传支付凭证
	 * @param data
	 */
	void payVipCard(Map<String, Object> data);
	
	/**
	 * 获取卖家/卖家信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getContactPhone(Map<String, Object> data);
	
	/**
	 * 获取交易中会员卡列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getSaleCardList(Map<String, Object> data);
	
	/**
	 * 获取会员卡信息 审核页面
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getExamineInfo(Map<String, Object> data);
	
	/**
	 * 审核通过
	 * @param data
	 */
	void examinePast(Map<String, Object> data);
	
	/**
	 * 获取买家/卖家成交的订单个数
	 * @param data
	 * @return
	 */
	int getUserVipCount(Map<String, Object> data);
	
	/**
	 * 修改会员等级
	 * @param data
	 */
	void updateLevel(Map<String, Object> data);
}
