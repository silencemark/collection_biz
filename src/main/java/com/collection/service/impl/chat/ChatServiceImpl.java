package com.collection.service.impl.chat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IndexMapper;
import com.collection.dao.chat.ChatMapper;
import com.collection.rong.ApiHttpClient;
import com.collection.rong.models.FormatType;
import com.collection.rong.models.SdkHttpResult;
import com.collection.service.UserInfoService;
import com.collection.service.chat.ChatService;
import com.collection.util.Constants;

/**
 * 消息讨论组管理
 * 
 * @author pengqinghai
 * 
 */
public class ChatServiceImpl implements ChatService {
	private transient static Log log = LogFactory.getLog(ChatServiceImpl.class);
	@Autowired
	ChatMapper chatMapper;
	@Autowired
	IndexMapper indexMapper;
	@Autowired
	UserInfoService userInfoService;

	@Override
	public List<Map<String, Object>> getChatGroupList(Map<String, Object> map) {
		
		List<Map<String, Object>> grouplist = this.chatMapper
				.getChatGroupList(map);

		for (Map<String, Object> group : grouplist) {
			Map<String, Object> recordmap = this.chatMapper
					.getLastRecord(group);
			if (recordmap != null && recordmap.size() > 0) {
				group.put(
						"lastcontent",
						recordmap.get("lastcontent") == null ? "" : recordmap
								.get("lastcontent"));
				group.put("lasttime", recordmap.get("lasttime") == null ? ""
						: recordmap.get("lasttime"));
				group.put("lastdate", recordmap.get("createtime") == null ? ""
						: recordmap.get("createtime"));
				group.put("type", recordmap.get("type") == null ? ""
						: recordmap.get("type"));
			} else {
				group.put("lastcontent", "");
				group.put("lasttime", "");
			}
		}
		return grouplist;
	}

	@Override
	public int getChatGroupListCount(Map<String, Object> map) {
		
		return this.chatMapper.getChatGroupListCount(map);
	}

	@Override
	public int insertGroup(Map<String, Object> map) {
		
		Timestamp tm = new Timestamp(System.currentTimeMillis());
		map.put("delflag", "0");
		map.put("createtime", tm);
		return this.chatMapper.insertGroup(map);
	}

	@Override
	public int insertUserToGroup(Map<String, Object> map) {
		
		Timestamp tm = new Timestamp(System.currentTimeMillis());
		String groupuserid = UUID.randomUUID().toString().replace("-", "");
		map.put("groupuserid", groupuserid);
		map.put("delflag", "0");
		map.put("createtime", tm);
		return this.chatMapper.insertUserToGroup(map);
	}

	@Override
	public int insertChatRecord(Map<String, Object> map) {
		//content groupid type createid
		Timestamp tm = new Timestamp(System.currentTimeMillis());
		String recordid = UUID.randomUUID().toString().replace("-", "");
		if(map.get("recordid")!=null){
			recordid = map.get("recordid").toString();
		}
		map.put("userid", map.get("createid"));
		List<Map<String, Object>> userList = chatMapper
				.getChatGroupUserList(map);
		String isGroud = "1";
		String title = map.get("raelname")+":"+map.get("content");
		if(userList!=null && userList.size()>0){
			isGroud = "2";
		}
		if("2".equals(map.get("type")+"")){
			title = map.get("raelname")+":[图片]";
		}else if("3".equals(map.get("type")+"")){
			title = map.get("raelname")+":[语音]";
		}
		String url = "/message/talk.html?isGroud="+isGroud+"&groupId="+map.get("groupid");
		for(Map<String, Object> user:userList){
			//recordid,groupid,type,userid
			user.put("recordid", recordid);
			user.put("groupid", map.get("groupid"));
			user.put("type", map.get("type"));
			//将消息推送给其他人
			if(!map.get("createid").toString().equals(user.get("userid").toString()) && !"1".equals(user.get("isdisturb")+"")){
				Map<String, String> re = JPushAndriodAndIosMessage(user.get("userid").toString(),
						title, url); 
			}
			//添加状态记录
			insertChatRecordStatus(user);
		}
		//标记已读状态
		signChatRecordStatus(map);
		map.put("recordid", recordid);
		map.put("delflag", "0");
		map.put("createtime", tm);
		return this.chatMapper.insertChatRecord(map);
	}

