package com.collection.dao;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author silence
 *
 */
public interface UserInfoMapper {
	/**
	 * 添加用户信息
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertUserInfo(Map<String, Object> data);
	/**
	 * 修改用户信息
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateUserInfo(Map<String, Object> data);
	/**
	 * 获取用户信息
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getUserInfo(Map<String, Object> data);
	/**
	 * 查询使用方后台管理菜单
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getMenuList(Map<String,Object> data);
	/**
	 * 查询使用方后台管理菜单 用户
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getFunctionByUserList(Map<String, Object> data);
	/**
	 * 查询用户数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getUserNum(Map<String, Object> data);
	/**
	 * 店铺数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getShopNum(Map<String, Object> data);
	/**
	 * 查询企业云盘信息
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getCompanyMemory(Map<String, Object> data);
	/**
	 * 获取未使用系统用户
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getNotuseUserList(Map<String, Object> data);
	/**
	 * 获取未使用系统用户数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getNotuseUserListNum(Map<String, Object> data);
	/**
	 * 获取组织名称
	 * @param data
	 * @return
	 * @author silence
	 */
	String getOrganizeNames(Map<String, Object> data);
	
	/**
	 * 根据区域查询用户信息
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUserListByOrganize(Map<String, Object> data);
	/**
	 * 获取用户列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUserList(Map<String, Object> data);
	/**
	 * 获取用户列表数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getUserListNum(Map<String, Object> data);
	/**
	 * 修改用户登录时间
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateUserLogintime(Map<String, Object> data);
	/**
	 * 修改企业登录时间
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateCompanyLogintime(Map<String, Object> data);
	/**
	 * 获取用户统计信息
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUserStatistics(Map<String, Object> data);
	/**
	 * 获取用户新增统计信息
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getNewUserStatistics(Map<String, Object> data);
	
	/**
	 * 获取用户权限列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUserPowerList(Map<String, Object> data);
	/**
	 * 获取企业权限列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getCompanyPowerList(Map<String, Object> data);
	
	
	/**
	 * 查询组织下人员的总数
	 * @param map
	 * @return
	 */
	int getUserListByOrganizeCount(Map<String,Object> map);
	
	/**
	 * 获取权限的最后一次的更新时间
	 * @param map
	 * @return
	 */
	String getUserPowerUpdateTime(Map<String,Object> map);
	
	/**
	 * 清空所有的相同的registrationid
	 * 然后根据userid赋值registrationid
	 * @param map
	 */
	void updateUserRegistrationId(Map<String,Object> map);
}
