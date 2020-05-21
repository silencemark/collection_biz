package com.collection.service.impl.worksheet;

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
import com.collection.dao.worksheet.RewardInfoMapper;
import com.collection.service.worksheet.RewardInfoService;
import com.collection.util.Constants;

public class RewardInfoServiceImpl implements RewardInfoService{

	@Autowired
	RewardInfoMapper rewardInfoMapper;

	@Resource 
	IndexMapper indexMapper;
	
	@Resource
	UserInfoMapper userInfoMapper;
	@Resource PersonalMapper personalMapper;

	/**
	 * 查询奖励单列表信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getRewardListInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 9);
		map.put("cfu_delflag", 0);
		map.put("cro_delflag", 0);
		map.put("cru_resourcetype", 2);
//		map.put("status", 1);
//		map.put("result", 1);
		return this.rewardInfoMapper.getRewardListInfo(map);
	}

	/**
	 * 查询奖励单列表总数
	 * @param map
	 * @return
	 */
	@Override
	public int getRewardListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 9);
		map.put("cfu_delflag", 0);
		map.put("cro_delflag", 0);
		map.put("cru_resourcetype", 2);
//		map.put("status", 1);
//		map.put("result", 1);
		return this.rewardInfoMapper.getRewardListCount(map);
	}

	/**
	 * 查询已完成的奖励单时间列表
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getRewardListTimes(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 9);
		map.put("cfu_delflag", 0);
		map.put("cro_delflag", 0);
		map.put("cru_resourcetype", 2);
		List<Map<String,Object>> list = this.rewardInfoMapper.getRewardListTimes(map);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("resourcetype", 9);
		param.put("cfu_delflag", 0);
		param.put("cro_delflag", 0);
		param.put("cru_resourcetype", 2);
		param.put("status", 1);
		param.put("companyid", map.get("companyid"));
		param.put("receiveid", map.get("receiveid"));
		for(Map<String,Object> m : list){
			param.put("begintime", m.get("createtime"));
			param.put("endtime", m.get("createtime"));
			List<Map<String,Object>> rewardlist = this.rewardInfoMapper.getRewardListInfo(param);
			m.put("rewardlist", rewardlist);
		}
		return list;
	}

	/**
	 * 查询已完成的奖励单的时间的总数
	 * @param map
	 * @return
	 */
	@Override
	public int getRewardListTimesCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 9);
		map.put("cfu_delflag", 0);
		map.put("cro_delflag", 0);
		map.put("cru_resourcetype", 2);
		return this.rewardInfoMapper.getRewardListTimesCount(map);
	}
	
	/**
	 * 查询奖励单详情
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getRewardDetailInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		//查询发布范围
		Map<String, Object> rangemap=new HashMap<String, Object>();
		rangemap.put("resourcetype", 2);
		rangemap.put("resourceid", map.get("rewardid"));
		List<Map<String,Object>> list = this.indexMapper.getRangeList(rangemap);
		
		//查询图片和语音
		rangemap.put("resourcetype", 9);
		List<Map<String,Object>> filelist = this.indexMapper.getFileList(rangemap);
		
		map.put("delflag", 0);
		map = this.rewardInfoMapper.getRewardDetailInfo(map);
		map.put("rangelist", list);
		map.put("filelist", filelist);

		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(map.containsKey("CCuserids")){
			String ccuserids = String.valueOf(map.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		map.put("ccuserlist", ccuserlist);
		
		return map;
	}

	/**
	 * 添加奖励单信息
	 * @param map
	 */
	@Override
	public String insertRewardInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		//给奖励单信息初始值
		String rewardid = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("rewardid", rewardid);
		map.put("delflag", 0);
		map.put("createtime", new Date());
		map.put("status", 0);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("userid", map.get("createid"));
		userInfo=this.userInfoMapper.getUserInfo(userInfo);
		//添加到审批提醒表
		if(userInfo!=null && userInfo.size()>0){
			Map<String,	Object> remindmap=new HashMap<String, Object>();
			remindmap.put("remindid", UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid",map.get("companyid"));
			remindmap.put("userid",map.get("examineuserid"));
			remindmap.put("title","审批");
			remindmap.put("content",userInfo.get("realname")+"申请了一个奖励单,请您审批");
			remindmap.put("linkurl", "worksheet/reward_check.html?rewardid="+rewardid+"&userid"+map.get("examineuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getRewardDetailInfo?rewardid="+rewardid);
			remindmap.put("createid", map.get("createid"));
			remindmap.put("resourceid", rewardid);
			remindmap.put("resourcetype", 9);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=map.get("examineuserid")+"";
				String title=userInfo.get("realname")+"申请了一个奖励单,请您审批";
				String url="/worksheet/reward_check.html?rewardid="+rewardid+"&userid"+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",map.get("companyid"));
		forwordmap.put("resourceid",rewardid);
		forwordmap.put("receiveid",map.get("createid"));
		forwordmap.put("createid",map.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",9);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加审批人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",map.get("companyid"));
		forwordmap.put("resourceid",rewardid);
		forwordmap.put("receiveid",map.get("examineuserid"));
		forwordmap.put("createid",map.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",9);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
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
						forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",map.get("companyid"));
						forwordmap.put("resourceid",rewardid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",map.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",9);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"申请了一个奖励单,请查看！";
						String url="/worksheet/reward_detail.html?rewardid="+rewardid+"&userid"+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			map.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		//添加奖励单信息
		this.rewardInfoMapper.insertRewardInfo(map);
		return rewardid;
	}

	/**
	 * 提交奖励单的审核信息
	 * @param map
	 */
	@Override
	public void updateRewarddInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		map.put("status", 1);
		
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", map.get("updateid"));
		remindmap.put("resourceid", map.get("rewardid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		this.rewardInfoMapper.updateRewarddInfo(map);
	}
	
	
	/**
	 * 查询奖励类型
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getDictionListInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.rewardInfoMapper.getDictionListInfo(map);
	}

	
	
/*--------------------------------------------------------------------------处罚单------------------------------------------------------------------------*/

	/**
	 * 查询处罚单信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPunishListInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 8);
		map.put("cru_resourcetype", 3);
		map.put("delflag", 0);
		return this.rewardInfoMapper.getPunishListInfo(map);
	}

	/**
	 * 查询处罚单的总数
	 * @param map
	 * @return
	 */
	@Override
	public int getPunishListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 8);
		map.put("cru_resourcetype", 3);
		map.put("delflag", 0);
		return this.rewardInfoMapper.getPunishListCount(map);
	}

	
	/**
	 * 查询处罚单时间列表信息
	 * @param map
	 * @return
	 */
	
	@Override
	public List<Map<String, Object>> getPunishListTimesInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 8);
		map.put("cru_resourcetype", 3);
		map.put("delflag", 0);
		List<Map<String,Object>> list = this.rewardInfoMapper.getPunishListTimesInfo(map);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("resourcetype", 8);
		param.put("cru_resourcetype", 3);
		param.put("delflag", 0);
		param.put("status", 1);
		param.put("companyid", map.get("companyid"));
		param.put("receiveid", map.get("receiveid"));
		for(Map<String,Object> mm : list){
			param.put("begintime", mm.get("createtime"));
			param.put("endtime", mm.get("createtime"));
			List<Map<String,Object>> punishlist = this.getPunishListInfo(param);
			mm.put("punishlist", punishlist);
		}
		return list;
	}

	/**
	 * 查询惩罚单列表的总数
	 * @param map
	 * @return
	 */
	@Override
	public int getPunishListTimesCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 8);
		map.put("cru_resourcetype", 3);
		map.put("delflag", 0);
		return this.rewardInfoMapper.getPunishListTimesCount(map);
	}
	
	/**
	 * 查询处罚单详细信息
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getPunishDetailInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		//查询发布范围
		Map<String, Object> rangemap=new HashMap<String, Object>();
		rangemap.put("resourcetype", 3);
		rangemap.put("resourceid", map.get("punishid"));
		List<Map<String,Object>> list = this.indexMapper.getRangeList(rangemap);
		
		//查询图片和语音
		rangemap.put("resourcetype", 8);
		List<Map<String,Object>> filelist = this.indexMapper.getFileList(rangemap);
		map.put("delflag", 0);
		Map<String,Object> punishmap = this.rewardInfoMapper.getPunishDetailInfo(map);
		punishmap.put("filelist", filelist);
		punishmap.put("rangelist", list);
		
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(punishmap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(punishmap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		punishmap.put("ccuserlist", ccuserlist);
		
		return punishmap;
	}

	/**
	 * 修改处罚单信息
	 * @param map
	 */
	@Override
	public void updatePunishInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		map.put("status", 1);
		
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", map.get("updateid"));
		remindmap.put("resourceid", map.get("punishid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
				
		this.rewardInfoMapper.updatePunishInfo(map);
	}

	/**
	 * 添加处罚单信息
	 * @param map
	 */
	@Override
	public String insertPunishInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String punish = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("punishid", punish);
		map.put("delflag", 0);
		map.put("createtime", new Date());
		map.put("status", 0);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("userid", map.get("createid"));
		userInfo=this.userInfoMapper.getUserInfo(userInfo);
		//添加到审批提醒表
		if(userInfo!=null && userInfo.size()>0){
			Map<String,	Object> remindmap=new HashMap<String, Object>();
			remindmap.put("remindid", UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid",map.get("companyid"));
			remindmap.put("userid",map.get("examineuserid"));
			remindmap.put("title","审批");
			remindmap.put("content",userInfo.get("realname")+"申请了一个处罚单,请您审批");
			remindmap.put("linkurl", "worksheet/punish_check.html?punishid="+punish);
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getPunishDetailInfo?punishid="+punish+"&userid="+map.get("examineuserid"));
			remindmap.put("createid", map.get("createid"));
			remindmap.put("resourceid", punish);
			remindmap.put("resourcetype",8);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=map.get("examineuserid")+"";
				String title=userInfo.get("realname")+"申请了一个处罚单,请您审批";
				String url="/worksheet/punish_check.html?punishid="+punish+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",map.get("companyid"));
		forwordmap.put("resourceid",punish);
		forwordmap.put("receiveid",map.get("createid"));
		forwordmap.put("createid",map.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",8);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加审批人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",map.get("companyid"));
		forwordmap.put("resourceid",punish);
		forwordmap.put("receiveid",map.get("examineuserid"));
		forwordmap.put("createid",map.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",8);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
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
						forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",map.get("companyid"));
						forwordmap.put("resourceid",punish);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",map.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",8);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"申请了一个处罚单,请查看！";
						String url="/worksheet/punish_detail.html?punishid="+punish+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			map.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.rewardInfoMapper.insertPunishInfo(map);
		return punish;
	}

	
	
/*----------------------------------------------------------------------报修单-------------------------------------------------------------------------*/

	
	/**
	 * 查询报修单信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getRepairListInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 13);
		map.put("delflag", 0);
		return this.rewardInfoMapper.getRepairListInfo(map);
	}

	/**
	 * 查询报修单的总数
	 * @param map
	 * @return
	 */
	@Override
	public int getRepairListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 13);
		map.put("delflag", 0);
		return this.rewardInfoMapper.getRepairListCount(map);
	}

	/**
	 * 查询报修单时间列表信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getRepairTimesListInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 13);
		map.put("delflag", 0);
		List<Map<String,Object>> list = this.rewardInfoMapper.getRepairTimesListInfo(map);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("resourcetype", 13);
		param.put("delflag", 0);
		param.put("status", 1);
		param.put("companyid", map.get("companyid"));
		param.put("receiveid", map.get("receiveid"));
		for(Map<String,Object> mm : list){
			param.put("begintime", mm.get("createtime"));
			param.put("endtime", mm.get("createtime"));
			List<Map<String,Object>> repairlist = this.getRepairListInfo(param);
			mm.put("repairlist", repairlist);
		}
		return list;
	}

	/**
	 * 查询报修单时间列表总数
	 * @param map
	 */
	@Override
	public int getRepairTimesListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 13);
		map.put("delflag", 0);
		return this.rewardInfoMapper.getRepairTimesListCount(map);
	}
	
	/**
	 * 查询报修单详细信息
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getRepairDetailInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String, Object> rangemap=new HashMap<String, Object>();
		rangemap.put("resourcetype", 13);
		rangemap.put("resourceid", map.get("repairid"));
		List<Map<String, Object>> filelist=this.indexMapper.getFileList(rangemap);
		
		map.put("delflag", 0);
		Map<String, Object>   rewardmap=this.rewardInfoMapper.getRepairDetailInfo(map);
		rewardmap.put("filelist", filelist);
		
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(rewardmap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(rewardmap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		rewardmap.put("ccuserlist", ccuserlist);
		
		return rewardmap;
	}

	/**
	 * 提交抄送人意见
	 * @param map
	 */
	@Override
	public void updateRepairInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		map.put("status", 1);
		
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", map.get("updateid"));
		remindmap.put("resourceid", map.get("repairid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		this.rewardInfoMapper.updateRepairInfo(map);
	}

	/**
	 * 添加报修单信息
	 * @param map
	 */
	@Override
	public String insertRepairInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String repair = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("repairid", repair);
		map.put("delflag", 0);
		map.put("createtime", new Date());
		map.put("status", 0);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("userid", map.get("createid"));
		userInfo=this.userInfoMapper.getUserInfo(userInfo);
		//添加到审批提醒表
		if(userInfo!=null && userInfo.size()>0){
			Map<String,	Object> remindmap=new HashMap<String, Object>();
			remindmap.put("remindid", UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid",map.get("companyid"));
			remindmap.put("userid",map.get("examineuserid"));
			remindmap.put("title","审批");
			remindmap.put("content",userInfo.get("realname")+"申请了一个报修单,请您审批");
			remindmap.put("linkurl", "worksheet/repair_detail.html?repairid="+repair+"&userid="+map.get("examineuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getRepairDetailInfo?repairid="+repair);
			remindmap.put("createid", map.get("createid"));
			remindmap.put("resourceid", repair);
			remindmap.put("resourcetype", 13);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=map.get("examineuserid")+"";
				String title=userInfo.get("realname")+"申请了一个报修单,请您审批";
				String url="/worksheet/repair_detail.html?repairid="+repair+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",map.get("companyid"));
		forwordmap.put("resourceid",repair);
		forwordmap.put("receiveid",map.get("createid"));
		forwordmap.put("createid",map.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",13);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加审批人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",map.get("companyid"));
		forwordmap.put("resourceid",repair);
		forwordmap.put("receiveid",map.get("examineuserid"));
		forwordmap.put("createid",map.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",13);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		String ccuserids = "";
		//添加抄送人到转发表
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
						forwordmap.put("resourceid",repair);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",map.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",13);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"申请了一个报修单,请查看！";
						String url="/worksheet/repair_detail.html?repairid="+repair+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			map.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.rewardInfoMapper.insertRepairInfo(map);
		return repair;
	}


	
/*-----------------------------------------------------------------菜品成本---------------------------------------------------------------------------------*/

	
	/**
	 * 查询菜品成本列表信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getDishesListInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 14);
		map.put("delflag", 0);
		return this.rewardInfoMapper.getDishesListInfo(map);
	}

	/**
	 * 查询菜品成本列表总数
	 * @param map
	 * @return
	 */
	@Override
	public int getDishesListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 14);
		map.put("delflag", 0);
		return this.rewardInfoMapper.getDishesListCount(map);
	}

	/**
	 * 查询菜品成本时间列表信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getDishesListTimesInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 14);
		map.put("delflag", 0);
		List<Map<String,Object>> list = this.rewardInfoMapper.getDishesListTimesInfo(map);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("resourcetype", 14);
		param.put("delflag", 0);
		param.put("status", 1);
		param.put("receiveid", map.get("receiveid"));
		param.put("companyid", map.get("companyid"));
		for(Map<String,Object> mm : list){
			param.put("begintime", mm.get("createtime"));
			param.put("endtime", mm.get("createtime"));
			List<Map<String,Object>> disheslist = this.rewardInfoMapper.getDishesListInfo(param);
			mm.put("disheslist", disheslist);
		}
		return list;
	}

	/**
	 * 查询菜品成本时间列表信息的总数
	 * @param map
	 * @return
	 */
	@Override
	public int getDishesListTimesCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("resourcetype", 14);
		map.put("delflag", 0);
		return this.rewardInfoMapper.getDishesListTimesCount(map);
	}
	
	/**
	 * 查询菜品详情信息
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getDishesDetailInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String, Object> rangemap=new HashMap<String, Object>();
		
		rangemap.put("resourcetype", 14);
		rangemap.put("resourceid",map.get("dishesid"));
		
	
		List<Map<String, Object>> filelist=this.indexMapper.getFileList(rangemap);
		map.put("delflag", 0);
		Map<String,Object> costmap=this.rewardInfoMapper.getDishesDetailInfo(map);
		costmap.put("filelist", filelist);
		
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(costmap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(costmap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		costmap.put("ccuserlist", ccuserlist);
		
		return costmap;
	}

	/**
	 * 提交抄送人意见
	 * @param map
	 */
	@Override
	public void updateDishesInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		map.put("status", 1);
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", map.get("updateid"));
		remindmap.put("resourceid", map.get("dishesid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		this.rewardInfoMapper.updateDishesInfo(map);
	}

	/**
	 * 添加菜品成本信息
	 * @param map
	 */
	@Override
	public String insertDishesInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String dishesid = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("dishesid", dishesid);
		map.put("delflag", 0);
		map.put("createtime", new Date());
		map.put("status", 0);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("userid", map.get("createid"));
		userInfo=this.userInfoMapper.getUserInfo(userInfo);
		//添加到审批提醒表
		if(userInfo!=null && userInfo.size()>0){
			Map<String,	Object> remindmap=new HashMap<String, Object>();
			remindmap.put("remindid", UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid",map.get("companyid"));
			remindmap.put("userid",map.get("examineuserid"));
			remindmap.put("title","审批");
			remindmap.put("content",userInfo.get("realname")+"提交了一个菜品成本单,请您审批");
			remindmap.put("linkurl", "worksheet/cost_detail.html?dishesid="+dishesid+"&userid="+map.get("examineuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getcostDetailInfo?dishesid="+dishesid);
			remindmap.put("createid", map.get("createid"));
			remindmap.put("resourceid", dishesid);
			remindmap.put("resourcetype", 14);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=map.get("examineuserid")+"";
				String title=userInfo.get("realname")+"提交了一个菜品成本单,请您审批";
				String url="/worksheet/cost_detail.html?dishesid="+dishesid+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",map.get("companyid"));
		forwordmap.put("resourceid",dishesid);
		forwordmap.put("receiveid",map.get("createid"));
		forwordmap.put("createid",map.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",14);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加审批人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",map.get("companyid"));
		forwordmap.put("resourceid",dishesid);
		forwordmap.put("receiveid",map.get("examineuserid"));
		forwordmap.put("createid",map.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",14);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		String ccuserids = "";
		//添加抄送人到转发表
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
						forwordmap.put("resourceid",dishesid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",map.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",14);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"提交了一个菜品成本单,请查看！";
						String url="/worksheet/cost_detail.html?dishesid="+dishesid+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			map.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.rewardInfoMapper.insertDishesInfo(map);
		return dishesid;
	}

	
	
	/**
	 * 查询用户的职位信息
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getUserPosition(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.rewardInfoMapper.getUserPosition(map);
	}
	
	
	
	
	
	
	
	

	/**
	 * 修改转发表中的阅读状态
	 * @param map
	 */
	@Override
	public void updateForwardReadStatus(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.rewardInfoMapper.updateForwardReadStatus(map);
	}

	/**
	 * 修改发布范围表中的阅读状态
	 * @param map
	 */
	@Override
	public void updateRangeReadStatus(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.rewardInfoMapper.updateRangeReadStatus(map);
	}
	/**
	 * 根据公司id获取菜品类型列表信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getDishestypeListInfoByOrganizeId(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return rewardInfoMapper.getDishestypeListInfoByOrganizeId(map);
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
