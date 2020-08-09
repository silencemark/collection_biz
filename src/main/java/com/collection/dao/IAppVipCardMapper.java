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
	 * 获取会员卡对应的手办列表
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getGarageKitList(Map<String, Object> data);
	
	/**
	 * 获取手办数量
	 * @param data
	 * @return
	 */
	int getGarageKitListCount(Map<String, Object> data);
	
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
	 * 用户加成长值
	 * @param data
	 */
	void addGrowthValue(Map<String, Object> data);
	
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
	 * 获取我的历史会员卡列表
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getMyHisCardList(Map<String, Object> data);
	
	/**
	 * 根据订单id获取会员卡信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getMemberCardInfo(Map<String, Object> data);
	
	/**
	 * 根据id获取会员卡信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getMemberCardInfoById(Map<String, Object> data);
	
	/**
	 * 获取可出售订单id列表
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getSellOrderListByid(Map<String, Object> data);
	
	/**
	 * 获取到期订单列表ID
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getDueOrderListByid(Map<String, Object> data);
	
	/**
	 * 获取会员VIP卡对应的视频包 废弃
	 * @param data
	 * @return
	 */
	//List<Map<String, Object>> getMovieByCardId(Map<String, Object> data);
	
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
	 * 获取当天总订单数量+1
	 * @return
	 */
	int getOrderNum();
	
	/**
	 * 增加溢出资产到卖家个人信息
	 * @param data
	 */
	void addUserInfoOverProfit(Map<String, Object> data);
	
	/**
	 * 增加溢出记录
	 * @param data
	 */
	void insertOverFlow(Map<String, Object> data);
	
	/**
	 * 参与抢购中
	 * @param data
	 */
	void insertRushToBuy(Map<String, Object> data);
	

	/**
	 * 获取我的会员卡列表（抢购中）
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getRushToBuyList(Map<String, Object> data);
	
	/**
	 * 获取我当天抢购的记录
	 * @param data
	 * @return
	 */
	Map<String, Object> getRushToBuyById(Map<String, Object> data);
	
	/**
	 * 回退到待出售状态订单
	 * @param data
	 */
	void updateWaitSell(Map<String, Object> data);
	
	/**
	 * 修改当天抢购状态
	 * @param data
	 */
	void updateRushToBuy(Map<String, Object> data);
	
	/**
	 * 冻结买家/卖家账号
	 * @param data
	 */
	void frozenOrder(Map<String, Object> data);
	
	/**
	 * 获取买家/卖家 超过支付时间的订单
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getMoreThanWaitTime(Map<String, Object> data);
	
	/**
	 * 获取买家/卖家 超过审核时间的订单
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getMoreThanExamineTime(Map<String, Object> data);
	
	/**
	 * 评论视频
	 * @param data
	 */
	void commentMovie(Map<String, Object> data);
	
	/**
	 * 新增订单评论次数
	 * @param data
	 */
	void addOrderCommentCount(Map<String, Object> data);
	
	/**
	 * 浏览电影热度加1  type  1 普通电影  2 会员电影
	 * @param data
	 */
	void addMovieHot(Map<String, Object> data);
	
	/**
	 * 获取奖励总金额
	 * @param data
	 * @return
	 */
	double getSumRewardPrice(Map<String, Object> data);
	
	/**
	 * 新增奖励记录
	 * @param data
	 */
	void addRewardRecord(Map<String, Object> data);
	
	/**
	 * 查询手办详情
	 * @param data
	 * @return
	 */
	Map<String, Object> getGarageKitInfo(Map<String, Object> data);
	
	/**
	 * 获取喜欢人的头像
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getGarageLikeHeadimage(Map<String, Object> data);
	
	/**
	 * 喜欢/取消喜欢手办
	 * @param data
	 */
	void likeGarageKit(Map<String, Object> data);
	
	/**
	 * 查询是否喜欢
	 * @param data
	 * @return
	 */
	Map<String, Object> getLikeGarageKit(Map<String, Object> data);
	
	/**
	 * 添加喜欢
	 * @param data
	 */
	void insertLikeGarageKit(Map<String, Object> data);
	
	/**
	 * 修改喜欢
	 * @param data
	 */
	void updateGarageKitLike(Map<String, Object> data);
}
