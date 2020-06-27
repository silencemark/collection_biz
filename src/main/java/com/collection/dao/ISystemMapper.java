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
	 * 获取首页普通信息
	 * @author silence
	 * @return
	 */
	Map<String, Object> getIndexInfo(Map<String, Object> data);
	
	/**
	 * 获取首页信息列表
	 * @author silence
	 * @return
	 */
	List<Map<String, Object>> getNoticeList(Map<String, Object> data);
	/**
	 * 获取首页信息数量
	 * @author silence
	 * @return
	 */
	int getNoticeListNum(Map<String, Object> data);

	/**
	 * 获取首公告总数量
	 * @author dsl
	 * @return
	 */
	int getSystemMessageCount(Map<String, Object> map);
	/**
	 * 获取首公告列表
	 * @author dsl
	 * @return
	 */
	List<Map<String, Object>> getSystemMessage(Map<String, Object> map);
	/**
	 * 新增公告
	 * @author dsl
	 * @return
	 */
	void insertSystemMessage(Map<String, Object> map);
	
	/**
	 * 修改公告
	 * @author dsl
	 * @return
	 */
	void updataSystemMessageDetail(Map<String, Object> map);
	/**
	 *系统反馈总数
	 * @author dsl
	 * @return
	 */
	int getSystemBackCount(Map<String, Object> map);
	/**
	 *系统反馈列表
	 * @author dsl
	 * @return
	 */
	List<Map<String, Object>> getSystemBacklist(Map<String, Object> map);
	
	/**
	 *系统反馈处理
	 * @author dsl
	 * @return
	 */
	void updataSystemBack(Map<String, Object> map);
	/**
	 * 新增系统反馈回复
	 * @author dsl
	 * @return
	 */
	void insertSystemBackReply(Map<String, Object> map);
	/**
	 * 查询系统反馈回复
	 * @author dsl
	 * @return
	 */
	Map<String, Object> getSystemBackReply(Map<String, Object> map);

	/**
	 * 查询管理方用户列表
	 * @author silence
	 * @return
	 */
	List<Map<String, Object>> getAdminUserList(Map<String, Object> data);
	/**
	 * 查询管理方用户列表数量
	 * @author silence
	 * @return
	 */
	int getAdminUserListNum(Map<String, Object> data);	
	/**
	 * 修改管理方信息
	 * @author silence
	 * @return
	 */
	void updateAdminUser(Map<String, Object> data);
	/**
	 * 添加管理方用户信息
	 * @author silence
	 * @return
	 */
	void insertAdminUser(Map<String, Object> data);
	/**
	 * 添加管理方操作日志信息
	 * @author silence
	 * @return
	 */
	List<Map<String, Object>> getLogList(Map<String, Object> data);
	int getLogListNum(Map<String, Object> data);
	
	
	/**
	 * 查询每日报表字典类型列表
	 * @author silence
	 * @return
	 */
	List<Map<String, Object>> getEverydayTypeList(Map<String, Object> data);
	/**
	 * 查询每日报表字典列表
	 * @author silence
	 * @return
	 */
	List<Map<String, Object>> getEverydayDataList(Map<String, Object> data);
	/**
	 *查询pc头部设置
	 * @author dsl
	 * @return
	 */
	List<Map<String, Object>> getSystemPctop(Map<String, Object> map);
	int getSystemPctopCount(Map<String, Object> map);
	/**
	 *修改pc头部设置
	 * @author dsl
	 * @return
	 */
	void updateSystemPctop(Map<String, Object> map);
	/**
	 *查询系统模块
	 * @author dsl
	 * @return
	 */
	List<Map<String, Object>> getSystemPctopModular(Map<String, Object> map);
	
	/**
	 *新增头部菜单
	 * @author dsl
	 * @return
	 */
	void insertSystemPctop(Map<String, Object> map);
	
	int getSystemDictTypeCount(Map<String, Object> map);
	/**
	 * 字典管理类型
	 * @author dsl
	 * @return
	 */
	List<Map<String, Object>> getSystemDictType(Map<String, Object> map);
	/**
	 * 字典管理
	 * @author dsl
	 * @return
	 */
	List<Map<String, Object>> getSystemDict(Map<String, Object> map);
	/**
	 *修改字典管理
	 * @author dsl
	 * @return
	 */
	void updateSystemDict(Map<String, Object> map);
	/**
	 *新增字典管理
	 * @author dsl
	 * @return
	 */
	void insertSystemDict(Map<String, Object> map);
	/**
	 *	轮番图查询
	 * @author dsl
	 * @return
	 */
	List<Map<String, Object>> getManageBanner(Map<String, Object> map);
	/**
	 *	轮番图修改
	 * @author dsl
	 * @return
	 */
	void updateManageBanner(Map<String, Object> map);
	/**
	 *	轮番图新增
	 * @author dsl
	 * @return
	 */
	void insertManageBanner(Map<String, Object> map);
	/**
	 *	轮番图排序查询
	 * @author dsl
	 * @return
	 */
	List<Map<String, Object>> getManageBannerSort(Map<String, Object> map);
	
	/**
	 *	APP参数配置查询
	 * @author dsl
	 * @return
	 */
	List<Map<String, Object>> getManageaAppconfig(Map<String, Object> map);
	/**
	 *	APP参数配置修改
	 * @author dsl
	 * @return
	 */
	void updateManageAppconfig(Map<String, Object> map);
	/**
	 *	APP参数配置新增
	 * @author dsl
	 * @return
	 */
	void insertManageAppconfigr(Map<String, Object> map);
	/**
	 *	查询当前排序最大值
	 * @author dsl
	 * @return
	 */
	int getManageaAppconfigOrder(Map<String, Object> map);
	/**
	 * 添加每日报表字典
	 * @author silence
	 * @return
	 */
	void insertDailyReportData(Map<String, Object> data);
	
	/**
	 * 修改每日报表字典
	 * @author silence
	 * @return
	 */
	void updateDailyReportData(Map<String, Object> data);
}
