package com.collection.dao;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author silence
 *
 */
public interface IManageBackStageMapper {
	/**
	 * 查询用户信息
	 * @author silence
	 * @return
	 */
	Map<String, Object> getUserInfo(Map<String, Object> data);
	
	/**
	 * 获取用户信息总数
	 * @param data
	 * @return
	 */
	int getUserListCount(Map<String, Object> data);
	
	/**
	 * 获取用户信息
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getUserList(Map<String, Object> data);
	
	/**
	 * 修改用户信息
	 * @param data
	 */
	void updateUserInfo(Map<String, Object> data);
	
	
	/**
	 * 首页信息
	 * @return
	 */
	Map<String, Object> getIndexInfo();
	
	/**
	 * 查询通知信息
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getNoticeList(Map<String, Object> data);
	
	/**
	 * 查询信息条数
	 * @param data
	 * @return
	 */
	int getNoticeListCount(Map<String, Object> data);
	
	/**
	 * 获取banner信息
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getBannerList(Map<String, Object> data);
	
	/**
	 * 获取banner信息次数
	 * @param data
	 * @return
	 */
	int getBannerListCount(Map<String, Object> data);
	
	/**
	 * 获取banner信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getBannerInfo(Map<String, Object> data);
	
	/**
	 * 新增banner
	 * @param data
	 */
	void insertBanner(Map<String, Object> data);
	
	/**
	 * 修改banner
	 * @param data
	 */
	void updateBanner(Map<String, Object> data);
	
	/**
	 * 获取广告信息列表
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getAdvertList(Map<String, Object> data);
	
	/**
	 * 获取广告信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getAdvertInfo(Map<String, Object> data);
	
	/**
	 * 获取广告信息次数
	 * @param data
	 * @return
	 */
	int getAdvertListCount(Map<String, Object> data);
	
	/**
	 * 新增广告
	 * @param data
	 */
	void insertAdvert(Map<String, Object> data);
	
	/**
	 * 修改广告
	 * @param data
	 */
	void updateAdvert(Map<String, Object> data);
	
	/**
	 * 删除广告
	 * @param data
	 */
	void deleteAdvert(Map<String, Object> data);
	
	/**
	 * 获取订单列表
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getOrderList(Map<String, Object> data);
	
	/**
	 * 获取订单列表次数
	 * @param data
	 * @return
	 */
	int getOrderListCount(Map<String, Object> data);
	
	/**
	 * 冻结订单双方
	 * @param data
	 */
	void frozenOrder(Map<String, Object> data);
	
	/**
	 * 退还订单给卖家
	 * @param data
	 */
	void returnOrder(Map<String, Object> data);
	
	/**
	 * 审核拒绝更改状态
	 * @param data
	 */
	void updateOrderStatus(Map<String, Object> data);
	
	/**
	 * 获取首页免费电影管理列表
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getIndexMovieList(Map<String, Object> data);
	
	/**
	 * 获取首页免费电影管理列表次数
	 * @param data
	 * @return
	 */
	int getIndexMovieListCount(Map<String, Object> data);
	
	/**
	 * 修改首页免费电影信息
	 * @param data
	 */
	void updateIndexMovie(Map<String, Object> data);
	
	/**
	 * 新增首页免费电影信息
	 * @param data
	 */
	void insertIndexMovie(Map<String, Object> data);
	
	/**
	 * 获取会员卡信息列表
	 * @return
	 */
	List<Map<String, Object>> getMemberCardList(Map<String, Object> data);
	
	/**
	 * 获取会员卡信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getMemberCardInfo(Map<String, Object> data);
	
	/**
	 * 获取会员卡信息总数
	 * @param data
	 * @return
	 */
	int getMemberCardListCount(Map<String, Object> data);
	
	/**
	 * 修改会员卡信息
	 * @param data
	 */
	void updateMemberCard(Map<String, Object> data);
	
	/**
	 * 获取会员等级信息列表
	 * @return
	 */
	List<Map<String, Object>> getLevelList(Map<String, Object> data);
	
	/**
	 * 获取会员等级信息总数
	 * @param data
	 * @return
	 */
	int getLevelListCount(Map<String, Object> data);
	
	/**
	 * 修改会员等级信息
	 * @param data
	 */
	void updateLevel(Map<String, Object> data);
	
	/**
	 *  获取审核信息列表
	 * @return
	 */
	List<Map<String, Object>> getCertificationList(Map<String, Object> data);
	
	/**
	 * 获取审核信息列表总数
	 * @param data
	 * @return
	 */
	int getCertificationListCount(Map<String, Object> data);
	
	/**
	 * 更改用户表认证状态
	 * @param data
	 */
	void updateUserCertification(Map<String, Object> data);
	
	/**
	 * 审核实名认证
	 * @param data
	 */
	void updateCertification(Map<String, Object> data);
	
	/**
	 *  获取收款方式信息列表
	 * @return
	 */
	List<Map<String, Object>> getPaymentMethodList(Map<String, Object> data);
	
	/**
	 * 获取收款方式信息列表总数
	 * @param data
	 * @return
	 */
	int getPaymentMethodListCount(Map<String, Object> data);
	
	/**
	 *  获取投诉建议列表
	 * @return
	 */
	List<Map<String, Object>> getQuestionList(Map<String, Object> data);
	
	/**
	 * 获取投诉建议列表总数
	 * @param data
	 * @return
	 */
	int getQuestionListCount(Map<String, Object> data);
	
	/**
	 * 回复投诉
	 * @param data
	 */
	void replyQuestion(Map<String, Object> data);
	
	/**
	 *  获取兑换记录列表
	 * @return
	 */
	List<Map<String, Object>> getExchangeList(Map<String, Object> data);
	
	/**
	 * 获取兑换记录列表总数
	 * @param data
	 * @return
	 */
	int getExchangeListCount(Map<String, Object> data);
	
	/**
	 * 获取会员卡视频管理列表
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getMemberMovieList(Map<String, Object> data);
	
	/**
	 * 获取会员卡视频管理列表次数
	 * @param data
	 * @return
	 */
	int getMemberMovieListCount(Map<String, Object> data);
	
	/**
	 * 修改会员卡视频信息
	 * @param data
	 */
	void updateMemberMovie(Map<String, Object> data);
	
	/**
	 * 新增会员卡视频信息
	 * @param data
	 */
	void insertMemberMovie(Map<String, Object> data);
	
	/**
	 * 获取系统消息列表
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getSysNoticeList(Map<String, Object> data);
	
	/**
	 * 获取系统消息列表列表次数
	 * @param data
	 * @return
	 */
	int getSysNoticeListCount(Map<String, Object> data);
	
	/**
	 * 发送系统通知消息
	 * @param data
	 */
	void sendSysNotice(List<Map<String, Object>> data);
	
	/**
	 * 获取抢购概率列表
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getRateList(Map<String, Object> data);
	
	/**
	 * 获取抢购概率列表次数
	 * @param data
	 * @return
	 */
	int getRateListCount(Map<String, Object> data);
	
	/**
	 * 修改抢购概率信息
	 * @param data
	 */
	void updateRate(Map<String, Object> data);
	
	/**
	 * 新增抢购概率信息
	 * @param data
	 */
	void insertRate(Map<String, Object> data);
	
	/**
	 * 获取买卖双方订单信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getOrderBuyAndSellInfo(Map<String, Object> data);
}
