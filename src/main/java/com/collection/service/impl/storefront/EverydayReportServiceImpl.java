package com.collection.service.impl.storefront;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IndexMapper;
import com.collection.dao.UserInfoMapper;
import com.collection.dao.personal.PersonalMapper;
import com.collection.dao.storefront.EverydayReportMapper;
import com.collection.service.storefront.EverydayReportService;
import com.collection.util.Constants;
/**
 * 店面管理
 * @author silence
 *
 */
public class EverydayReportServiceImpl implements EverydayReportService{

	@Autowired EverydayReportMapper EverydayReportMapper;
	@Autowired IndexMapper indexMapper;
	@Autowired UserInfoMapper userinfoMapper;
	@Autowired PersonalMapper personalMapper;
	
	@Override
	public String insertEverydayReport(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String reportid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("reportid", reportid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		data.put("status", 0);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo=this.userinfoMapper.getUserInfo(userInfo);
		//添加到审批提醒表
		if(userInfo!=null && userInfo.size()>0){
			Map<String,	Object> remindmap=new HashMap<String, Object>();
			remindmap.put("remindid", UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid",data.get("companyid"));
			remindmap.put("userid",data.get("examineuserid"));
			remindmap.put("title","查看");
			remindmap.put("content",userInfo.get("realname")+"抄送了一个每日报表,请您查看");
			remindmap.put("linkurl", "restaurant/dailyreport_check.html?reportid="+reportid);
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getDailyreportInfo?reportid="+reportid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", reportid);
			remindmap.put("resourcetype", 21);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title=userInfo.get("realname")+"发送了一个每日报表,请您查看";
				String url="/restaurant/dailyreport_check.html?reportid="+reportid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",reportid);
		forwordmap.put("receiveid",data.get("createid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",21);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加审批人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",reportid);
		forwordmap.put("receiveid",data.get("examineuserid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",21);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);

		String ccuserids = "";
		//添加抄送人到转发表
		if(data.containsKey("userlist") && !"".equals(data.get("userlist"))){
			JSONObject json=JSONObject.fromObject(data.get("userlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String,Object> user : userlist){
				try {
					String userid=String.valueOf(user.get("userid"));
					if(!userid.equals(data.get("examineuserid")) && !userid.equals(data.get("createid"))){
						forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",data.get("companyid"));
						forwordmap.put("resourceid",reportid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",21);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"发送了一个每日报表,请您查看";
						String url="/restaurant/dailyreport_detail.html?reportid="+reportid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.EverydayReportMapper.insertEverydayReport(data);
		return reportid;
	}
	@Override
	
	public List<Map<String, Object>> getEverydayReportList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getEverydayReportList(data);
	}
	@Override
	public int getEverydayReportListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getEverydayReportListNum(data);
	}
	@Override
	public List<Map<String, Object>> getEverydayReportByDate(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> everydaylist=this.EverydayReportMapper.getEverydayReportByDate(data);
		for(Map<String, Object> everymap:everydaylist){
			data.put("createtime", everymap.get("createtime"));
			List<Map<String, Object>> reportlist=this.EverydayReportMapper.getReportByDate(data);
			everymap.put("reportlist", reportlist);
		}
		return everydaylist;
	}
	@Override
	public int getEverydayReportByDateNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getEverydayReportByDateNum(data);
	}
	@Override
	public List<Map<String, Object>> getEverydayReportModule(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getEverydayReportModule(data);
	}
	@Override
	public List<Map<String, Object>> getEverydayReportDictList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getEverydayReportDictList(data);
	}
	@Override
	public void insertReportExtend(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String extendid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("createtime", new Date());
		data.put("delflag", 0);
		data.put("extendid", extendid);
		this.EverydayReportMapper.insertReportExtend(data);
	}
	@Override
	public Map<String, Object> getEverydayReportInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> reportmap=this.EverydayReportMapper.getEverydayReportInfo(data);
		List<Map<String, Object>> extendlist=this.EverydayReportMapper.getExtendList(reportmap);
		reportmap.put("extendlist", extendlist);
		
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(reportmap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(reportmap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		reportmap.put("ccuserlist", ccuserlist);
		
		return reportmap;
	}
	@Override
	public void updateEverydayReport(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("reportid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		this.EverydayReportMapper.updateEverydayReport(data);
	}
	@Override
	public List<Map<String, Object>> getReportIncomeChart(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> chartlist=new ArrayList<Map<String,Object>>();
		
		List<Map<String, Object>> consumelist=this.EverydayReportMapper.getIncomeChartByConsume(data);
		
		/*List<Map<String, Object>> uselist=this.EverydayReportMapper.getIncomeChartByUse(data);
		
		for(Map<String, Object> consume:consumelist){
			for(Map<String, Object> use:uselist){
				if(consume.get("comparetime").equals(use.get("comparetime"))){
					Map<String, Object> chartmap=new HashMap<String, Object>();
					chartmap.put("comparetime",consume.get("comparetime"));
					chartmap.put("price",Double.parseDouble(consume.get("sumprice")+"")-Double.parseDouble(use.get("sumprice")+""));
					chartlist.add(chartmap);
				}
			}
		}*/
		return consumelist;
	}
	@Override
	public List<Map<String, Object>> getReportFlowChart(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getReportFlowChart(data);
	}
	@Override
	public List<Map<String, Object>> getReportConsumptionChart(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getReportConsumptionChart(data);
	}
	@Override
	public List<Map<String, Object>> getReportSatisfyChart(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getReportSatisfyChart(data);
	}
	@Override
	public List<Map<String, Object>> getReportAttendanceChart(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> chartlist=new ArrayList<Map<String,Object>>();
		
		List<Map<String, Object>> alllist=this.EverydayReportMapper.getReportAttendanceChartAll(data);
		
		List<String> dataids=new ArrayList<String>();
		dataids.add("20");

		data.put("dataids", dataids);
		List<Map<String, Object>> attendancelist=this.EverydayReportMapper.getReportAttendanceChart(data);
		//每天的营业额
		List<Map<String, Object>> consumelist=this.EverydayReportMapper.getIncomeChartByConsume(data);
		
		for(Map<String, Object> all:alllist){
			for(int i=0; i<attendancelist.size(); i++){
				for(int k=0; k<consumelist.size(); k++){
					if(all.get("comparetime").equals(attendancelist.get(i).get("comparetime")) && all.get("comparetime").equals(consumelist.get(k).get("comparetime"))){
						Map<String, Object> chartmap=new HashMap<String, Object>();
						chartmap.put("comparetime",attendancelist.get(i).get("comparetime"));
						chartmap.put("price", consumelist.get(k).get("price"));
						if(String.valueOf(all.get("sumprice")).equals("0.0") || String.valueOf(all.get("sumprice")).equals("0")){
							chartmap.put("value",0);
						}else{
							//chartmap.put("value",(Double.parseDouble(attendance.get("sumprice")+"")*100)/Double.parseDouble(all.get("sumprice")+""));
							chartmap.put("value",attendancelist.get(i).get("sumprice"));
						}
						chartlist.add(chartmap);
					}
				}
			}
		}
		return chartlist;
		
	}
	@Override
	public List<Map<String, Object>> getReportModuleList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getReportModuleList(data);
	}
	@Override
	public List<Map<String, Object>> getDictTypeList() {
		// TODO Auto-generated method stub
		List<Map<String, Object>> typelist=this.EverydayReportMapper.getTypeList();
		for(Map<String, Object> type:typelist){
			List<Map<String, Object>> dictlist=this.EverydayReportMapper.getDictList(type);
			type.put("dictlist", dictlist);
		}
		return typelist;
	}
	@Override
	public String insertTemplate(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String templateid=UUID.randomUUID().toString().replaceAll("-","");
		data.put("templateid", templateid);
		data.put("createtime", new Date());
		data.put("delflag",0);
		this.EverydayReportMapper.insertTemplate(data);
		return templateid;
	}
	@Override
	public void insertReportTemplateExtend(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("extendid", UUID.randomUUID().toString().replaceAll("-",""));
		data.put("createtime", new Date());
		data.put("delflag",0);
		this.EverydayReportMapper.insertReportTemplateExtend(data);
	}
	@Override
	public void updateTemplate(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("updatetime", new Date());
		this.EverydayReportMapper.updateTemplate(data);
	}
	@Override
	public Map<String, Object> getTemplateInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> templateInfo=this.EverydayReportMapper.getTemplateInfo(data);
		List<Map<String, Object>> extendlist=this.EverydayReportMapper.getTemplateExtend(data);
		templateInfo.put("extendlist", extendlist);
		
		Map<String, Object> rangemap=new HashMap<String, Object>();
		rangemap.put("resourceid", data.get("templateid"));
		rangemap.put("resourcetype", 11);
		List<Map<String, Object>> rangelist=this.indexMapper.getRangeList(rangemap);
		templateInfo.put("rangelist", rangelist);
		return templateInfo;
	}
	@Override
	public void deleteTemplateExtend(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.EverydayReportMapper.deleteTemplateExtend(data);
	}
	/**
	 * 根据组织id查询组织信息
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getOrganizeByOrganizeid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getOrganizeByOrganizeid(map);
	}
	@Override
	public Map<String, Object> getTodayReport(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getTodayReport(data);
	}
	@Override
	public String getLastBianjiNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getLastBianjiNum(data);
	}
	
	/**
	 * 推送通知栏信息
	 * @param userid
	 * @param title
	 * @param url
	 */
	public void JPushAndriodAndIosMessage(String userid, String title, String url){
		/*//获取推送的类型
		String type = Constants.JPUSHTYPE;
		//registrationid推送
		if(type.equals("registrationid")){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("userid", userid);
			//查询用户的registrationid
			try {
				String registrationid = this.indexMapper.getRegistrationIdByUserId(param);
				if(!"".equals(registrationid) && registrationid != null){
					//推送信息
					JPushRegIdUtil.PushUrlByRegId(registrationid, title, url);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}else if(type.equals("userid")){//userid 推送
			try {
				JPushAliaseUtil.PushUrlByAliase(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}*/
	}
	@Override
	public List<Map<String, Object>> getEverydayReportListOnlyOne(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getEverydayReportListOnlyOne(map);
	}
	@Override
	public int getEverydayReportListNumOnlyOne(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getEverydayReportListNumOnlyOne(map);
	}
	@Override
	public int getReportConsumptionChartCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getReportConsumptionChartCount(map);
	}
	@Override
	public int getReportSatisfyChartCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.EverydayReportMapper.getReportSatisfyChartCount(map);
	} 
}
