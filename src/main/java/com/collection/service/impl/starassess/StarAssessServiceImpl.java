package com.collection.service.impl.starassess;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IndexMapper;
import com.collection.dao.UserInfoMapper;
import com.collection.dao.personal.PersonalMapper;
import com.collection.dao.starassess.StarAssessMapper;
import com.collection.service.starassess.StarAssessService;
import com.collection.util.Constants;
/**
 * 采购管理
 * @author silence
 *
 */
public class StarAssessServiceImpl implements StarAssessService{

	@Autowired StarAssessMapper starAssessMapper;
	@Resource IndexMapper indexMapper;
	@Resource UserInfoMapper userinfoMapper;
	@Resource PersonalMapper personalMapper;
	
	/**
	 * app 查询自评项目列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> appGetEvaluateTemplateList(Map<String,Object> map){
		// TODO Auto-generated method stub
		return this.starAssessMapper.appGetEvaluateTemplateList(map);
	}
	
	/**
	 * 查询自评项目列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getEvaluateTemplateList(Map<String,Object> map){
		// TODO Auto-generated method stub
		return this.starAssessMapper.getEvaluateTemplateList(map);
	}
	
	@Override
	public List<Map<String, Object>> getStarProjectList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getStarProjectList(data);
	}
	@Override
	public String insertEvaluate(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String evaluateid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("evaluateid", evaluateid);
		data.put("createtime", new Date());
		data.put("status", 0);
		data.put("delflag", 0);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo=this.userinfoMapper.getUserInfo(userInfo);
		//添加到审批提醒表
		if(userInfo!=null && userInfo.size()>0){
			Map<String,	Object> remindmap=new HashMap<String, Object>();
			remindmap.put("remindid", UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid",data.get("companyid"));
			remindmap.put("userid",data.get("examineuserid"));
			remindmap.put("title","审批");
			remindmap.put("content",userInfo.get("realname")+"申请了一个岗位星值自评,请您审批");
			remindmap.put("linkurl", "kpi/jobstar_check.html?evaluateid="+evaluateid+"&userid="+data.get("examineuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/jobStarDetail?evaluateid="+evaluateid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", evaluateid);
			remindmap.put("resourcetype", 6);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title=userInfo.get("realname")+"申请了一个岗位星值自评,请您审批";
				String url="/kpi/jobstar_check.html?evaluateid="+evaluateid+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",evaluateid);
		forwordmap.put("receiveid",data.get("createid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",6);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加审批人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",evaluateid);
		forwordmap.put("receiveid",data.get("examineuserid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",6);
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
						forwordmap.put("resourceid",evaluateid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",6);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"申请了一个岗位星值自评,请查看！";
						String url="/kpi/jobstar_detail.html?evaluateid="+evaluateid+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.starAssessMapper.insertEvaluate(data);
		return evaluateid;
	}
	@Override
	public void insertEvaluateStar(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("starid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("delflag", 0);
		data.put("createtime", new Date());
		this.starAssessMapper.insertEvaluateStar(data);
	}
	@Override
	public Map<String, Object> getEvaluateInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String,Object> evaluatemap = this.starAssessMapper.getEvaluateInfo(data);
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(evaluatemap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(evaluatemap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		data.put("resourceid", data.get("evaluateid"));
		List<Map<String,Object>> commentlist= starAssessMapper.getCommentForwardUserids(data);
		evaluatemap.put("ccuserlist", ccuserlist);
		evaluatemap.put("forwardnamelist", commentlist);
		return evaluatemap;
	}
	@Override
	public List<Map<String, Object>> getEvaluateStarList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getEvaluateStarList(data);
	}
	@Override
	public void updateEvaluate(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("evaluateid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		this.starAssessMapper.updateEvaluate(data);
	}
	@Override
	public List<Map<String, Object>> getEvaluateList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getEvaluateList(data);
	}
	@Override
	public int getEvaluateListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getEvaluateListNum(data);
	}
	
	/*---------------------------------------综合星值-------------------------------------------------------*/
	
