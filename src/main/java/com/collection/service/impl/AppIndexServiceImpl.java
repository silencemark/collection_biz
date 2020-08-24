package com.collection.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;

import com.collection.dao.IAppIndexMapper;
import com.collection.dao.IAppUserCenterMapper;
import com.collection.dao.IAppVipCardMapper;
import com.collection.dao.ISystemMapper;
import com.collection.service.IAppIndexService;
/**
 * app首页相关
 * @author silence
 *
 */
public class AppIndexServiceImpl implements IAppIndexService{

	@Autowired IAppIndexMapper appIndexMapper;
	
	@Autowired IAppUserCenterMapper appUserCenterMapper;	
	
	@Autowired IAppVipCardMapper appVipCardMapper;
	
	@Autowired ISystemMapper systemMapper;
	
	private Logger logger = Logger.getLogger(AppIndexServiceImpl.class);

	@Override
	public List<Map<String, Object>> getHomePageBanner(Map<String, Object> data) {
		return appIndexMapper.getHomePageBanner(data);
	}

	@Override
	public List<Map<String, Object>> getAdvertisement(Map<String, Object> data) {
		return appIndexMapper.getAdvertisement(data);
	}

	@Override
	public List<Map<String, Object>> getHomePageMovie(Map<String, Object> data) {
		boolean fristflag = false;
		//分页处理如果没传 默认就是 第一页  每页6条
		if(data.get("startnum") == null || "".equals(data.get("startnum").toString())) {
			data.put("startnum", 0);
			fristflag = true;
		}
		if(data.get("rownum") == null || "".equals(data.get("rownum").toString())) {
			data.put("rownum", 6);
			fristflag = true;
		}
		data.put("startnum", Integer.parseInt(data.get("startnum").toString()));
		data.put("rownum", Integer.parseInt(data.get("rownum").toString()));
		//如果类型是全部 则包含会员视频和普通视频
		//type = 1 会员专享(会员专享的不在这个表，在会员视频表）
		List<Map<String, Object>> resultlist = new ArrayList<Map<String,Object>>();
		//定义一个视频查询集合内存
		List<Map<String, Object>> homePageMovie = new ArrayList<Map<String,Object>>();
		//定义一个pagenum
		int movienum =  0;
		if("0".equals(data.get("type"))){
			//珍藏电影
			data.put("type", 2);
			movienum =  appIndexMapper.getHomePageMovieCount(data);
			homePageMovie = appIndexMapper.getHomePageMovie(data);
			Map<String, Object> typeMap = new HashMap<String, Object>();
			typeMap.put("type", 2);
			if(fristflag){typeMap.put("pageno", 1);}
			typeMap.put("movienum", movienum);
			typeMap.put("typename", "珍藏电影");
			typeMap.put("movielist", homePageMovie);
			resultlist.add(typeMap);
			//推荐动漫
			data.put("type", 3);
			movienum =  appIndexMapper.getHomePageMovieCount(data);
			homePageMovie = appIndexMapper.getHomePageMovie(data);
			typeMap = new HashMap<String, Object>();
			typeMap.put("type", 3);
			if(fristflag){typeMap.put("pageno", 1);}
			typeMap.put("movienum", movienum);
			typeMap.put("typename", "推荐动漫");
			typeMap.put("movielist", homePageMovie);
			resultlist.add(typeMap);
			//精品电视剧
			data.put("type", 4);
			movienum =  appIndexMapper.getHomePageMovieCount(data);
			homePageMovie = appIndexMapper.getHomePageMovie(data);
			typeMap = new HashMap<String, Object>();
			typeMap.put("type", 4);
			if(fristflag){typeMap.put("pageno", 1);}
			typeMap.put("movienum", movienum);
			typeMap.put("typename", "电视剧");
			typeMap.put("movielist", homePageMovie);
			resultlist.add(typeMap);
			//精品推荐
			data.put("type", 1);
			movienum =  appIndexMapper.getHomePageMovieCount(data);
			homePageMovie = appIndexMapper.getHomePageMovie(data);
			typeMap = new HashMap<String, Object>();
			typeMap.put("type", 1);
			if(fristflag){typeMap.put("pageno", 1);}
			typeMap.put("movienum", movienum);
			typeMap.put("typename", "精品推荐");
			typeMap.put("movielist", homePageMovie);
			resultlist.add(typeMap);
			//综艺节目
			data.put("type", 5);
			movienum =  appIndexMapper.getHomePageMovieCount(data);
			homePageMovie = appIndexMapper.getHomePageMovie(data);
			typeMap = new HashMap<String, Object>();
			typeMap.put("type", 5);
			if(fristflag){typeMap.put("pageno", 1);}
			typeMap.put("movienum", movienum);
			typeMap.put("typename", "综艺节目");
			typeMap.put("movielist", homePageMovie);
			resultlist.add(typeMap);
			return resultlist;
		} else {
			movienum =  appIndexMapper.getHomePageMovieCount(data);
			homePageMovie = appIndexMapper.getHomePageMovie(data);
			Map<String, Object> typeMap = new HashMap<String, Object>();
			typeMap.put("type", data.get("type"));
			switch (Integer.parseInt(data.get("type").toString())) {
			case 1:
				typeMap.put("typename", "精品推荐");
				break;
			case 2:
				typeMap.put("typename", "珍藏电影");
				break;
			case 3:
				typeMap.put("typename", "推荐动漫");
				break;
			case 4:
				typeMap.put("typename", "电视剧");
				break;
			case 5:
				typeMap.put("typename", "综艺节目");
				break;
			}
			if(fristflag){typeMap.put("pageno", 1);}
			typeMap.put("movienum", movienum);
			typeMap.put("movielist", homePageMovie);
			resultlist.add(typeMap);
			return resultlist;
		}
		
	}

