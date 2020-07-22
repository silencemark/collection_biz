package com.collection.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.collection.dao.IAppUserCenterMapper;
import com.collection.dao.IAppVipCardMapper;
import com.collection.dao.ISystemMapper;
import com.collection.service.IAppVipCardService;
/**
 * VIP会员卡相关功能
 * @author silence
 *
 */
public class AppVipCardServiceImpl implements IAppVipCardService{

	@Autowired IAppVipCardMapper appVipCardMapper;

	@Autowired IAppUserCenterMapper appUserCenterMapper;	
	
	@Autowired ISystemMapper systemMapper;	
	
	private Logger logger = Logger.getLogger(AppVipCardServiceImpl.class);
	
	@Override
	public List<Map<String, Object>> getVipCardList(Map<String, Object> data) {
		return appVipCardMapper.getVipCardList(data);
	}

	@Override
	public List<Map<String, Object>> getWaitPayCard(Map<String, Object> data) {
		//查询时，如果你是买家且待支付的订单超过了支付时间，那么冻结你的账户，且订单回退到待出售，短信提醒卖家 和买家
		Map<String, Object> buyMap = new HashMap<String, Object>();
		buyMap.put("buyuserid", data.get("userid"));
		List<Map<String, Object>> waitPayList = this.appVipCardMapper.getMoreThanWaitTime(buyMap);
		for (Map<String, Object> waitPay : waitPayList) {
			//冻结买家用户
			buyMap.put("status", 2);
			this.appVipCardMapper.frozenOrder(buyMap);
			//更改买家抢购状态
			waitPay.put("status", 3);
			this.appVipCardMapper.updateRushToBuy(waitPay);
			//订单回到待出售删除 买家id 和 抢购时间
			this.appVipCardMapper.updateWaitSell(waitPay);
			
			//新增买家通知
            Map<String, Object> notice = new HashMap<String, Object>();
    		notice.put("title", "冻结通知");
    		notice.put("message", "由于您未及时支付订单，您的账号已被冻结");
    		notice.put("userid", waitPay.get("buyuserid"));
    		notice.put("createtime", new Date());
    		this.systemMapper.insertUserNotice(notice);
    		
    		//新增卖家通知
            notice = new HashMap<String, Object>();
    		notice.put("title", "出售订单通知");
    		notice.put("message", "由于您出售的订单买家未及时支付订单，买家已被冻结，您的订单回到待出售状态");
    		notice.put("userid", waitPay.get("selluserid"));
    		notice.put("createtime", new Date());
    		this.systemMapper.insertUserNotice(notice);
		}
		//查询时，如果你是买家且待审核的订单超过了审核时间，那么 自动审核通过
		List<Map<String, Object>> examineList = this.appVipCardMapper.getMoreThanExamineTime(buyMap);
		for (Map<String, Object> examine : examineList) {
			//自动审核
			this.appVipCardMapper.examinePast(examine);
			//审核通过之后的逻辑和通知处理
			examineAfter(examine);
		}
		return appVipCardMapper.getWaitPayCard(data);
	}

	@Override
	public Map<String, Object> getPayVipCardInfo(Map<String, Object> data) {
		return appVipCardMapper.getPayVipCardInfo(data);
	}

