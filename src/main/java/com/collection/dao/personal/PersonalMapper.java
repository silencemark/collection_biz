package com.collection.dao.personal;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author silence
 *
 */
public interface PersonalMapper {
	 
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
	 * 获取当前用户kpi星值 总数
	 * @param data
	 * @return
	 * @author silence
	 */
	int getSumKPIStar(Map<String, Object> data);
	
	
	/**
	 * 我的首页获取当前用户kpi星值 总数
	 * @param data
	 * @return
	 * @author silence
	 */
	int getSumKPIXing(Map<String, Object> data);
	
	/**
	 * 查询系统公告总记录数
	 * @param data
	 * @return
	 */
	int getSystemMessageCountByUserId(Map<String, Object> data);
	
	/**
	 * 查询系统公告列表
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getSystemMessageListByUserId(Map<String, Object> data);
	
	/**
	 * 查询系统公告详情
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getSystemMessageNoticeById(Map<String, Object> data);
	
	
	/**
	 * 根据公司或者组织查询顾客满意总记录条数 
	 * @param data
	 * @return
	 */
	int getEvaluateTotalRecode(Map<String, Object> data);
	
	/**
	 * 根据公司或者组织查询顾客各项指标总评分 
	 * @param data
	 * @return
	 */
	Map<String, Object> getEvluateCount(Map<String, Object> data);

	/**
	 * 查询当前公司总人数 
	 * @param data
	 * @return
	 */
	int getAllPersonalCount(Map<String, Object> data);
	
	
	
	
	/**
	 * 修改手机号和密码
	 * @param map
	 */
	void updateUserInfoPhoneAndPwd(Map<String,Object> map);
	
	/**
	 * 添加反馈信息
	 * @param map
	 */
	void insertFeedBackInfo(Map<String,Object> map);
	
	/**
	 * 查询意见反馈信息列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getFeedBackListInfo(Map<String,Object> map);
	
	/**
	 * 查询意见反馈信息总数
	 * @param map
	 * @return
	 */
	int getFeedBackListCount(Map<String,Object> map);
	
	/**
	 * 查询意见反馈的回复信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getFeedBackReplyListInfo(Map<String,Object> map);
	
	/**
	 * 修改反馈信息未读
	 * @param map
	 */
	void getUpdateFeedBackDetailInfo(Map<String,Object> map);
	
	/**
	 * 验证密码是否正确
	 * @param map
	 * @return
	 */
	Map<String,Object> getVerificationPwd(Map<String,Object> map);
	
	
	
	
	
	
	
	/**
	 * 查询公司的基本信息
	 * @param map
	 * @return
	 */
	Map<String,Object> getCompanyBasicInfo(Map<String,Object> map);
	
	/**
	 * 修改公司名称
	 * @param map
	 */
	void updateCompanyNameInfo(Map<String,Object> map);
	
	/**
	 * 查询公司组织架构列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getOrganizeListInfo(Map<String,Object> map);
	
	/**
	 * 查询组织架构基本信息详情
	 * @param map
	 * @return
	 */
	Map<String,Object> getOrganizeDetailInfo(Map<String,Object> map);
	
	/**
	 * 查询部门上级是否有店面 
	 * @param map
	 */
	int selectOrganizeType(Map<String,Object> map);
	/**
	 * 查询部门下级是否有店面 
	 * @param map
	 */
	int selectOrganizeTypelower(Map<String,Object> map);
	
	/**
	 * 修改组织架构基本信息
	 * @param map
	 */
	void updateOrganizeInfo(Map<String,Object> map);
	
	/**
	 * 添加组织架构信息
	 * @param map
	 */
	void insertOrganizeInfo(Map<String,Object> map);
	
	
	
	
	
	
	/**
	 * 查询组织架构人员信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getOrganizeUserListInfo(Map<String,Object> map);
	
	/**
	 * 查询组织人员信息详情
	 * @return
	 */
	Map<String,Object> getOrganizeUserDetailInfo(Map<String,Object> map);
	
	/**
	 * 修改用户的基本信息
	 * @param map
	 */
	void updateUserBasicInfo(Map<String,Object> map);
	
	/**
	 * 批量修改用户信息
	 * @param map
	 */
	void updateUserBasicInfoByUserIdList(Map<String,Object> map);
	
	/**
	 * 修改组织人员所属的部门
	 * @param map
	 */
	void updateOrganizeUserInfo(Map<String,Object> map);
	
	/**
	 * 添加组织用户信息
	 * @param map
	 */
	void insertOrganizeUserInfo(Map<String,Object> map);
	
	/**
	 * 查询功能权限表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getAuthorityListInfo(Map<String,Object> map);
	
	/**
	 * 添加用户权限信息
	 * @param map
	 */
	void insertUserPowerInfo(Map<String,Object> map);
	
	String queryDataCode(Map<String, Object> map);
	
	/**
	 *  修改组织架构+1
	 * @param map
	 */
	void updateOrganizeInfoAdd(Map<String, Object> map);
	
