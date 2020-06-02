package com.collection.service.impl;

import java.util.HashMap;
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

	@Override
	public List<Map<String, Object>> getSaleCardList(Map<String, Object> data) {
		return this.appVipCardMapper.getSaleCardList(data);
	}

	@Override
	public Map<String, Object> getExamineInfo(Map<String, Object> data) {
		return this.appVipCardMapper.getExamineInfo(data);
	}

	@Override
	public void examinePast(Map<String, Object> data) {
		//1、审核通过更改订单状态为已完成
		this.appVipCardMapper.examinePast(data);
		//获取买家已购买会员卡张数
		Map<String, Object> buyUser = new HashMap<String, Object>();
		buyUser.put("buyuserid", data.get("buyuserid"));
		buyUser.put("status", 3);
		int count = this.appVipCardMapper.getUserVipCount(buyUser);
		//2、判断买家是否是第一次购买如果是那么买家晋升为普通会员
		if(count == 1) {
			buyUser = new HashMap<String, Object>();
			buyUser.put("levelid", 1);
			buyUser.put("userid", data.get("buyuserid"));
			this.appVipCardMapper.updateLevel(buyUser);
		}
		//获取卖家卖出会员卡张数
		Map<String, Object> sellUser = new HashMap<String, Object>();
		sellUser.put("selluserid", data.get("selluserid"));
		sellUser.put("status", 3);
		count = this.appVipCardMapper.getUserVipCount(sellUser);
		//3、当前卖家为第一次卖 那么加对应成长值变为青铜会员
		if (count == 1) {
			sellUser = new HashMap<String, Object>();
			sellUser.put("levelid", 2);
			sellUser.put("userid", data.get("selluserid"));
			this.appVipCardMapper.updateLevel(sellUser);
		}
		//4、当前卖家的邀请者加1000成长值
		
		//5、给父级5%收益和爷级2%收益 （爸爷的个人资产） 且新增记录到团队收益表c_t_app_teamprofit
		
		//6、增加资产总和
		
		//7、如果增长的价格溢出最大价格，那么增加个人溢出资产总和，添加记录到溢出记录表
		
	}
}