	@Override
	public int updateUserGroup(Map<String, Object> map) {
		
		map.put("updatetime", new Date());
		return this.chatMapper.updateUserGroup(map);
	}

	@Override
	public int getMyGroupNum(Map<String, Object> map) {
		
		return this.chatMapper.getMyGroupNum(map);
	}

	@Override
	public List<Map<String, Object>> getChatUserList(Map<String, Object> data) {
		
		return this.chatMapper.getChatUserList(data);
	}

	@Override
	public Map<String, Object> getChatGroupInfo(Map<String, Object> map) {
		
		Map<String, Object> groupInfo = this.chatMapper.getChatGroupInfo(map);
		// 查询用户列表
		if (groupInfo != null && groupInfo.size() > 0) {
			List<Map<String, Object>> userlist = this.chatMapper
					.getChatGroupUserList(map);
			groupInfo.put("userlist", userlist);
			if ("2".equals(groupInfo.get("isgroup") + "")) {// 单聊
				for (Map<String, Object> d : userlist) {
					if (!map.get("this_userid").toString()
							.equals(d.get("userid").toString())) {
						groupInfo.put("groupname", d.get("realname"));
						groupInfo.put("groupurl", d.get("headimage"));
						break;
					}
				}
			}
		}
		return groupInfo;
	}

	@Override
	public void updateGroup(Map<String, Object> data) {
		
		data.put("updatetime", new Date());
		this.chatMapper.updateGroup(data);
	}

	@Override
	public Map<String, Object> getUserGroup(Map<String, Object> data) {
		
		return this.chatMapper.getUserGroup(data);
	}

	@Override
	public Map<String, Object> createOneGroup(Map<String, Object> map)
			throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<String> userIds = new ArrayList<String>();
		userIds.add(map.get("createid") + "");
		userIds.add(map.get("userid") + "");

		// 判断之前是否创建过
		Map<String, Object> groupmap = new HashMap<String, Object>();
		String cartId = chatMapper.isCreateOneCart(
				map.get("createid") + "", map.get("userid") + "");
		if (cartId != null) { // 之前创建过
			data.put("groupId", cartId);
			return data;
		}
		groupmap.put("isgroup", 2);
		// 添加群到数据库
		String groupId = UUID.randomUUID().toString().replaceAll("-", "");
		// int num=this.chatService.getMyGroupNum(map);
		String groupName = "";
		data.put("groupId", groupId);
		data.put("groupName", groupName);
		Map<String, Object> usermap = new HashMap<String, Object>();
		usermap.put("userid", map.get("createid"));
		usermap = userInfoService.getUserInfo(usermap);
		groupName = usermap.get("realname") + "、";

		usermap = new HashMap<String, Object>();
		usermap.put("userid", map.get("userid") + "");
		usermap = userInfoService.getUserInfo(usermap);
		groupName += usermap.get("realname");

		groupmap.put("groupid", groupId);
		groupmap.put("groupname", groupName);
		groupmap.put("createid", map.get("createid"));
		insertGroup(groupmap);
		// 添加用户 群关系表到数据库
		for (String userid : userIds) {
			Map<String, Object> groupusermap = new HashMap<String, Object>();
			groupusermap.put("groupid", groupId);
			groupusermap.put("userid", userid);
			insertUserToGroup(groupusermap);
		}

