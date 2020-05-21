package com.collection.dao.oa;
import java.util.List;
import java.util.Map;
/**
 * oa办公管理
 * @author silence
 *
 */
public interface OfficeMapper {
	
	/**
	 * 添加企业简报模版
	 * @param map
	 * @return
	 */
	void insertCompanyModule(Map<String,Object> map);
	/**
	 * 查询企业简报模版列表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getCompanyModuleList(Map<String,Object> map);
	
	/**
	 * 查询企业简报模板列表总数
	 * @param map
	 * @return
	 */
	int getCompanyModuleListCount(Map<String,Object> map);
	
	/**
	 * 修改企业简报栏目信息
	 * @param map
	 */
	void updateCompanyModuleInfo(Map<String,Object> map);
	/**
	 * 添加企业简报
	 * @param map
	 * @return
	 */
	void insertBrief(Map<String, Object> data);
	/**
	 * 查询企业简报列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getBriefList(Map<String,Object> map);
	/**
	 * 查询企业简报列表数量
	 * @param map
	 * @return
	 * @author silence
	 */
	int getBriefListNum(Map<String,Object> map);
	/**
	 * 查询企业简报详情
	 * @param map
	 * @return
	 * @author silence
	 */
	Map<String, Object> getBriefInfo(Map<String,Object> map);
	
	/**
	 * 修改企业简报信息
	 * @param map
	 */
	void updateBriefInfo(Map<String,Object> map);
	
	
	/**
	 * 查询餐饮大师云盘列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getSystemCloudList(Map<String,Object> map);
	/**
	 * 查询餐饮大师云盘数量
	 * @param map
	 * @return
	 * @author silence
	 */
	int getSystemCloudListNum(Map<String,Object> map);
	/**
	 * 添加企业简报模版
	 * @param map
	 * @return
	 * @author silence
	 */
	void insertCompanyCloudModule(Map<String,Object> map);
	/**
	 * 查询企业简报模版列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getCompanyCloudModuleList(Map<String,Object> map);
	
	/**
	 * 查询企业文件夹总数量
	 * @param map
	 * @return
	 */
	int getCompanyCloudModuleListCount(Map<String,Object> map);
	
	/**
	 * 查询企业云盘列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getCompanyCloudList(Map<String,Object> map);
	/**
	 * 查询企业云盘数量
	 * @param map
	 * @return
	 * @author silence
	 */
	int getCompanyCloudListNum(Map<String,Object> map);
	/**
	 * 获取用户生日 员工关怀 列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUserBirthdayList(Map<String,Object> map);
	/**
	 * 获取用户生日 员工关怀 列表 数量
	 * @param map
	 * @return
	 * @author silence
	 */
	int getUserBirthdayListNum(Map<String,Object> map);
	/**
	 * 荣誉榜用户 列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUserRewardList(Map<String,Object> map);
	/**
	 * 荣誉榜用户数量
	 * @param map
	 * @return
	 * @author silence
	 */
	int getUserRewardListNum(Map<String,Object> map);
	
	/*---------------------------------------------备用金---------------------------------------*/
	/**
	 * 添加备用金申请
	 * @param map
	 * @return
	 * @author silence
	 */
	void insertReserveAmount(Map<String,Object> map);
	
	/**
	 * 备用金申请列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReserveAmountList(Map<String,Object> map);
	/**
	 * 备用金申请数量
	 * @param map
	 * @return
	 * @author silence
	 */
	int getReserveAmountListNum(Map<String,Object> map);
	/**
	 * 备用金详情
	 * @param map
	 * @return
	 * @author silence
	 */
	Map<String, Object> getReserveAmountInfo(Map<String, Object> data);
	/**
	 * 备用金审核（修改）
	 * @param map
	 * @return
	 * @author silence
	 */
	void updateReserveAmount(Map<String, Object> data);
	
	/*---------------------------------------------任务---------------------------------------*/
	/**
	 * 添加任务
	 * @param map
	 * @return
	 * @author silence
	 */
	void insertTask(Map<String, Object> data);
	/**
	 * 添加到协办人员表
	 * @param map
	 * @return
	 * @author silence
	 */
	void insertTaskAssist(Map<String, Object> data);
	/**
	 * 任务列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getTaskList(Map<String,Object> map);
	/**
	 * 任务数量
	 * @param map
	 * @return
	 * @author silence
	 */
	int getTaskListNum(Map<String,Object> map);
	
	/**
	 * 查询任务的时间的列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getTaskTimeList(Map<String,Object> map);
	
	/**
	 * 查询任务时间列表的总数
	 * @param map
	 * @return
	 */
	int getTaskTimsListCount(Map<String,Object> map);
	
	/**
	 * 任务协办人员列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getTaskAssisList(Map<String, Object> data);
	/**
	 * 获取任务信息
	 * @param map
	 * @return
	 * @author silence
	 */
	Map<String, Object> getTaskInfo(Map<String, Object> data);
	/**
	 * 修改任务信息 （完成）
	 * @param map
	 * @return
	 * @author silence
	 */
	void updateTask(Map<String, Object> data);
	
	/*---------------------------------------------请假单---------------------------------------*/
	/**
	 * 添加请假单信息
	 * @param map
	 * @return
	 * @author silence
	 */
	void insertLeave(Map<String, Object> data);
	/**
	 * 请假单列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getLeaveList(Map<String,Object> map);
	/**
	 * 请假单数量
	 * @param map
	 * @return
	 * @author silence
	 */
	int getLeaveListNum(Map<String,Object> map);
	
	/**
	 * 查询请假单时间列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getLeaveTimesList(Map<String,Object> map);
	
	/**
	 * 查询请假单时间列表的总数
	 * @param map
	 * @return
	 */
	int getLeaveTimesCount(Map<String,Object> map);
	