	/**
	 *  修改组织架构父级-1
	 * @param map
	 */
	void updateOrganizeInfoSub(Map<String, Object> map);
	
	/**
	 * 修改当前组织下面的子级的datacode 
	 * @param map
	 */
	void updateOrganizeInfoModifyCode(Map<String, Object> map);
	
	/**
	 * 查询是否有子级
	 * @param map
	 */
	int selectDataCode(Map<String, Object> map);
	
	/**
	 * 查询修改后的组织是否等于原本组织
	 * @param map
	 */
	
	int selectOrgaParent(Map<String, Object> map);
	
	/**
	 * 修改组织的usernum+1
	 * @param map
	 */
	void updateUserOrganizeAdd(Map<String, Object> map);
	
	/**
	 * 修改组织的usernum-1
	 * @param map
	 */
	void updateUserOrganize(Map<String, Object> map);
	
	/**
	 * 新增人员
	 * @param map
	 */
	void insertUserInfo(Map<String, Object> map);
	
	/**
	 * 当前登录用户组织区域下的人员
	 * 传入参数：resourceid  = userid (当前登录的用户id)  ;  userid = userid(查询的用户id)
	 * @param map
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	List<Map<String, Object>> getPersonalReleaseByUserId(Map<String, Object> map);
	
	/**
	 * 查询用户属于那些组织区域
	 * 传入参数：resourceid  = userid (当前登录的用户id)  ;  userid = userid(查询的用户id)
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getPersonalUsreByUserId(Map<String, Object> map);
	/**
	 * 查询manganid
	 * @param map
	 */
	String selectDataManageid(Map<String, Object> map);
	
	
	/**
	 * 查询当前用户下所有用户
	 * @param map
	 */
	List<Map<String, Object>> getUserInfoAll(Map<String, Object> map);
	
	/**
	 * 查询当前用户所属组织下的全部人员的详细信息
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectUserByOrganizeid(Map<String, Object> map);
	
	/**
	 *  查询出用户已有的权限
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getPowerByUserId(Map<String, Object> map);
	/**
	 * 查询权限列表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getPowerAll(Map<String, Object> map);
	
	/**
	 * 修改用户权限
	 * @param map
	 * @return
	 */
	void getdeletePowerByUserId(Map<String, Object> map);
	
	/**
	 * 查询权限管理一级菜单
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getPowerOne(Map<String, Object> map);
	
	/**
	 * 查询拥有该权限的人员
	 * @param map
	 */
	List<Map<String, Object>> getPowerUserByPowerId(Map<String, Object> map);
	
	
	List<Map<String, Object>> getUserListByOrganize(Map<String, Object> map);
	
	Map<String, Object> getCountpowerCompany(Map<String, Object> map);
	
	List<Map<String, Object>> getPowerCompanyOne(Map<String, Object> map);
	
	/**
	 * 查询出该公司权限是否存在
	 * @param map
	 */
	List<Map<String, Object>> selectPowerCompany(Map<String, Object> map);
	
	/**
	 * 修改该公司权限
	 * @param map
	 */
	void getUpdatPowerCompanyByid(Map<String, Object> map);
	
	/**
	 * 新增公司权限
	 * @param map
	 */
	void insertPowerCompany(Map<String, Object> map);
	/**
	 * 修改系统公告
	 * @param map
	 */
	void updataSystemMessageDetail(Map<String, Object> map);
	/**
	 * 查询组织是否存在
	 * @param map
	 */
	int getAllOrganizelCount(Map<String, Object> map);
	
	/**
	 * 查询一级菜单的子集个数
	 * @param map
	 */
	int getPowerOneCount(Map<String, Object> map);
	
	/**
	 * 企业的二级菜单
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getPowerTwo(Map<String, Object> map);
	/**
	 *	获取当前组织下所有的organizeid
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getOrganizeidAll(Map<String, Object> map);
	
	/**
	 *	查询当前用户是否存在改公司
	 * @param map
	 * @return
	 */
	int getUserInfoCount(Map<String, Object> map);

	
	/**
	 *	查询权限下已有权限的用户
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getPowerUser(Map<String, Object> map);
	
	/**
	 *	查询当前登陆者的organizeid
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getOrganizeUserId(Map<String, Object> map);
	/**
	 *	查询organizeid对应的datacode
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getDatacodeByOrgid(Map<String, Object> map);
	/**
	 *	查询当前登陆者的管理区域上級 organizeid 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getOrganizeByDataCodeList(Map<String, Object> map);
	/**
	 *	查询当前登陆者的管理区域上級 organizeid 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getOrganizeByDataCode(Map<String, Object> map);

	/**
	 *	删除用户组织表信息
	 * @param map
	 * @return
	 */
	void deleteUserorganize(Map<String, Object> data);
	
	/**
	 * 查询第一个管理方管理员信息
	 * @param map
	 * @return
	 */
	Map<String,Object> getManageUserInfo(Map<String,Object> map);
	
}
