package com.collection.dao;
import java.util.Map;
/**
 * 
 * @author silence
 *
 */
public interface IAppLoginMapper {
	/**
	 * 登录
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> login(Map<String, Object> data);
	
	/**
	 * @param data
	 * @return
	 * @author silence
	 * 检验手机号
	 */
	Map<String, Object> checkPhone(Map<String, Object> data);
	
	/**
	 * @param data
	 * @return
	 * @author silence
	 * 检验手机号
	 * @return 
	 */
	void insertUserInfo(Map<String, Object> data);
	
	/**
	 * @param data
	 * @return
	 * @author silence
	 * 根据邀请码获取父ID
	 * @return 
	 */
	int getParentIdByInviteCode(Map<String, Object> data);
	
	/**
	 * @param data
	 * @return
	 * @author silence
	 * 修改密码
	 * @return 
	 */
	void updateUserInfo(Map<String, Object> data);
	
	/**
	 * @param data
	 * @return
	 * @author silence
	 * 获取用户信息
	 * @return data
	 */
	Map<String, Object> getUserInfo(Map<String, Object> data);
}
