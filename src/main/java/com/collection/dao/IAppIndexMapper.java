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
}
