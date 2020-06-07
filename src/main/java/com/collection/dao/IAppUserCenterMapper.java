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
	 * 签到
	 * @param data
	 */
	void signIn(Map<String, Object> data);
	
	/**
	 * 根据用户信息的当前成长值匹配修改会员等级
	 * @param data
	 */
	void updateUserInfoLevel(Map<String, Object> data);
	
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
	 * 进行认证
	 * @param data
	 */
	void certification(Map<String, Object> data);
	
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
}
