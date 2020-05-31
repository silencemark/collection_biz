package com.collection.service.impl;

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
		if("0".equals(data.get("type"))){
			List<Map<String, Object>> homePageMovie = appIndexMapper.getHomePageMovie(data);
			List<Map<String, Object>> memberMovie = appIndexMapper.getMemberMovieList(data);
			homePageMovie.addAll(memberMovie);
			return homePageMovie;
		} else if ("1".equals(data.get("type"))){
			List<Map<String, Object>> memberMovie = appIndexMapper.getMemberMovieList(data);
			return memberMovie;
		} else {
			List<Map<String, Object>> homePageMovie = appIndexMapper.getHomePageMovie(data);
			return homePageMovie;
		}
		
	}

}