	@Override
	public Map<String, Object> payVipCard(Map<String, Object> datamap) {
		//拿出payorder的base64文件流
		String base64Data = String.valueOf(datamap.get("payorder"));
		Map<String, Object> result = new HashMap<String, Object>();
        try{  
            logger.debug("上传文件的数据："+base64Data);
            logger.debug("对数据进行判断");
            if(base64Data == null || "".equals(base64Data)){
                logger.info("上传失败，上传图片数据为空");
                result.put("status", 1);
        		result.put("message", "上传失败，上传图片数据为空");
        		return result;
            }
            /*else{
                String [] d = base64Data.split("base64,");
                if(d != null && d.length == 2){
                    dataPrix = d[0];
                    data = d[1];
                }else{
                	logger.info("上传失败，数据不合法");
                    result.put("status", 1);
            		result.put("message", "上传图片格式不合法");
            		return result;
                }
            }
 
            logger.debug("对数据进行解析，获取文件名和流数据");
            String suffix = "";
            if("data:image/jpeg;".equalsIgnoreCase(dataPrix)){//data:image/jpeg;base64,base64编码的jpeg图片数据
                suffix = ".jpg";
            } else if("data:image/x-icon;".equalsIgnoreCase(dataPrix)){//data:image/x-icon;base64,base64编码的icon图片数据
                suffix = ".ico";
            } else if("data:image/gif;".equalsIgnoreCase(dataPrix)){//data:image/gif;base64,base64编码的gif图片数据
                suffix = ".gif";
            } else if("data:image/png;".equalsIgnoreCase(dataPrix)){//data:image/png;base64,base64编码的png图片数据
                suffix = ".png";
            }else{
                logger.info("上传图片格式不合法"+dataPrix);
                result.put("status", 1);
        		result.put("message", "上传图片格式不合法");
        		return result;
            }*/
            String tempFileName = System.currentTimeMillis()/1000l+"_app" + ".jpg";
            logger.debug("生成文件名为："+tempFileName);
 
            //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
            byte[] bs = Base64Utils.decodeFromString(base64Data);
            try{
                //使用apache提供的工具类操作流
                FileUtils.writeByteArrayToFile(new File("/home/silence/collection_web/upload/images/"+ tempFileName), bs);  
            }catch(Exception ee){
            	logger.info("上传失败，写入文件失败"+ee.getMessage());
            	result.put("status", 0);
        		result.put("message", "上传失败，写入文件失败");
        		return result;
            }
            //新增通知
            Map<String, Object> cardinfo = appVipCardMapper.getPayVipCardInfo(datamap);
            Map<String, Object> notice = new HashMap<String, Object>();
    		notice.put("title", "出售"+cardinfo.get("typename"));
    		notice.put("message", "您出售的"+cardinfo.get("typename")+"买家支付成功，请尽快审核");
    		notice.put("userid", cardinfo.get("selluserid"));
    		notice.put("createtime", new Date());
    		this.systemMapper.insertUserNotice(notice);
            
            datamap.put("payorder", "/upload/images/"+tempFileName);
            datamap.put("buytime", new Date());
            this.appVipCardMapper.payVipCard(datamap);
    		result.put("status", 0);
    		result.put("message", "上传成功，等待审核");
        }catch (Exception e) {  
        	logger.error("上传失败"+ e.getMessage());
        	result.put("status", 0);
    		result.put("message", "上传失败");
    		return result;
        }
		return result;
	}

	@Override
	public Map<String, Object> getContactPhone(Map<String, Object> data) {
		return this.appVipCardMapper.getContactPhone(data);
	}

	@Override
	public List<Map<String, Object>> getSaleCardList(Map<String, Object> data) {
		//查询时，如果你是卖家且待支付的订单超过了支付时间，那么冻结你的账户，且订单回退到待出售，短信提醒卖家 和买家
		Map<String, Object> buyMap = new HashMap<String, Object>();
		buyMap.put("selluserid", data.get("userid"));
		List<Map<String, Object>> waitPayList = this.appVipCardMapper.getMoreThanWaitTime(buyMap);
		for (Map<String, Object> waitPay : waitPayList) {
			//冻结买家用户
			buyMap.put("status", 2);
			this.appVipCardMapper.frozenOrder(waitPay);
			//更改买家抢购状态
			waitPay.put("status", 3);
			this.appVipCardMapper.updateRushToBuy(waitPay);
			//订单回到待出售删除 买家id 和 抢购时间
			this.appVipCardMapper.updateWaitSell(waitPay);
			
			//新增买家通知
            Map<String, Object> notice = new HashMap<String, Object>();
    		notice.put("title", "冻结通知");
    		notice.put("message", "由于您未及时支付订单，您的账号已被冻结");
    		notice.put("userid", waitPay.get("buyuserid"));
    		notice.put("createtime", new Date());
    		this.systemMapper.insertUserNotice(notice);
    		
    		//新增卖家通知
            notice = new HashMap<String, Object>();
    		notice.put("title", "出售订单通知");
    		notice.put("message", "由于您出售的订单买家未及时支付订单，买家已被冻结，您的订单回到待出售状态");
    		notice.put("userid", waitPay.get("selluserid"));
    		notice.put("createtime", new Date());
    		this.systemMapper.insertUserNotice(notice);
		}
		//查询时，如果你是卖家且待审核的订单超过了审核时间，那么 自动审核通过
		List<Map<String, Object>> examineList = this.appVipCardMapper.getMoreThanExamineTime(buyMap);
		for (Map<String, Object> examine : examineList) {
			//自动审核
			this.appVipCardMapper.examinePast(examine);
			//审核通过之后的逻辑和通知处理
			examineAfter(examine);
		}
		return this.appVipCardMapper.getSaleCardList(data);
	}

