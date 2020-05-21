package com.collection.service.impl.oa;

import java.sql.Timestamp;
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
import com.collection.dao.UserInfoMapper;
import com.collection.dao.oa.JournalMapper;
import com.collection.dao.personal.PersonalMapper;
import com.collection.service.oa.JournalService;
import com.collection.util.Constants;
/**
 * 日志管理
 * @author pengqinghai
 *
 */
public class JournalServiceImpl implements JournalService{

	@Autowired JournalMapper journalMapper;
	@Autowired IndexMapper indexMapper;
	@Autowired UserInfoMapper userInfoMapper;
	@Autowired PersonalMapper personalMapper;

  
	//把自己加入转发表
	private void sendToUser(Map<String,Object> map){
		Timestamp tm = new Timestamp(System.currentTimeMillis());
		Map<String,Object> dataMap = new HashMap<String, Object>();
		String forwarduserid = UUID.randomUUID().toString().replace("-", "");
        dataMap.put("forwarduserid", forwarduserid);
    	dataMap.put("resourceid", map.get("resourceid"));
    	dataMap.put("companyid", map.get("companyid"));
    	dataMap.put("createid", map.get("userid"));
    	dataMap.put("receiveid", map.get("userid"));
    	dataMap.put("delflag", "0");
    	dataMap.put("createtime", tm);
    	dataMap.put("isread", "1");
    	dataMap.put("resourcetype", map.get("resourcetype"));
    	dataMap.put("updatetime", tm);
    	dataMap.put("updateid", map.get("userid"));
    	indexMapper.insertForword(dataMap);
		
	}
	
	@SuppressWarnings("unused")
	private void sendToOther(Map<String,Object> map){
		Timestamp tm = new Timestamp(System.currentTimeMillis());
		Map<String,Object> dataMap = new HashMap<String, Object>();
		String forwarduserid = UUID.randomUUID().toString().replace("-", "");
        dataMap.put("forwarduserid", forwarduserid);
    	dataMap.put("resourceid", map.get("resourceid"));
    	dataMap.put("companyid", map.get("companyid"));
    	dataMap.put("createid", map.get("userid"));
    	dataMap.put("receiveid", map.get("commentuserid"));
    	dataMap.put("delflag", "0");
    	dataMap.put("createtime", tm);
    	dataMap.put("isread", "0");
    	dataMap.put("resourcetype", map.get("resourcetype"));
    	dataMap.put("updatetime", tm);
    	dataMap.put("updateid", map.get("userid"));
    	indexMapper.insertForword(dataMap);
		
	}