	/**
	 * 请假详情
	 * @param map
	 * @return
	 * @author silence
	 */
	Map<String,Object> getLeaveInfo(Map<String, Object> data);
	/**
	 * 审批请假单 （修改）
	 * @param map
	 * @return
	 * @author silence
	 */
	void updateLeave(Map<String,Object> map);
	
	
	/*---------------------------------------------报销单---------------------------------------*/
	
	
	/**
	 * 今日报销单数量
	 * @param map
	 * @return
	 * @author silence
	 */
	int getExpenseNumByCompany(Map<String, Object> data);
	/**
	 * 添加报销单
	 * @param map
	 * @return
	 * @author silence
	 */
	void insertExpense(Map<String, Object> data);
	
	/**
	 * 查询报销单时间列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getExpenseTimesList(Map<String,Object> map);
	
	/**
	 * 查询报销单时间列表的总数
	 * @param map
	 * @return
	 */
	int getExpenseTimesListCount(Map<String,Object> map);
	
	/**
	 * 添加报销明细单
	 * @param map
	 * @return
	 * @author silence
	 */
	void insertExpenseDetail(Map<String, Object> data);
	/**
	 * 报销单列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getExpenseList(Map<String,Object> map);
	/**
	 * 报销单数量
	 * @param map
	 * @return
	 * @author silence
	 */
	int getExpenseListNum(Map<String,Object> map);
	/**
	 * 报销单详情
	 * @param map
	 * @return
	 * @author silence
	 */
	Map<String, Object> getExpenseInfo(Map<String, Object> data);
	/**
	 * 报销详情列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getExpenseDetailList(Map<String, Object> data);
	
	/**
	 * 报销单修改
	 * @param map
	 * @return
	 * @author silence
	 */
	void updateExpense(Map<String, Object> data);
	
	/*---------------------------------------------请示单---------------------------------------*/
	
	/**
	 * 请示单添加
	 * @param map
	 * @return
	 * @author silence
	 */
	void insertRequest(Map<String, Object> data);
	/**
	 * 请示列表
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getRequestList(Map<String,Object> map);
	/**
	 * 请示单数量
	 * @param map
	 * @return
	 * @author silence
	 */
	int getRequestListNum(Map<String,Object> map);
	
	/**
	 * 查询请示单时间列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getRequestTimesList(Map<String,Object> map);
	
	/**
	 * 查询请示单时间列表的总数量
	 * @param map
	 * @return
	 */
	int getRequestTimesCount(Map<String,Object> map);
	
	/**
	 * 请示详情
	 * @param map
	 * @return
	 * @author silence
	 */
	Map<String, Object> getRequestInfo(Map<String, Object> data);
	/**
	 * 审批（修改）请示
	 * @param map
	 * @return
	 * @author silence
	 */
	void updateRequest(Map<String,Object> data);
	/**
	 * 获取简报未读数量
	 * @param map
	 * @return
	 * @author silence
	 */
	int getBriefNotreadNum(Map<String, Object> data);
	
	/**
	 * 备用金申请 根据时间
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReserveAmountByDate(Map<String, Object> data);
	/**
	 * 备用金申请数量 根据时间
	 * @param map
	 * @return
	 * @author silence
	 */
	int getReserveAmountByDateNum(Map<String, Object> data);
	/**
	 * 备用金申请 根据时间
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReserveByDate(Map<String, Object> data);
	
	
	
	
	
	
	
	
	
	
	/**
	 * 查询公司的企业网盘信息
	 * @param map
	 * @return
	 */
	Map<String,Object> getCompanyCloudInfo(Map<String,Object> map);
	
	/**
	 * 添加扩容信息
	 * @param map
	 */
	void insertCloudCapacityInfo(Map<String,Object> map);
	
	/**
	 * 查询扩容记录信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getCloudCapacityList(Map<String,Object> map);
	
	/**
	 * 查询扩容记录总数
	 * @param map
	 * @return
	 */
	int getCloudCapacityCount(Map<String,Object> map);
	
	/**
	 * 查询公司申请的总的云空间大小
	 * @param map
	 * @return
	 */
	String getCloudCapacitySjmemoryCount(Map<String,Object> map);
	
	/**
	 * 修改云盘文件夹信息
	 * @param map
	 */
	void updateCompanyCloudModuleInfo(Map<String,Object> map);
	
	
	
	/**
	 * 查询云盘文件
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getCompanyCloudFileList(Map<String,Object> map);
	
	/**
	 * 查询云盘文件的总数
	 * @param map
	 * @return
	 */
	int getCompanyCloudFileListCount(Map<String,Object> map);
	
	/**
	 * 添加文件信息
	 * @param map
	 */
	void insertCompanyCloudFileInfo(Map<String,Object> map);
	
	/**
	 * 修改文件信息
	 * @param map
	 */
	void updateCompanyCloudFileInfo(Map<String,Object> map);
	
	/**
	 * 查询文件详情
	 * @param map
	 * @return
	 */
	Map<String,Object> getCompanyCloudFileInfo(Map<String,Object> map);
	
	/**
	 * 修改内存大小
	 * @param map
	 */
	void updateCompanyCloudUseMemoryInfo(Map<String,Object> map);
	
	/**
	 * 使用放后台查询企业简报信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getBriefListInfo(Map<String,Object> map);
	
	/**
	 * 使用方后台查询查询企业简报总数
	 * @param map
	 * @return
	 */
	int getBriefListCount(Map<String,Object> map);
	
	/**
	 * 查询还有一个小时结束的任务信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getOverTaskOneHour(Map<String,Object> map);
	
}