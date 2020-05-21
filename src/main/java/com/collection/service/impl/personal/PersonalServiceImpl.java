package com.collection.service.impl.personal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.collection.dao.CompanyMapper;
import com.collection.dao.IndexMapper;
import com.collection.dao.personal.PersonalMapper;
import com.collection.dao.worksheet.OrganizeTemplateMapper;
import com.collection.dao.worksheet.WorkSheetInspectMapper;
import com.collection.service.managebackstage.CustomerFileService;
import com.collection.service.personal.PersonalService;
import com.collection.service.worksheet.WorkSheetInspectService;
import com.collection.util.ImageUtil;

/**
 * 用户管理
 * @author silence
 *
 */
public class PersonalServiceImpl implements PersonalService{

	@Autowired PersonalMapper personalMapper;
	@Autowired IndexMapper indexMapper;
	@Autowired WorkSheetInspectMapper workSheetInspectMapper;
	@Autowired OrganizeTemplateMapper organizeTemplateMapper;
	@Autowired CompanyMapper companyMapper;
	@Autowired CustomerFileService customerFileService;
	
	
	@Override
	public void updateUserInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.personalMapper.updateUserInfo(data);
	}

	@Override
	public Map<String, Object> getUserInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.personalMapper.getUserInfo(data);
	}

	@Override
	public int getSystemMessageCountByUserId(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.personalMapper.getSystemMessageCountByUserId(data);
	}

	@Override
	public List<Map<String, Object>> getSystemMessageListByUserId(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.personalMapper.getSystemMessageListByUserId(data);
	}
	
	@Override
	public List<Map<String, Object>> getSystemMessageNoticeById(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.personalMapper.getSystemMessageNoticeById(data);
	}

	@Override
	public Map<String, Object> getUserIndexPage(Map<String, Object> data) {
 		 Map<String,Object> dataMap = new HashMap<String, Object>();
		 //用户基本信息
		 Map<String,Object> userMap = this.personalMapper.getUserInfo(data);
		 data.put("companyid", userMap.get("companyid"));
		 data.put("createid", data.get("userid"));
		 if(data.containsKey("companyid") && data.get("companyid") != ""){
			 //kpi总值
			 int kpiCount = this.personalMapper.getSumKPIXing(data);
			 dataMap.put("kpiCount", kpiCount);
			 if(data.containsKey("organizeid") && data.get("organizeid") != ""  ){
				 Map<String,Object> organizeMap = this.indexMapper.getOrganizeInfo(data);
				 //店面人数
				 List<Map<String, Object>> organizelist = this.personalMapper.getOrganizeidAll(data);
				 List<String> organizeidlist=new ArrayList<String>();
				 for (int i = 0; i < organizelist.size(); i++) {
						organizeidlist.add(organizelist.get(i).get("organizeid").toString());
					}
				 data.put("organizeidlist", organizeidlist);
				 int totalPersonal = this.personalMapper.getAllPersonalCount(data);
				 //得到店面综合得分
				 Map<String,Object> starMap = this.personalMapper.getEvluateCount(data);
				 //得到店面总共有多少人评分
				 int totalEvluate = this.personalMapper.getEvaluateTotalRecode(data);
				 dataMap.put("totalPersonal", totalPersonal);
				 dataMap.put("starMap", starMap);
				 dataMap.put("organizeMap", organizeMap);
				 dataMap.put("totalEvluate", totalEvluate);
			 }
		 }
		 dataMap.put("userMap", userMap);
		
		 return dataMap;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 修改手机号和密码
	 * @param map
	 */
	@Override
	public void updateUserInfoPhoneAndPwd(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.personalMapper.updateUserInfoPhoneAndPwd(map);
	}

	/**
	 * 添加反馈信息
	 * @param map
	 */
	@Override
	public void insertFeedBackInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("feedbackid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("isread", 1);
		map.put("delflag", 0);
		map.put("createtime", new Date());
		this.personalMapper.insertFeedBackInfo(map);
	}

	/**
	 * 查询意见反馈信息列表
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getFeedBackListInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("delflag", 0);
		return this.personalMapper.getFeedBackListInfo(map);
	}

	/**
	 * 查询意见反馈信息总数
	 * @param map
	 * @return
	 */
	@Override
	public int getFeedBackListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("delflag", 0);
		return this.personalMapper.getFeedBackListCount(map);
	}

	/**
	 * 查询意见反馈的回复信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getFeedBackReplyListInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("delflag", 0);
		return this.personalMapper.getFeedBackReplyListInfo(map);
	}
	
	
	/**
	 *  修改反馈信息未读
	 * @param map
	 */
	@Override
	public void getUpdateFeedBackDetailInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.personalMapper.getUpdateFeedBackDetailInfo(map);
	}


	/**
	 * 验证密码是否正确
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getVerificationPwd(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getVerificationPwd(map);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 查询公司的基本信息
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getCompanyBasicInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getCompanyBasicInfo(map);
	}

	/**
	 * 修改公司名称
	 * @param map
	 */
	@Override
	public void updateCompanyNameInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.personalMapper.updateCompanyNameInfo(map);
	}

	/**
	 * 查询公司组织架构列表信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOrganizeListInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("delflag", 0);
		return this.personalMapper.getOrganizeListInfo(map);
	}


	/**
	 * 查询组织架构基本信息详情
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getOrganizeDetailInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("delflag", 0);
		return this.personalMapper.getOrganizeDetailInfo(map);
	}
	
	/**
	 * 查询部门上级是否有店面 
	 * @param map
	 */
	@Override
	public int selectOrganizeType(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.selectOrganizeType(map);
	}

	/**
	 * 查询部门下级是否有店面 
	 * @param map
	 */
	@Override
	public int selectOrganizeTypelower(Map<String, Object> map) {
		// TODO Auto-generated method stub
		 	  
		return this.personalMapper.selectOrganizeTypelower(map);
	}

	
	/**
	 * 修改当前组织下面的子级的datacode
	 * @param map
	 */
	@Override
	public void updateOrganizeInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		if(map.containsKey("updataorganizeid") && map.get("updataorganizeid") != null){
			int orgaParent = this.personalMapper.selectOrgaParent(map);
			if( orgaParent == 0 ){
				String datacode = getDataCode(map);
				map.put("datacode", datacode);
				this.personalMapper.updateOrganizeInfoModifyCode(map);
				this.personalMapper.updateOrganizeInfoAdd(map);
				this.personalMapper.updateOrganizeInfoSub(map);	
			}
		}
		if(!map.containsKey("priority") || "".equals(map.get("priority"))){
			//得到要修改到的组织下的最大的排序值
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("organizeid", map.get("updataorganizeid"));
			int priority = this.companyMapper.getOrganizeMaxPriorityNum(paramMap);
			map.put("priority", (priority+1));
		}
		
		this.personalMapper.updateOrganizeInfo(map);
	}
	/**
	 * 删除组织架构
	 * @param map
	 */
	@Override
	public void updateOrganizeDelete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.personalMapper.updateOrganizeInfoSub(map);	
		this.personalMapper.updateOrganizeInfo(map);
	}
	/**
	 * 添加组织架构信息
	 * @param map
	 */
	@Override
	public void insertOrganizeInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("childnum", 0);
		map.put("usernum", 0);
		String datacode = getDataCode(map);
		map.put("datacode", datacode);
		
		if(!map.containsKey("priority") || "".equals(map.get("priority"))){
			//排序值添加
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("organizeid", map.get("updataorganizeid"));
			int number = this.companyMapper.getOrganizeMaxPriorityNum(param);
			map.put("priority", (number + 1));
		}
		
		//新增数据
		this.personalMapper.insertOrganizeInfo(map);
		//修改父级childnum+1
		this.personalMapper.updateOrganizeInfoAdd(map);
		
		String[] typeids = {"9","10","11","12","13"};
		for(String typeid : typeids){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("typeid", typeid);
			//查询默认的检查模板信息
			List<Map<String,Object>> templatelist = this.workSheetInspectMapper.getDefaultTemplateList(paramMap);
			for(Map<String,Object> tempmap : templatelist){
				String templateid = UUID.randomUUID().toString().replaceAll("-", "");
				tempmap.put("templateid", templateid);
				tempmap.put("delflag", 0);
				tempmap.put("createtime", new Date());
				tempmap.put("createid", map.get("userid"));
				tempmap.put("companyid", map.get("companyid"));
				tempmap.put("organizeid", map.get("organizeid"));
				int priority = this.organizeTemplateMapper.getOrganizeTemplateMaxPriority(tempmap);
				tempmap.put("priority", priority);
				this.organizeTemplateMapper.insertOrganizeTemplateInfo(tempmap);
				//查询默认的模板对应的默认的检查项目
				List<Map<String,Object>> projectlist = this.workSheetInspectMapper.getDefaultProjectList(tempmap);
				int num = 1;
				for(Map<String,Object> promap : projectlist){
					promap.put("projectid", UUID.randomUUID().toString().replaceAll("-", ""));
					promap.put("templateid", templateid);
					promap.put("projectname", promap.get("cname"));
					promap.put("priority", num);
					promap.put("delflag", 0);
					promap.put("createtime", new Date());
					promap.put("createid", map.get("userid"));
					this.organizeTemplateMapper.insertOrganizeTemplateProjectInfo(promap);
					num++;
				}
			}
		}
	}

	
	public String getDataCode(Map<String, Object> map) {
		String datacode = null;
		//查询子级中datacode最大的一个
		String lastDataCode = this.personalMapper.queryDataCode(map);
		if(lastDataCode !=null && !"".equals(lastDataCode)){
			int length = lastDataCode.length();
			String shortDateCode = lastDataCode.substring(length-3, length);
			String endCode =(Integer.valueOf(shortDateCode)+1)+"" ;
			datacode = lastDataCode.substring(0, length-3)+endCode;
		}else{
			datacode = map.get("updatadatacode").toString()+"101";
		}
		
		return datacode;
	}

	/**
	 * 查询组织架构人员信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOrganizeUserListInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("delflag", 0);
		return this.personalMapper.getOrganizeUserListInfo(map);
	}

	/**
	 * 查询组织人员信息详情
	 * @return
	 */
	@Override
	public Map<String, Object> getOrganizeUserDetailInfo(Map<String,Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getOrganizeUserDetailInfo(map);
	}

	/**
	 * 修改用户的基本信息
	 * @param map
	 */
	@Override
	public void updateUserBasicInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		boolean isuserid = map.containsKey("userid") && !"".equals(map.get("userid"));
		boolean isuseridlist = map.containsKey("useridlist") && !"".equals(map.get("useridlist"));
		if(!isuserid && !isuseridlist){
			throw new RuntimeException("网络异常,请稍后重试！");
		}
		
		map.put("updatetime", new Date());
		if(isuserid){
			//判断是否是修改名称
			if(map.containsKey("realname") && !"".equals(map.get("realname"))){
				//查询用户头像
				Map<String,Object> usermap = this.personalMapper.getUserInfo(map);
				if(!String.valueOf(usermap.get("realname")).equals(map.get("realname"))){
					String headimage = String.valueOf(usermap.get("headimage"));
					if(usermap.containsKey("headimage") && !"".equals(headimage)){
						if(headimage.indexOf("/systemDefaultUserHeadimg") > 0){
							try {
								String headimg = ImageUtil.randomlyGeneratedDefaultAvatar(String.valueOf(map.get("realname")), String.valueOf(map.get("userid")),
										String.valueOf(map.get("getRealPath")), String.valueOf(map.get("fileDirectory"))+String.valueOf(usermap.get("comapnyid")));
								//记录头像信息修改公司的磁盘空间大小
								 try {
								    	Map<String,Object> param = new HashMap<String,Object>();
								    	param.put("companyid", usermap.get("companyid"));
								    	param.put("userid", map.get("userid"));
								    	param.put("size", 8);
								    	param.put("type", map.get("headimgtype"));
								    	param.put("url", headimg);
										this.customerFileService.insertCustomerFileInfo(param);
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}
								map.put("headimage", headimg);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
			this.personalMapper.updateUserBasicInfo(map);
		}else if(isuseridlist){
			this.personalMapper.updateUserBasicInfoByUserIdList(map);
		}else{
			throw new RuntimeException("网络异常,请稍后重试！");
		}
	}
	
	/**
	 * 修改用户所属的部门的usernum-1
	 * @param map
	 */
	@Override
	public void updateUserOrganize(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("usernum",1);
		this.personalMapper.updateUserOrganize(map);
	}
	
	/**
	 * 修改用户所属的部门的usernum+1
	 * @param map
	 */
	@Override
	public void updateUserOrganizeAdd(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		this.personalMapper.updateUserOrganizeAdd(map);
	}

	/**
	 * 修改组织人员所属的部门
	 * @param map
	 */
	@Override
	public void updateOrganizeUserInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.personalMapper.updateOrganizeUserInfo(map);
	}
	/**
	 * 添加人员信息
	 * @param map
	 */
	@Override
	@Transactional
	public void insertUserInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("userid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("delflag", 0);
		map.put("status", 1);
		map.put("isfristlogin", 0);
		map.put("isshowphone", 1);
		map.put("managerole", 1);
		map.put("createtime", new Date());
		if(!map.containsKey("headimage") || "".equals(map.get("headimage"))){
			String imgurl = "";
			try {
				imgurl = ImageUtil.randomlyGeneratedDefaultAvatar(String.valueOf(map.get("realname")), String.valueOf(map.get("userid")),
						String.valueOf(map.get("getRealPath")), String.valueOf(map.get("fileDirectory"))+String.valueOf(map.get("companyid")));
				//记录头像信息修改公司的磁盘空间大小
				 try {
				    	Map<String,Object> param = new HashMap<String,Object>();
				    	param.put("companyid", map.get("companyid"));
				    	param.put("userid", map.get("userid"));
				    	param.put("size", 8);
				    	param.put("type", 13);
				    	param.put("url", imgurl);
						this.customerFileService.insertCustomerFileInfo(param);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.put("headimage", imgurl);
		}
		this.personalMapper.insertUserInfo(map);
		if(map.containsKey("organizelist") && !"".equals(map.get("organizelist"))){
			JSONObject json=JSONObject.fromObject(map.get("organizelist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> organizelist=(List<Map<String, Object>>)json.get("organizelist");
			if(organizelist != null && organizelist.size() > 0){
				for(Map<String,Object> orgmap : organizelist){
					map.put("organizeid", orgmap.get("organizeid"));
					this.insertOrganizeUserInfo(map);		
					map.put("usernum", 1);
					this.updateUserOrganizeAdd(map);
				}
			}
		}
	}


	/**
	 * 添加组织用户信息
	 * @param map
	 */
	@Override
	public void insertOrganizeUserInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("manageid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("delflag", 0);
		map.put("createtime", new Date());
		this.personalMapper.insertOrganizeUserInfo(map);
	}

	/**
	 * 查询功能权限表
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getAuthorityListInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getAuthorityListInfo(map);
	}

	/**
	 * 添加用户权限信息
	 * @param map
	 */
	@Override
	public void insertUserPowerInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("poweruserid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("status", 1);
		map.put("delflag", 0);
		map.put("createtime", new Date());
		this.personalMapper.insertUserPowerInfo(map);
	}

	/**
	 *查询是否有子级
	 * @param map
	 */
	@Override
	public int selectDataCode(Map<String, Object> map) {
		// TODO Auto-generated method stub
		 return this.personalMapper.selectDataCode(map);
	}
	
	/**
	 * 当前登录用户组织区域下的人员
	 * 传入参数：resourceid  = userid (当前登录的用户id)  ; 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPersonalReleaseByUserId(
			Map<String, Object> map) {
		return  this.personalMapper.getPersonalReleaseByUserId(map);
	}

	/**
	 * 查询用户属于那些组织区域
	 * 传入参数：resourceid  = userid (当前登录的用户id)  ; 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPersonalUsreByUserId(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return  this.personalMapper.getPersonalUsreByUserId(map);
	}

	@Override
	public String selectDataManageid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.selectDataManageid(map);
	}

	
	/**
	 * 查询当前公司下所有用户
	 * @param map
	 */
	@Override
	public List<Map<String, Object>> getUserInfoAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getUserInfoAll(map);
	}

	/**
	 * 查询当前用户所属组织下的全部人员的详细信息
	 * @param map
	 */
	@Override
	public List<Map<String, Object>> selectUserByOrganizeid(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.selectUserByOrganizeid(map);
	}
	
	/**
	 * 查询出用户已有的权限
	 *  @param map
	 */
	@Override
	public List<Map<String, Object>> getPowerByUserId(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getPowerByUserId(map);
	}

	
	/**
	 * 查询权限列表
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPowerAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getPowerAll(map);
	}
	/**
	 * 取消/赋予用户权限
	 * @param map
	 * @return
	 */
	@Override
	public void getdeletePowerByUserId(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		this.personalMapper.getdeletePowerByUserId(map);
	}

	/**
	 * 查询权限管理一级菜单
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPowerOne(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
		return this.personalMapper.getPowerOne(map);
	}
	/**
	 * 查询拥有该权限的人员
	 * @param map
	 */
	@Override
	public List<Map<String, Object>> getPowerUserByPowerId(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getPowerUserByPowerId(map);
	}

	@Override
	public List<Map<String, Object>> getUserListByOrganize(
			Map<String, Object> map) {
		
		return this.personalMapper.getUserListByOrganize(map);
	}

	@Override
	public Map<String, Object> getCountpowerCompany(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getCountpowerCompany(map);
	}

	@Override
	public List<Map<String, Object>> getPowerCompanyOne(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getPowerCompanyOne(map);
	}

	/**
	 * 查询出该公司权限是否存在
	 * @param map
	 */
	@Override
	public List<Map<String, Object>> selectPowerCompany(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.selectPowerCompany(map);
	}

	/**
	 * 修改该公司权限
	 * @param map
	 */
	@Override
	public void getUpdatPowerCompanyByid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		this.personalMapper.getUpdatPowerCompanyByid(map);
	}

	/**
	 * 新增公司权限
	 * @param map
	 */
	@Override
	public void insertPowerCompany(Map<String, Object> map) {
		map.put("powercompanyid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("delflag", 0);
		map.put("createtime", new Date());
		this.personalMapper.insertPowerCompany(map);
		
	}

	@Override
	public void updataSystemMessageDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.personalMapper.updataSystemMessageDetail(map);
	}

	@Override
	public int getAllOrganizelCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getAllOrganizelCount(map);
	}

	@Override
	public int getPowerOneCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getPowerOneCount(map);
	}
	
	@Override
	public List<Map<String, Object>> getPowerTwo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getPowerTwo(map);
	}

	@Override
	public List<Map<String, Object>> getOrganizeidAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getOrganizeidAll(map);
	}

	@Override
	public int getUserInfoCount(Map<String, Object> map) {
		return this.personalMapper.getUserInfoCount(map);
	}

	@Override
	public List<Map<String, Object>> getPowerUser(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getPowerUser(map);
	}

	@Override
	public List<Map<String, Object>> getOrganizeUserId(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getOrganizeUserId(map);
	}

	@Override
	public List<Map<String, Object>> getDatacodeByOrgid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getDatacodeByOrgid(map);
	}

	@Override
	public List<Map<String, Object>> getOrganizeByDataCodeList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getOrganizeByDataCodeList(map);
	}

	@Override
	public List<Map<String, Object>> getOrganizeByDataCode(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getOrganizeByDataCode(map);
	}

	@Override
	public void deleteUserorganize(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.personalMapper.deleteUserorganize(data);
		
		//查询用户所有的所属组织
		List<Map<String, Object>> organizelist=this.indexMapper.getOrganizeListByUser(data);
		for(Map<String, Object> organize:organizelist){
			Map<String, Object> organizeMap=new HashMap<String, Object>();
			//减少组织的子集数量
			organizeMap.put("usernum",1);
			organizeMap.put("organizeid", organize.get("organizeid"));
			this.indexMapper.reduceOrganizeNum(organizeMap);
		}
	}

	/**
	 * 查询第一个管理方管理员信息
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getManageUserInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.personalMapper.getManageUserInfo(map);
	}
//	public static void main(String[] args) {
//		ClassPathXmlApplicationContext context;
//
//		context = new ClassPathXmlApplicationContext("local/spring.xml");
//		PersonalService ps= (PersonalService) context.getBean("personalService");
//		Map<String,Object> map = new HashMap<String,Object>();
//		List<String> userlist = new ArrayList<String>();
//		userlist.add("086fb2d75d71486b91abadd3c4e7478e");
//		userlist.add("0c6580a478aa40c08265852e05cb153f");
//		map.put("useridlist", userlist);
//		map.put("realname", "wangshu2");
//		ps.updateUserBasicInfo(map);
//	}

}
