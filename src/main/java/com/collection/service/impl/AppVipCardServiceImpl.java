package com.collection.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IAppVipCardMapper;
import com.collection.service.IAppVipCardService;
/**
 * VIP会员卡相关功能
 * @author silence
 *
 */
public class AppVipCardServiceImpl implements IAppVipCardService{

	@Autowired IAppVipCardMapper appVipCardMapper;

	@Override
	public List<Map<String, Object>> getVipCardList(Map<String, Object> data) {
		return appVipCardMapper.getVipCardList(data);
	}

	@Override
	public List<Map<String, Object>> getWaitPayCard(Map<String, Object> data) {
		return appVipCardMapper.getWaitPayCard(data);
	}

	@Override
	public Map<String, Object> getPayVipCardInfo(Map<String, Object> data) {
		return appVipCardMapper.getPayVipCardInfo(data);
	}

	@Override
	public void payVipCard(Map<String, Object> data) {
		this.appVipCardMapper.payVipCard(data);
	}

	@Override
	public Map<String, Object> getContactPhone(Map<String, Object> data) {
		return this.appVipCardMapper.getContactPhone(data);
	}
}
