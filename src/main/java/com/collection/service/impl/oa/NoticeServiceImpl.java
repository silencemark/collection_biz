package com.collection.service.impl.oa;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IndexMapper;
import com.collection.dao.UserInfoMapper;
import com.collection.dao.oa.NoticeMapper;
import com.collection.service.oa.NoticeService;
/**
 * 通知管理
 * @author pengqinghai
 *
 */
public class NoticeServiceImpl implements NoticeService{

	@Autowired NoticeMapper noticeMapper;
	@Autowired IndexMapper indexMapper;
	@Autowired UserInfoMapper userInfoMapper;

	
	Map<String,Object> exsitesmap = new HashMap<String,Object>();
	@Override
	public int addNotice(Map<String, Object> map) {
		//清空
		exsitesmap.clear();
		
		String noticeId = UUID.randomUUID().toString().replace("-", "");
		map.put("noticeid",noticeId );
		map.put("delflag", "0");
		map.put("createtime", new Date());
		map.put("createid", map.get("userid"));
		map.put("status", "0");
	    int row = this.noticeMapper.addNotice(map);
	    
	    Map<String,Object> filemap = new HashMap<String,Object>();
		filemap.put("companyid", map.get("companyid"));
		filemap.put("resourceid", noticeId);
		filemap.put("resourcetype", 27);
		
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
	    
	    if(row>0){
	    	this.sendToUser(map);
	    	this.sendToArea(map);
	    }
		return row;
	}
  