	@Override
	public Map<String, Object> getExamineInfo(Map<String, Object> data) {
		return this.appVipCardMapper.getExamineInfo(data);
	}

	@Transactional
	public void examineAfter(Map<String, Object> data){
		//1、新增买家通知
        Map<String, Object> notice = new HashMap<String, Object>();
		notice.put("title", "抢购通知");
		notice.put("message", "恭喜你，卖家已审核完成，请查看已抢购成功最新任务卡");
		notice.put("userid", data.get("buyuserid"));
		notice.put("createtime", new Date());
		this.systemMapper.insertUserNotice(notice);
		
		//获取卖家卖出会员卡张数
		Map<String, Object> sellUser = new HashMap<String, Object>();
		sellUser.put("selluserid", data.get("selluserid"));
		sellUser.put("status", 3);
		int count = this.appVipCardMapper.getUserVipCount(sellUser);
		//2、当前卖家的邀请者加1000成长值
		//查询当前卖家的邀请者 父id 和 爷爷id
		sellUser = new HashMap<String, Object>();
		sellUser.put("userid", data.get("selluserid"));
		Map<String, Object> elder = this.appVipCardMapper.getElderid(sellUser);
		if(elder != null) {
			if (count == 1) {
				sellUser = new HashMap<String, Object>();
				sellUser.put("growthvalue", 1000);
				sellUser.put("userid", elder.get("parentid"));
				this.appVipCardMapper.addGrowthValue(sellUser);
				//新增卖家通知
	            notice = new HashMap<String, Object>();
	    		notice.put("title", "成长值通知");
	    		notice.put("message", "恭喜您，您邀请的好友完成了第一次任务，恭喜您获得1000点成长值");
	    		notice.put("userid", elder.get("parentid"));
	    		notice.put("createtime", new Date());
	    		this.systemMapper.insertUserNotice(notice);
	    		
	    		//如果成长值达到下一个值改变会员等级
	    		Map<String, Object> levelMap = appUserCenterMapper.getUserNewOldLevel(notice);
	    		if (!levelMap.get("levelid").toString().equals(levelMap.get("oldlevelid").toString())){
	    			appUserCenterMapper.updateUserInfoLevel(levelMap);
	    			//新增买家通知
	                notice = new HashMap<String, Object>();
	        		notice.put("title", "会员等级通知");
	        		notice.put("message", "恭喜您，您的会员等级升级为"+levelMap.get("levelname"));
	        		this.systemMapper.insertUserNotice(notice);
	    		}
			}
			//3、给父级5%收益和爷级2%收益 （爸爷的个人资产） 且新增记录到团队收益表c_t_app_teamprofit
			double profitprice1 = Double.parseDouble(data.get("profitprice").toString()) * 0.05d;
			sellUser.put("profitprice", profitprice1);
			//增加父级收益
			this.appVipCardMapper.addParentsAndGrandPa(sellUser);
			//通知父亲收益
			
			sellUser = new HashMap<String, Object>();
			double profitprice2 = Double.parseDouble(data.get("profitprice").toString()) * 0.02d;
			if(elder.get("grandpaid") != null) {
				sellUser.put("userid", elder.get("grandpaid"));
				sellUser.put("profitprice", profitprice2);
				//增加爷爷收益
				this.appVipCardMapper.addParentsAndGrandPa(sellUser);
				//通知爷爷 收益
				
			}
			//新增到父级爷级团队收益表
			Map<String, Object> teamprofit = new HashMap<String, Object>();
			teamprofit.put("userid", data.get("selluserid"));
			teamprofit.put("parentid", elder.get("parentid"));
			teamprofit.put("parentprofit", profitprice1);
			teamprofit.put("grandfatherid", elder.get("grandpaid"));
			teamprofit.put("grandfatherprofit", profitprice2);
			teamprofit.put("createtime", new Date());
			this.appVipCardMapper.insertTeamProfit(teamprofit);
		}
		//4、给卖家自己增长成长值
		sellUser = new HashMap<String, Object>();
		sellUser.put("growthvalue", (int)Double.parseDouble(data.get("profitprice").toString()));
		sellUser.put("userid", data.get("selluserid"));
		this.appVipCardMapper.addGrowthValue(sellUser);
		//如果成长值达到下一个值改变会员等级
		Map<String, Object> levelMap = appUserCenterMapper.getUserNewOldLevel(sellUser);
		if (!levelMap.get("levelid").toString().equals(levelMap.get("oldlevelid").toString())){
			appUserCenterMapper.updateUserInfoLevel(levelMap);
			//新增买家通知
			sellUser = new HashMap<String, Object>();
			sellUser.put("title", "会员等级通知");
    		sellUser.put("message", "恭喜您，您的会员等级升级为"+levelMap.get("levelname"));
    		sellUser.put("createtime", new Date());
    		this.systemMapper.insertUserNotice(notice);
		}
		//5、增加自己资产总和
		sellUser = new HashMap<String, Object>();
		sellUser.put("userid", data.get("selluserid"));
		sellUser.put("profitprice", data.get("profitprice"));
		this.appVipCardMapper.addMySumassets(sellUser);
	}
	
