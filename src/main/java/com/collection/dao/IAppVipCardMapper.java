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
}