	@Override
	public String insertOverallvaluate(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String overallvaluateid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("overallvaluateid", overallvaluateid);
		data.put("createtime", new Date());
		data.put("status", 0);
		data.put("delflag", 0);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo=this.userinfoMapper.getUserInfo(userInfo);
		//添加到审批提醒表
		if(userInfo!=null && userInfo.size()>0){
			Map<String,	Object> remindmap=new HashMap<String, Object>();
			remindmap.put("remindid", UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid",data.get("companyid"));
			remindmap.put("userid",data.get("examineuserid"));
			remindmap.put("title","审批");
			remindmap.put("content",userInfo.get("realname")+"申请了一个综合星值自评,请您审批");
			remindmap.put("linkurl", "kpi/allstar_check.html?overallvaluateid="+overallvaluateid+"&userid="+data.get("examineuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/allstarDetail?overallvaluateid="+overallvaluateid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", overallvaluateid);
			remindmap.put("resourcetype", 7);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title=userInfo.get("realname")+"申请了一个综合星值自评,请您审批";
				String url="/kpi/allstar_check.html?overallvaluateid="+overallvaluateid+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",overallvaluateid);
		forwordmap.put("receiveid",data.get("createid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",7);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加审批人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",overallvaluateid);
		forwordmap.put("receiveid",data.get("examineuserid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",7);
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
						forwordmap.put("resourceid",overallvaluateid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",7);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"申请了一个综合星值自评,请查看！";
						String url="/kpi/allstar_detail.html?overallvaluateid="+overallvaluateid+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		this.starAssessMapper.insertOverallvaluate(data);
		return overallvaluateid;
	}
	@Override
	public void insertOverallvaluateStar(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("starid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("delflag", 0);
		data.put("createtime", new Date());
		this.starAssessMapper.insertOverallvaluateStar(data);
	}
	@Override
	public Map<String, Object> getOverallvaluateInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String,Object> overallmap = this.starAssessMapper.getOverallvaluateInfo(data);
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(overallmap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(overallmap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		data.put("resourceid", data.get("overallvaluateid"));
		List<Map<String,Object>> commentlist= starAssessMapper.getCommentForwardUserids(data);
		overallmap.put("ccuserlist", ccuserlist);
		overallmap.put("forwardnamelist", commentlist);
		return overallmap;
	}
	@Override
	public List<Map<String, Object>> getOverallvaluateStarList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getOverallvaluateStarList(data);
	}
	@Override
	public void updateOverallvaluate(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("overallvaluateid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		this.starAssessMapper.updateOverallvaluate(data);
	}
	@Override
	public List<Map<String, Object>> getOverallvaluateList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getOverallvaluateList(data);
	}
	@Override
	public int getOverallvaluateListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getOverallvaluateListNum(data);
	}
	@Override
	public List<Map<String, Object>> getUserStarList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getUserStarList(data);
	}
	@Override
	public int getUserStarListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getUserStarListNum(data);
	}
	@Override
	public List<Map<String, Object>> getStarRuleList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
		return this.starAssessMapper.getStarRuleList(data);
	}
	@Override
	public int getStarRuleListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getStarRuleListNum(data);
	}
	@Override
	public Map<String, Object> getStarRuleInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getStarRuleInfo(data);
	}
	
	
	/**
	 * 查询综合星值的已处理的时间列表
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOverallvaluateListTime(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("status", 1);
		List<Map<String,Object>> list = this.starAssessMapper.getOverallvaluateListTime(map);
		for(Map<String,Object> time : list){
			map.put("beforetime", time.get("createtime"));
			map.put("aftertime", time.get("createtime"));
			List<Map<String,Object>> overallvaluatelist = this.starAssessMapper.getOverallvaluateList(map);
			time.put("overallvaluatelist", overallvaluatelist);
		}
		return list;
	}
	
	/**
	 * 查询综合星值的已处理的时间列表 的总数
	 * @param map
	 * @return
	 */
	@Override
	public int getOverallvaluateListTimeCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("status", 1);
		return this.starAssessMapper.getOverallvaluateListTimeCount(map);
	}
	
	/**
	 * 查询岗位星值时间列表
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getEvaluateListTime(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("status", 1);
		List<Map<String,Object>> list = this.starAssessMapper.getEvaluateListTime(map);
		for(Map<String,Object> time : list){
			map.put("beforetime", time.get("createtime"));
			map.put("aftertime", time.get("createtime"));
			List<Map<String,Object>> evaluateList = this.starAssessMapper.getEvaluateList(map);
			time.put("evaluatelist", evaluateList);
		}
		return list;
	}
	
	/**
	 * 查询岗位星值时间列表总数
	 * @param map
	 * @return
	 */
	@Override
	public int getEvaluateListTimeCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getEvaluateListTimeCount(map);
	}
	
