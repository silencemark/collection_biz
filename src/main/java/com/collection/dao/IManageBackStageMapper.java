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
}
