package com.collection.service.impl.worksheet;

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
import com.collection.dao.worksheet.InspectMapper;
import com.collection.service.worksheet.InspectService;
import com.collection.util.Constants;
/**
 * 检查管理
 * @author silence
 *
 */
public class InspectServiceImpl implements InspectService{

	@Autowired InspectMapper inspectMapper;
	@Autowired IndexMapper indexMapper;
	@Autowired UserInfoMapper userinfoMapper;
	@Autowired PersonalMapper personalMapper;
	
	@Override
	public List<Map<String, Object>> getInspectTemplateList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.inspectMapper.getInspectTemplateList(data);
	}

	@Override
	public List<Map<String, Object>> getTemplateProjectList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.inspectMapper.getTemplateProjectList(data);
	}

	@Override
	public String insertLeaveshop(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String inspectid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("inspectid", inspectid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		data.put("status", 0);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo=this.userinfoMapper.getUserInfo(userInfo);
		//添加到提醒表
		if(userInfo!=null && userInfo.size()>0){
			Map<String,	Object> remindmap=new HashMap<String, Object>();
			remindmap.put("remindid", UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid",data.get("companyid"));
			remindmap.put("userid",data.get("examineuserid"));
			remindmap.put("title","查看");
			remindmap.put("content",userInfo.get("realname")+"抄送了一个离店报告,请您查看");
			remindmap.put("linkurl", "worksheet/leave_detail.html?inspectid="+inspectid+"&userid="+data.get("examineuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getLeavedetailInfo?inspectid="+inspectid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", inspectid);
			remindmap.put("resourcetype", 10);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);

			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("userid", userid);
				String registrationid = this.indexMapper.getRegistrationIdByUserId(param);
				String title=userInfo.get("realname")+"抄送了一个离店报告,请您查看";
				String url="/worksheet/leave_detail.html?inspectid="+inspectid+"&userid="+userid;
				//JPushAndriodAndIosMessage(userid, title, url);
				//JPushRegIdUtil.PushUrlByRegId(registrationid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",inspectid);
		forwordmap.put("receiveid",data.get("createid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",10);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加审批人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",inspectid);
		forwordmap.put("receiveid",data.get("examineuserid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",10);
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
						forwordmap.put("resourceid",inspectid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",10);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"抄送了一个离店报告,请您查看";
						String url="/worksheet/leave_detail.html?inspectid="+inspectid+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.inspectMapper.insertLeaveshop(data);
		return inspectid;
	}

	@Override
	public void insertInspectStar(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("starid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime", new Date());
		data.put("delflag", 0);
		this.inspectMapper.insertInspectStar(data);
	}

	@Override
	public List<Map<String, Object>> getLeaveShopList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.inspectMapper.getLeaveShopList(data);
	}

	@Override
	public int getLeaveShopListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.inspectMapper.getLeaveShopListNum(data);
	}

	/**
	 * 查询离店时间列表
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getLeaveShopListTimes(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.inspectMapper.getLeaveShopListTimes(map);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("userid", map.get("userid"));
		param.put("status", map.get("status"));
		param.put("companyid", map.get("companyid"));
		for(Map<String,Object> mm : list){
			param.put("starttime", mm.get("createtime"));
			param.put("endtime", mm.get("createtime"));
			List<Map<String,Object>> leaveshoplist = this.inspectMapper.getLeaveShopList(param);
			mm.put("leaveshoplist", leaveshoplist);
		}
		return list;
	}

	/**
	 * 查询离店检查时间列表总数
	 * @param map
	 * @return
	 */
	@Override
	public int getLeaveShopListTimesCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.inspectMapper.getLeaveShopListTimesCount(map);
	}
	
	@Override
	public Map<String, Object> getLeaveShopInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> inspectInfo=this.inspectMapper.getLeaveShopInfo(data);
		if(inspectInfo != null && inspectInfo.size()> 0){
			List<Map<String, Object>> starlist=this.inspectMapper.getInspectStarList(inspectInfo);
			inspectInfo.put("starlist", starlist);
		}
		
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(inspectInfo.containsKey("CCuserids")){
			String ccuserids = String.valueOf(inspectInfo.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		inspectInfo.put("ccuserlist", ccuserlist);
		
		return inspectInfo;
	}

	@Override
	public void updateLeaveShop(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("inspectid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		this.inspectMapper.updateLeaveShop(data);
	}

	
	
	@Override
	public String insertBeforeMeal(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String inspectid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("inspectid", inspectid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		data.put("status", 0);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo=this.userinfoMapper.getUserInfo(userInfo);
		//添加到提醒表
		if(userInfo!=null && userInfo.size()>0){
			Map<String,	Object> remindmap=new HashMap<String, Object>();
			remindmap.put("remindid", UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid",data.get("companyid"));
			remindmap.put("userid",data.get("examineuserid"));
			remindmap.put("title","查看");
			remindmap.put("content",userInfo.get("realname")+"抄送了一个餐前检查报告,请您查看");
			remindmap.put("linkurl", "worksheet/precheck_detail.html?inspectid="+inspectid+"&userid="+data.get("examineuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getBeforeMealInfo?inspectid="+inspectid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", inspectid);
			remindmap.put("resourcetype", 11);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title=userInfo.get("realname")+"抄送了一个餐前检查报告,请您查看";
				String url="/worksheet/precheck_detail.html?inspectid="+inspectid+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",inspectid);
		forwordmap.put("receiveid",data.get("createid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",11);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加审批人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",inspectid);
		forwordmap.put("receiveid",data.get("examineuserid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",11);
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
						forwordmap.put("resourceid",inspectid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",11);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"抄送了一个餐前检查报告,请您查看";
						String url="/worksheet/precheck_detail.html?inspectid="+inspectid+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.inspectMapper.insertBeforeMeal(data);
		return inspectid;
	}

	@Override
	public List<Map<String, Object>> getBeforeMealList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.inspectMapper.getBeforeMealList(data);
	}

	@Override
	public int getBeforeMealListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.inspectMapper.getBeforeMealListNum(data);
	}

	/**
	 * 查询餐前检查列表
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBeforeMealListTimes(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.inspectMapper.getBeforeMealListTimes(map);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("userid", map.get("userid"));
		param.put("status", map.get("status"));
		param.put("companyid", map.get("companyid"));
		for(Map<String,Object> mm : list){
			param.put("starttime", mm.get("createtime"));
			param.put("endtime", mm.get("createtime"));
			List<Map<String,Object>> beforemeallist = this.inspectMapper.getBeforeMealList(param);
			mm.put("beforemeallist", beforemeallist);
		}
		return list;
	}

	/**
	 * 查询餐前检查列表总数
	 * @param map
	 * @return
	 */
	@Override
	public int getBeforeMealListTimesCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.inspectMapper.getBeforeMealListTimesCount(map);
	}
	
	@Override
	public Map<String, Object> getBeforeMealInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> inspectInfo=this.inspectMapper.getBeforeMealInfo(data);
		if(inspectInfo != null && inspectInfo.size()> 0){
			List<Map<String, Object>> starlist=this.inspectMapper.getInspectStarList(inspectInfo);
			inspectInfo.put("starlist", starlist);
		}
		
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(inspectInfo.containsKey("CCuserids")){
			String ccuserids = String.valueOf(inspectInfo.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		inspectInfo.put("ccuserlist", ccuserlist);
		
		return inspectInfo;
	}

	@Override
	public void updateBeforeMeal(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("inspectid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		this.inspectMapper.updateBeforeMeal(data);
		
	}

	
	@Override
	public String insertKitchen(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String inspectid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("inspectid", inspectid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		data.put("status", 0);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo=this.userinfoMapper.getUserInfo(userInfo);
		//添加到提醒表
		if(userInfo!=null && userInfo.size()>0){
			Map<String,	Object> remindmap=new HashMap<String, Object>();
			remindmap.put("remindid", UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid",data.get("companyid"));
			remindmap.put("userid",data.get("examineuserid"));
			remindmap.put("title","查看");
			remindmap.put("content",userInfo.get("realname")+"发送了一个厨房检查报告,请您查看");
			remindmap.put("linkurl", "worksheet/kitchencheck_detail.html?inspectid="+inspectid+"&userid="+data.get("examineuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getKitchenInfo?inspectid="+inspectid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", inspectid);
			remindmap.put("resourcetype", 12);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title=userInfo.get("realname")+"发送了一个厨房检查报告,请您查看";
				String url="/worksheet/kitchencheck_detail.html?inspectid="+inspectid+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",inspectid);
		forwordmap.put("receiveid",data.get("createid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",12);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加审批人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",inspectid);
		forwordmap.put("receiveid",data.get("examineuserid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",12);
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
						forwordmap.put("resourceid",inspectid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",12);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"抄送了一个厨房检查报告,请您查看";
						String url="/worksheet/kitchencheck_detail.html?inspectid="+inspectid+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.inspectMapper.insertKitchen(data);
		return inspectid;
	}

	@Override
	public List<Map<String, Object>> getKitchenList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.inspectMapper.getKitchenList(data);
	}

	@Override
	public int getKitchenListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.inspectMapper.getKitchenListNum(data);
	}

	/**
	 * 查询厨房检查时间列表信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getKitchenListTimes(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.inspectMapper.getKitchenListTimes(map);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("userid", map.get("userid"));
		param.put("status", map.get("status"));
		param.put("companyid", map.get("companyid"));
		for(Map<String,Object> mm : list){
			param.put("starttime", mm.get("createtime"));
			param.put("endtime", mm.get("createtime"));
			List<Map<String,Object>> kitchenlist = this.inspectMapper.getKitchenList(param);
			mm.put("kitchenlist", kitchenlist);
		}
		return list;
	}

	/**
	 * 查询厨房检查时间列表信息的总数
	 * @param map
	 * @return
	 */
	@Override
	public int getKitchenListTimesCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.inspectMapper.getKitchenListTimesCount(map);
	}
	
	@Override
	public Map<String, Object> getKitchenInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> inspectInfo=this.inspectMapper.getKitchenInfo(data);
		if(inspectInfo != null && inspectInfo.size()> 0){
			List<Map<String, Object>> starlist=this.inspectMapper.getInspectStarList(inspectInfo);
			inspectInfo.put("starlist", starlist);
		}
		
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(inspectInfo.containsKey("CCuserids")){
			String ccuserids = String.valueOf(inspectInfo.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		inspectInfo.put("ccuserlist", ccuserlist);
		
		return inspectInfo;
	}

	@Override
	public void updateKitchen(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("inspectid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		this.inspectMapper.updateKitchen(data);
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
	public List<Map<String, Object>> getReportPrecheckInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.inspectMapper.getReportPrecheckInfo(map);
		return list;
	}

	@Override
	public List<Map<String, Object>> getReportKitChenCheckInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.inspectMapper.getReportKitChenCheckInfo(map);
		return list;
	}

	@Override
	public List<Map<String, Object>> getReportLeaveShopInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.inspectMapper.getReportLeaveShopInfo(map);
		return list;
	}

	@Override
	public List<Map<String, Object>> getKitchenListOnlyOne(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("status", 1);
		return this.inspectMapper.getKitchenListOnlyOne(map);
	}

	@Override
	public int getKitchenListNumOnlyOne(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("status", 1);
		return this.inspectMapper.getKitchenListNumOnlyOne(map);
	}

	@Override
	public List<Map<String, Object>> getLeaveShopListOnlyOne(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("status", 1);
		return this.inspectMapper.getLeaveShopListOnlyOne(map);
	}

	@Override
	public int getLeaveShopListNumOnlyOne(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("status", 1);
		return this.inspectMapper.getLeaveShopListNumOnlyOne(map);
	}

	@Override
	public List<Map<String, Object>> getBeforeMealListOnlyOne(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("status", 1);
		return this.inspectMapper.getBeforeMealListOnlyOne(map);
	}

	@Override
	public int getBeforeMealListNumOnlyOne(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("status", 1);
		return this.inspectMapper.getBeforeMealListNumOnlyOne(map);
	} 

}
