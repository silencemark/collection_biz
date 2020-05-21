package com.collection.dao;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author silence
 *
 */
public interface IndexMapper {
	/**
	 * 新增 到 图片/文件 表
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertfile(Map<String, Object> data);
	/**
	 * 查询发布范围用户表
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getReleaseRange(Map<String, Object> data);
	/**
	 * 新增发布范围用户表
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertReleaseRangeUser(Map<String, Object> data);
	/**
	 * 查询发布范围
	 * @param map
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getRangeList(Map<String, Object> data);
	/**
	 * 新增发布范围
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertReleaseRange(Map<String, Object> data);
	/**
	 * 新增提醒信息
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertRemindInfo(Map<String, Object> data);
	/**
	 * 新提醒列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getRemindList(Map<String, Object> data);
	/**
	 * 新提醒总数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getRemindnum(Map<String, Object> data);
	
	/**
	 * 获取首页banner
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getHomePageBanner(Map<String, Object> data);
	/**
	 * banner
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getBannerList(Map<String, Object> data);
	/**
	 * 查询转发表未读数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getForwordNotreadNum(Map<String, Object> data);
	/**
	 * 查询发布范围人员表未读数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getRangeNotreadNum(Map<String, Object> data);
	
	/**
	 * 组织列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getOrganizeList(Map<String, Object> data);
	/**
	 * 组织下的成员集合
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUserList(Map<String, Object> data);
	
	/**
	 * 查询企业通讯录人员信息
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getUserListnew(Map<String, Object> data);
	
	/**
	 * 查询企业通讯录总人数
	 * @param map
	 * @return
	 */
	int getUserListnewCount(Map<String,Object> map);
	
	/**
	 * 查询城市列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getCityList(Map<String, Object> data);
	/**
	 * 查询当前人的所有所属组织机构
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getOrganizeListByUser(Map<String, Object> data);
	/**
	 * 新增转发数据
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertForword(Map<String, Object> data);
	/**
	 * 获取文件列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getFileList(Map<String, Object> data);
	/**
	 * 获取分享的点赞列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String,Object>> getPraiseList(Map<String, Object> data);
	/**
	 * 获取评论列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getCommentList(Map<String, Object> data);
	/**
	 * 添加分享圈 未读数记录表
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertShareNotread(Map<String, Object> data);
	/**
	 * 获取未读数量数据
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getNotreadMap(Map<String, Object> data);
	
	
	/**
	 * 新增评论信息
	 * @param data
	 */
	void insertComment(Map<String, Object> data);
	
	/**
	 * 修改转发状态为已读
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateForwardReadStatus(Map<String, Object> data);
	
	/**
	 * 修改发布范围状态为已读
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateRangeReadStatus(Map<String, Object> data);
	/**
	 * 获取字典列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getDictData(Map<String, Object> data);
	/**
	 * 修改范围未读
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateRangeIsread(Map<String, Object> data);
	/**
	 * 修改转发未读
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateForwardIsread(Map<String, Object> data);
	/**
	 * 修改系统公告已读
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateSystemMessageStatus(Map<String, Object> data);
	
	/**
	 * 获取和风天气 城市id
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getCityId(Map<String, Object> data);
	/**
	 * 查询用户 模糊搜索
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUserBySearch(Map<String, Object> data);
	/**
	 * 查询店铺 模糊搜索
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getOrganizeBySearch(Map<String, Object> data);
	/**
	 * 查询组织信息
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getOrganizeInfo(Map<String, Object> data);
	
	/**
	 * 查询组织下的 禁用人员
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUserListDisabled(Map<String, Object> map);
	
	/**
	 * 模糊查询组织下的 禁用人员
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUserBySearchDisabled(Map<String, Object> data);
	
	/**
	 * 查询组织下所有未被删除的用户
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUserListAll(Map<String, Object> map);
	/**
	 * 删除发布范围
	 * @param data
	 * @return
	 * @author silence
	 */
	void deleteRange(Map<String, Object> data);
	/**
	 * 查询填写范围的
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getOrganizeListByRange(Map<String, Object> data);
	
	int getUserListAllCount(Map<String, Object> map);
	
	/**
	 * 获取天气图标地址
	 * @param data
	 * @return
	 * @author silence
	 */
	String getWeatherUrl(String getWeatherUrl);
	
	/**
	 * 添加天气情况
	 * @param map
	 */
	void insertWeatherInfo(Map<String,Object> map);
	
	/**
	 * 修改该单子所有的人 未读
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateIsreadAll(Map<String, Object> data);
	
	/**
	 * 添加管理日志
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertManageLog(Map<String, Object> data);
	/**
	 * 查询公司所有店面
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getAllShopList(Map<String, Object> data);
	/**
	 * 修改提醒
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateRemind(Map<String, Object> data);
	/**
	 * 切换店面
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getNextOrgainizePurchase(Map<String, Object> data);
	
	/**
	 * 查询 归还 日期到期 的集合
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReturnAmountList();
	
	/**
	 * 查询 同意归还 日期到期 的集合
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReturnAmountListagree();
	
	/**
	 * 根据发布范围查询组织架构中的店面
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getShopListByReleaseRange(Map<String,Object> map);
	
	/**
	 * 根据发布范围查询组织架构 
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getOrganizeListByReleaseRange(Map<String,Object> map);
	
	/**
	 * 查询字典数据
	 * @param map
	 * @return
	 */
	Map<String,Object> getDataList(Map<String,Object> map);
	/**
	 * 根据每日报表发布范围查询组织架构 
	 * @param map
	 * @return
	 */
	
	List<Map<String, Object>> getOrganizeListByDailyReport(Map<String, Object> data);
	
	/**
	 * 减少组织子集数量
	 * @param map
	 * @return
	 */
	void reduceOrganizeNum(Map<String, Object> data);
	/**
	 * 查询模块版本号
	 * @param pkey
	 * @return
	 */
	String getSysParamByModel(String pkey);
	
	/**
	 * 更新模块版本号
	 * @param map
	 * @return
	 */
	int updateSysParam(Map<String, Object> data);
	
	/**
	 * 根据organizeid查询组织下面所有的人员信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getUserListByOrganizeid(Map<String,Object> map);
	
	/**
	 * 查询某组织机构下的所有的组织信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getDownOrganizeListByDatacode(Map<String,Object> map);
	
	/**
	 * 根据userid查询设备id
	 * @param map
	 * @return
	 */
	String getRegistrationIdByUserId(Map<String,Object> map);
	
	/**
	 * 删除用户关联的信息
	 * @param map
	 */
	void deleteForwaredUserInfo(Map<String,Object> map);
	
	/**
	 * 添加用户的使用日志
	 * @param map
	 */
	void insertUserUseLog(Map<String,Object> map);
	
}