	@Override
	@Transactional
	public void examinePast(Map<String, Object> data) {
		//1、审核通过更改订单状态为已完成
		this.appVipCardMapper.examinePast(data);
		//审核通过之后的逻辑和通知处理
		examineAfter(data);
	}

	@Override
	public List<Map<String, Object>> getMyCardList(Map<String, Object> data) {
		//点击查询前可根据好评数和剩余观看天数到时间了 可置为到期可出售状态
		//查询买家为当前用户 且时间大于 可出售最低时限 且 好评数大于需要的好评次数的订单
		List<Map<String, Object>> orderlist = this.appVipCardMapper.getSellOrderListByid(data);
		//更改订单为 到期可出售的债券status = 4
		for (Map<String, Object> orderMap : orderlist) {
			orderMap.put("status", 4);
			this.appVipCardMapper.updateCardOrderStatus(orderMap);
		}
		//查询买家为当前用户 且时间大于剩余时限的订单
		orderlist = this.appVipCardMapper.getDueOrderListByid(data);
		//更改订单为过期不可出售的债券status = 5
		for (Map<String, Object> orderMap : orderlist) {
			orderMap.put("status", 5);
			this.appVipCardMapper.updateCardOrderStatus(orderMap);
		}
		return this.appVipCardMapper.getMyCardList(data);
	}
	
