package com.collection.service.impl.purchase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IndexMapper;
import com.collection.dao.UserInfoMapper;
import com.collection.dao.personal.PersonalMapper;
import com.collection.dao.purchase.PurchaseMapper;
import com.collection.service.purchase.PurchaseService;
import com.collection.util.Constants;
/**
 * 采购管理
 * @author silence
 *
 */
public class PurchaseServiceImpl implements PurchaseService{

	@Autowired PurchaseMapper purchaseMapper;
	@Autowired UserInfoMapper userinfoMapper;
	@Resource IndexMapper indexMapper;
	@Autowired PersonalMapper personalMapper;
	
	@Override
	public List<Map<String, Object>> getPurchaseList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchaseList(data);
	}

	@Override
	public int getPurchaseListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchaseListNum(data);
	}

	@Override
	public List<Map<String, Object>> getPurchaseListByDate(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		
		List<Map<String, Object>> list=this.purchaseMapper.getPurchaseListByDate(data);
		for(Map<String, Object> order:list){
			data.put("createtime", order.get("createtime"));
			List<Map<String, Object>> orderlist=this.purchaseMapper.getPurchaseByDate(data);
			order.put("orderlist", orderlist);
		}
		return list;
	}

	@Override
	public int getPurchaseListByDateNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchaseListByDateNum(data);
	}

	@Override
	public List<Map<String, Object>> getShopList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getShopList(data);
	}

	@Override
	public String insertPurchase(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String orderid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("orderid", orderid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		data.put("status", 0);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo=this.userinfoMapper.getUserInfo(userInfo);
		//添加到审批提醒表
		if(userInfo!=null && userInfo.size()>0){
			Map<String,	Object> remindmap=new HashMap<String, Object>();
			remindmap.put("remindid", UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid",data.get("companyid"));
			remindmap.put("userid",data.get("examineuserid"));
			remindmap.put("title","审批");
			remindmap.put("content",userInfo.get("realname")+"申请了一个采购(入库)单,请您审批");
			remindmap.put("linkurl", "purchase/purchase_check.html?orderid="+orderid);
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getPurchaseInfo?orderid="+orderid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", orderid);
			remindmap.put("resourcetype", 1);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title=userInfo.get("realname")+"申请了一个采购(入库)单,请您审批";
				String url="/purchase/purchase_check.html?orderid="+orderid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",orderid);
		forwordmap.put("receiveid",data.get("createid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",1);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加审批人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",orderid);
		forwordmap.put("receiveid",data.get("examineuserid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",1);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		String ccuserids = "";
		//添加抄送人到转发表
		if(data.containsKey("userlist") && data.get("userlist")!=null && !"".equals(data.get("userlist"))){
			JSONObject json=JSONObject.fromObject(data.get("userlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String,Object> user : userlist){
				try {
					String userid=String.valueOf(user.get("userid"));
					if(!userid.equals(data.get("examineuserid")) && !userid.equals(data.get("createid"))){
						forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",data.get("companyid"));
						forwordmap.put("resourceid",orderid);
						forwordmap.put("receiveid",user.get("userid"));
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",1);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"申请了一个采购(入库)单,请查看！";
						String url="/purchase/purchase_detail.html?orderid="+orderid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}

		this.purchaseMapper.insertPurchase(data);
		return orderid;
	}

	@Override
	public void insertPurchaseMaterial(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("materielid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime",new Date());
		data.put("delflag", 0);
		data.put("stock", data.get("num"));
		data.put("residualmoney", data.get("sumprice"));
		if(data.containsKey("price") && data.get("price")!=null && !"".equals(data.get("price"))){
			String price=data.get("price").toString();
			if(price.indexOf(".")>0){
				if(price.length()-price.indexOf(".")-1>6){
					data.put("price",price.substring(0,price.indexOf(".")+7));
				}
			}
		}
		this.purchaseMapper.insertPurchaseMaterial(data);
	}

	
	@Override
	public Map<String, Object> getPurchaseInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String,Object> purchasemap = this.purchaseMapper.getPurchaseInfo(data);
		
		if(data.containsKey("sendid") && data.get("sendid")!=null && !"".equals(data.get("sendid"))){
			Map<String,Object> nonemap=this.purchaseMapper.getUsername(data);
			if(nonemap!=null && nonemap.containsKey("realname")){
				purchasemap.put("realname",nonemap.get("realname"));
			}
		}
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(purchasemap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(purchasemap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		purchasemap.put("ccuserlist", ccuserlist);
		return purchasemap;
	}

	@Override
	public List<Map<String, Object>> getPurchaseMaterialList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchaseMaterialList(data);
	}

	@Override
	public List<Map<String, Object>> getPurchaseMaterialListnew(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchaseMaterialListnew(data);
	}
	
	@Override
	public List<Map<String, Object>> getPurchaseCommentList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchaseCommentList(data);
	}

	@Override
	public void insertForword(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("delflag", 0);
		data.put("createtime", new Date());
		this.purchaseMapper.insertForword(data);
	}

	@Override
	public String insertComment(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String commentid = UUID.randomUUID().toString().replaceAll("-", "");
		data.put("commentid", commentid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		this.purchaseMapper.insertComment(data);
		return commentid;
	}

	@Override
	public void updatePurchaseOrder(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("orderid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		this.purchaseMapper.updatePurchaseOrder(data);
	}

	@Override
	public List<Map<String, Object>> getApplyOrderList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getApplyOrderList(data);
	}

	@Override
	public int getApplyOrderListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getApplyOrderListNum(data);
	}

	@Override
	public String insertApply(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String orderid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("orderid", orderid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		data.put("status", 0);
		
		Map<String, Object> userInfo=new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo=this.userinfoMapper.getUserInfo(userInfo);
		//添加到审批提醒表
		if(userInfo!=null && userInfo.size()>0){
			Map<String,	Object> remindmap=new HashMap<String, Object>();
			remindmap.put("remindid", UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid",data.get("companyid"));
			remindmap.put("userid",data.get("examineuserid"));
			remindmap.put("title","审批");
			remindmap.put("content",userInfo.get("realname")+"申请了一个申购单,请您审批");
			remindmap.put("linkurl", "purchase/apply_check.html?orderid="+orderid);
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getApplyInfo?orderid="+orderid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", orderid);
			remindmap.put("resourcetype", 2);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title=userInfo.get("realname")+"申请了一个申购单,请您审批";
				String url="/purchase/apply_check.html?orderid="+orderid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//添加自己到转发表
		Map<String, Object> forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",orderid);
		forwordmap.put("receiveid",data.get("createid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",1);
		forwordmap.put("resourcetype",2);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加审批人转发表
		forwordmap=new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
		forwordmap.put("companyid",data.get("companyid"));
		forwordmap.put("resourceid",orderid);
		forwordmap.put("receiveid",data.get("examineuserid"));
		forwordmap.put("createid",data.get("createid"));
		forwordmap.put("isread",0);
		forwordmap.put("resourcetype",2);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		//添加抄送人到转发表
		String ccuserids = "";
		if(data.containsKey("userlist") && data.get("userlist")!=null && !"".equals(data.get("userlist"))){
			JSONObject json=JSONObject.fromObject(data.get("userlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String,Object> user : userlist){
				try {
					String userid=String.valueOf(user.get("userid"));
					if(!userid.equals(data.get("examineuserid")) && !userid.equals(data.get("createid"))){
						forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",data.get("companyid"));
						forwordmap.put("resourceid",orderid);
						forwordmap.put("receiveid",user.get("CCuserid"));
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",2);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						
						//推送信息
						String title=userInfo.get("realname")+"申请了一个申购单,请查看！";
						String url="/purchase/apply_detail.html?orderid="+orderid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			data.put("CCuserids", ccuserids.substring(0,(ccuserids.length() - 1)));
		}
		
		this.purchaseMapper.insertApply(data);
		return orderid;
	}

	@Override
	public void insertApplyMaterial(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("materielid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime",new Date());
		data.put("delflag", 0);
		this.purchaseMapper.insertApplyMaterial(data);
	}

	@Override
	public Map<String, Object> getApplyInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String,Object> applymap = this.purchaseMapper.getApplyInfo(data);
		
		if(data.containsKey("sendid") && data.get("sendid")!=null && !"".equals(data.get("sendid"))){
			Map<String,Object> nonemap=this.purchaseMapper.getUsername(data);
			if(nonemap!=null && nonemap.containsKey("realname")){
				applymap.put("realname",nonemap.get("realname"));
			}
		}
		
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(applymap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(applymap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		applymap.put("ccuserlist", ccuserlist);
		return applymap;
	}

	@Override
	public List<Map<String, Object>> getApplyMaterialList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getApplyMaterialList(data);
	}

	@Override
	public void updateApplyOrder(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("orderid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		this.purchaseMapper.updateApplyOrder(data);
	}

	@Override
	public List<Map<String, Object>> getMaterialDetail(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getMaterialDetail(data);
	}

	@Override
	public List<Map<String, Object>> getPurchaseStatisticsList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchaseStatisticsList(data);
	}

	
	@Override
	public int getMaterialDetailNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getMaterialDetailNum(data);
	}

	@Override
	public int getPurchaseStatisticsListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchaseStatisticsListNum(data);
	}

	@Override
	public int getPurchaseNumByCompany(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchaseNumByCompany(data);
	}

	@Override
	public int getApplyNumByCompany(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getApplyNumByCompany(data);
	}

	@Override
	public List<Map<String, Object>> getMaterialType(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getMaterialType(data);
	}

	/**
	 * 根据时间查询采购入库的总金额
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getPurchasePayAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchasePayAmount(map);
	} 
	
	@Override
	public int getPurchaseCommentListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchaseCommentListNum(data);
	}

	@Override
	public List<Map<String, Object>> getPurchaseDateByname(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> purchasedate=this.purchaseMapper.getPurchaseDateByname(data);
		for(Map<String, Object> purchase:purchasedate){
			data.put("createtime", purchase.get("createtime"));
			List<Map<String, Object>> orderlist=this.purchaseMapper.getOrderListByDate(data);
			purchase.put("orderlist", orderlist);
		}
		return purchasedate;
	}

	@Override
	public int getPurchaseDateBynameNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchaseDateBynameNum(data);
	}

	@Override
	public List<Map<String, Object>> getApplyOrderByDate(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=this.purchaseMapper.getApplyOrderByDate(data);
		for(Map<String, Object> order:list){
			data.put("createtime", order.get("createtime"));
			List<Map<String, Object>> orderlist=this.purchaseMapper.getApplyListByDate(data);
			order.put("orderlist", orderlist);
		}
		return list;
	}

	@Override
	public int getApplyOrderByDateNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getApplyOrderByDateNum(data);
	}

	@Override
	public List<Map<String, Object>> getStockInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getStockInfo(data);
	}

	/**
	 * 查询查询库存的总金额
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getStockPayAmountInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getStockPayAmountInfo(map);
	}
	
	@Override
	public List<Map<String, Object>> getPurchaseTypeList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getPurchaseTypeList(data);
	}
	@Override
	public List<Map<String, Object>> getApplyTypeList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.purchaseMapper.getApplyTypeList(data);
	}

	
	/**
	 * 推送通知栏信息
	 * @param userid
	 * @param title
	 * @param url
	 */
	public void JPushAndriodAndIosMessage(String userid, String title, String url){
		/*//获取推送的类型
		String type = Constants.JPUSHTYPE;
		//registrationid推送
		if(type.equals("registrationid")){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("userid", userid);
			//查询用户的registrationid
			try {
				String registrationid = this.indexMapper.getRegistrationIdByUserId(param);
				if(!"".equals(registrationid) && registrationid != null){
					//推送信息
					JPushRegIdUtil.PushUrlByRegId(registrationid, title, url);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}else if(type.equals("userid")){//userid 推送
			try {
				JPushAliaseUtil.PushUrlByAliase(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}*/
	}

	@Override
	public List<Map<String, Object>> getApplynameMaterialList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		
		return this.purchaseMapper.getApplynameMaterialList(data);
	}
	
}
