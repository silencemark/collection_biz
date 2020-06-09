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

	/**
	 * 获取父id 和爷id
	 * @param data
	 * @return
	 */
	Map<String, Object> getElderid(Map<String, Object> data);
	
	/**
	 * 邀请者加成长值
	 * @param data
	 */
	void addParentGrowthValue(Map<String, Object> data);
	
	/**
	 * 父级加收益5%
	 * 父级的父级加2%
	 * @param data
	 */
	void addParentsAndGrandPa(Map<String, Object> data);
	
	/**
	 * 增加团队收益表记录
	 * @param data
	 */
	void insertTeamProfit(Map<String, Object> data);
	

	/**
	 * 增加个人总资产收益总和
	 * @param data
	 */
	void addMySumassets(Map<String, Object> data);
	
	/**
	 * 获取我的会员卡列表（审核通过）
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getMyCardList(Map<String, Object> data);
	
	/**
	 * 获取会员卡信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getMemberCardInfo(Map<String, Object> data);
	
	/**
	 * 获取会员VIP卡对应的视频包
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getMovieByCardId(Map<String, Object> data);
	
	/**
	 * 获取会员卡信息（初始化出售页面）
	 * @param data
	 * @return
	 */
	Map<String, Object> getSellCardInfo(Map<String, Object> data);
	
	/**
	 * 修改会员卡类型为过期
	 * @param data
	 */
	void updateCardOrderStatus(Map<String, Object> data);
	
	/**
	 * 根据价格获取会员卡类型
	 * @param data
	 * @return
	 */
	Map<String, Object> getMemberCardByPrice(Map<String, Object> data);
	
	/**
	 * 新建卖出会员卡订单
	 * @param data
	 */
	void insertOrder(Map<String, Object> data);
	
	/**
	 * 增加溢出资产到卖家个人信息
	 * @param data
	 */
	void addUserInfoOverProfit(Map<String, Object> data);
}