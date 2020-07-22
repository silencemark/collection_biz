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
		//分页处理如果没传 默认就是 第一页  每页6条
		if(data.get("startnum") == null || "".equals(data.get("startnum").toString())) {
			data.put("startnum", 0);
		}
		if(data.get("rownum") == null || "".equals(data.get("rownum").toString())) {
			data.put("rownum", 6);
		}
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
			typeMap.put("movienum", movienum);
			typeMap.put("typename", "电视剧");
			typeMap.put("movielist", homePageMovie);
			resultlist.add(typeMap);
			movienum =  appIndexMapper.getMemberMovieListCount(data);
			List<Map<String, Object>> memberMovie = appIndexMapper.getMemberMovieList(data);
			typeMap = new HashMap<String, Object>();
			typeMap.put("type", 1);
			typeMap.put("movienum", movienum);
			typeMap.put("typename", "会员专享");
			typeMap.put("movielist", memberMovie);
			resultlist.add(typeMap);
			return resultlist;
		} else if ("1".equals(data.get("type"))){
			movienum =  appIndexMapper.getMemberMovieListCount(data);
			homePageMovie = appIndexMapper.getMemberMovieList(data);
			Map<String, Object> typeMap = new HashMap<String, Object>();
			typeMap.put("type", 1);
			typeMap.put("movienum", movienum);
			typeMap.put("typename", "会员专享");
			typeMap.put("movielist", homePageMovie);
			resultlist.add(typeMap);
			return resultlist;
		} else {
			movienum =  appIndexMapper.getHomePageMovieCount(data);
			homePageMovie = appIndexMapper.getHomePageMovie(data);
			Map<String, Object> typeMap = new HashMap<String, Object>();
			typeMap.put("type", data.get("type"));
			switch (Integer.parseInt(data.get("type").toString())) {
			case 2:
				typeMap.put("typename", "珍藏电影");
				break;
			case 3:
				typeMap.put("typename", "推荐动漫");
				break;
			case 4:
				typeMap.put("typename", "电视剧");
				break;
			}
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

}
