package com.collection.dao.chat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
/**
 * 
 * 消息Mapper
 *
 */
public interface ChatMapper {
	
	/**
	 * 消息讨论组 列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getChatGroupList(Map<String,Object> map);
	
	/**
	 * 消息讨论组 信息
	 * @param map
	 * @return
	 */
	Map<String,Object> getChatGroupInfo(Map<String,Object> map);
	/**
	 * app 消息讨论组  记录数
	 * @param map
	 * @return
	 */
	int getChatGroupListCount(Map<String,Object> map);
	
	/**
	 * 新增讨论组
	 * @param map
	 * @return
	 */
	int insertGroup(Map<String,Object> map);
	
	/**
	 * 邀请进讨论组
	 * @param map
	 * @return
	 */
	int insertUserToGroup(Map<String,Object> map);
	
	
	/**
	 * 发送消息 
	 * @param map
	 * @return
	 */
	int insertChatRecord(Map<String,Object> map);
	
	/**
	 * 聊天分组 - 用户表相关操作 
	 * @param map
	 * @return
	 */
	int updateUserGroup(Map<String,Object> map);
	
	
	
	/**
	 * 获取我的群数量
	 * @param map
	 * @return
	 */
	int getMyGroupNum(Map<String,Object> map);
	
	Map<String, Object> getLastRecord(Map<String,Object> map);

	/**
	 * 查询用户信息
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getChatUserList(Map<String, Object> data);
	/**
	 * 修改群信息
	 * @param map
	 * @return
	 */
	void updateGroup(Map<String, Object> data);
	/**
	 * 查询用户群信息
	 * @param map
	 * @return
	 */
	Map<String,Object> getUserGroup(Map<String, Object> data) ;

	/**
	 * 判断是否创建过单聊
	 * @param userid1
	 * @param userid2
	 * @return
	 */
	String isCreateOneCart(@Param("userid1") String userid1,@Param("userid2") String userid2);

	/**
	 * 得到用户的所有聊天会话个数
	 * @param map
	 * @return
	 */
	int getMyAllCartListCount(Map<String, Object> map);

	/**
	 * 得到用户的所有聊天会话 分页
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getMyAllCartList(Map<String, Object> map);

	/**
	 * 查询聊天组成员
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getChatGroupUserList(Map<String, Object> data);
	
	//begin=============================聊天信息 及其状态===================
	/**
	 * 添加消息状态
	 * @param map
	 * @return
	 */
	int insertChatRecordStatus(Map<String, Object> map);
	/**
	 * 标记已读状态
	 * @param map
	 * @return
	 */
	int signChatRecordStatus(Map<String, Object> map);
	
	/**
	 * 标记语音已读状态
	 * @param map
	 * @return
	 */
	int signVoiceChatRecordStatus(Map<String, Object> map);
	
	/**
	 * 获取用户是否有未读信息数
	 * @param map
	 * @return
	 */
	int getChatRecordStatusCount(Map<String, Object> map);
	
	/**
	 * app 聊天记录 列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getChatRecordList(Map<String,Object> map);
	
	/**
	 * app  聊天记录   记录数
	 * @param map
	 * @return
	 */
	int getChatRecordListCount(Map<String,Object> map);

	/**
	 * 清空聊天记录
	 * @param map
	 * @return
	 */
	int deleteChatRecord(Map<String, Object> map);
	
	//end=============================聊天信息 及其状态===================
	
	/**
	 * 查询用户所在群组的信息 
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getGroupListInfoByUserid(Map<String,Object> map);
	
	/**
	 * 根据群组的id查询群组的人数 
	 * @param map
	 * @return
	 */
	int getGroupUserNumByGroupId(Map<String,Object> map);
	
	/**
	 * 根据groupid删除群组中的人员信息
	 * @param map
	 */
	void deleteGroupUserInfo(Map<String,Object> map);
	
	/**
	 * 修改群组中的信息 
	 * @param map
	 */
	void updateGroupInfo(Map<String,Object> map);
	
	/**
	 * 将删除人员未读的信息的状态改为已读
	 * @param map
	 */
	void updateChatRecordStatus(Map<String,Object> map);
}