	//把自己加入转发表
	private void sendToUser(Map<String,Object> map){
		Timestamp tm = new Timestamp(System.currentTimeMillis());
		Map<String,Object> dataMap = new HashMap<String, Object>();
		String forwarduserid = UUID.randomUUID().toString().replace("-", "");
        dataMap.put("forwarduserid", forwarduserid);
    	dataMap.put("resourceid", map.get("noticeid"));
    	dataMap.put("companyid", map.get("companyid"));
    	dataMap.put("createid", map.get("userid"));
    	dataMap.put("receiveid", map.get("userid"));
    	dataMap.put("delflag", "0");
    	dataMap.put("createtime", tm);
    	dataMap.put("isread", "1");
    	dataMap.put("resourcetype", "27");
    	dataMap.put("updatetime", tm);
    	dataMap.put("updateid", map.get("userid"));
    	indexMapper.insertForword(dataMap);
		//用于判断自己是否已经接收到通知了
    	exsitesmap.put(String.valueOf(map.get("userid")), "");
	}
	
	
	//发布范围
	@SuppressWarnings("unchecked")
	private void sendToArea(Map<String,Object> map){
		Map<String,Object> usermap = new HashMap<String,Object>();
		usermap.put("userid", map.get("userid"));
		usermap = this.userInfoMapper.getUserInfo(usermap);
		
		//定义一个map对象，保存已经发送的用户的userid
		Map<String,Object> sendusermap = new HashMap<String,Object>();
		
			//发布范围
			JSONObject json=JSONObject.fromObject(map.get("userlist")+"");
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String, Object> user:userlist){
				Timestamp tm = new Timestamp(System.currentTimeMillis());
				user.put("companyid", map.get("companyid"));
 				user.put("resourceid", map.get("noticeid"));
 				user.put("resourcetype", 1);
 				if(user.containsKey("organizeid") && !"".equals(user.get("organizeid"))){
 					user.put("organizeid", user.get("organizeid"));
 					user.put("type", 2);
 					
 					//添加到发布范围用户表
 					List<Map<String, Object>> userinfolist=getUserByorganize(user);
 					for(Map<String, Object> userinfo:userinfolist){
 						//判断用户是否已经接收过 通知了
 						if(!exsitesmap.containsKey(userinfo.get("userid"))){
	 						Map<String, Object> releaserangemap=new HashMap<String, Object>();
	 						releaserangemap.put("companyid", map.get("companyid"));
	 						releaserangemap.put("resourceid", map.get("noticeid"));
	 						releaserangemap.put("resourcetype", 1);
	 						releaserangemap.put("userid", userinfo.get("userid"));
	 						Map<String, Object> rangeusermap=this.indexMapper.getReleaseRange(releaserangemap);
	 						if(rangeusermap == null || rangeusermap.size() == 0){
	 							releaserangemap.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
	 							releaserangemap.put("delflag", 0);
	 							releaserangemap.put("createtime", new Date());
	 							releaserangemap.put("isread", 0);
	 							this.indexMapper.insertReleaseRangeUser(releaserangemap);
	 						}
	 						
	 						//通知栏推送
	 						try {
	 							//推送信息
	 							String userid=userinfo.get("userid")+"";
	 							//判断当前用户是否已经推送过消息了
	 							if(!sendusermap.containsKey(userid)){
		 							String title=usermap.get("realname")+ "发布了一个通知，请查看！";
		 							String url="/oa/notice_detail.html?noticeid="+map.get("noticeid")+"&userid="+userid;
		 							JPushAndriodAndIosMessage(userid, title, url);
		 							
		 							sendusermap.put(userid, "");
	 							}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
	 						exsitesmap.put(String.valueOf(userinfo.get("userid")), "");
 						}
 					}
 				}
 				if(user.containsKey("userid") && !"".equals(user.get("userid"))){
 					user.put("userid", user.get("userid"));
 					user.put("type", 1);
 					//判断用户是否已经接收过 通知了
 					if(!exsitesmap.containsKey(user.get("userid"))){
	 					//添加到发布范围用户表
	 					Map<String, Object>  releaserangemap=new HashMap<String, Object>();
						releaserangemap.put("companyid", map.get("companyid"));
						releaserangemap.put("resourceid", map.get("noticeid"));
						releaserangemap.put("resourcetype", 1);
						releaserangemap.put("userid", user.get("userid"));
						Map<String, Object> rangeusermap=this.indexMapper.getReleaseRange(releaserangemap);
							if(rangeusermap == null || rangeusermap.size() == 0){
								releaserangemap.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
								releaserangemap.put("delflag", 0);
								releaserangemap.put("createtime", new Date());
								releaserangemap.put("isread", 0);
								this.indexMapper.insertReleaseRangeUser(releaserangemap);
							}
						//通知栏推送
						try {
							//推送信息
							String userid=user.get("userid")+"";
							//判断当前用户是否已经推送过消息了
							if(!sendusermap.containsKey(userid)){
								Map<String,Object> param = new HashMap<String,Object>();
								param.put("userid", userid);
								String registrationid = this.indexMapper.getRegistrationIdByUserId(param);
								String title=usermap.get("realname")+ "发布了一个通知，请查看！";
								String url="/oa/notice_detail.html?noticeid="+map.get("noticeid")+"&userid="+userid;
								JPushAndriodAndIosMessage(userid, title, url);
								
								sendusermap.put(userid, "");
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						exsitesmap.put(String.valueOf(user.get("userid")), "");
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
//	public List<Map<String, Object>> userinfolistall=new ArrayList<Map<String,Object>>();
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
		
		//查询某组织机构下面所有的人员信息
		return indexMapper.getUserListByOrganizeid(data);
	}

 

	@Override
	public List<Map<String, Object>> getNoticeList(Map<String, Object> map) {
		map.put("resourcetype", "27");
		// TODO Auto-generated method stub
		return this.noticeMapper.getNoticeList(map);
	}


	@Override
	public int getNoticeListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.noticeMapper.getNoticeListCount(map);
	}
	
	@Override
	public Map<String,Object> getNoticeDetail(Map<String,Object> map){
		map.put("resourcetype", "27");
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.noticeMapper.getNoticeList(map);
		//查询图片和语音
		Map<String, Object> rangemap=new HashMap<String, Object>();
		rangemap.put("resourcetype", 27);
		rangemap.put("resourceid", map.get("noticeid"));
		List<Map<String,Object>> filelist = this.indexMapper.getFileList(rangemap);
		Map<String,Object> noticemap = (Map<String, Object>) list.get(0);
		noticemap.put("filelist", filelist);
		return noticemap;
	}

	@Override
	public void updatenotice(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("delflag", 1);
		map.put("updatetime",new Date());
		this.noticeMapper.updatenotice(map);
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
