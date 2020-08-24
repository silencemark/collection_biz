package com.collection.dao;
import java.util.List;
import java.util.Map;
/**
 * 个人中心相关
 * @author silence
 *
 */
public interface IAppUserCenterMapper {
	/**
	 * 进入个人中心
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getMyCenter(Map<String, Object> data);
	
	/**
	 * 获取提现次数
	 * @param data
	 * @return
	 */
	Map<String, Object> getCashOutNum(Map<String, Object> data);
	
	/**
	 * 签到
	 * @param data
	 */
	void signIn(Map<String, Object> data);
	
	/**
	 * 获取当天签到信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getSignTodays(Map<String, Object> data);
	
	/**
	 * 根据用户信息的当前成长值匹配修改会员等级
	 * @param data
	 */
	void updateUserInfoLevel(Map<String, Object> data);
	
	/**
	 * 获取用户当前用户等级 和 当前成长值对应的用户等级信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getUserNewOldLevel(Map<String, Object> data);
	
	/**
	 * 获取的会员体系成长值
	 * @param data
	 * @return
	 */
	Map<String, Object> myGrowthValue(Map<String, Object> data);
	
	/**
	 * 获取所有的会员等级名称及成长值
	 * @return
	 */
	List<Map<String, Object>> getMemberGrowList();
	
	/**
	 * 新增签到记录
	 * @param data
	 */
	void insertSign(Map<String, Object> data);
	
	/**
	 * 新增xgo记录明细
	 * @param data
	 */
	void addXgoRecord(Map<String, Object> data);
	
	/**
	 * 进行认证
	 * @param data
	 */
	void certification(Map<String, Object> data);
	
	/**
	 * 修改用户信息实名认证
	 * @param data
	 */
	void updateUserCertification(Map<String, Object> data);
	
	/**
	 * 获取实名认证进度
	 * @param data
	 * @return
	 */
	Map<String, Object> getCertification(Map<String, Object> data);
	
	/**
	 * 我的子集收益集合
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getSonProfit(Map<String, Object> data);
	
	/**
	 * 我的孙子集收益集合
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getGrandSonProfit(Map<String, Object> data);
	
	/**
	 * 我的资产
	 * @param data
	 * @return
	 */
	Map<String, Object> myAssets(Map<String, Object> data);
	
	/**
	 * 兑换记录
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getExchangeList(Map<String, Object> data);
	
	/**
	 * 新增兑换记录
	 * @param data
	 */
	void insertExchange(Map<String, Object> data);
	
	/**
	 * 获取我的邀请码 和 qr邀请二维码
	 * @param data
	 * @return
	 */
	Map<String, Object> myInviteCode(Map<String, Object> data);
	
	/**
	 * 更新二维码地址入库
	 * @param data
	 */
	void updateQrcode(Map<String, Object> data);
	
	
	/**
	 * 获取个人资料信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getMyUserInfo(Map<String, Object> data);
	
	/**
	 * 查询上级用户信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getParentUserInfo(Map<String, Object> data);
	
	
	/**
	 * 修改用户信息
	 * @param data
	 */
	void updateUserInfo(Map<String, Object> data);
	
	/**
	 * 获取收款方式
	 * @param data
	 * @return
	 */
	Map<String, Object> getPaymentMethod(Map<String, Object> data);
	
	/**
	 * 新增收款方式
	 * @param data
	 */
	void addPaymentMethod(Map<String, Object> data );
	
	/**
	 * 修改付息收款方式
	 * @param data
	 */
	void updatePaymentMethod(Map<String, Object> data);
	
	/**
	 * 获取我提过的问题及回复
	 * @return
	 */
	List<Map<String, Object>> getMyQuestion(Map<String, Object> data);
	
	/**
	 * 提交我的提问及投诉建议
	 * @param data
	 */
	void addMyQuestion(Map<String, Object> data);
	
	/**
	 * 获取用户的通知信息列表
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getUserNotice(Map<String, Object> data);
	
	/**
	 * 获取用户的通知信息列表数量
	 * @param data
	 * @return
	 */
	int getUserNoticeCount(Map<String, Object> data);
	
	/**
	 * 获取用户的消息未读数量
	 * @param data
	 * @return
	 */
	Map<String, Object> getNoticeUnreadNum(Map<String, Object> data);
	
	/**
	 *  改变用户通知消息 的未读/已读状态
	 * @param data
	 */
	void updateNoticeStatus(Map<String, Object> data);
	
	/**
	 * 获取xgo明细记录列表信息
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getXgoRecord(Map<String, Object> data);
	
	/**
	 * 减少xgo数量
	 * @param data
	 */
	void reduceUserXgo(Map<String, Object> data);
	
	/**
	 * 增加xgo数量
	 * @param data
	 */
	void addUserXgo(Map<String, Object> data);
	
	/**
	 * 获取获取排行榜用户信息
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getRank(Map<String, Object> data);
	
	/**
	 * 获取推荐收益排行榜用户信息
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getExtensionRank(Map<String, Object> data);
	
	/**
	 * 获取下级有效用户个数 和 总下级个数
	 * @param data
	 * @return
	 */
	Map<String, Object> getEffectiveUserCount(Map<String, Object> data);
	
	/**
	 * 查询我的收货地址列表
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getAddressList(Map<String, Object> data);
	
	/**
	 * 修改收货地址
	 * @param data
	 */
	void updateAddress(Map<String, Object> data);
	
	/**
	 * 新增收货地址
	 * @param data
	 */
	void insertAddress(Map<String, Object> data);
	
	/**
	 * 删除收货地址
	 * @param data
	 */
	void deleteAddress(Map<String, Object> data);
}
