package com.collection.dao;
import java.util.List;
import java.util.Map;
/**
 * app首页相关
 * @author silence
 *
 */
public interface IAppIndexMapper {
	
	/**
	 * 获取首页banner图
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getHomePageBanner(Map<String, Object> data);
	
	/**
	 * 获取首页广告信息
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getAdvertisement(Map<String, Object> data);
	
	/**
	 * 获取首页免费影片列表信息
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getHomePageMovie(Map<String, Object> data);
	
	/**
	 * 获取首页免费影片列表条数
	 * @param data
	 * @return
	 * @author silence
	 */
	int getHomePageMovieCount(Map<String, Object> data);
	
	
	/**
	 * 获取会员专享列表信息
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getMemberMovieList(Map<String, Object> data);
	
	/**
	 * 获取会员专享视频条数
	 * @param data
	 * @return
	 */
	int getMemberMovieListCount(Map<String, Object> data);
	
	/**
	 * 模糊查询首页免费影片
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getHomePageVideoDesc(Map<String, Object> data);
	
	/**
	 * 新增社区分享
	 * @param data
	 * @return
	 */
	void addCommunity(Map<String, Object> data);
	
	/**
	 * 新增社区分享的图片
	 * @param data
	 * @return
	 */
	void addCommunityImg(List<Map<String, Object>> data);
	
	/**
	 * 修改社区分享图封面
	 * @param data
	 * @return
	 */
	void updateCommunity(Map<String, Object> data);
	
	/**
	 * 获取社区朋友圈列表条数
	 * @param data
	 * @return
	 * @author silence
	 */
	int getCommunityListCount(Map<String, Object> data);
	
	
	/**
	 * 获取社区朋友圈列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getCommunityList(Map<String, Object> data);
	
	
	/**
	 * 查询朋友圈社区分享详情页面
	 * @param data
	 * @return
	 */
	Map<String, Object> getCommunityDetail(Map<String, Object> data);
	
	/**
	 * 获取社区朋友圈的图片列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getCommunityImgList(Map<String, Object> data);
	
	/**
	 * 点赞记录加一
	 * @param data
	 */
	void likeCommunity(Map<String, Object> data);
	
	/**
	 * 新增点赞记录表
	 * @param data
	 */
	void insertLikeCommunity(Map<String, Object> data);
	
	/**
	 * 查询点赞的数量
	 * @param data
	 * @return
	 */
	int getCommunityLikeCount(Map<String, Object> data);
	
	/**
	 * 新增回复
	 * @param data
	 */
	void addCommunityReply(Map<String, Object> data);
	
	/**
	 * 新增评论
	 * @param data
	 */
	void addCommunityComment(Map<String, Object> data);
	
	/**
	 * 获取评论信息
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getCommunityComment(Map<String, Object> data);
	
	/**
	 * 查询点赞记录表
	 * @param data
	 * @return
	 */
	Map<String, Object> getCommunityLike(Map<String, Object> data);
	
	/**
	 * 修改点赞记录
	 * @param data
	 */
	void updateCommunityLike(Map<String, Object> data);
	
	/**
	 * 查询当天是否发布过社区动态记录
	 * @param data
	 * @return
	 */
	Map<String, Object> getTodayCommunity(Map<String, Object> data);
}
