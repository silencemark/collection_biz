package com.collection.dao;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author silence
 *
 */
public interface ISystemMapper {
	/**
	 * 查询用户信息
	 * @author silence
	 * @return
	 */
	Map<String, Object> getUserInfo(Map<String, Object> data);
	/**
	 * 修改用户信息
	 * @author silence
	 * @return
	 */
	void updateUserInfo(Map<String, Object> data);

	/**
	 * 新增通知 
	 * @param data
	 */
	void insertNotice(Map<String, Object> data);
}
