package com.collection.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IndexMapper;
import com.collection.dao.SystemMapper;
import com.collection.dao.UserInfoMapper;
import com.collection.service.IndexService;
import com.collection.util.Constants;

/**
 * 采购管理
 * 
 * @author silence
 * 
 */
public class IndexServiceImpl implements IndexService {

	@Autowired
	IndexMapper indexMapper;
	@Autowired
	SystemMapper systemMapper;
	@Autowired
	UserInfoMapper userInfoMapper;

	@Override
	public List<Map<String, Object>> getRemindList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getRemindList(data);
	}

	@Override
	public int getRemindnum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getRemindnum(data);
	}

	@Override
	public List<Map<String, Object>> getBannerList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getBannerList(data);
	}

	@Override
	public List<Map<String, Object>> getHomePageBanner(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getHomePageBanner(data);
	}

	@Override
	public int getForwordNotreadNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		int forwordnum = this.indexMapper.getForwordNotreadNum(data);
		int rangenum = 0;
		if (data.containsKey("rangetypes")) {
			rangenum = this.indexMapper.getRangeNotreadNum(data);
		}
		return forwordnum + rangenum;
	}

	/**
	 * 查询转发表未读数量
	 * 
	 * @param data
	 * @return
	 * @author silence
	 */
	@Override
	public int getRangeNotreadNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.indexMapper.getRangeNotreadNum(map);
	}

	@Override
	public List<Map<String, Object>> getOrganizeList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getOrganizeList(data);
	}

	@Override
	public List<Map<String, Object>> getUserList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getUserList(data);
	}

	@Override
	public List<Map<String, Object>> getUserListnew(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getUserListnew(data);
	}
	
	@Override
	public List<Map<String, Object>> getCityList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getCityList(data);
	}

	@Override
	public List<Map<String, Object>> getOrganizeListByUser(
			Map<String, Object> data) {
		List<Map<String, Object>> organizelistall = new ArrayList<Map<String, Object>>();

		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", data.get("userid"));
		userInfo = this.userInfoMapper.getUserInfo(userInfo);
		if (userInfo != null
				&& String.valueOf(userInfo.get("managerole")).equals("3")) {
			organizelistall = this.indexMapper.getAllShopList(data);
		} else if (userInfo != null
				&& String.valueOf(userInfo.get("managerole")).equals("2")) {
			Map<String, Object> organizemap = new HashMap<String, Object>();
			organizemap.put("userid", data.get("userid"));
			organizemap.put("companyid", data.get("companyid"));
			List<Map<String, Object>> organizelist = this.indexMapper
					.getOrganizeListByRange(organizemap);

			for (Map<String, Object> organize : organizelist) {
				organizeList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> organizelistone = getOrganize(organize);
				organizelistall.addAll(organizelistone);
			}
			organizelistall.addAll(organizelist);
			// list去重复
			for (int i = 0; i < organizelistall.size() - 1; i++) {
				for (int j = organizelistall.size() - 1; j > i; j--) {
					if (organizelistall.get(j).equals(organizelistall.get(i))) {
						organizelistall.remove(j);
					}
				}
			}
		} else {
			// TODO Auto-generated method stub
			List<Map<String, Object>> organizelist = this.indexMapper
					.getOrganizeListByUser(data);

			List<String> datacodelist = new ArrayList<String>();
			for (Map<String, Object> organize : organizelist) {
				String datacode = organize.get("datacode") + "";
				List<String> datalist = getcodelist(datacode);
				datacodelist.addAll(datalist);
			}
			// 查询父级架构、
			Map<String, Object> organizemap = new HashMap<String, Object>();
			organizemap.put("datacodelist", datacodelist);
			List<Map<String, Object>> organizeall = this.indexMapper
					.getOrganizeList(organizemap);

			for (Map<String, Object> organize : organizelist) {
				organizeList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> organizelistone = getOrganize(organize);
				organizelistall.addAll(organizelistone);
			}

			organizelistall.addAll(organizeall);

			organizelistall.addAll(organizelist);
			// list去重复
			for (int i = 0; i < organizelistall.size() - 1; i++) {
				for (int j = organizelistall.size() - 1; j > i; j--) {
					if (organizelistall.get(j).equals(organizelistall.get(i))) {
						organizelistall.remove(j);
					}
				}
			}
		}
		return organizelistall;
	}

	@Override
	public List<Map<String, Object>> getOrganizeByUser(Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> organizelist = this.indexMapper
				.getOrganizeListByUser(data);

		return organizelist;
	}

	private List<Map<String, Object>> organizeList = new ArrayList<Map<String, Object>>();

	public List<Map<String, Object>> getOrganize(Map<String, Object> data) {
		Map<String, Object> onemap = new HashMap<String, Object>();
		onemap.put("organizeid", data.get("organizeid"));
		List<Map<String, Object>> onelist = this.indexMapper
				.getOrganizeList(onemap);
		if (onelist != null && onelist.size() > 0) {
			organizeList.addAll(onelist);
			for (Map<String, Object> one : onelist) {
				getOrganize(one);
			}
		}
		return organizeList;
	}

	@Override
	public void insertRemindInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("noticeid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime", new Date());
		data.put("delflag", 0);
		this.indexMapper.insertRemindInfo(data);
	}

	@Override
	public List<Map<String, Object>> getCommentList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getCommentList(data);
	}

	@Override
	public void insertComment(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.indexMapper.insertComment(data);
	}

	@Override
	public void updateForwardRangeReadStatus(Map<String, Object> data) {
		data.put("isread", 1);
		// 标记发布范围为已读
		this.indexMapper.updateRangeReadStatus(data);
		// 标记转发表为已读
		this.indexMapper.updateForwardReadStatus(data);

	}

	@Override
	public void insertReleaseRange(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("rangeid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("delflag", 0);
		data.put("createtime", new Date());
		data.put("isread", 1);

		this.indexMapper.insertReleaseRange(data);
	}

	@Override
	public void insertReleaseRangeUser(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> rangeusermap = this.indexMapper
				.getReleaseRange(data);
		if (rangeusermap == null || rangeusermap.size() == 0) {
			data.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
			data.put("delflag", 0);
			data.put("createtime", new Date());
			if (data.containsKey("createid")
					&& data.containsKey("userid")
					&& String.valueOf(data.get("createid")).equals(
							String.valueOf(data.get("userid")))) {
				data.put("isread", 1);
			} else {
				data.put("isread", 0);
			}
			this.indexMapper.insertReleaseRangeUser(data);
		}
	}

	@Override
	public void insertfile(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("fileid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime", new Date());
		data.put("delflag", 0);
		this.indexMapper.insertfile(data);
	}

	/**
	 * 查询发布范围
	 * 
	 * @param map
	 * @return
	 * @author silence
	 */
	@Override
	public List<Map<String, Object>> getRangeList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getRangeList(data);
	}

	@Override
	public List<Map<String, Object>> getDictData(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getDictData(data);
	}

	@Override
	public void updateIsread(Map<String, Object> data) {
		// TODO Auto-generated method stub
		// 标记发布范围为未读
		this.indexMapper.updateRangeIsread(data);
		// 标记转发表为未读
		this.indexMapper.updateForwardIsread(data);
		// 标记系统公告人员表为已读/未读
		this.indexMapper.updateSystemMessageStatus(data);
	}

	@Override
	public Map<String, Object> getCityId(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getCityId(data);
	}

	@Override
	public List<Map<String, Object>> getUserBySearch(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getUserBySearch(data);
	}

	@Override
	public List<Map<String, Object>> getUserBySearchDisabled(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getUserBySearchDisabled(data);
	}

	@Override
	public List<Map<String, Object>> getOrganizeBySearch(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getOrganizeBySearch(data);
	}

	@Override
	public Map<String, Object> getOrganizeInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getOrganizeInfo(data);
	}

	@Override
	public List<Map<String, Object>> getUserListDisabled(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.indexMapper.getUserListDisabled(map);
	}

	@Override
	public List<Map<String, Object>> getOrganizeByUserList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		// 查询当前所拥有的组织
		// List<Map<String, Object>>
		// organizelist=this.indexMapper.getOrganizeListByUser(data);
		List<Map<String, Object>> organizelist = this.indexMapper
				.getOrganizeListByRange(data);
		organizeList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> organizelistall = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> organize : organizelist) {
			organizeList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> organizelistone = getOrganize(organize);
			organizelistall.addAll(organizelistone);
		}
		organizelistall.addAll(organizelist);

		List<String> datacodelist = new ArrayList<String>();
		for (Map<String, Object> organize : organizelist) {
			String datacode = organize.get("datacode") + "";
			List<String> datalist = getcodelist(datacode);
			datacodelist.addAll(datalist);
		}
		for(int i=0; i<datacodelist.size()-1; i++){
			for(int j=datacodelist.size()-1; j>i ;j--){
				if(datacodelist.get(i).equals(datacodelist.get(j))){
					datacodelist.remove(j);
				}
			}
		}
		// 查询父级架构、
		Map<String, Object> organizemap = new HashMap<String, Object>();
		organizemap.put("datacodelist", datacodelist);
		List<Map<String, Object>> organizeall = this.indexMapper
				.getOrganizeList(organizemap);

		for (Map<String, Object> all : organizeall) {
			all.put("isonlyread", 1);
			for (Map<String, Object> orga : organizelistall) {
				if (all.toString().equals(orga.toString())) {
					organizeall.remove(all);
				}
			}
		}
		organizelistall.addAll(organizeall);

		// list去重复
		for (int i = 0; i < organizelistall.size() - 1; i++) {
			for (int j = organizelistall.size() - 1; j > i; j--) {
				if (organizelistall.get(j).get("organizeid").equals(organizelistall.get(i).get("organizeid"))) {
					organizelistall.remove(j);
				}
			}
		}
				
		return organizelistall;
	}

	public static List<String> getcodelist(String code) {
		List<String> codelist = new ArrayList<String>();
		for (int i = 1; i <= code.length(); i++) {
			if (i % 3 == 0) {
				codelist.add(code.substring(0, code.length() - i));
			}
		}
		return codelist;
	}

	public static void main(String[] args) {
		List<String> datalist = getcodelist("1001101102101101");
		System.out.println(datalist);
	}

	@Override
	public List<Map<String, Object>> getUserListAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.indexMapper.getUserListAll(map);
	}

	@Override
	public void deleteRange(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.indexMapper.deleteRange(data);
	}

	@Override
	public int getUserListAllCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.indexMapper.getUserListAllCount(map);
	}

	@Override
	public String getWeatherUrl(String weathername) {
		// TODO Auto-generated method stub
		String weatherUrl = this.indexMapper.getWeatherUrl(weathername);
		if(weatherUrl == "" || weatherUrl == null || "".equals(weatherUrl)){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("weathername", weathername);
			this.indexMapper.insertWeatherInfo(map);
		}
		return weatherUrl;
	}

	@Override
	public void insertForword(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.indexMapper.insertForword(data);
	}

	@Override
	public void updateIsreadAll(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.indexMapper.updateIsreadAll(data);
	}

	@Override
	public void insertManageLog(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("codeid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime", new Date());
		this.indexMapper.insertManageLog(data);
	}

	@Override
	public List<Map<String, Object>> getNextOrgainizePurchase(
			Map<String, Object> data) {
		// TODO Auto-generated method stub

		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", data.get("userid"));
		userInfo = this.userInfoMapper.getUserInfo(userInfo);
		List<Map<String, Object>> organizelistall = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> shoplist = this.indexMapper
				.getOrganizeList(data);
		if (userInfo != null
				&& String.valueOf(userInfo.get("managerole")).equals("3")) {
			return shoplist;
		} else if (userInfo != null
				&& String.valueOf(userInfo.get("managerole")).equals("2")) {
			Map<String, Object> organizemap = new HashMap<String, Object>();
			organizemap.put("userid", data.get("userid"));
			organizemap.put("companyid", data.get("companyid"));
			List<Map<String, Object>> organizelist = this.indexMapper
					.getOrganizeListByRange(organizemap);
			for (Map<String, Object> organize : organizelist) {
				organizeList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> organizelistone = getOrganize(organize);
				organizelistall.addAll(organizelistone);
			}
			organizelistall.addAll(organizelist);
			// list去重复
			for (int i = 0; i < organizelistall.size() - 1; i++) {
				for (int j = organizelistall.size() - 1; j > i; j--) {
					if (organizelistall.get(j).equals(organizelistall.get(i))) {
						organizelistall.remove(j);
					}
				}
			}
			for (Map<String, Object> shop : shoplist) {
				int status = 0;
				for (Map<String, Object> org : organizelistall) {
					if (String.valueOf(org.get("organizeid")).equals(
							String.valueOf(shop.get("organizeid")))) {
						status = 1;
					}
				}
				if (status == 0) {
					shop.put("isnopower", 1);
				} else {
					shop.put("isnopower", 0);
				}
			}
			return shoplist;
		} else {
			// TODO Auto-generated method stub
			List<Map<String, Object>> organizelist = this.indexMapper
					.getOrganizeListByUser(data);

			List<String> datacodelist = new ArrayList<String>();
			for (Map<String, Object> organize : organizelist) {
				String datacode = organize.get("datacode") + "";
				List<String> datalist = getcodelist(datacode);
				datacodelist.addAll(datalist);
			}
			// 查询父级架构、
			Map<String, Object> organizemap = new HashMap<String, Object>();
			organizemap.put("datacodelist", datacodelist);
			List<Map<String, Object>> organizeall = this.indexMapper
					.getOrganizeList(organizemap);

			for (Map<String, Object> organize : organizelist) {
				organizeList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> organizelistone = getOrganize(organize);
				organizelistall.addAll(organizelistone);
			}

			organizelistall.addAll(organizeall);

			organizelistall.addAll(organizelist);
			// list去重复
			for (int i = 0; i < organizelistall.size() - 1; i++) {
				for (int j = organizelistall.size() - 1; j > i; j--) {
					if (organizelistall.get(j).equals(organizelistall.get(i))) {
						organizelistall.remove(j);
					}
				}
			}
			for (Map<String, Object> shop : shoplist) {
				int status = 0;
				for (Map<String, Object> org : organizelistall) {
					if (String.valueOf(org.get("organizeid")).equals(
							String.valueOf(shop.get("organizeid")))) {
						status = 1;
					}
				}
				if (status == 0) {
					shop.put("isnopower", 1);
				} else {
					shop.put("isnopower", 0);
				}
			}
			return shoplist;
		}
	}

	@Override
	public List<Map<String, Object>> getReturnAmountList() {
		// TODO Auto-generated method stub
		return this.indexMapper.getReturnAmountList();
	}

	@Override
	public List<Map<String, Object>> getReturnAmountListagree() {
		// TODO Auto-generated method stub
		return this.indexMapper.getReturnAmountListagree();
	}
	
	/**
	 * 根据发布范围查询组织架构中的店面
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getShopListByReleaseRange(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.indexMapper.getShopListByReleaseRange(map);
	}

	/**
	 * 根据发布范围查询组织架构
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOrganizeListByReleaseRange(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.indexMapper.getOrganizeListByReleaseRange(map);
	}

	@Override
	public Map<String, Object> getDataList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.indexMapper.getDataList(map);
	}

	@Override
	public List<Map<String, Object>> getOrganizeListByDailyReport(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getOrganizeListByDailyReport(data);
	}

	@Override
	public Map<String, Object> getNextOrgainizePurchaseUser(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取未读数量数据
	 * 
	 * @param data
	 * @return
	 * @author silence
	 */
	@Override
	public Map<String, Object> getNotreadMap(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.indexMapper.getNotreadMap(data);
	}

	@Override
	public String getResourceVersion() {
		return this.indexMapper.getSysParamByModel(Constants.RESOURCE_VERSION);
	}

	@Override
	public Map<String, Object> getLaunchedVersion(int version) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		//获取系统最新版本
		Map<String, Object> paramMap = new HashMap<String, Object>();
		int newversion = Integer.parseInt(this.indexMapper.getSysParamByModel(Constants.LAUNCHED_VERSION));
		//当系统版本比用户应用版本新
		if(newversion>version){
			returnMap.put("version", newversion);
			returnMap.put("success", true);
			//leadmodel  引导图
			//startmodel 启动图
			paramMap = new HashMap<String, Object>();
			paramMap.put("model", "startmodel");
			List<Map<String, Object>> datalist = systemMapper.getManageaAppconfig(paramMap);
			returnMap.put("indeximg", Constants.PROJECT_PATH+datalist.get(0).get("url"));
			
			paramMap = new HashMap<String, Object>();
			paramMap.put("model", "leadmodel");
			datalist = systemMapper.getManageaAppconfig(paramMap);
			List<Map<String, Object>> valueList = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < datalist.size(); i++) {
				Map<String, Object> val = new HashMap<String, Object>();
				val.put("img", Constants.PROJECT_PATH+datalist.get(i).get("url"));
				val.put("order", datalist.get(i).get("order"));
				valueList.add(val);
			}
			returnMap.put("contentlist", valueList);
			returnMap.put("success", true);
		}else{
			returnMap.put("version", version);
			returnMap.put("success", true);
		}
		return returnMap;
	}
	
	@Override
	public Map<String, Object> getBottomMenuVersion(int version) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		//获取系统最新版本
		Map<String, Object> paramMap = new HashMap<String, Object>();
		int newversion = Integer.parseInt(this.indexMapper.getSysParamByModel(Constants.BOTTOMMENU_VERSION));
		//当系统版本比用户应用版本新
		if(newversion>version){
			returnMap.put("version", newversion);
			returnMap.put("success", true);
			paramMap = new HashMap<String, Object>();
			paramMap.put("model", "bottommenumodel");
			List<Map<String, Object>> datalist = systemMapper.getManageaAppconfig(paramMap);
			List<Map<String, Object>> valueList = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < datalist.size(); i++) {
				String value = (String) datalist.get(i).get("content");
				JSONObject valueJSON = JSONObject.fromObject(value);
				Map<String, Object> val = new HashMap<String, Object>();
				val.put("name", Constants.PROJECT_PATH+valueJSON.get("name"));
				val.put("order", valueJSON.get("order"));
				val.put("img", Constants.PROJECT_PATH+valueJSON.get("img"));
				val.put("selectedimg", Constants.PROJECT_PATH+valueJSON.get("selectedimg"));
				val.put("color", valueJSON.get("color"));
				val.put("selectedcolor", valueJSON.get("selectedcolor"));
				valueList.add(val);
			}
			returnMap.put("menulist", valueList);
			returnMap.put("success", true);
		}else{
			returnMap.put("version", version);
			returnMap.put("success", true);
		}
		return returnMap;
	}
	
	@Override
	public int updateSysParam(Map<String, Object> data) {
		
		if(data.get("codeid")!=null && data.get("model")==null){
			List<Map<String, Object>> datalist = systemMapper.getManageaAppconfig(data);
			data.put("model",datalist.get(0).get("model"));
		}
		if(data.get("model")!=null 
				&& 
				("leadmodel".equals(data.get("model")) || "startmodel".equals(data.get("model")))
			){
			String version = this.indexMapper.getSysParamByModel(Constants.LAUNCHED_VERSION);
			if(version==null || version.equals("")){
				version="0";
			}
			int newversion = Integer.parseInt(version);
			data.put("pvalue", newversion+1);
			data.put("pkey", Constants.LAUNCHED_VERSION);
			return indexMapper.updateSysParam(data);
		}else{
			return 0;
		}
	}

	/**
	 * 根据organizeid查询组织下面所有的人员信息
	 */
	@Override
	public List<Map<String, Object>> getUserListByOrganizeid(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.indexMapper.getUserListByOrganizeid(map);
	}

	/**
	 * 根据userid查询设备id
	 * @param map
	 * @return
	 */
	@Override
	public String getRegistrationIdByUserId(String userid) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("userid", userid);
		return this.indexMapper.getRegistrationIdByUserId(map);
	}
	
	
	@Override
	public List<Map<String,Object>> getUserManageStucture(Map<String,Object> data){
		
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", data.get("userid"));
		userInfo = this.userInfoMapper.getUserInfo(userInfo);

		if (userInfo != null && String.valueOf(userInfo.get("managerole")).equals("3")) {
			List<Map<String, Object>> shoplist = this.indexMapper.getOrganizeList(data);
			for(Map<String,Object> shopmap : shoplist){
				Map<String,Object> usermap = new HashMap<String,Object>();
				usermap.put("organizeid", shopmap.get("organizeid"));
				usermap.put("companyid", shopmap.get("companyid"));
				List<Map<String,Object>> userlist = new ArrayList<Map<String,Object>>();
				if(data.containsKey("disabled")){
					userlist = this.indexMapper.getUserListDisabled(usermap);
				}else{
					userlist = this.indexMapper.getUserList(usermap);
				}
				shopmap.put("userlist", userlist);
				shopmap.put("isnopower", 0);
			}
			return shoplist;
		} else if (userInfo != null && String.valueOf(userInfo.get("managerole")).equals("2")) {
			
			List<Map<String,Object>> organizeListAll = new ArrayList<Map<String,Object>>();
			
			Map<String, Object> organizemap = new HashMap<String, Object>();
			organizemap.put("userid", data.get("userid"));
			organizemap.put("companyid", data.get("companyid"));
			//查询用户管理的范围
			List<Map<String, Object>> organizelist = this.indexMapper.getOrganizeListByRange(organizemap);
			for (Map<String, Object> organize : organizelist) {
				List<String> datacodelist = new ArrayList<String>();
				String datacode = organize.get("datacode") + "";
				List<String> datalist = getcodelist(datacode);
				datacodelist.addAll(datalist);
				
				// 查询父级架构、
				organizemap = new HashMap<String, Object>();
				organizemap.put("datacodelist", datacodelist);
				List<Map<String, Object>> organizeall = this.indexMapper.getOrganizeList(organizemap);
				organizeListAll.addAll(organizeall);
				
				//查询当前用户所属组织架构的所有的下级组织架构
				Map<String,Object> dowmmap = new HashMap<String,Object>();
				dowmmap.put("code", datacode);
				dowmmap.put("companyid", organize.get("companyid"));
				List<Map<String,Object>> downorganizelist = this.indexMapper.getDownOrganizeListByDatacode(dowmmap);
				for(Map<String,Object> downmap : downorganizelist){
					Map<String,Object> usermap = new HashMap<String,Object>();
					usermap.put("organizeid", downmap.get("organizeid"));
					usermap.put("companyid", downmap.get("companyid"));
					List<Map<String,Object>> userlist = new ArrayList<Map<String,Object>>();
					if(data.containsKey("disabled")){
						userlist = this.indexMapper.getUserListDisabled(usermap);
					}else{
						userlist = this.indexMapper.getUserList(usermap);
					}
					downmap.put("userlist", userlist);
					downmap.put("isnopower", 0);
				}
				organizeListAll.addAll(downorganizelist);
			}
			return organizePriority(organizeListAll);
		} else {
			// TODO Auto-generated method stub
			//查询当前用户所在的组织架构信息
			List<Map<String, Object>> organizelist = this.indexMapper.getOrganizeListByUser(data);
			
			List<Map<String,Object>> organizeListAll = new ArrayList<Map<String,Object>>();
			//解析datacode
			for (Map<String, Object> organize : organizelist) {
				List<String> datacodelist = new ArrayList<String>();
				String datacode = organize.get("datacode") + "";
				List<String> datalist = getcodelist(datacode);
				datacodelist.addAll(datalist);
				
				// 查询父级架构、
				Map<String, Object> organizemap = new HashMap<String, Object>();
				organizemap.put("datacodelist", datacodelist);
				List<Map<String, Object>> organizeall = this.indexMapper.getOrganizeList(organizemap);
				organizeListAll.addAll(organizeall);
				
				//查询当前用户所属组织架构的所有的下级组织架构
				Map<String,Object> dowmmap = new HashMap<String,Object>();
				dowmmap.put("code", datacode);
				dowmmap.put("companyid", organize.get("companyid"));
				List<Map<String,Object>> downorganizelist = this.indexMapper.getDownOrganizeListByDatacode(dowmmap);
				for(Map<String,Object> downmap : downorganizelist){
					Map<String,Object> usermap = new HashMap<String,Object>();
					usermap.put("organizeid", downmap.get("organizeid"));
					usermap.put("companyid", downmap.get("companyid"));
					List<Map<String,Object>> userlist = new ArrayList<Map<String,Object>>();
					if(data.containsKey("disabled")){
						userlist = this.indexMapper.getUserListDisabled(usermap);
					}else{
						userlist = this.indexMapper.getUserList(usermap);
					}
					downmap.put("userlist", userlist);
					downmap.put("isnopower", 0);
				}
				organizeListAll.addAll(downorganizelist);
			}

			return organizePriority(organizeListAll);
		}
	}

	public List<Map<String,Object>> organizePriority(List<Map<String,Object>> orglist){
		if(orglist.size() > 1){
			for (int i = 0; i < orglist.size(); i++){
	            for (int j = i; j < orglist.size(); j++){
	                if (Integer.parseInt(String.valueOf(orglist.get(i).get("priority"))) > Integer.parseInt(String.valueOf(orglist.get(j).get("priority")))){
	                    Map<String,Object> temp = orglist.get(i);
	                    orglist.set(i, orglist.get(j));
	                    orglist.set(j, temp);
	                }
	            }
	        }
		}
		return orglist;
	}
	
	/**
	 * 查询某组织机构下的所有的组织信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getDownOrganizeListByDatacode(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.indexMapper.getDownOrganizeListByDatacode(map);
	}

	/**
	 * 删除用户关联的信息
	 * @param map
	 */
	@Override
	public void deleteForwaredUserInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.indexMapper.deleteForwaredUserInfo(map);
	}

	@Override
	public String getFormalVersionNumber(String key) {
		// TODO Auto-generated method stub
		return this.indexMapper.getSysParamByModel(key);
	}

	@Override
	public int getUserListnewCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.indexMapper.getUserListnewCount(map);
	}

	@Override
	public void insertUserUseLog(Map<String, Object> map) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("useid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("usetime", sdf.format(new Date()));
		map.put("lastusetime", new Date());
		this.indexMapper.insertUserUseLog(map);
	}
}