		// 创建会话
		SdkHttpResult result = ApiHttpClient.createGroup(Constants.cloudappkey,
				Constants.cloudappsecret, userIds, groupId, groupName,
				FormatType.json);
		log.info(result);
		return data;
	}

	@Override
	public Map<String, Object> createManyGroup(Map<String, Object> map,
			List<Map<String, Object>> userList) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<String> userIds = new ArrayList<String>();
		int status = 0;
		String groupName = "";
		String userid1 = "";
		for (Map<String, Object> user : userList) {
			if (user.get("userid").equals(map.get("createid"))) {
				status = 1;
			}
			if (!userIds.contains(user.get("userid") + "")) {
				userIds.add(user.get("userid") + "");
				groupName += "、" + user.get("realname");
				userid1 = user.get("userid") + "";
			}
		}
		if (status == 0) {
			Map<String, Object> usermap = new HashMap<String, Object>();
			usermap.put("userid", map.get("createid"));
			usermap = this.userInfoService.getUserInfo(usermap);
			userIds.add(map.get("createid") + "");
			groupName = usermap.get("realname") + groupName;
		}
		
		if(userIds.size()==2){
			map.put("userid", userid1);
			return createOneGroup(map);
		}
		
		if(groupName.indexOf("、")==0){
			groupName = groupName.substring(1);
		}
		String groupId = UUID.randomUUID().toString().replaceAll("-", "");

		// int num=this.chatService.getMyGroupNum(map);

		// 添加群到数据库
		Map<String, Object> groupmap = new HashMap<String, Object>();
		groupmap.put("groupid", groupId);
		groupmap.put("groupname", groupName);
		groupmap.put("createid", map.get("createid"));
		groupmap.put("isgroup", 1);
		insertGroup(groupmap);
		// 添加用户 群关系表到数据库
		for (String userid : userIds) {
			Map<String, Object> groupusermap = new HashMap<String, Object>();
			groupusermap.put("groupid", groupId);
			groupusermap.put("userid", userid);
			insertUserToGroup(groupusermap);
		}
		
		SdkHttpResult result = ApiHttpClient.createGroup(Constants.cloudappkey,
				Constants.cloudappsecret, userIds, groupId, groupName,
				FormatType.json);
		log.info(result);
		
		data.put("status", 0);
		data.put("groupId", groupId);
		return data;
	}

	@Override
	public List<Map<String, Object>> getMyAllCartList(Map<String, Object> map) {
		List<Map<String, Object>> datalist = chatMapper.getMyAllCartList(map);
		if (datalist != null && datalist.size() > 0) {
			for (Map<String, Object> data : datalist) {
				if ("2".equals(data.get("isgroup") + "")) {// 单聊
					List<Map<String, Object>> userLis = chatMapper
							.getChatGroupUserList(data);
					for (Map<String, Object> d : userLis) {
						if (!map.get("userid").toString()
								.equals(d.get("userid").toString())) {
							data.put("groupname", d.get("realname"));
							data.put("groupurl", d.get("headimage"));
							break;
						}
					}
				}
			}
		}
		return datalist;
	}

	@Override
	public int getMyAllCartListCount(Map<String, Object> map) {
		return chatMapper.getMyAllCartListCount(map);
	}
	
	@Override
	public List<Map<String, Object>> getChatGroupUserList(
			Map<String, Object> map) {
		return chatMapper.getChatGroupUserList(map);
	}
	
	@Override
	public List<Map<String, Object>> getChatRecordList(Map<String, Object> map) {
		
		return this.chatMapper.getChatRecordList(map);
	}

	@Override
	public int getChatRecordListCount(Map<String, Object> map) {
		
		return this.chatMapper.getChatRecordListCount(map);
	}
	
	@Override
	public int getChatRecordStatusCount(Map<String, Object> map) {
		
		return this.chatMapper.getChatRecordStatusCount(map);
	}
	
	@Override
	public int insertChatRecordStatus(Map<String, Object> map) {
		
		return this.chatMapper.insertChatRecordStatus(map);
	}
	
	@Override
	public boolean signChatRecordStatus(Map<String, Object> map) {
		
		return this.chatMapper.signChatRecordStatus(map)>0;
	}
	
	@Override
	public boolean signVoiceChatRecordStatus(Map<String, Object> map) {
		
		return this.chatMapper.signVoiceChatRecordStatus(map)>0;
	}
	
	@Override
	public int deleteChatRecord(Map<String, Object> map) {
		return this.chatMapper.deleteChatRecord(map);
	}

	@Override
	public List<Map<String, Object>> getGroupListInfoByUserid(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.chatMapper.getGroupListInfoByUserid(map);
	}

	@Override
	public int getGroupUserNumByGroupId(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.chatMapper.getGroupUserNumByGroupId(map);
	}

	@Override
	public void deleteGroupUserInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.chatMapper.deleteGroupUserInfo(map);
	}

	@Override
	public void updateGroupInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.chatMapper.updateGroupInfo(map);
	}

	@Override
	public void updateChatRecordStatus(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.chatMapper.updateChatRecordStatus(map);
	}

	/**
	 * 推送通知栏信息
	 * @param userid
	 * @param title
	 * @param url
	 * @return 
	 */
	public Map<String, String> JPushAndriodAndIosMessage(String userid, String title, String url){
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
					return JPushRegIdUtil.PushUrlByRegId(registrationid, title, url);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}else if(type.equals("userid")){//userid 推送
			try {
				return JPushAliaseUtil.PushUrlByAliase(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}*/
		return null;
	} 
	
}
