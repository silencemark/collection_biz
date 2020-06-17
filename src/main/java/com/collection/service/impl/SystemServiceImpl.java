package com.collection.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.CompanyMapper;
import com.collection.dao.SystemMapper;
import com.collection.service.SystemService;
/**
 * 采购管理
 * @author silence
 *
 */
public class SystemServiceImpl implements SystemService{
	@Autowired SystemMapper systemMapper;
	@Autowired CompanyMapper companyMapper;
	
	@Override
	public Map<String, Object> getUserInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.systemMapper.getUserInfo(data);
	}

	@Override
	public void updateUserInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("updatetime", new Date());
		this.systemMapper.updateUserInfo(data);
	}

	@Override
	public Map<String, Object> getIndexInfo() {
		// TODO Auto-generated method stub
		Map<String, Object> indexInfo=new HashMap<String, Object>();
		Map<String, Object> companyinfo=new HashMap<String, Object>();
		companyinfo.put("edition", 2);
		//查询收费和免费的公司
		int chargenum=this.companyMapper.getCompanyNum(companyinfo);
		companyinfo.put("edition", 1);
		int freechargenum=this.companyMapper.getCompanyNum(companyinfo);
		
		indexInfo.put("chargenum", chargenum);
		indexInfo.put("freechargenum", freechargenum);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("isfristlogin",0);
		int usernum=this.companyMapper.getUserNum(userInfo);
		userInfo.put("isfristlogin",1);
		int nologinnum=this.companyMapper.getUserNum(userInfo);
		
		indexInfo.put("usernum", usernum);
		indexInfo.put("nologinnum", nologinnum);
		
		return indexInfo;
	}

	@Override
	public List<Map<String, Object>> getNoticeList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.systemMapper.getNoticeList(data);
	}

	@Override
	public int getNoticeListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.systemMapper.getNoticeListNum(data);
	}

	@Override
	public int getSystemMessageCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getSystemMessageCount(map);
	}

	@Override
	public List<Map<String, Object>> getSystemMessage(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getSystemMessage(map);
	}

	@Override
	public void insertSystemMessage(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("messageid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("createtime", new Date());
		map.put("delflag",0);
		this.systemMapper.insertSystemMessage(map);//推送信息
		String title="您有一个新的系统公告,请你查看！";
		String url="/member/notice_detail.html?messageid="+map.get("messageid")+"";
		//JPushUtil.PushAllUrl(title,url);
	}

	@Override
	public void updataSystemMessageDetail(Map<String, Object> map) {
		map.put("updatetime", new Date());
		this.systemMapper.updataSystemMessageDetail(map);
		
	}

	@Override
	public int getSystemBackCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getSystemBackCount(map);
	}

	@Override
	public List<Map<String, Object>> getSystemBacklist(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getSystemBacklist(map);
	}

	@Override
	public void  updataSystemBack(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		this.systemMapper.updataSystemBack(map);
	}

	@Override
	public void insertSystemBackReply(Map<String, Object> map) {
		map.put("replyid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("createtime", new Date());
		map.put("delflag",0);
		map.put("status",0);
		this.systemMapper.insertSystemBackReply(map);
		
	}

	@Override
	public Map<String, Object> getSystemBackReply(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getSystemBackReply(map);
	}


	@Override
	public List<Map<String, Object>> getAdminUserList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.systemMapper.getAdminUserList(data);
	}

	@Override
	public int getAdminUserListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.systemMapper.getAdminUserListNum(data);
	}

	@Override
	public void updateAdminUser(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.systemMapper.updateAdminUser(data);
	}

	@Override
	public void insertAdminUser(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("userid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("delflag", 0);
		data.put("status", 1);
		data.put("createtime", new Date());
		this.systemMapper.insertAdminUser(data);
	}

	@Override
	public List<Map<String, Object>> getLogList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.systemMapper.getLogList(data);
	}

	@Override
	public int getLogListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.systemMapper.getLogListNum(data);
	}

	@Override
	public List<Map<String, Object>> getEverydayDataList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=this.systemMapper.getEverydayTypeList(data);
		for(Map<String, Object> type:list){
			List<Map<String, Object>> datalist=this.systemMapper.getEverydayDataList(type);
			type.put("datalist", datalist);
		} 
		return list;
	}

	@Override
	public List<Map<String, Object>> getSystemPctop(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getSystemPctop(map);
	}

	@Override
	public int getSystemPctopCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getSystemPctopCount(map);
	}
	@Override
	public void updateSystemPctop(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		this.systemMapper.updateSystemPctop(map);
	}
	@Override
	public List<Map<String, Object>> getSystemPctopModular(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getSystemPctopModular(map);
	}
	@Override
	public void insertSystemPctop(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("codeid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("delflag", 0);
		map.put("status",0);
		map.put("createtime", new Date());
		this.systemMapper.insertSystemPctop(map);
	}

	@Override
	public int getSystemDictTypeCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getSystemDictTypeCount(map);
	}

	@Override
	public List<Map<String, Object>> getSystemDictType(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getSystemDictType(map);
	}

	@Override
	public List<Map<String, Object>> getSystemDict(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getSystemDict(map);
	}

	@Override
	public void updateSystemDict(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		this.systemMapper.updateSystemDict(map);
	}

	@Override
	public void insertSystemDict(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("dataid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("delflag", 0);
		map.put("status",0);
		map.put("createtime", new Date());
		this.systemMapper.insertSystemDict(map);
	}

	@Override
	public List<Map<String, Object>> getManageBanner(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getManageBanner(map);
	}

	@Override
	public void updateManageBanner(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		this.systemMapper.updateManageBanner(map);
	}

	@Override
	public void insertManageBanner(Map<String, Object> map) {
		map.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("delflag", 0);
		map.put("status",0);
		map.put("createtime", new Date());
		this.systemMapper.insertManageBanner(map);
	}

	@Override
	public List<Map<String, Object>> getManageBannerSort(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getManageBannerSort(map);
	}

	@Override
	public List<Map<String, Object>> getManageaAppconfig(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getManageaAppconfig(map);
	}

	@Override
	public void updateManageAppconfig(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		this.systemMapper.updateManageAppconfig(map);
	}

	@Override
	public void insertManageAppconfigr(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("codeid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("delflag", 0);
		map.put("status",0);
		map.put("createtime", new Date());
		this.systemMapper.insertManageAppconfigr(map);
	}

	@Override
	public int getManageaAppconfigOrder(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemMapper.getManageaAppconfigOrder(map);
	}

	@Override
	public void insertDailyReportData(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("dataid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("delflag", 0);
		data.put("createtime", new Date());
		this.systemMapper.insertDailyReportData(data);
	}

	@Override
	public void updateDailyReportData(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("updatetime", new Date());
		this.systemMapper.updateDailyReportData(data);
	}



	
}