	@Override
	public List<Map<String, Object>> getHomePageVideoDesc(
			Map<String, Object> data) {
		return this.appIndexMapper.getHomePageVideoDesc(data);
	}

	@Override
	public Map<String, Object> addCommunity(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		//用户信息
		Map<String, Object> userinfo = this.appUserCenterMapper.getMyUserInfo(data);
		//1、账号冻结不能发布
		if ("2".equals(userinfo.get("status").toString())) {
			result.put("status", 1);
			result.put("message", "您的账号已被冻结，不能发布动态");
			return result;
		}
		//2、判断当前用户是否实名认证、
		if (!"2".equals(userinfo.get("isrealname").toString())) {
			result.put("status", 2);
			result.put("message", "您还没有实名认证，不能发布动态");
			return result;
		} 
		//3、是否当前用户绑定收款方式
		if (!"1".equals(userinfo.get("ispaymentmethod").toString())) {
			result.put("status", 2);
			result.put("message", "您还没有绑定收款方式，不能发布动态");
			return result;
		}
		//4、是否设置支付密码
		if (!"1".equals(userinfo.get("ispaypass").toString())) {
			result.put("status", 2);
			result.put("message", "您还没有设置支付密码，不能发布动态");
			return result;
		}
		//5、判断当天是否发布2次过（一天发两次）
		List<Map<String, Object>> communitylist = this.appIndexMapper.getTodayCommunity(data);
		if(communitylist != null && communitylist.size() > 1 ){
			result.put("status", 1);
     		result.put("message", "上传失败，每天最多发布2条动态");
     		return result;
		}
		//6、首先入库社区朋友圈表并拿到主建id
		this.appIndexMapper.addCommunity(data);
		//获取用户上传的图片集合‘
		@SuppressWarnings("unchecked")
		ObjectMapper mapper = new ObjectMapper();  
	    //使用jackson解析数据  
	    JavaType jt = mapper.getTypeFactory().constructParametricType(ArrayList.class, Map.class);     
	    List<Map<String, Object>> image = null;
		try {
			image = (List<Map<String, Object>>)mapper.readValue(data.get("imagelist").toString(), jt);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		logger.info(data.toString());
		List<Map<String, Object>> imagelist = new ArrayList<Map<String,Object>>();
		Map<String, Object> datamap = new HashMap<String, Object>();
		int count = 0;
		for(Map<String, Object> ima : image) {
			String base64Data = ima.get("imgurl").toString();
	        try{  
	            logger.debug("上传文件的数据："+base64Data);
	            logger.debug("对数据进行判断");
	            if(base64Data == null || "".equals(base64Data)){
	                logger.info("上传失败，上传图片数据为空");
	                result.put("status", 1);
	        		result.put("message", "上传失败，上传图片数据为空");
	        		return result;
	            }
	            String tempFileName = System.currentTimeMillis()/1000l+"_community" +count+ ".jpg";
	            logger.debug("生成文件名为："+tempFileName);
	            //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
	            byte[] bs = Base64Utils.decodeFromString(base64Data);
	            try{
	                //使用apache提供的工具类操作流
	                FileUtils.writeByteArrayToFile(new File("/home/silence/collection_web/upload/community/"+ tempFileName), bs);  
	            }catch(Exception ee){
	            	logger.info("上传失败，写入文件失败"+ee.getMessage());
	            	result.put("status", 1);
	        		result.put("message", "上传失败，写入文件失败");
	        		return result;
	            }
	           
	            datamap = new HashMap<String, Object>();
	            datamap.put("imgurl", "/upload/community/"+tempFileName);
	            datamap.put("communityid", data.get("communityid"));
	            datamap.put("createtime", new Date());
	            imagelist.add(datamap);
	        } catch (Exception e) {  
	        	logger.error("上传失败"+ e.getMessage());
	        	result.put("status", 1);
	    		result.put("message", "上传失败");
	    		return result;
	        }
	        count++;
		}
		if (imagelist != null && imagelist.size() > 0) {
			//2、批量入库社区图片表
			this.appIndexMapper.addCommunityImg(imagelist);
			//3、修改社区封面图 为第一张
			data.put("coverimg", imagelist.get(0).get("imgurl"));
			this.appIndexMapper.updateCommunity(data);
		}
		result.put("status", 0);
		result.put("message", "上传成功");
		return result;
	}

	@Override
	public Map<String, Object> getCommunityList(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		//分页处理如果没传 默认就是 第一页  每页6条
		if(data.get("startnum") == null || "".equals(data.get("startnum").toString())) {
			data.put("startnum", 0);
		}
		if(data.get("rownum") == null || "".equals(data.get("rownum").toString())) {
			data.put("rownum", 10);
		}
		data.put("startnum", Integer.parseInt(data.get("startnum").toString()));
		data.put("rownum", Integer.parseInt(data.get("rownum").toString()));
		//定义一个pagenum
		int communitynum =  appIndexMapper.getCommunityListCount(data);
		//定义一个查询集合内存
		List<Map<String, Object>> communitylist = appIndexMapper.getCommunityList(data);
		for (Map<String, Object> community : communitylist) {
			//查询所有的社区的图片
			List<Map<String, Object>> imglist = this.appIndexMapper.getCommunityImgList(community);
			community.put("imglist", imglist);
		}
		result.put("communitynum", communitynum);
		result.put("communitylist", communitylist);
		return result;
	}

	@Override
	public void likeCommunity(Map<String, Object> data) {
		//查询用户是否有过该社区动态的点赞记录
		Map<String, Object> like = this.appIndexMapper.getCommunityLike(data);
		if (like == null || like.isEmpty()) {
			//朋友圈点赞加一
			data.put("likesnum", 1);
			this.appIndexMapper.likeCommunity(data);
			//新增点赞记录表
			this.appIndexMapper.insertLikeCommunity(data);
			//查询点赞数量5个赞加0.5元 
			/*int count = this.appIndexMapper.getCommunityLikeCount(data);
			if (count == 5) {
				//2、五个赞送0.5元可兑换资产(包含点赞后取消的)
				data.put("profitprice", 0.5);
				this.appVipCardMapper.addParentsAndGrandPa(data);
				//3、系统通知
				Map<String, Object> notice = new HashMap<String, Object>();
				notice.put("title", "社区通知");
				notice.put("message", "恭喜你，您完成享社区动态发布并获得5个点赞任务，您获得0.5元可兑换资产奖励，请注意查收");
				notice.put("userid", data.get("userid"));
				notice.put("createtime", new Date());
				this.systemMapper.insertUserNotice(notice);
			} else if (count == 10) {
				//2、10个赞送0.5元可兑换资产(包含点赞后取消的)
				data.put("profitprice", 0.5);
				this.appVipCardMapper.addParentsAndGrandPa(data);
				//3、系统通知
				Map<String, Object> notice = new HashMap<String, Object>();
				notice.put("title", "社区通知");
				notice.put("message", "恭喜你，您完成享社区动态获得10个点赞任务，共获得1元可兑换资产奖励，请注意查收");
				notice.put("userid", data.get("userid"));
				notice.put("createtime", new Date());
				this.systemMapper.insertUserNotice(notice);
			} else if (count == 50) {
				//2、50个赞送4元可兑换资产(包含点赞后取消的)
				data.put("profitprice", 4);
				this.appVipCardMapper.addParentsAndGrandPa(data);
				//3、系统通知
				Map<String, Object> notice = new HashMap<String, Object>();
				notice.put("title", "社区通知");
				notice.put("message", "恭喜你，您完成享社区动态获得50个点赞 任务，共获得5元可兑换资产奖励，请注意查收");
				notice.put("userid", data.get("userid"));
				notice.put("createtime", new Date());
				this.systemMapper.insertUserNotice(notice);
			}*/
		} else {
			//取消点赞
			if("1".equals(like.get("status").toString())) {
				//朋友圈点赞减一
				data.put("likesnum", -1);
			} else {
				//朋友圈点赞加一
				data.put("likesnum", 1);
			}
			this.appIndexMapper.likeCommunity(data);
			//修改点赞记录表
			like.put("status", data.get("status"));
			this.appIndexMapper.updateCommunityLike(like);
		}
	}

	@Override
	public void addCommunityReply(Map<String, Object> data) {
		this.appIndexMapper.addCommunityReply(data);
	}

	@Override
	public void addCommunityComment(Map<String, Object> data) {
		this.appIndexMapper.addCommunityComment(data);
	}

	@Override
	public List<Map<String, Object>> getCommunityComment(
			Map<String, Object> data) {
		return this.appIndexMapper.getCommunityComment(data);
	}

	@Override
	public Map<String, Object> getCommunityDetail(Map<String, Object> data) {
		Map<String, Object> result = appIndexMapper.getCommunityDetail(data);
		//查询所有的社区的图片
		List<Map<String, Object>> imglist = this.appIndexMapper.getCommunityImgList(result);
		result.put("imglist", imglist);
		result.put("status", 0);
		result.put("message", "查询详情成功");
		return result;
	}

	@Override
	public Map<String, Object> getNewSystemNotice(Map<String, Object> data) {
		return this.appIndexMapper.getNewSystemNotice(data);
	}

}
