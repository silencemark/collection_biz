package com.collection.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.CompanyMapper;
import com.collection.dao.UserInfoMapper;
import com.collection.service.UserInfoService;
import com.collection.service.managebackstage.CustomerFileService;
/**
 * 采购管理
 * @author silence
 *
 */
public class UserInfoServiceImpl implements UserInfoService{

	@Autowired UserInfoMapper userInfoMapper;

	@Resource private CompanyMapper companyMapper;
	@Resource private CustomerFileService customerFileService;
	
	@Override
	public void insertUserInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("username", data.get("phone"));
		data.put("isshowphone", 0);
		data.put("status",1);
		data.put("delflag", 0);
		data.put("createtime", new Date());
		String imgurl = "/app/appcssjs/images/defaultheadimage.png";
		imgurl = "";
				//ImageUtil.randomlyGeneratedDefaultAvatar(String.valueOf(data.get("realname")),String.valueOf(data.get("userid")),
				//String.valueOf(data.get("gerRealPath")), String.valueOf(data.get("fileDirectory"))+String.valueOf(data.get("companyid")));
		//记录头像信息修改公司的磁盘空间大小
		 try {
		    	Map<String,Object> param = new HashMap<String,Object>();
		    	param.put("companyid", data.get("companyid"));
		    	param.put("userid", data.get("userid"));
		    	param.put("size", 8);
		    	param.put("type", 1);
		    	param.put("url", imgurl);
				this.customerFileService.insertCustomerFileInfo(param);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		data.put("headimage", imgurl);
		this.userInfoMapper.insertUserInfo(data);
	}

	@Override
	public void updateUserInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.userInfoMapper.updateUserInfo(data);
	}

	@Override
	public Map<String, Object> getUserInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.userInfoMapper.getUserInfo(data);
	}

	@Override
	public List<Map<String, Object>> getMenuList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> menulist=new ArrayList<Map<String,Object>>();
		if(data.containsKey("managerole") && String.valueOf(data.get("managerole")).equals("3")){
			//超级管理员拥有所有权限
			menulist=this.userInfoMapper.getMenuList(new HashMap<String, Object>());
		}else if(data.containsKey("managerole") && String.valueOf(data.get("managerole")).equals("2")){
			//普通管理员拥有部分权限
			menulist=this.userInfoMapper.getFunctionByUserList(new HashMap<String, Object>());
		}
		return menulist;
	}

	@Override
	public Map<String, Object> getIndexInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> companymap=this.companyMapper.getCompanyInfo(data);
		if(companymap != null && companymap.size()>0){
			//查询 总员工数量
			Map<String, Object> companyInfo=new HashMap<String, Object>();
			companyInfo.put("companyid", companymap.get("companyid"));
			int allusernum=this.userInfoMapper.getUserNum(companyInfo);
			companymap.put("allusernum", allusernum);
			//查询未激活的员工人数
			companyInfo.put("isfristlogin", 1);
			int notusernum=this.userInfoMapper.getUserNum(companyInfo);
			companymap.put("notusernum", notusernum);
			//查询店面数量
			companyInfo.put("type", 3);
			int shopnum=this.userInfoMapper.getShopNum(companyInfo);
			companymap.put("shopnum", shopnum);
			//查询剩余内存
			Map<String, Object> memorymap=this.userInfoMapper.getCompanyMemory(companyInfo);
			if(memorymap != null && memorymap.size()>0){
				companymap.put("maxmemory", memorymap.get("maxmemory"));
				companymap.put("usedmemory", memorymap.get("usedmemory"));
			}
		}
		return companymap;
	}

	@Override
	public List<Map<String, Object>> getNotuseUserList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> userlist=this.userInfoMapper.getNotuseUserList(data);
		return userlist;
	}

	@Override
	public int getNotuseUserListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.userInfoMapper.getNotuseUserListNum(data);
	}

	@Override
	public List<Map<String, Object>> getUserListByOrganize(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.userInfoMapper.getUserListByOrganize(data);
	}

	@Override
	public List<Map<String, Object>> getUserList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.userInfoMapper.getUserList(data);
	}

	@Override
	public int getUserListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.userInfoMapper.getUserListNum(data);
	}

	@Override
	public void updateLoginime(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.userInfoMapper.updateUserLogintime(data);
		this.userInfoMapper.updateCompanyLogintime(data);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyuseid", UUID.randomUUID().toString().replaceAll("-", ""));
		param.put("companyid", data.get("companyid"));
		param.put("usetime", sdf.format(new Date()));
		param.put("lastusetime", new Date());
		param.put("userid", data.get("userid"));
		this.companyMapper.insertCompanyUseLog(param);
	}

	@Override
	public List<Map<String, Object>> getUserStatistics(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.userInfoMapper.getUserStatistics(data);
	}

	@Override
	public List<Map<String, Object>> getNewUserStatistics(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.userInfoMapper.getNewUserStatistics(data);
	}

	@Override
	public Map<String, Object> getUserPowerList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
		List<Map<String, Object>> powerlist=new ArrayList<Map<String,Object>>();
		if(String.valueOf(data.get("managerole")).equals("3")){//超管
			//查询公司所有权限
			Map<String, Object> companyMap=new HashMap<String, Object>();
			companyMap.put("companyid", data.get("companyid"));
			powerlist=this.userInfoMapper.getCompanyPowerList(companyMap);
			
			
		}else{
			//查询人员所有权限
			Map<String, Object> userMap=new HashMap<String, Object>();
			userMap.put("userid", data.get("userid"));
			powerlist=this.userInfoMapper.getUserPowerList(userMap);
			
		}
		Map<String, Object> powermap=new HashMap<String, Object>();
		for(Map<String, Object> power:powerlist){
			powermap.put("power"+power.get("datacode"), power.get("powername")+"");
		}
		return powermap;
	}

	@Override
	public Map<String, Object> getCompanyPowerList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
		List<Map<String, Object>> powerlist=this.userInfoMapper.getCompanyPowerList(data);
		
		Map<String, Object> powermap=new HashMap<String, Object>();
		for(Map<String, Object> power:powerlist){
			powermap.put("power"+power.get("datacode"), power.get("powername")+"");
		}
		return powermap;
	}
	
	/**
	 * 查询组织下人员的总数
	 * @param map
	 * @return
	 */
	@Override
	public int getUserListByOrganizeCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.userInfoMapper.getUserListByOrganizeCount(map);
	}

	/**
	 * 获取权限的最后一次的更新时间
	 * @param map
	 * @return
	 */
	@Override
	public String getUserPowerUpdateTime(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.userInfoMapper.getUserPowerUpdateTime(map);
	}

	/**
	 * 清空所有的相同的registrationid
	 * 然后根据userid赋值registrationid
	 * @param map
	 */
	@Override
	public void updateUserRegistrationId(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.userInfoMapper.updateUserRegistrationId(map);
	}

}
