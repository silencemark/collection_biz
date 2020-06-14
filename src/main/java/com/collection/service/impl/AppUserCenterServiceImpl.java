package com.collection.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IAppUserCenterMapper;
import com.collection.dao.IAppVipCardMapper;
import com.collection.service.IAppUserCenterService;
/**
 * 个人中心相关
 * @author silence
 *
 */
public class AppUserCenterServiceImpl implements IAppUserCenterService{

	@Autowired IAppUserCenterMapper appUserCenterMapper;
	@Autowired IAppVipCardMapper appVipCardMapper;

	@Override
	public Map<String, Object> getMyCenter(Map<String, Object> data) {
		return appUserCenterMapper.getMyCenter(data);
	}

	@Override
	public void signIn(Map<String, Object> data) {
		//签到送1-50成长值 和一个 xgo
		int value = new Random().nextInt(50)+1;
		data.put("growthvalue", value);
		this.appUserCenterMapper.signIn(data);
		//新增到签到记录表
		data.put("xgocoin", 1);
		data.put("createtim", new Date());
		this.appUserCenterMapper.insertSign(data);
		//如果成长值达到下一个值改变会员等级
		appUserCenterMapper.updateUserInfoLevel(data);
	}

	@Override
	public Map<String, Object> myGrowthValue(Map<String, Object> data) {
		//查询当前用户的成长值
		Map<String, Object> result = this.appUserCenterMapper.myGrowthValue(data);
		//查询所有会员等级
		List<Map<String, Object>> memberGrowList= this.appUserCenterMapper.getMemberGrowList();
		result.put("memberGrowList", memberGrowList);
		return result;
	}

	@Override
	public void certification(Map<String, Object> data) {
		data.put("createtime", new Date());
		this.appUserCenterMapper.certification(data);
	}

	@Override
	public Map<String, Object> getCertification(Map<String, Object> data) {
		return this.appUserCenterMapper.getCertification(data);
	}

	@Override
	public Map<String, Object> myTeam(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		//获取子集
		List<Map<String, Object>> sonlist = this.appUserCenterMapper.getSonProfit(data);
		int allnum = sonlist.size();
		for(Map<String, Object> son:sonlist){
			//获取孙子集
			son.put("grandid", data.get("userid"));
			List<Map<String, Object>> grandsonlist = this.appUserCenterMapper.getGrandSonProfit(son);
			if(grandsonlist != null && grandsonlist.size() > 0){
				allnum += grandsonlist.size();
			}
			son.put("grandsonlist", grandsonlist);
		}
		result.put("sonlist", sonlist);
		result.put("sonnum", sonlist.size());
		result.put("alllowernum", allnum);
		return result;
	}

	@Override
	public Map<String, Object> myAssets(Map<String, Object> data) {
		return this.appUserCenterMapper.myAssets(data);
	}

	@Override
	public List<Map<String, Object>> getExchangeList(Map<String, Object> data) {
		return this.appUserCenterMapper.getExchangeList(data);
	}

	@Override
	public Map<String, Object> exchangeVipCard(Map<String, Object> data) {
		//1、根据价格生成一张会员卡（规则是按在价格区间，持有时限正序第一个） 算出一个隔日的待出售时间
		Map<String, Object> cardMap = this.appVipCardMapper.getMemberCardByPrice(data);
		if(cardMap!= null) {
			cardMap.put("cardprice", data.get("cardprice"));
			cardMap.put("selluserid", data.get("userid"));
			String ordernum = AppVipCardServiceImpl.generateUniqueKey();
			cardMap.put("ordernum", ordernum);
			cardMap.put("ordertype", 2);
			cardMap.put("createtime", new Date());
			//2、生成待出售的订单
			this.appVipCardMapper.insertOrder(cardMap);
			//3、个人中心可兑换资产减少
			data.put("overprofit", Double.parseDouble(data.get("cardprice").toString()) * -1);
			this.appVipCardMapper.addUserInfoOverProfit(data);
			//4、新增兑换记录
			data.put("amount", data.get("cardprice"));
			data.put("ordernum", ordernum);
			this.appUserCenterMapper.insertExchange(data);
			data = new HashMap<String, Object>();
			data.put("status", 0);
			data.put("message", "兑换成功");
			return data;
		} else {
			data = new HashMap<String, Object>();
			data.put("status", 1);
			data.put("message", "没有符合的会员卡，请输入合适的价格");
			return data;
		}
	}

	@Override
	public Map<String, Object> myInviteCode(Map<String, Object> data) {
		return this.appUserCenterMapper.myInviteCode(data);
	}

	@Override
	public void updateQrcode(Map<String, Object> data) {
		this.appUserCenterMapper.updateQrcode(data);
	}

	@Override
	public Map<String, Object> getMyUserInfo(Map<String, Object> data) {
		return this.appUserCenterMapper.getMyUserInfo(data);
	}

	@Override
	public void updateHeadImg(Map<String, Object> data) {
		this.appUserCenterMapper.updateUserInfo(data);
	}

	@Override
	public void updateNickName(Map<String, Object> data) {
		this.appUserCenterMapper.updateUserInfo(data);
	}

	@Override
	public Map<String, Object> updatePassWord(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		//1、判断旧密码是否正确
		result = this.appUserCenterMapper.getMyUserInfo(data);
		if (result != null && result.size() > 0 ){
			this.appUserCenterMapper.updateUserInfo(data);
			result = new HashMap<String, Object>();
			result.put("status", 0);
			result.put("message", "登录密码修改成功");
		} else {
			result = new HashMap<String, Object>();
			result.put("status", 1);
			result.put("message", "旧密码输入错误");
		}
		return result;
	}

	@Override
	public void setPayPassWord(Map<String, Object> data) {
		this.appUserCenterMapper.updateUserInfo(data);
	}

	@Override
	public Map<String, Object> getPaymentMethod(Map<String, Object> data) {
		Map<String, Object> result = this.appUserCenterMapper.getPaymentMethod(data);
		if (result == null) {
			this.appUserCenterMapper.addPaymentMethod(data);
			result = new HashMap<String, Object>();
		}
		return result;
	}

	@Override
	public void updatePaymentMethod(Map<String, Object> data) {
		this.appUserCenterMapper.updatePaymentMethod(data);
	}
	
	
}