	/**
	 * 添加kpi星级考核规则
	 * @param map
	 */
	@Override
	public String insertStarRankingRuleInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String ruleid = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("ruleid", ruleid);
		map.put("delflag", 0);
		map.put("createtime", new Date());
		this.starAssessMapper.insertStarRankingRuleInfo(map);
		return ruleid;
	}
	
	/**
	 * 修改kpi星级考核规则
	 * @param map
	 */
	@Override
	public void updateStarRankingRuleInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		this.starAssessMapper.updateStarRankingRuleInfo(map);
	}
	
	/**
	 * 查询规则详情
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getStarRankingRuleDetailInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String,Object> rulemap = this.starAssessMapper.getStarRankingRuleDetailInfo(map);
		Map<String, Object> rangemap=new HashMap<String, Object>();
		rangemap.put("resourcetype", 10);
		rangemap.put("resourceid", map.get("ruleid"));
		List<Map<String, Object>> rangelist=this.indexMapper.getRangeList(rangemap);
		rulemap.put("rangelist", rangelist);
		return rulemap;
	}

	/**
	 * 查询自评模板总数
	 * @param map
	 * @return
	 */
	@Override
	public int getEvaluateTemplateListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getEvaluateTemplateListCount(map);
	}

	/**
	 * 添加自评模板
	 * @param map
	 */
	@Override
	public String insertEvaluateTemplateInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String templateid = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("templateid", templateid);
		map.put("delflag", 0);
		map.put("createtime", new Date());
		this.starAssessMapper.insertEvaluateTemplateInfo(map);
		return templateid;
	}

	/**
	 * 修改自评模板信息
	 * @param map
	 */
	@Override
	public void updateEvaluateTemplateInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.starAssessMapper.updateEvaluateTemplateInfo(map);
	}

	/**
	 * 添加检查项目
	 * @param map
	 */
	@Override
	public void insertEvaluateProjectInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("projectid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("createtime", new Date());
		map.put("delflag", 0);
		this.starAssessMapper.insertEvaluateProjectInfo(map);
	}

	/**
	 * 修改检查项目信息
	 * @param map
	 */
	@Override
	public void updateEvaluateProjectInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.starAssessMapper.updateEvaluateProjectInfo(map);
	}

	
	@Override
	public Map<String, Object> getEvaluateTemplateDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String,Object> param = new HashMap<String,Object>();
		
		List<Map<String,Object>> prolist = this.getStarProjectList(map);
		
		Map<String, Object> rangemap=new HashMap<String, Object>();
		rangemap.put("resourcetype", 9);
		rangemap.put("resourceid", map.get("templateid"));
		List<Map<String, Object>> rangelist=this.indexMapper.getRangeList(rangemap);
		
		param.put("projectlist", prolist);
		param.put("rangelist", rangelist);
		
		return param;
	}

	/**
	 * 查询单个模板信息
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getEvaluateDetailInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getEvaluateDetailInfo(map);
	}

	/**
	 * 修改岗位自评星值信息
	 * @param map
	 */
	@Override
	public void updateEvaluateStarInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.starAssessMapper.updateEvaluateStarInfo(map);
	}

	/**
	 * 修改综合自评星值信息
	 * @param map
	 */
	@Override
	public void updateOverAllaluateStarInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.starAssessMapper.updateOverAllaluateStarInfo(map);
	}

	/**
	 * 查询公司的组织id
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getOrganizeByCompanyId(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getOrganizeByCompanyId(map);
	}

	/**
	 * 查询模板名称是否重复
	 * @param map
	 * @return
	 */
	@Override
	public int getEValuateTemplateNameIsExists(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.starAssessMapper.getEValuateTemplateNameIsExists(map);
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
}