	@Override
	public List<Map<String, Object>> getJournalList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.journalMapper.getJournalList(map);
	}

	@Override
	public int getJournalListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.journalMapper.getJournalListCount(map);
	}



	@Override
	public int insertDaily(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Timestamp tm = new Timestamp(System.currentTimeMillis());
		String dailyid = UUID.randomUUID().toString().replace("-", "");
		map.put("dailyid", dailyid);
		map.put("createtime",tm);
		map.put("updateid", map.get("userid"));
		map.put("delflag", "0");
		map.put("status", "0");
		
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", map.get("userid"));
		userInfo = this.userInfoMapper.getUserInfo(userInfo);
		// 添加到审批提醒表
		if (userInfo != null && userInfo.size() > 0) {
			Map<String, Object> remindmap = new HashMap<String, Object>();
			remindmap.put("remindid",
					UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid", map.get("companyid"));
			remindmap.put("userid", map.get("commentuserid"));
			remindmap.put("title", "审批");
			remindmap.put("content", userInfo.get("realname")+"申请了一个日报,请您审批");
			remindmap.put("linkurl", "oa/log_detail.html?jotype=1&pid="+dailyid+"&userid="+map.get("commentuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getPcOaLogDetail?jotype=1&pid="+dailyid);
			remindmap.put("createid", map.get("userid"));
			remindmap.put("resourceid", dailyid);
			remindmap.put("resourcetype", 23);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=map.get("commentuserid")+"";
				String title=userInfo.get("realname")+"申请了一个日报,请您审批";
				String url="/oa/log_detail.html?jotype=1&pid="+dailyid+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		String ccuserids = "";
		//添加抄送人到转发表
		if(map.containsKey("CCuserlist") && !"".equals(map.get("CCuserlist"))){
			JSONObject json=JSONObject.fromObject(map.get("CCuserlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String,Object> user : userlist){
				try {
					String userid=String.valueOf(user.get("userid"));
					if(!userid.equals(map.get("examineuserid")) && !userid.equals(map.get("createid"))){
						Map<String,Object>forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",map.get("companyid"));
						forwordmap.put("resourceid",dailyid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",map.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",23);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"申请了一个日报,请查看！";
						String url="/oa/log_detail.html?jotype=1&pid="+dailyid+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			map.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		int row = this.journalMapper.insertDaily(map);
		if(row>0){
			map.put("resourcetype", "23");
			map.put("resourceid", dailyid);
			this.sendToUser(map);
			this.sendToArea(map);
			this.sendToOther(map);
		}
		 Map<String,Object> filemap = new HashMap<String,Object>();
		filemap.put("companyid", map.get("companyid"));
		filemap.put("resourceid", dailyid);
		filemap.put("resourcetype", 23);
		
		filemap.put("delflag", 0);
		//添加添加图片的路径
		String files = String.valueOf(map.get("filelist"));
		if(!"".equals(files) && map.containsKey("filelist")){
			String[] filelist = files.split(",");
			for(String url : filelist){
				filemap.put("fileid", UUID.randomUUID().toString().replaceAll("-",""));
				filemap.put("createtime", new Date());
				filemap.put("visiturl", url);
				filemap.put("type", 1);
				this.indexMapper.insertfile(filemap);
			}
		}
		
		//添加语音信息
		String sound = String.valueOf(map.get("sound"));
		if(!"".equals(map.get("sound")) && map.containsKey("sound")){
			filemap.put("fileid", UUID.randomUUID().toString().replaceAll("-",""));
			filemap.put("createtime", new Date());
			filemap.put("visiturl", sound);
			filemap.put("type", 2);
			this.indexMapper.insertfile(filemap);
		}
		return row; 
	}



	@Override
	public int insertWeekly(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Timestamp tm = new Timestamp(System.currentTimeMillis());
		String weeklyid = UUID.randomUUID().toString().replace("-", "");
		map.put("weeklyid", weeklyid);
		map.put("createtime",tm);
		map.put("updateid", map.get("userid"));
		map.put("delflag", "0");
		map.put("status", "0");
	    
	    Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", map.get("userid"));
		userInfo = this.userInfoMapper.getUserInfo(userInfo);
		// 添加到审批提醒表
		if (userInfo != null && userInfo.size() > 0) {
			Map<String, Object> remindmap = new HashMap<String, Object>();
			remindmap.put("remindid",
					UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid", map.get("companyid"));
			remindmap.put("userid", map.get("commentuserid"));
			remindmap.put("title", "审批");
			remindmap.put("content", userInfo.get("realname")+ "申请了一个周报,请您审批");
			remindmap.put("linkurl", "oa/log_detail.html?jotype=2&pid="+weeklyid+"&userid="+map.get("commentuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getPcOaLogDetail?jotype=2&pid="+weeklyid);
			remindmap.put("createid", map.get("userid"));
			remindmap.put("resourceid", weeklyid);
			remindmap.put("resourcetype", 24);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=map.get("commentuserid")+"";
				String title=userInfo.get("realname")+ "申请了一个周报,请您审批";
				String url="/oa/log_detail.html?jotype=2&pid="+weeklyid+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	    
		String ccuserids = "";
		//添加抄送人到转发表
		if(map.containsKey("CCuserlist") && !"".equals(map.get("CCuserlist"))){
			JSONObject json=JSONObject.fromObject(map.get("CCuserlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String,Object> user : userlist){
				try {
					String userid=String.valueOf(user.get("userid"));
					if(!userid.equals(map.get("examineuserid")) && !userid.equals(map.get("createid"))){
						Map<String,Object>forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",map.get("companyid"));
						forwordmap.put("resourceid",weeklyid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",map.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",24);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+ "申请了一个周报,请查看！";
						String url="/oa/log_detail.html?jotype=2&pid="+weeklyid+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			map.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		int row = this.journalMapper.insertWeekly(map);
	    if(row>0){
	    	map.put("resourcetype", "24");
			map.put("resourceid", weeklyid);
			this.sendToUser(map);
			this.sendToArea(map);
			this.sendToOther(map);
	    }
	    
	    Map<String,Object> filemap = new HashMap<String,Object>();
		filemap.put("companyid", map.get("companyid"));
		filemap.put("resourceid", weeklyid);
		filemap.put("resourcetype", 24);
		
		filemap.put("delflag", 0);
		//添加添加图片的路径
		String files = String.valueOf(map.get("filelist"));
		if(!"".equals(files) && map.containsKey("filelist")){
			String[] filelist = files.split(",");
			for(String url : filelist){
				filemap.put("fileid", UUID.randomUUID().toString().replaceAll("-",""));
				filemap.put("createtime", new Date());
				filemap.put("visiturl", url);
				filemap.put("type", 1);
				this.indexMapper.insertfile(filemap);
			}
		}
		
		//添加语音信息
		String sound = String.valueOf(map.get("sound"));
		if(!"".equals(map.get("sound")) && map.containsKey("sound")){
			filemap.put("fileid", UUID.randomUUID().toString().replaceAll("-",""));
			filemap.put("createtime", new Date());
			filemap.put("visiturl", sound);
			filemap.put("type", 2);
			this.indexMapper.insertfile(filemap);
		}
	    
	    return row;
	}



	@Override
	public int insertMonthly(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Timestamp tm = new Timestamp(System.currentTimeMillis());
		String monthlyid = UUID.randomUUID().toString().replace("-", "");
		map.put("monthlyid", monthlyid);
		map.put("createtime",tm);
		map.put("updateid", map.get("userid"));
		map.put("delflag", "0");
		map.put("status", "0");
		
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", map.get("userid"));
		userInfo = this.userInfoMapper.getUserInfo(userInfo);
		// 添加到审批提醒表
		if (userInfo != null && userInfo.size() > 0) {
			Map<String, Object> remindmap = new HashMap<String, Object>();
			remindmap.put("remindid",
					UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid", map.get("companyid"));
			remindmap.put("userid", map.get("commentuserid"));
			remindmap.put("title", "审批");
			remindmap.put("content", userInfo.get("realname")+ "申请了一个月报,请您审批");
			remindmap.put("linkurl", "oa/log_detail.html?jotype=3&pid="+monthlyid+"&userid="+map.get("commentuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getPcOaLogDetail?jotype=3&pid="+monthlyid);
			remindmap.put("createid", map.get("userid"));
			remindmap.put("resourceid", monthlyid);
			remindmap.put("resourcetype", 25);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=map.get("commentuserid")+"";
				String title=userInfo.get("realname")+ "申请了一个月报,请您审批";
				String url="/oa/log_detail.html?jotype=3&pid="+monthlyid+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		String ccuserids = "";
		//添加抄送人到转发表
		if(map.containsKey("CCuserlist") && !"".equals(map.get("CCuserlist"))){
			JSONObject json=JSONObject.fromObject(map.get("CCuserlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String,Object> user : userlist){
				try {
					String userid=String.valueOf(user.get("userid"));
					if(!userid.equals(map.get("examineuserid")) && !userid.equals(map.get("createid"))){
						Map<String,Object>forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",map.get("companyid"));
						forwordmap.put("resourceid",monthlyid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",map.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",25);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+ "申请了一个月报,请查看！";
						String url="/oa/log_detail.html?jotype=3&pid="+monthlyid+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			map.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		int row = this.journalMapper.insertMonthly(map);
		if(row>0){
			map.put("resourcetype", "25");
			map.put("resourceid", monthlyid);
			this.sendToUser(map);
			this.sendToArea(map);
			this.sendToOther(map);
		}
		 Map<String,Object> filemap = new HashMap<String,Object>();
			filemap.put("companyid", map.get("companyid"));
			filemap.put("resourceid", monthlyid);
			filemap.put("resourcetype", 25);
			
			filemap.put("delflag", 0);
			//添加添加图片的路径
			String files = String.valueOf(map.get("filelist"));
			if(!"".equals(files) && map.containsKey("filelist")){
				String[] filelist = files.split(",");
				for(String url : filelist){
					filemap.put("fileid", UUID.randomUUID().toString().replaceAll("-",""));
					filemap.put("createtime", new Date());
					filemap.put("visiturl", url);
					filemap.put("type", 1);
					this.indexMapper.insertfile(filemap);
				}
			}
			
			//添加语音信息
			String sound = String.valueOf(map.get("sound"));
			if(!"".equals(map.get("sound")) && map.containsKey("sound")){
				filemap.put("fileid", UUID.randomUUID().toString().replaceAll("-",""));
				filemap.put("createtime", new Date());
				filemap.put("visiturl", sound);
				filemap.put("type", 2);
				this.indexMapper.insertfile(filemap);
			}
		    
		return row;
	}
	
 

	//发布范围
	@SuppressWarnings({ "unchecked"})
	private void sendToArea(Map<String,Object> map){
			//发布范围
			JSONObject json=JSONObject.fromObject(map.get("userlist")+"");
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String, Object> user:userlist){
				user.put("companyid", map.get("companyid"));
 				user.put("resourceid", map.get("resourceid"));
 				user.put("resourcetype", map.get("range_resourcetype"));
 				if(user.containsKey("organizeid") && !"".equals(map.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> userinfo:userinfolist){
 						if(String.valueOf(map.get("userid")).equals(userinfo.get("userid")) || String.valueOf(map.get("commentuserid")).equals(userinfo.get("userid"))){
 							continue;
 						}
 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
 						releaserangemap.put("companyid", map.get("companyid"));
 						releaserangemap.put("resourceid", map.get("resourceid"));
 						releaserangemap.put("resourcetype", map.get("range_resourcetype"));
 						releaserangemap.put("userid", userinfo.get("userid"));
 						Map<String, Object> rangeusermap=this.indexMapper.getReleaseRange(releaserangemap);
 						if(rangeusermap == null || rangeusermap.size() == 0){
 							releaserangemap.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
 							releaserangemap.put("delflag", 0);
 							releaserangemap.put("createtime", new Date());
 							releaserangemap.put("isread", 0);
 							this.indexMapper.insertReleaseRangeUser(releaserangemap);
 						}
 						
 					}
 				}
 				if(user.containsKey("userid") && !"".equals(map.get("userid"))){
 					user.put("userid", user.get("userid"));
 					user.put("type", 1);
 					if(String.valueOf(map.get("userid")).equals(user.get("userid")) || String.valueOf(map.get("commentuserid")).equals(user.get("userid"))){
						continue;
					}
 					//添加到发布范围用户表
 					Map<String, Object>  releaserangemap=new HashMap<String, Object>();
					releaserangemap.put("companyid", map.get("companyid"));
					releaserangemap.put("resourceid", map.get("resourceid"));
					releaserangemap.put("resourcetype", map.get("range_resourcetype"));
					releaserangemap.put("userid", user.get("userid"));
					Map<String, Object> rangeusermap=this.indexMapper.getReleaseRange(releaserangemap);
						if(rangeusermap == null || rangeusermap.size() == 0){
							releaserangemap.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
							releaserangemap.put("delflag", 0);
							releaserangemap.put("createtime", new Date());
							releaserangemap.put("isread", 0);
							this.indexMapper.insertReleaseRangeUser(releaserangemap);
						}
 				}
 				SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
 				//添加到发布范围表
 				user.put("rangeid", UUID.randomUUID().toString().replaceAll("-", ""));
 				user.put("delflag", 0);
 				user.put("createtime", sdf.format(new Date()));
 				user.put("isread", 0);
				this.indexMapper.insertReleaseRange(user);
	    }
			
	}
	  
	//public List<Map<String, Object>> userinfolistall=new ArrayList<Map<String,Object>>();
	public List<Map<String, Object>> getUserByorganize(Map<String, Object> data){
		//查询当前进来的组织的人员
		/*List<Map<String, Object>> userlist=this.indexMapper.getUserList(data);
		userinfolistall.addAll(userlist);
		Map<String, Object> organizemap=new HashMap<String, Object>();
		organizemap.put("companyid", data.get("companyid"));
		organizemap.put("organizeid", data.get("organizeid"));
		//查询进来组织的儿子组织
		List<Map<String, Object>> organizelist=this.indexMapper.getOrganizeList(organizemap);
		if(organizelist != null && organizelist.size()>0){
			for(Map<String, Object> organize:organizelist){
				//递归调用
				getUserByorganize(organize);
			}
		}
		List<Map<String, Object>> temp = new ArrayList<Map<String,Object>>();
		//temp = userinfolistall;
		temp.addAll(0, userinfolistall);
		userinfolistall.clear();
		return temp;*/
		return indexMapper.getUserListByOrganizeid(data);
	}

      
      /**
  	 * 查询日志详情
  	 * @param map
  	 * @return
  	 */
	@Override
	public Map<String, Object> getLogDetailInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String,Object> logmap = this.journalMapper.getLogDetailInfo(map);
		//查询图片和语音
		Map<String, Object> rangemap=new HashMap<String, Object>();
		int jotype = 0;
		int rangetype = 0;
		if("1".equals(map.get("jotype"))){
			jotype = 23;
			rangetype = 4;
		}else if("2".equals(map.get("jotype"))){
			jotype = 24;
			rangetype = 5;
		}else if("3".equals(map.get("jotype"))){
			jotype = 25; 
			rangetype = 6;
		}
		//查询发布范围
		rangemap.put("resourcetype", rangetype);
		rangemap.put("resourceid", map.get("logid"));
		List<Map<String,Object>> list = this.indexMapper.getRangeList(rangemap);
		
		rangemap.put("resourcetype", jotype);
		rangemap.put("resourceid", map.get("logid"));
		List<Map<String,Object>> filelist = this.indexMapper.getFileList(rangemap);
		logmap.put("filelist", filelist);
		logmap.put("rangelist", list);
		
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(logmap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(logmap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		logmap.put("ccuserlist", ccuserlist);
		
		return logmap;
	}

	/**
	 * 查询日志的时间列表信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getJournalTimesList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.journalMapper.getJournalTimesList(map);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("status", 1);
		param.put("companyid", map.get("companyid"));
		param.put("receiveid", map.get("receiveid"));
		for(Map<String,Object> mm : list){
			param.put("starttime", mm.get("createtime"));
			param.put("endtime", mm.get("createtime"));
			List<Map<String,Object>> journallist = this.journalMapper.getJournalList(param);
			mm.put("journallist", journallist);
		}
		return list;
	}

	/**
	 * 查询日志的时间列表总数
	 * @param map
	 * @return
	 */
	@Override
	public int getJournalTimesListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.journalMapper.getJournalTimesListCount(map);
	}

	/**
	 * 提交抄送人意见
	 * @param map
	 */
	@Override
	public void updateJournalInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", map.get("updateid"));
		remindmap.put("resourceid", map.get("logid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		//发布范围显示小红点
		Map<String, Object> rangemap=new HashMap<String, Object>();
		int rangetype = 0;
		if("1".equals(map.get("jotype"))){
			rangetype = 4;
		}else if("2".equals(map.get("jotype"))){
			rangetype = 5;
		}else if("3".equals(map.get("jotype"))){
			rangetype = 6;
		}
		//查询发布范围
		rangemap.put("resourcetype", rangetype);
		rangemap.put("resourceid", map.get("logid"));
		List<Map<String,Object>> list = this.indexMapper.getRangeList(rangemap);
		
		for(Map<String, Object> user:list){
			if(user.containsKey("organizeid") && !"".equals(user.get("organizeid"))){
				//添加到发布范围用户表
				List<Map<String, Object>> userinfolist=getUserByorganize(user);
				for(Map<String, Object> userinfo:userinfolist){
					Map<String,Object> paramMap = new HashMap<String,Object>();
					paramMap.put("resourceid", map.get("logid"));
					paramMap.put("receiveid", userinfo.get("userid"));
					this.indexMapper.updateIsreadAll(paramMap);
				}
			}
			if(user.containsKey("userid") && !"".equals(user.get("userid"))){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("resourceid", map.get("logid"));
				paramMap.put("receiveid", user.get("userid"));
				this.indexMapper.updateIsreadAll(paramMap);
			}
		}
		map.put("updatetime", new Date());
		this.journalMapper.updateJournalInfo(map);
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
