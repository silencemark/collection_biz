package com.collection.service.impl.storefront;

import java.sql.Timestamp;
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
import com.collection.dao.storefront.StoreFrontMapper;
import com.collection.service.storefront.StoreFrontService;
import com.collection.util.Constants;
/**
 * 店面管理
 * @author silence
 *
 */
public class StoreFrontServiceImpl implements StoreFrontService{

	@Autowired StoreFrontMapper storeFrontMapper;
	@Autowired IndexMapper indexMapper;
	@Autowired UserInfoMapper userinfoMapper;
	@Autowired PersonalMapper personalMapper;
	
	@Override
	public String insertTourStore(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String tourstoreid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("tourstoreid", tourstoreid);
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
			remindmap.put("userid",data.get("copyuserid"));
			remindmap.put("title","查看");
			remindmap.put("content",userInfo.get("realname")+"发送了一个巡店日志,请您查看");
			remindmap.put("linkurl", "");
			remindmap.put("linkurl", "restaurant/patrollog_detail.html?tourstoreid="+tourstoreid);
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getTourStoreInfo?tourstoreid="+tourstoreid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", tourstoreid);
			remindmap.put("resourcetype", 22);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=data.get("copyuserid")+"";
				String title=userInfo.get("realname")+"发送了一个巡店日志,请您查看";
				String url="/restaurant/patrollog_detail.html?tourstoreid="+tourstoreid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",tourstoreid);
		forwordmap.put("receiveid",data.get("createid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",22);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加抄送人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",tourstoreid);
		forwordmap.put("receiveid",data.get("copyuserid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",22);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加到文件表 
		if(data.containsKey("filelist") && !"".equals(data.get("filelist"))){
			JSONObject json=JSONObject.fromObject(data.get("filelist")+"");
			List<Map<String, Object>> filelist=(List<Map<String, Object>>)json.get("filelist");
			if(filelist != null && filelist.size()>0){
				for(Map<String, Object> filemap:filelist){
					Map<String, Object> fileinfo=new HashMap<String, Object>();
					fileinfo.put("fileid",UUID.randomUUID().toString().replaceAll("-", ""));
					fileinfo.put("companyid", data.get("companyid"));
					fileinfo.put("resourceid", tourstoreid);
					fileinfo.put("delflag", 0);
					fileinfo.put("visiturl", filemap.get("visiturl"));
					fileinfo.put("resourcetype", filemap.get("resourcetype"));
					fileinfo.put("type", filemap.get("type"));
					fileinfo.put("size", filemap.get("size"));
					this.indexMapper.insertfile(fileinfo);
				}
			}
		}
		
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
						forwordmap.put("resourceid",tourstoreid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",22);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"抄送了一个巡店日志,请您查看";
						String url="/restaurant/patrollog_detail.html?tourstoreid="+tourstoreid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.storeFrontMapper.insertTourStore(data);
		return tourstoreid;
	}
	@Override
	public List<Map<String, Object>> getTourStoreList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.storeFrontMapper.getTourStoreList(data);
	}
	@Override
	public int getTourStoreListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.storeFrontMapper.getTourStoreListNum(data);
	}
	@Override
	public Map<String, Object> getTourStoreInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> storeinfo=this.storeFrontMapper.getTourStoreInfo(data);
		Map<String, Object> filemap=new HashMap<String, Object>();
		filemap.put("resourceid", storeinfo.get("tourstoreid"));
		filemap.put("resourcetype", 22);
		List<Map<String, Object>> filelist=indexMapper.getFileList(filemap);
		storeinfo.put("filelist", filelist);
		
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(storeinfo.containsKey("CCuserids")){
			String ccuserids = String.valueOf(storeinfo.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		storeinfo.put("ccuserlist", ccuserlist);
		
		return storeinfo;
	}
	@Override
	public void updateTourStore(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("tourstoreid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		this.storeFrontMapper.updateTourStore(data);
	}
	@Override
	public List<Map<String, Object>> getKpiStarList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> kpistarlist=this.storeFrontMapper.getKpiStarList(data);
//		for(Map<String, Object> kpistar:kpistarlist){
//			int selfstar=this.storeFrontMapper.getSelfStarNum(kpistar);
//			int overallstar=this.storeFrontMapper.getOverallStarNum(kpistar);
//			kpistar.put("selfstar", selfstar);
//			kpistar.put("overallstar", overallstar);
//		}
		return kpistarlist;
	}
	@Override
	public int getKpiStarListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.storeFrontMapper.getKpiStarListNum(data);
	}
	@Override
	public List<Map<String, Object>> getPathRewardList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.storeFrontMapper.getPathRewardList(data);
	}
	@Override
	public int getPathRewardListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.storeFrontMapper.getPathRewardListNum(data);
	}
	@Override
	public List<Map<String, Object>> getPathPunishList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.storeFrontMapper.getPathPunishList(data);
	}
	@Override
	public int getPathPunishListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.storeFrontMapper.getPathPunishListNum(data);
	}
	@Override
	public String insertStoreEvaluate(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String evaluateid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("evaluateid", evaluateid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		float nonef=Float.parseFloat(data.get("averagescore")!=null && !"".equals(data.get("averagescore"))?data.get("averagescore").toString():"0")*10;
		data.put("averagescore",Math.ceil(nonef)/10);
		data.put("status", 0);
		this.storeFrontMapper.insertStoreEvaluate(data);
		return evaluateid;
	}
	
	@Override
	public List<Map<String, Object>> getStoreEvaluateList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.storeFrontMapper.getStoreEvaluateList(data);
	}
	@Override
	public int getStoreEvaluateListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.storeFrontMapper.getStoreEvaluateListNum(data);
	}
	
	@Override
	public Map<String, Object> editFollow(Map<String, Object> data) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
	
		Map<String, Object> paramMap  = new HashMap<String,Object>();
		Timestamp tm = new Timestamp(System.currentTimeMillis());
		paramMap.put("createid", data.get("createid"));
		paramMap.put("evaluateid", data.get("evaluateid"));
		paramMap.put("updatetime", tm);
		paramMap.put("updateid", data.get("createid"));
		try{
		//判断之前是否关注过
		Map<String, Object> follow = this.storeFrontMapper.getFollowInfo(data);
			if(Integer.valueOf(follow.get("count").toString())>0){
			   //关注过了
				if(data.get("isattention").toString().equals("1")){
					 //取消关注
					paramMap.put("isfollow", "0");
					
				}else if(data.get("isattention").toString().equals("0")){
					//关注
					paramMap.put("isfollow", "1");
				}
				returnMap.put("status", "0");
				this.storeFrontMapper.updateFollowStatus(paramMap);
			}else{
			   //没关注过
				String followid = UUID.randomUUID().toString().replace("-","");
				paramMap.put("followid", followid);
				paramMap.put("createtime", tm);
				paramMap.put("delflag", 0);
				paramMap.put("isfollow", 1);
				paramMap.put("companyid", data.get("companyid"));
				this.storeFrontMapper.insertEvaluateFollow(paramMap);
			}
			returnMap = paramMap;
			returnMap.put("status", "0");
			returnMap.put("msg", "操作成功");
			
		}catch(Exception e){
			e.printStackTrace();
			returnMap.put("status", "1");
			returnMap.put("msg", "操作异常");
		}
		return returnMap;
	}
	@Override
	public List<Map<String, Object>> getTourDateList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.storeFrontMapper.getTourDateList(data);
	}
	@Override
	public int getTourDateListCount(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.storeFrontMapper.getTourDateListCount(data);
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
