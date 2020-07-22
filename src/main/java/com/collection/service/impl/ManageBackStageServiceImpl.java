package com.collection.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IManageBackStageMapper;
import com.collection.dao.ISystemMapper;
import com.collection.service.IManageBackStageService;

/**
 * 后台管理相关功能
 * @author silence
 *
 */
public class ManageBackStageServiceImpl implements IManageBackStageService{

	@Autowired IManageBackStageMapper manageBackStageMapper ;
	
	@Autowired ISystemMapper systemMapper;
	
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
		Map<String, Object> orderinfo = this.manageBackStageMapper.getOrderBuyAndSellInfo(data);
		//订单状态为冻结/解冻
		this.manageBackStageMapper.updateOrderStatus(data);
		if("-2".equals(data.get("status").toString())){
			//冻结双方用户
			data.put("status", 2);
			this.manageBackStageMapper.frozenOrder(data);
			//新增买家通知
			addUserNotice("冻结通知", "由于您抢购的订单数据异常，您的账号已被冻结（订单号："+orderinfo.get("ordernum")+"）", orderinfo.get("buyuserid").toString());
			//新增卖家通知
			addUserNotice("冻结通知", "由于您出售的订单数据异常，您的账号已被冻结（订单号："+orderinfo.get("ordernum")+"）", orderinfo.get("selluserid").toString());
		} else {
			//解冻结双方用户
			data.put("status", 0);
			this.manageBackStageMapper.frozenOrder(data);
			//新增买家通知
			addUserNotice("账号状态通知", "由于您抢购的订单已解除数据异常，您的账号已恢复正常（订单号："+orderinfo.get("ordernum")+"）", orderinfo.get("buyuserid").toString());
			//新增卖家通知
			addUserNotice("账号状态通知", "由于您出售的订单已解除数据异常，您的账号已恢复正常（订单号："+orderinfo.get("ordernum")+"）", orderinfo.get("selluserid").toString());
		}
	}
	
	/**
	 * 新增用户通知消息 
	 * @param title
	 * @param message
	 * @param userid
	 */
	public void addUserNotice(String title, String message, String userid) {
		Map<String, Object> notice = new HashMap<String, Object>();
		notice.put("title", title);
		notice.put("message", message);
		notice.put("userid", userid);
		notice.put("createtime", new Date());
		this.systemMapper.insertUserNotice(notice);
	}
	
	
	@Override
	public void returnOrder(Map<String, Object> data) {
		Map<String, Object> orderinfo = this.manageBackStageMapper.getOrderBuyAndSellInfo(data);
		//订单归还给卖家 订单回到待出售，删除 买家id 和 抢购时间，支付时间，到期时间（出售时间） 清空好评数量
		this.manageBackStageMapper.returnOrder(data);
		//新增买家通知
		addUserNotice("订单通知", "由于您抢购的订单付款异常，您抢购的订单已被退回给卖家（订单号："+orderinfo.get("ordernum")+"）", orderinfo.get("buyuserid").toString());
		//新增卖家通知
		addUserNotice("订单通知", "由于您出售的订单买家付款异常，您的订单经过系统审核，已退回给您，请在待出售订单中查看详细信息（订单号："+orderinfo.get("ordernum")+"）", orderinfo.get("selluserid").toString());
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
		this.manageBackStageMapper.updateUserCertification(data);
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

	@Override
	public List<Map<String, Object>> getNoticeList(Map<String, Object> data) {
		return this.manageBackStageMapper.getNoticeList(data);
	}

	@Override
	public int getNoticeListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getNoticeListCount(data);
	}

	@Override
	public Map<String, Object> getIndexInfo() {
		return this.manageBackStageMapper.getIndexInfo();
	}

	@Override
	public List<Map<String, Object>> getMemberMovieList(Map<String, Object> data) {
		return this.manageBackStageMapper.getMemberMovieList(data);
	}

	@Override
	public int getMemberMovieListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getMemberMovieListCount(data);
	}

	@Override
	public void updateMemberMovie(Map<String, Object> data) {
		 this.manageBackStageMapper.updateMemberMovie(data);
	}

	@Override
	public void insertMemberMovie(Map<String, Object> data) {
		 this.manageBackStageMapper.insertMemberMovie(data);
	}

	@Override
	public List<Map<String, Object>> getSysNoticeList(Map<String, Object> data) {
		return this.manageBackStageMapper.getSysNoticeList(data);
	}

	@Override
	public int getSysNoticeListCount(Map<String, Object> data) {
		return  this.manageBackStageMapper.getSysNoticeListCount(data);
	}

	@Override
	public void sendSysNotice(Map<String, Object> data) {
		//查询所有用户
		List<Map<String, Object>> userlist = this.manageBackStageMapper.getUserList(null);
		Date createtime = new Date();
		for(Map<String, Object> user: userlist) {
			user.putAll(data);
			user.put("createtime", createtime);
			user.put("type", 1);
		}
		this.manageBackStageMapper.sendSysNotice(userlist);
	}

	@Override
	public void updateRate(Map<String, Object> data) {
		this.manageBackStageMapper.updateRate(data);
	}

	@Override
	public void insertRate(Map<String, Object> data) {
		this.manageBackStageMapper.insertRate(data);
	}

	@Override
	public List<Map<String, Object>> getRateList(Map<String, Object> data) {
		return this.manageBackStageMapper.getRateList(data);
	}

	@Override
	public int getRateListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getRateListCount(data);
	}

	@Override
	public List<Map<String, Object>> getAdvertList(Map<String, Object> data) {
		return this.manageBackStageMapper.getAdvertList(data);
	}

	@Override
	public Map<String, Object> getAdvertInfo(Map<String, Object> data) {
		return this.manageBackStageMapper.getAdvertInfo(data);
	}

	@Override
	public int getAdvertListCount(Map<String, Object> data) {
		return this.manageBackStageMapper.getAdvertListCount(data);
	}

	@Override
	public void insertAdvert(Map<String, Object> data) {
		this.manageBackStageMapper.insertAdvert(data);
	}

	@Override
	public void updateAdvert(Map<String, Object> data) {
		this.manageBackStageMapper.updateAdvert(data);
	}

	@Override
	public void deleteAdvert(Map<String, Object> data) {
		this.manageBackStageMapper.deleteAdvert(data);
	}
	
}
