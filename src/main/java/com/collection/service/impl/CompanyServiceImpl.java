package com.collection.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.CompanyMapper;
import com.collection.dao.IndexMapper;
import com.collection.dao.personal.PersonalMapper;
import com.collection.dao.purchase.PurchaseMapper;
import com.collection.dao.worksheet.OrganizeTemplateMapper;
import com.collection.dao.worksheet.WorkSheetInspectMapper;
import com.collection.service.CompanyService;
import com.collection.service.IndexService;
import com.collection.service.purchase.PurchaseService;
/**
 * 公司管理
 * @author silence
 *
 */
public class CompanyServiceImpl implements CompanyService{

	@Autowired CompanyMapper companyMapper;
	
	@Autowired PersonalMapper personalMapper;
	
	@Autowired WorkSheetInspectMapper workSheetInspectMapper;
	
	@Autowired OrganizeTemplateMapper organizeTemplateMapper;

	@Override
	public Map<String, Object> getCompanyInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> companyinfo=this.companyMapper.getCompanyInfo(data);
		if(companyinfo != null && companyinfo.size() >0){
			double remainmemory = Double.parseDouble(String.valueOf(companyinfo.get("maxmemory")))
					- Double.parseDouble(String.valueOf(companyinfo.get("shujudmemory")))
					- Double.parseDouble(String.valueOf(companyinfo.get("qywpdmemory")));
			companyinfo.put("remainmemory", remainmemory);
			List<Map<String, Object>> licenselist=this.companyMapper.getLicenseList(data);
			companyinfo.put("licenselist", licenselist);
		}
		return companyinfo;
	}

	@Override
	public void insertCompanyInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("delflag", 0);
		data.put("createtime", new Date());
		this.companyMapper.insertCompanyInfo(data);
		
		//添加公司云盘表
		Map<String, Object> cloudmap=new HashMap<String, Object>();
		cloudmap.put("cloudid", UUID.randomUUID().toString().replaceAll("-", ""));
		cloudmap.put("companyid", data.get("companyid"));
		cloudmap.put("maxmemory", 10*1024*1024);
		cloudmap.put("delflag", 0);
		cloudmap.put("createtime", new Date());
		cloudmap.put("createid", data.get("createid"));
		this.companyMapper.insertCompanyCloud(cloudmap);
		//添加所有权限表
		List<Map<String,Object>> powerlist=this.companyMapper.getAllPowerList();
		for(Map<String, Object> power:powerlist){
			Map<String, Object> powercompany=new HashMap<String, Object>();
			powercompany.put("powercompanyid", UUID.randomUUID().toString().replaceAll("-", ""));
			powercompany.put("companyid",data.get("companyid"));
			powercompany.put("powerid",power.get("powerid"));
			powercompany.put("createid",data.get("createid"));
			powercompany.put("status",0);
			powercompany.put("delflag", 0);
			powercompany.put("createtime", new Date());
			this.companyMapper.insertCompanyPower(powercompany);
		}
	}

	@Override
	public void updateCompany(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.companyMapper.updateCompany(data);
	}

	@Override
	public List<Map<String, Object>> getCompanyListByPhone(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.companyMapper.getCompanyListByPhone(data);
	}

	@Override
	public List<Map<String, Object>> getLogList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.companyMapper.getLogList(data);
	}

	@Override
	public List<Map<String, Object>> getLogModelList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.companyMapper.getLogModelList(data);
	}

	@Override
	public int getLogListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.companyMapper.getLogListNum(data);
	}

	@Override
	public List<Map<String, Object>> getBannerList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.companyMapper.getBannerList(data);
	}

	@Override
	public void updateBanner(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.companyMapper.updateBanner(data);
	}

	@Override
	public void insertBanner(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("delflag",0);
		this.companyMapper.insertBanner(data);
	}

	@Override
	public void deleteLicense(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.companyMapper.deleteLicense(data);
	}

	@Override
	public void insertLicense(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("licenseid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("delflag", 0);
		data.put("createtime", new Date());
		this.companyMapper.insertLicense(data);
	}

	@Override
	public void insertUserFunction(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("userfunctionid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime", new Date());
		data.put("delflag",0);
		this.companyMapper.insertUserFunction(data);
	}

	@Override
	public List<Map<String, Object>> getAllCompanyList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.companyMapper.getAllCompanyList(data);
	}

	@Override
	public int getAllCompanyListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.companyMapper.getAllCompanyListNum(data);
	}

	@Override
	public List<Map<String, Object>> getPowerByCompany(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.companyMapper.getPowerByCompany(data);
	}

	@Override
	public void updateCompanyPower(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.companyMapper.updateCompanyPower(data);
	}

	@Override
	public List<Map<String, Object>> getCompanyStatistics(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.companyMapper.getCompanyStatistics(data);
	}

	@Override
	public List<Map<String, Object>> getCompanyBlackList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.companyMapper.getCompanyBlackList(data);
	}

	@Override
	public int getCompanyBlackListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.companyMapper.getCompanyBlackListNum(data);
	}

	@Override
	public List<Map<String, Object>> getNewCompanyStatistics(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.companyMapper.getNewCompanyStatistics(data);
	}

	@Override
	public String getMaxCompanyDataCode() {
		// TODO Auto-generated method stub
		return this.companyMapper.getMaxCompanyDataCode();
	}

	@Override
	public void insertOrganize(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("createtime", new Date());
		data.put("delflag", 0);
		this.personalMapper.insertOrganizeInfo(data);
		
		//添加创建者到根组织
		Map<String, Object> userorganize=new HashMap<String, Object>();
		userorganize.put("manageid", UUID.randomUUID().toString().replaceAll("-", ""));
		userorganize.put("organizeid",data.get("organizeid"));
		userorganize.put("userid", data.get("createid"));
		userorganize.put("delflag", 0);
		userorganize.put("createtime", new Date());
		userorganize.put("companyid", data.get("lastcompanyid"));
		userorganize.put("createid", data.get("createid"));
		this.companyMapper.insertOrganizeUser(userorganize);
	}

	@Override
	public void insertOrganizeUser(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("delflag", 0);
		data.put("createtime", new Date());
		data.put("manageid", UUID.randomUUID().toString().replaceAll("-", ""));
		this.companyMapper.insertOrganizeUser(data);
		
		//修改组织子集数量+1
		Map<String, Object> organizemap=new HashMap<String, Object>();
		organizemap.put("organizeid", data.get("organizeid"));
		organizemap.put("usernum", 1);
		this.companyMapper.updateOrganize(organizemap);
	}

	@Override
	public void insertOrganizeMap(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.companyMapper.insertOrganizeMap(data);
		
		//插入默认的模板
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
				tempmap.put("createid", data.get("userid"));
				tempmap.put("companyid", data.get("companyid"));
				tempmap.put("organizeid", data.get("organizeid"));
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
					promap.put("createid", data.get("userid"));
					this.organizeTemplateMapper.insertOrganizeTemplateProjectInfo(promap);
					num++;
				}
			}
		}
	}

	/**
	 * 查询组织架构的上下级是否存在店面
	 * 传入参数：datacode，companyid
	 * @param map
	 * @return
	 */
	@Override
	public int getOrganizeStoreIsExsits(Map<String, Object> map) {
		// TODO Auto-generated method stub
		int count = 0;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("companyid", map.get("companyid"));
		paramMap.put("myorganizeid", map.get("myorganizeid"));
		//查询某组织架构上级是否存在店面
		String datacode = String.valueOf(map.get("datacode"));
		int num = datacode.length();
		List<String> datacodelist = new ArrayList<String>();
		for(int i=0; i<num; i+=3){
			if((num - i) >= 3){
				datacodelist.add(datacode.substring(0, (num-i)));
			}
		}
		paramMap.put("datacodelist", datacodelist);
		count = this.companyMapper.getOrganizeStoreIsExsits(paramMap);
		if(map.containsKey("update")){
			//查询某组织架构下级是否存在店面
			paramMap.put("likedatacode", datacode);
			paramMap.remove("datacodelist");
			count += this.companyMapper.getOrganizeStoreIsExsits(paramMap);
		}
		return count;
	}

	
	/**
	 * 查询某组织机构下面 最大的排序值
	 * @param map
	 * @return
	 */
	@Override
	public int getOrganizeMaxPriorityNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.companyMapper.getOrganizeMaxPriorityNum(map);
	}

	/**
	 * 查询公司和创建者信息
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getComapnyCreateUserInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.companyMapper.getComapnyCreateUserInfo(map);
	}

	@Override
	public void updatecompanyedition(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.companyMapper.updatecompanyedition(map);
	}

	/**
	 * 查询某组织上级的店面信息
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> queryUserUpIsExistsShop(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("companyid", map.get("companyid"));
		//查询某组织架构上级是否存在店面
		String datacode = String.valueOf(map.get("datacode"));
		int num = datacode.length();
		List<String> datacodelist = new ArrayList<String>();
		for(int i=0; i<num; i+=3){
			if((num - i) >= 3){
				datacodelist.add(datacode.substring(0, (num-i)));
			}
		}
		paramMap.put("datacodelist", datacodelist);
		return this.companyMapper.queryUserUpIsExistsShop(paramMap);
	}

	/**
	 * 查询人员所属的组织架构
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getUserShops(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.companyMapper.getUserShops(map);
	}

	@Override
	public int getCompanyUsedUserNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.companyMapper.getCompanyUsedUserNum(map);
	}

	@Override
	public List<Map<String,Object>> getAllComapnyListByPage(Map<String,Object> map){
		List<Map<String,Object>> companylist = this.companyMapper.getAllComapnyListByPage(map);
		return companylist;
	}
	
}
