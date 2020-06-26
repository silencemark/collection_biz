package com.collection.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IAppIndexMapper;
import com.collection.service.IAppIndexService;
/**
 * app首页相关
 * @author silence
 *
 */
public class AppIndexServiceImpl implements IAppIndexService{

	@Autowired IAppIndexMapper appIndexMapper;

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
		//如果类型是全部 则包含会员视频和普通视频
		//type = 1 会员专享(会员专享的不在这个表，在会员视频表）
		List<Map<String, Object>> resultlist = new ArrayList<Map<String,Object>>();
		if("0".equals(data.get("type"))){
			//珍藏电影
			data.put("type", 2);
			List<Map<String, Object>> homePageMovie = appIndexMapper.getHomePageMovie(data);
			Map<String, Object> typeMap = new HashMap<String, Object>();
			typeMap.put("type", 2);
			typeMap.put("typename", "珍藏电影");
			typeMap.put("movielist", homePageMovie);
			resultlist.add(typeMap);
			//推荐动漫
			data.put("type", 3);
			homePageMovie = appIndexMapper.getHomePageMovie(data);
			typeMap = new HashMap<String, Object>();
			typeMap.put("type", 3);
			typeMap.put("typename", "推荐动漫");
			typeMap.put("movielist", homePageMovie);
			resultlist.add(typeMap);
			//精品电视剧
			data.put("type", 4);
			homePageMovie = appIndexMapper.getHomePageMovie(data);
			typeMap = new HashMap<String, Object>();
			typeMap.put("type", 4);
			typeMap.put("typename", "热血电视剧");
			typeMap.put("movielist", homePageMovie);
			resultlist.add(typeMap);
			
			List<Map<String, Object>> memberMovie = appIndexMapper.getMemberMovieList(data);
			typeMap = new HashMap<String, Object>();
			typeMap.put("type", 1);
			typeMap.put("typename", "会员专享");
			typeMap.put("movielist", memberMovie);
			resultlist.add(typeMap);
			return resultlist;
		} else if ("1".equals(data.get("type"))){
			List<Map<String, Object>> memberMovie = appIndexMapper.getMemberMovieList(data);
			Map<String, Object> typeMap = new HashMap<String, Object>();
			typeMap.put("type", 1);
			typeMap.put("movielist", memberMovie);
			resultlist.add(typeMap);
			return resultlist;
		} else {
			List<Map<String, Object>> homePageMovie = appIndexMapper.getHomePageMovie(data);
			Map<String, Object> typeMap = new HashMap<String, Object>();
			typeMap.put("type", data.get("type"));
			typeMap.put("movielist", homePageMovie);
			resultlist.add(typeMap);
			return resultlist;
		}
		
	}

}
