package com.collection.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
		//查询当前卖家的邀请者 父id 和 爷爷id
		sellUser = new HashMap<String, Object>();
		sellUser.put("userid", data.get("selluserid"));
		Map<String, Object> elder = this.appVipCardMapper.getElderid(sellUser);
		
		sellUser = new HashMap<String, Object>();
		sellUser.put("growthvalue", 1000);
		sellUser.put("userid", elder.get("parentid"));
		this.appVipCardMapper.addParentGrowthValue(sellUser);
		//5、给父级5%收益和爷级2%收益 （爸爷的个人资产） 且新增记录到团队收益表c_t_app_teamprofit
		double profitprice1 = Double.parseDouble(data.get("profitprice").toString()) * 0.05d;
		sellUser.put("profitprice", profitprice1);
		//增加父级收益
		this.appVipCardMapper.addParentsAndGrandPa(sellUser);
		sellUser = new HashMap<String, Object>();
		double profitprice2 = Double.parseDouble(data.get("profitprice").toString()) * 0.02d;
		sellUser.put("userid", elder.get("grandpaid"));
		sellUser.put("profitprice", profitprice2);
		//增加爷爷收益
		this.appVipCardMapper.addParentsAndGrandPa(sellUser);
		
		//新增到父级爷级团队收益表
		Map<String, Object> teamprofit = new HashMap<String, Object>();
		teamprofit.put("userid", data.get("selluserid"));
		teamprofit.put("parentid", elder.get("parentid"));
		teamprofit.put("parentprofit", profitprice1);
		teamprofit.put("grandfatherid", elder.get("grandpaid"));
		teamprofit.put("grandfatherprofit", profitprice2);
		teamprofit.put("createtime", new Date());
		this.appVipCardMapper.insertTeamProfit(teamprofit);
		//6、增加自己资产总和
		sellUser = new HashMap<String, Object>();
		sellUser.put("userid", data.get("selluserid"));
		sellUser.put("profitprice", data.get("profitprice"));
		this.appVipCardMapper.addMySumassets(sellUser);
	}

	@Override
	public List<Map<String, Object>> getMyCardList(Map<String, Object> data) {
		return this.appVipCardMapper.getMyCardList(data);
	}

	@Override
	public Map<String, Object> getMemberCardInfo(Map<String, Object> data) {
		//获取VIP会员卡信息
		Map<String, Object> cardInfo = this.appVipCardMapper.getMemberCardInfo(data);
		//获取视频包集合信息
		List<Map<String, Object>> movielist = this.appVipCardMapper.getMovieByCardId(cardInfo);
		cardInfo.put("movielist", movielist);
		return cardInfo;
	}

	@Override
	public Map<String, Object> getSellCardInfo(Map<String, Object> data) {
		return this.appVipCardMapper.getSellCardInfo(data);
	}

	@Override
	public void commitSellCard(Map<String, Object> data) {
		//1、如果出售价格 大于最大限制(20000)价格 多余资产直接加入个人溢出资产总和 并新增到添加记录到溢出记录表
		double cardprice = Double.parseDouble(data.get("cardprice").toString());
		if (cardprice > 20000) {
			double overprofit = 20000d - cardprice;
			cardprice = 20000d;
			data.put("overprofit", overprofit);
			this.appVipCardMapper.addUserInfoOverProfit(data);
		}
		data.put("cardprice", cardprice);
		//2、把原来的会员卡订单置为过期
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderid", data.get("orderid"));
		map.put("status", 5);
		this.appVipCardMapper.updateCardOrderStatus(data);
		//3、根据价格生成一张会员卡（规则是按在价格区间，持有时限正序第一个） 算出一个隔日的待出售时间
		Map<String, Object> cardMap = this.appVipCardMapper.getMemberCardByPrice(data);
		cardMap.put("cardprice", cardprice);
		cardMap.put("selluserid", data.get("userid"));
		cardMap.put("ordernum", UUID.randomUUID().toString().replaceAll("-", ""));
		cardMap.put("ordertype", 1);
		//4、生成待出售的订单
		this.appVipCardMapper.insertOrder(cardMap);
	}
}
