package com.collection.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IManageBackStageMapper;
import com.collection.service.IManageBackStageService;
/**
 * 后台管理相关功能
 * @author silence
 *
 */
public class ManageBackStageServiceImpl implements IManageBackStageService{

	@Autowired IManageBackStageMapper manageBackStageMapper ;
	
	@Override
	public Map<String, Object> getUserInfo(Map<String, Object> data) {
		return this.manageBackStageMapper.getUserInfo(data);
	}

	@Override
	public int getUserListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getUserListCount(data);
	}

	@Override
	public List<Map<String, Object>> getUserList(Map<String, Object> data) {
		return this.manageBackStageMapper.getUserList(data);
	}

	@Override
	public List<Map<String, Object>> getBannerList(Map<String, Object> data) {
		return this.manageBackStageMapper.getBannerList(data);
	}

	@Override
	public int getBannerListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getBannerListCount(data);
	}

	@Override
	public Map<String, Object> getBannerInfo(Map<String, Object> data) {
		return this.manageBackStageMapper.getBannerInfo(data);
	}

	@Override
	public void insertBanner(Map<String, Object> data) {
		this.manageBackStageMapper.insertBanner(data);
	}

	@Override
	public void updateBanner(Map<String, Object> data) {
		this.manageBackStageMapper.updateBanner(data);
	}

	@Override
	public void updateUserInfo(Map<String, Object> data) {
		this.manageBackStageMapper.updateUserInfo(data);
	}

	@Override
	public List<Map<String, Object>> getOrderList(Map<String, Object> data) {
		return this.manageBackStageMapper.getOrderList(data);
	}

	@Override
	public int getOrderListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getOrderListCount(data);
	}

	@Override
	public void frozenOrder(Map<String, Object> data) {
		//订单状态为冻结/解冻
		this.manageBackStageMapper.updateOrderStatus(data);
		if("-2".equals(data.get("status").toString())){
			//冻结双方用户
			data.put("status", 2);
			this.manageBackStageMapper.frozenOrder(data);
		} else {
			//解冻结双方用户
			data.put("status", 0);
			this.manageBackStageMapper.frozenOrder(data);
		}
	}

	@Override
	public void updateOrderStatus(Map<String, Object> data) {
		this.manageBackStageMapper.updateOrderStatus(data);
	}

	@Override
	public List<Map<String, Object>> getIndexMovieList(Map<String, Object> data) {
		return this.manageBackStageMapper.getIndexMovieList(data);
	}

	@Override
	public int getIndexMovieListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getIndexMovieListCount(data);
	}

	@Override
	public void updateIndexMovie(Map<String, Object> data) {
		this.manageBackStageMapper.updateIndexMovie(data);
	}

	@Override
	public void insertIndexMovie(Map<String, Object> data) {
		this.manageBackStageMapper.insertIndexMovie(data);
	}

	@Override
	public List<Map<String, Object>> getMemberCardList(Map<String, Object> data) {
		return this.manageBackStageMapper.getMemberCardList(data);
	}

	@Override
	public int getMemberCardListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getMemberCardListCount(data);
	}

	@Override
	public void updateMemberCard(Map<String, Object> data) {
		this.manageBackStageMapper.updateMemberCard(data);
	}

	@Override
	public List<Map<String, Object>> getLevelList(Map<String, Object> data) {
		return this.manageBackStageMapper.getLevelList(data);
	}

	@Override
	public int getLevelListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getLevelListCount(data);
	}

	@Override
	public void updateLevel(Map<String, Object> data) {
		this.manageBackStageMapper.updateLevel(data);
	}

	@Override
	public List<Map<String, Object>> getCertificationList(
			Map<String, Object> data) {
		return this.manageBackStageMapper.getCertificationList(data);
	}

	@Override
	public int getCertificationListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getCertificationListCount(data);
	}

	@Override
	public void updateCertification(Map<String, Object> data) {
		this.manageBackStageMapper.updateCertification(data);
	}

	@Override
	public List<Map<String, Object>> getPaymentMethodList(
			Map<String, Object> data) {
		return this.manageBackStageMapper.getPaymentMethodList(data);
	}

	@Override
	public int getPaymentMethodListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getPaymentMethodListCount(data);
	}

	@Override
	public List<Map<String, Object>> getQuestionList(Map<String, Object> data) {
		return this.manageBackStageMapper.getQuestionList(data);
	}

	@Override
	public int getQuestionListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getQuestionListCount(data);
	}

	@Override
	public void replyQuestion(Map<String, Object> data) {
		this.manageBackStageMapper.replyQuestion(data);
	}

	@Override
	public List<Map<String, Object>> getExchangeList(Map<String, Object> data) {
		return this.manageBackStageMapper.getExchangeList(data);
	}

	@Override
	public int getExchangeListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getExchangeListCount(data);
	}
	
}
