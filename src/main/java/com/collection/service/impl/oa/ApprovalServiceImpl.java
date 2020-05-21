package com.collection.service.impl.oa;

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
import com.collection.dao.oa.ApprovalMapper;
import com.collection.dao.personal.PersonalMapper;
import com.collection.service.oa.ApprovalService;
import com.collection.util.Constants;
/**
 * 通用审批
 * @author pengqinghai
 *
 */
public class ApprovalServiceImpl implements ApprovalService{

	@Autowired ApprovalMapper approvalMapper;
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
    	indexMapper.insertForword(dataMap);
		
	}
	
	//把审核者加入转发表
	@SuppressWarnings("unused")
	private void sendToOther(Map<String,Object> map){
		Timestamp tm = new Timestamp(System.currentTimeMillis());
		Map<String,Object> dataMap = new HashMap<String, Object>();
		String forwarduserid = UUID.randomUUID().toString().replace("-", "");
        dataMap.put("forwarduserid", forwarduserid);
    	dataMap.put("resourceid", map.get("resourceid"));
    	dataMap.put("companyid", map.get("companyid"));
    	dataMap.put("createid", map.get("userid"));
    	dataMap.put("receiveid", map.get("examineuserid"));
    	dataMap.put("delflag", "0");
    	dataMap.put("createtime", tm);
    	dataMap.put("isread", "0");
    	dataMap.put("resourcetype", map.get("resourcetype"));
    	indexMapper.insertForword(dataMap);
		
	}



	@Override
	public List<Map<String, Object>> getCurrencyExamineList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.approvalMapper.getCurrencyExamineList(map);
	}



	@Override
	public int getCurrencyExamineListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.approvalMapper.getCurrencyExamineListCount(map);
	}

	@Override
	public int inserCurrencyExamine(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Timestamp tm = new Timestamp(System.currentTimeMillis());
		String currencyexamineid = UUID.randomUUID().toString().replace("-", "");
		map.put("currencyexamineid",currencyexamineid);
		map.put("delflag","0");
		map.put("createtime",tm);
		map.put("status","0");
		map.put("createid", map.get("userid"));
		
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", map.get("createid"));
		userInfo = this.userInfoMapper.getUserInfo(userInfo);
		String ccuserids = "";
		//添加抄送人到转发表
		Map<String,Object> forwordmap = new HashMap<String,Object>();
		if(map.containsKey("userlist") && !"".equals(map.get("userlist"))){
			JSONObject json=JSONObject.fromObject(map.get("userlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String,Object> user : userlist){
				try {
					String userid=String.valueOf(user.get("userid"));
					if(!userid.equals(map.get("examineuserid")) && !userid.equals(map.get("createid"))){
						forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",map.get("companyid"));
						forwordmap.put("resourceid",currencyexamineid);
						forwordmap.put("receiveid",user.get("userid"));
						forwordmap.put("createid",map.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",15);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"申请了一个通用审批,请查看！";
						String url="/oa/approval_detail.html?currencyexamineid="+currencyexamineid+"&userid="+userid;
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			map.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		int row = this.approvalMapper.inserCurrencyExamine(map);
		
		// 添加到审批提醒表
		if (userInfo != null && userInfo.size() > 0) {
			Map<String, Object> remindmap = new HashMap<String, Object>();
			remindmap.put("remindid",
					UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid", map.get("companyid"));
			remindmap.put("userid", map.get("examineuserid"));
			remindmap.put("title", "审批");
			remindmap.put("content", userInfo.get("realname")+"申请了一个通用审批,请您审批");
			remindmap.put("linkurl", "oa/approval_check.html?currencyexamineid="+currencyexamineid+"&userid="+map.get("examineuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getPcOaApprovaDetail?currencyexamineid="+currencyexamineid);
			remindmap.put("createid", map.get("userid"));
			remindmap.put("resourceid", currencyexamineid);
			remindmap.put("resourcetype", 15);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=map.get("examineuserid")+"";
				String title=userInfo.get("realname")+"申请了一个通用审批,请您审批";
				String url="/oa/approval_check.html?currencyexamineid="+currencyexamineid+"&userid="+userid;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		
		if(row>0){
			map.put("resourcetype", "15");
			map.put("resourceid", currencyexamineid);
			this.sendToUser(map);
			this.sendToOther(map);
		}
		Map<String,Object> filemap = new HashMap<String,Object>();
		filemap.put("companyid", map.get("companyid"));
		filemap.put("resourceid", currencyexamineid);
		filemap.put("resourcetype", 15);
		
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
	public List<Map<String, Object>> getCurrencyExamineTimesList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.approvalMapper.getCurrencyExamineTimesList(map);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyid", map.get("companyid"));
		param.put("userid", map.get("userid"));
		param.put("status", map.get("status"));
		for(Map<String,Object> mm : list){
			param.put("starttime", mm.get("createtime"));
			param.put("endtime", mm.get("createtime"));
			List<Map<String,Object>> examinelist = this.getCurrencyExamineList(param);
			mm.put("examinelist", examinelist);
		}
		return list;
	}



	@Override
	public int getCurrencyExamineTimesCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.approvalMapper.getCurrencyExamineTimesCount(map);
	}



	@Override
	public Map<String,Object> getCurrencyExamineDetailInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String,Object> detailmap = this.approvalMapper.getCurrencyExamineDetailInfo(map);
		if(detailmap != null && detailmap.size() > 0){
			List<Map<String, Object>> filelist=new ArrayList<Map<String,Object>>();
			Map<String, Object> data=new HashMap<String, Object>();
			map.put("resourceid", detailmap.get("currencyexamineid"));
			map.put("resourcetype", 15);
			filelist=this.indexMapper.getFileList(map);
			detailmap.put("filelist", filelist);
			
			List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
			if(detailmap.containsKey("CCuserids")){
				String ccuserids = String.valueOf(detailmap.get("CCuserids"));
				String [] userids = ccuserids.split(",");
				for(String userid : userids){
					Map<String,Object> paramMap = new HashMap<String,Object>();
					paramMap.put("userid", userid);
					Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
					ccuserlist.add(usermap);
				}
			}
			detailmap.put("ccuserlist", ccuserlist);
		}
		return detailmap;
	}

	@Override
	public void updateCurrencyExamineInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		map.put("status", 1);
		map.put("updateid", map.get("userid"));
		
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", map.get("userid"));
		remindmap.put("resourceid", map.get("currencyexamineid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		this.approvalMapper.updateCurrencyExamineInfo(map);
	}
}