	@Override
	public List<Map<String, Object>> getMyHisCardList(Map<String, Object> data) {
		return this.appVipCardMapper.getMyHisCardList(data);
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
	public Map<String, Object> commitSellCard(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		//用户信息
		Map<String, Object> userinfo = this.appUserCenterMapper.getMyUserInfo(data);
		//1、账号冻结不能出售
		if ("2".equals(userinfo.get("status").toString())) {
			result.put("status", 1);
			result.put("message", "您的账号已被冻结，不能出售订单");
			return result;
		}
		//1、如果出售价格 大于最大限制(20000)价格 多余资产直接加入个人溢出资产总和 并新增到添加记录到溢出记录表
		double cardprice = Double.parseDouble(data.get("cardprice").toString());
		if (cardprice > 20000) {
			double overprofit = 20000d - cardprice;
			cardprice = 20000d;
			data.put("overprofit", overprofit);
			//添加到个人可兑换资产
			this.appVipCardMapper.addUserInfoOverProfit(data);
			//添加到溢出 
			data.put("createtime", new Date());
			this.appVipCardMapper.insertOverFlow(data);
		}
		data.put("cardprice", cardprice);
		//2、把原来的会员卡订单置为过期
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderid", data.get("orderid"));
		map.put("status", 5); 
		this.appVipCardMapper.updateCardOrderStatus(map);
		//3、根据价格匹配一张会员卡（规则是按在价格区间，持有时限正序第一个） 算出一个隔日的待出售时间
		Map<String, Object> cardMap = this.appVipCardMapper.getMemberCardByPrice(data);
		cardMap.put("cardprice", cardprice);
		cardMap.put("selluserid", data.get("userid"));
		
		//查询一个当天的订单数据加一
		int ordernum = appVipCardMapper.getOrderNum();
		//生成订单号
		cardMap.put("ordernum", generateUniqueKey() + ordernum);
		cardMap.put("ordertype", 1);
		cardMap.put("createtime", new Date());
		//4、生成待出售的订单
		this.appVipCardMapper.insertOrder(cardMap);
		result.put("status", 0);
		result.put("message", "出售成功");
		return result;
	}

	@Override
	public Map<String, Object> insertRushToBuy(Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		//用户信息
		Map<String, Object> userinfo = this.appUserCenterMapper.getMyUserInfo(data);
		//会员卡信息
		Map<String, Object> cardinfo = this.appVipCardMapper.getMemberCardInfoById(data);
		//1、账号冻结不能抢购
		if ("2".equals(userinfo.get("status").toString())) {
			result.put("status", 1);
			result.put("message", "您的账号已被冻结，不能参与抢购");
			return result;
		}
		//2、防止重复点击抢购接口
		Map<String, Object> rushMap = this.appVipCardMapper.getRushToBuyById(data);
		if (rushMap != null && rushMap.size() > 0){
			result.put("status", 1);
			result.put("message", "您已经参与抢购，请勿重复点击");
			return result;
		}
		//3、判断当前用户是否实名认证、
		if (!"2".equals(userinfo.get("isrealname").toString())) {
			result.put("status", 1);
			result.put("message", "您还没有实名认证");
			return result;
		} 
		//4、是否当前用户绑定收款方式
		if (!"1".equals(userinfo.get("ispaymentmethod").toString())) {
			result.put("status", 1);
			result.put("message", "您还没有绑定收款方式");
			return result;
		}
		//5、是否设置支付密码
		if (!"1".equals(userinfo.get("ispaypass").toString())) {
			result.put("status", 1);
			result.put("message", "您还没有设置支付密码");
			return result;
		}
		//6、查询XGO够不够抢购，如果不够不能抢购
		if(Integer.parseInt(userinfo.get("xgocoin").toString()) < Integer.parseInt(cardinfo.get("xgocoin").toString())){
			result.put("status", 1);
			result.put("message", "您的XGO币不足，抢购失败");
			return result;
		}
		//7.查询结算时间是否已经超过，已过不能抢购（再查询一次抢购的会员卡 是否 可抢购即可）
		if ("0".equals(cardinfo.get("isbuy").toString())) {
			result.put("status", 1);
			result.put("message", "任务领取时间已结束");
			return result;
		}
		data.put("createtime", new Date());
		this.appVipCardMapper.insertRushToBuy(data);
		result.put("status", 0);
		result.put("message", "参与抢购成功,请到我的抢购中查看抢购结果");
		return result;
	}

	@Override
	public List<Map<String, Object>> getRushToBuyList(Map<String, Object> data) {
		return this.appVipCardMapper.getRushToBuyList(data);
	}
	
	
	/**
     * 生成主键id
     * 时间+随机数
     * silence
     * @return
     */
    public static synchronized String generateUniqueKey(){
        //Random random = new Random();
        // 随机数的3位随机数
        //Integer r = random.nextInt(900) + 1000;
        // 返回  13位时间
        Long timeMillis = System.currentTimeMillis();
        // 10位时间戳 + 当天订单数量总量调用的地方查询 + 1
        return  timeMillis.toString().substring(0, 10);
    }

	@Override
	public void commentMovie(Map<String, Object> data) {
		//订单评论次数加一
		this.appVipCardMapper.addOrderCommentCount(data);
		data.put("createtime", new Date());
		this.appVipCardMapper.commentMovie(data);
	}

	@Override
	public void addMovieHot(Map<String, Object> data) {
		//如果是会员电影是会员电影加一 
		if("2".equals(data.get("type"))) {
			data.put("tablename", "c_t_app_membermovie");
		} else {
			data.put("tablename", "c_t_app_movie");
		}
		this.appVipCardMapper.addMovieHot(data);
	}

}
