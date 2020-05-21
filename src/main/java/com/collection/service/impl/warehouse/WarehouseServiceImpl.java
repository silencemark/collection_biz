package com.collection.service.impl.warehouse;

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
import com.collection.dao.warehouse.WarehouseMapper;
import com.collection.service.warehouse.WarehouseService;
import com.collection.util.Constants;
/**
 * 采购管理
 * @author silence
 *
 */
public class WarehouseServiceImpl implements WarehouseService{

	@Autowired WarehouseMapper warehouseMapper;
	@Autowired UserInfoMapper userinfoMapper;
	@Resource IndexMapper indexMapper;
	@Autowired PurchaseMapper purchaseMapper;
	@Autowired PersonalMapper personalMapper;

	@Override
	public List<Map<String, Object>> getMaterialList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getMaterialList(data);
	}

	@Override
	public int getMaterialListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getMaterialListNum(data);
	}

	@Override
	public String insertMaterial(Map<String, Object> data) {
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
			remindmap.put("content",userInfo.get("realname")+"申请了一个用料单,请您审批");
			remindmap.put("linkurl", "warehouse/use_check.html?orderid="+orderid);
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getMaterialInfo?orderid="+orderid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", orderid);
			remindmap.put("resourcetype", 3);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title=userInfo.get("realname")+"申请了一个用料单,请您审批";
				String url="/warehouse/use_check.html?orderid="+orderid;
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
		forwordmap.put("resourcetype",3);
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
		forwordmap.put("resourcetype",3);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
			
		String ccuserids = "";
		//添加抄送人到转发表
		if(data.containsKey("userlist") && !"".equals(data.get("userlist"))){
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
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",3);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"申请了一个用料单,请查看！";
						String url="/warehouse/use_detail.html?orderid="+orderid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.warehouseMapper.insertMaterial(data);
		return orderid;
	}

	@Override
	public void insertUsedMaterial(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("materielid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime", new Date());
		data.put("delflag", 0);
		this.warehouseMapper.insertUsedMaterial(data);
		//减采购的库存
		/*String [] materielids=(data.get("purchasematerielid")+"").split("_");
		int num=Integer.parseInt(data.get("num")+"");
		double sumprice=Double.parseDouble(data.get("sumprice")+"");
		for(int i=0;i<materielids.length;i++){
			Map<String, Object> materielmap=new HashMap<String, Object>();
			materielmap.put("materielid", materielids[i]);
			materielmap=this.warehouseMapper.getMaterial(materielmap);
			if(materielmap != null && materielmap.size() > 0){
				int nownum=Integer.parseInt(materielmap.get("stock")==null?"0":materielmap.get("stock")+"");
				if(num >= nownum){
					Map<String, Object> purchasemap=new HashMap<String, Object>();
					purchasemap.put("materielid", materielids[i]);
					purchasemap.put("reducenum", nownum);
					purchasemap.put("residualmoney", 0);
					this.warehouseMapper.reducePurchaseNum(purchasemap);
					sumprice=sumprice-Double.parseDouble(materielmap.get("residualmoney")==null || "".equals(materielmap.get("residualmoney"))?"0":materielmap.get("residualmoney")+"");
					
					num=num-nownum;
				}else if(num!=0){
					Map<String, Object> purchasemap=new HashMap<String, Object>();
					purchasemap.put("materielid", materielids[i]);
					purchasemap.put("reducenum", num);
					purchasemap.put("residualmoney",Double.parseDouble(materielmap.get("residualmoney")==null || "".equals(materielmap.get("residualmoney"))?"0":materielmap.get("residualmoney")+"")-sumprice);
					this.warehouseMapper.reducePurchaseNum(purchasemap);
					
					num=0;
				}
			}
			
		}*/
	}

	@Override
	public int getMaterialNumByCompany(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getMaterialNumByCompany(data);
	}

	@Override
	public Map<String, Object> getMaterialInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String,Object> materialMap = this.warehouseMapper.getMaterialInfo(data);
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(materialMap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(materialMap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		materialMap.put("ccuserlist", ccuserlist);
		return materialMap;
	}

	@Override
	public List<Map<String, Object>> getUsedMaterialList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getUsedMaterialList(data);
	}

	@Override
	public String updateMaterialOrder(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("orderid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		if(String.valueOf(data.get("result")).equals("1")){
			boolean bool=true;
			List<Map<String, Object>> materialList=this.warehouseMapper.getUsedMaterialList(data);
			for(Map<String, Object> material:materialList){
				String [] materielids=(material.get("purchasematerielid")+"").split("_");
				float num=Float.parseFloat(material.get("num")+"");
				float num1=0;
				for(int i=0;i<materielids.length;i++){
					Map<String, Object> materielmap=new HashMap<String, Object>();
					materielmap.put("materielid", materielids[i]);
					materielmap=this.warehouseMapper.getMaterial(materielmap);
					num1+=Float.parseFloat(materielmap.get("stock")==null?"0":materielmap.get("stock")+"");
				}
				if(num>num1){
					bool=false;
					break;
				}
			}
			if(!bool){
				return "0";
			}
			
		}
		
		this.warehouseMapper.updateMaterialOrder(data);
		if(String.valueOf(data.get("result")).equals("1")){
			//审核未通过还原库存
			List<Map<String, Object>> materialList=this.warehouseMapper.getUsedMaterialList(data);
			for(Map<String, Object> material:materialList){
				
				String [] materielids=(material.get("purchasematerielid")+"").split("_");
				float num=Float.parseFloat(material.get("num")+"");
				double sumprice=Double.parseDouble(material.get("sumprice")+"");
				for(int i=0;i<materielids.length;i++){
					Map<String, Object> materielmap=new HashMap<String, Object>();
					materielmap.put("materielid", materielids[i]);
					materielmap=this.warehouseMapper.getMaterial(materielmap);
					if(materielmap != null && materielmap.size() > 0){
						Float nownum=Float.parseFloat(materielmap.get("stock")==null?"0":materielmap.get("stock")+"");
						if(num >= nownum){
							Map<String, Object> purchasemap=new HashMap<String, Object>();
							purchasemap.put("materielid", materielids[i]);
							purchasemap.put("reducenum", nownum);
							purchasemap.put("residualmoney", 0);
							this.warehouseMapper.reducePurchaseNum(purchasemap);
							sumprice=sumprice-Double.parseDouble(materielmap.get("residualmoney")==null || "".equals(materielmap.get("residualmoney"))?"0":materielmap.get("residualmoney")+"");
							
							num=num-nownum;
						}else if(num!=0){
							Map<String, Object> purchasemap=new HashMap<String, Object>();
							purchasemap.put("materielid", materielids[i]);
							purchasemap.put("reducenum", num);
							purchasemap.put("residualmoney",Double.parseDouble(materielmap.get("residualmoney")==null || "".equals(materielmap.get("residualmoney"))?"0":materielmap.get("residualmoney")+"")-sumprice);
							this.warehouseMapper.reducePurchaseNum(purchasemap);
							
							num=0;
						}
					}
					
				}
				
				/*Map<String, Object> purchasemap=new HashMap<String, Object>();
				purchasemap.put("materielid", material.get("purchasematerielid"));
				purchasemap.put("addstock", Integer.parseInt(material.get("num")+""));
				purchasemap.put("residualmoney", Double.parseDouble(material.get("price")+"")*Integer.parseInt(material.get("num")+""));
				this.warehouseMapper.updatePurchaseMaterial(purchasemap);*/
			}
		}
		return "1";
	}

	@Override
	public List<Map<String, Object>> getReturnOrderList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getReturnOrderList(data);
	}

	@Override
	public int getReturnOrderListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getReturnOrderListNum(data);
	}

	@Override
	public int getReturnNumByCompany(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getReturnNumByCompany(data);
	}

	@Override
	public String insertReturn(Map<String, Object> data) {
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
			remindmap.put("content",userInfo.get("realname")+"申请了一个退货单,请您审批");
			remindmap.put("linkurl", "warehouse/return_check.html?orderid="+orderid);
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getReturnInfo?orderid="+orderid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", orderid);
			remindmap.put("resourcetype", 4);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title=userInfo.get("realname")+"申请了一个退货单,请您审批";
				String url="/warehouse/return_check.html?orderid="+orderid;
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
		forwordmap.put("resourcetype",4);
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
		forwordmap.put("resourcetype",4);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
				
		String ccuserids = "";
		//添加抄送人到转发表
		if(data.containsKey("userlist") && !"".equals(data.get("userlist"))){
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
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",4);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"申请了一个退货单,请查看！";
						String url="/warehouse/return_detail.html?orderid="+orderid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.warehouseMapper.insertReturn(data);
		return orderid;
	}

	@Override
	public void insertReturnMaterial(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("materielid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime", new Date());
		data.put("delflag", 0);
		this.warehouseMapper.insertReturnMaterial(data);
		//减采购的库存
		/*String [] materielids=(data.get("purchasematerielid")+"").split("_");
		int num=Integer.parseInt(data.get("num")+"");
		double sumprice=Double.parseDouble(data.get("sumprice")+"");
		
		for(int i=0;i<materielids.length;i++){
			Map<String, Object> materielmap=new HashMap<String, Object>();
			materielmap.put("materielid", materielids[i]);
			materielmap=this.warehouseMapper.getMaterial(materielmap);
			if(materielmap != null && materielmap.size() > 0){
				int nownum=Integer.parseInt(materielmap.get("stock")==null?"0":materielmap.get("stock")+"");
				if(num >= nownum){
					Map<String, Object> purchasemap=new HashMap<String, Object>();
					purchasemap.put("materielid", materielids[i]);
					purchasemap.put("reducenum", nownum);
					purchasemap.put("residualmoney",0);
					this.warehouseMapper.reducePurchaseNum(purchasemap);
					sumprice=sumprice-Double.parseDouble(materielmap.get("residualmoney")==null || "".equals(materielmap.get("residualmoney"))?"0":materielmap.get("residualmoney")+"");
					num=num-nownum;
				}else if(num!=0){
					Map<String, Object> purchasemap=new HashMap<String, Object>();
					purchasemap.put("materielid", materielids[i]);
					purchasemap.put("reducenum", num);
					
					purchasemap.put("residualmoney",Double.parseDouble(materielmap.get("residualmoney")==null || "".equals(materielmap.get("residualmoney"))?"0":materielmap.get("residualmoney")+"")-sumprice);
					this.warehouseMapper.reducePurchaseNum(purchasemap);
					
					num=0;
				}
			}
			
		}*/
	}

	@Override
	public Map<String, Object> getReturnInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String,Object> returnMap =  this.warehouseMapper.getReturnInfo(data);
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(returnMap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(returnMap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		returnMap.put("ccuserlist", ccuserlist);
		return returnMap;
	}

	@Override
	public List<Map<String, Object>> getReturnMaterialList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getReturnMaterialList(data);
	}

	@Override
	public String updateReturnOrder(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("orderid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		if(String.valueOf(data.get("result")).equals("1")){
			boolean bool=true;
			List<Map<String, Object>> materialList=this.warehouseMapper.getReturnMaterialList(data);
			for(Map<String, Object> material:materialList){
				String [] materielids=(material.get("purchasematerielid")+"").split("_");
				float num=Float.parseFloat(material.get("num")+"");
				int num1=0;
				for(int i=0;i<materielids.length;i++){
					Map<String, Object> materielmap=new HashMap<String, Object>();
					materielmap.put("materielid", materielids[i]);
					materielmap=this.warehouseMapper.getMaterial(materielmap);
					num1+=Float.parseFloat(materielmap.get("stock")==null?"0":materielmap.get("stock")+"");
				}
				if(num>num1){
					bool=false;
					break;
				}
			}
			if(!bool){
				return "0";
			}
			
		}
		this.warehouseMapper.updateReturnOrder(data);
		
		if(String.valueOf(data.get("result")).equals("1")){
			//审核通过库存
			List<Map<String, Object>> materialList=this.warehouseMapper.getReturnMaterialList(data);
			for(Map<String, Object> material:materialList){
			

				String [] materielids=(material.get("purchasematerielid")+"").split("_");
				float num=Float.parseFloat(material.get("num")+"");
				double sumprice=Double.parseDouble(material.get("sumprice")+"");
				
				for(int i=0;i<materielids.length;i++){
					Map<String, Object> materielmap=new HashMap<String, Object>();
					materielmap.put("materielid", materielids[i]);
					materielmap=this.warehouseMapper.getMaterial(materielmap);
					if(materielmap != null && materielmap.size() > 0){
						float nownum=Float.parseFloat(materielmap.get("stock")==null?"0":materielmap.get("stock")+"");
						if(num >= nownum){
							Map<String, Object> purchasemap=new HashMap<String, Object>();
							purchasemap.put("materielid", materielids[i]);
							purchasemap.put("reducenum", nownum);
							purchasemap.put("residualmoney",0);
							this.warehouseMapper.reducePurchaseNum(purchasemap);
							sumprice=sumprice-Double.parseDouble(materielmap.get("residualmoney")==null || "".equals(materielmap.get("residualmoney"))?"0":materielmap.get("residualmoney")+"");
							num=num-nownum;
						}else if(num!=0){
							Map<String, Object> purchasemap=new HashMap<String, Object>();
							purchasemap.put("materielid", materielids[i]);
							purchasemap.put("reducenum", num);
							
							purchasemap.put("residualmoney",Double.parseDouble(materielmap.get("residualmoney")==null || "".equals(materielmap.get("residualmoney"))?"0":materielmap.get("residualmoney")+"")-sumprice);
							this.warehouseMapper.reducePurchaseNum(purchasemap);
							
							num=0;
						}
					}
				}
				
				/*Map<String, Object> purchasemap=new HashMap<String, Object>();
				//判断是否存在多个物料明细,如果多个分别还原，如果单个直接还原
				String materielid=material.get("purchasematerielid")!=null && !"".equals(material.get("purchasematerielid"))?material.get("purchasematerielid").toString():"";
				if(materielid.indexOf("_")>=0){
					String[] ary=materielid.split("_");
					String[] artnum=(material.get("sumnum")!=null && !"".equals(material.get("sumnum"))?material.get("sumnum").toString():"").split("_");
					if(artnum==null || artnum.length==0){
						continue;
					}
					int len=ary.length;
					for(int i=0;i<len;i++){
						//查询库存明细记录
						
					}
				}else{
					purchasemap.put("materielid", material.get("purchasematerielid"));
					purchasemap.put("addstock", Integer.parseInt(material.get("num")+""));
					purchasemap.put("residualmoney",material.get("sumprice"));
					this.warehouseMapper.updatePurchaseMaterial(purchasemap);	
				}*/
				
				
			}
		}
		return "1";
	}
	
	

	
	@Override
	public List<Map<String, Object>> getReportlossOrderList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getReportlossOrderList(data);
	}

	@Override
	public int getReportlossOrderListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getReportlossOrderListNum(data);
	}

	@Override
	public int getReportlossNumByCompany(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getReportlossNumByCompany(data);
	}

	@Override
	public String insertReportloss(Map<String, Object> data) {
		// TODO Auto-generated method stub
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
			remindmap.put("content",userInfo.get("realname")+"申请了一个报损单,请您审批");
			remindmap.put("linkurl", "warehouse/reportloss_check.html?orderid="+orderid);
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getReportlossInfo?orderid="+orderid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", orderid);
			remindmap.put("resourcetype", 5);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title=userInfo.get("realname")+"申请了一个报损单,请您审批";
				String url="/warehouse/reportloss_check.html?orderid="+orderid;
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
		forwordmap.put("resourcetype",5);
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
		forwordmap.put("resourcetype",5);
		forwordmap.put("createtime",new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
				
		String ccuserids = "";
		//添加抄送人到转发表
		if(data.containsKey("userlist") && !"".equals(data.get("userlist"))){
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
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",5);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname")+"申请了一个报损单,请查看！";
						String url="/warehouse/reportloss_detail.html?orderid="+orderid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.warehouseMapper.insertReportloss(data);
		return orderid;
	}

	@Override
	public void insertReportlossMaterial(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("materielid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime", new Date());
		data.put("delflag", 0);
		this.warehouseMapper.insertReportlossMaterial(data);
		//减采购的库存
		
		//减采购的库存
		/*String [] materielids=(data.get("purchasematerielid")+"").split("_");
		int num=Integer.parseInt(data.get("num")+"");
		double sumprice=Double.parseDouble(data.get("sumprice")+"");
		for(int i=0;i<materielids.length;i++){
			Map<String, Object> materielmap=new HashMap<String, Object>();
			materielmap.put("materielid", materielids[i]);
			materielmap=this.warehouseMapper.getMaterial(materielmap);
			if(materielmap != null && materielmap.size() > 0){
				int nownum=Integer.parseInt(materielmap.get("stock")==null?"0":materielmap.get("stock")+"");
				if(num >= nownum){
					Map<String, Object> purchasemap=new HashMap<String, Object>();
					purchasemap.put("materielid", materielids[i]);
					purchasemap.put("reducenum", nownum);
					purchasemap.put("residualmoney",0);
					this.warehouseMapper.reducePurchaseNum(purchasemap);
					sumprice=sumprice-Double.parseDouble(materielmap.get("residualmoney")==null || "".equals(materielmap.get("residualmoney"))?"0":materielmap.get("residualmoney")+"");
					num=num-nownum;
				}else if(num!=0){
					Map<String, Object> purchasemap=new HashMap<String, Object>();
					purchasemap.put("materielid", materielids[i]);
					purchasemap.put("reducenum", num);
					purchasemap.put("residualmoney",Double.parseDouble(materielmap.get("residualmoney")==null || "".equals(materielmap.get("residualmoney"))?"0":materielmap.get("residualmoney")+"")-sumprice);
					this.warehouseMapper.reducePurchaseNum(purchasemap);
					
					num=0;
				}
			}
			
		}*/
	}

	@Override
	public Map<String, Object> getReportlossInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String,Object> reportlossMap = this.warehouseMapper.getReportlossInfo(data);
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(reportlossMap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(reportlossMap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		reportlossMap.put("ccuserlist", ccuserlist);
		return reportlossMap;
	}

	@Override
	public List<Map<String, Object>> getReportlossMaterialList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getReportlossMaterialList(data);
	}

	@Override
	public String updateReportlossOrder(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("orderid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		if(String.valueOf(data.get("result")).equals("1")){
			boolean bool=true;
			List<Map<String, Object>> materialList=this.warehouseMapper.getReportlossMaterialList(data);
			for(Map<String, Object> material:materialList){
				String [] materielids=(material.get("purchasematerielid")+"").split("_");
				float num=Float.parseFloat(material.get("num")+"");
				float num1=0;
				for(int i=0;i<materielids.length;i++){
					Map<String, Object> materielmap=new HashMap<String, Object>();
					materielmap.put("materielid", materielids[i]);
					materielmap=this.warehouseMapper.getMaterial(materielmap);
					num1+=Float.parseFloat(materielmap.get("stock")==null?"0":materielmap.get("stock")+"");
				}
				if(num>num1){
					bool=false;
					break;
				}
			}
			if(!bool){
				return "0";
			}
			
		}
		
		
		
		this.warehouseMapper.updateReportlossOrder(data);
		if(String.valueOf(data.get("result")).equals("1")){
			//审核未通过还原库存
			List<Map<String, Object>> materialList=this.warehouseMapper.getReportlossMaterialList(data);
			for(Map<String, Object> material:materialList){
				
				
				String [] materielids=(material.get("purchasematerielid")+"").split("_");
				float num=Float.parseFloat(material.get("num")+"");
				double sumprice=Double.parseDouble(material.get("sumprice")+"");
				for(int i=0;i<materielids.length;i++){
					Map<String, Object> materielmap=new HashMap<String, Object>();
					materielmap.put("materielid", materielids[i]);
					materielmap=this.warehouseMapper.getMaterial(materielmap);
					if(materielmap != null && materielmap.size() > 0){
						float nownum=Float.parseFloat(materielmap.get("stock")==null?"0":materielmap.get("stock")+"");
						if(num >= nownum){
							Map<String, Object> purchasemap=new HashMap<String, Object>();
							purchasemap.put("materielid", materielids[i]);
							purchasemap.put("reducenum", nownum);
							purchasemap.put("residualmoney",0);
							this.warehouseMapper.reducePurchaseNum(purchasemap);
							sumprice=sumprice-Double.parseDouble(materielmap.get("residualmoney")==null || "".equals(materielmap.get("residualmoney"))?"0":materielmap.get("residualmoney")+"");
							num=num-nownum;
						}else if(num!=0){
							Map<String, Object> purchasemap=new HashMap<String, Object>();
							purchasemap.put("materielid", materielids[i]);
							purchasemap.put("reducenum", num);
							purchasemap.put("residualmoney",Double.parseDouble(materielmap.get("residualmoney")==null || "".equals(materielmap.get("residualmoney"))?"0":materielmap.get("residualmoney")+"")-sumprice);
							this.warehouseMapper.reducePurchaseNum(purchasemap);
							
							num=0;
						}
					}
					
				}
				
				/*Map<String, Object> purchasemap=new HashMap<String, Object>();
				purchasemap.put("materielid", material.get("purchasematerielid"));
				purchasemap.put("addstock", Integer.parseInt(material.get("num")+""));
				purchasemap.put("residualmoney", Double.parseDouble(material.get("price")+"")*Integer.parseInt(material.get("num")+""));
				this.warehouseMapper.updatePurchaseMaterial(purchasemap);*/
			}
		}
		return "1";
	}

	@Override
	public List<Map<String, Object>> getMaterialOrderByDate(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=this.warehouseMapper.getMaterialOrderByDate(data);
		for(Map<String, Object> order:list){
			data.put("createtime", order.get("createtime"));
			List<Map<String, Object>> orderlist=this.warehouseMapper.getMaterialByDate(data);
			order.put("orderlist", orderlist);
		}
		return list;
	}

	@Override
	public int getMaterialOrderByDateNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getMaterialOrderByDateNum(data);
	}

	@Override
	public List<Map<String, Object>> getReturnOrderByDate(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=this.warehouseMapper.getReturnOrderByDate(data);
		for(Map<String, Object> order:list){
			data.put("createtime", order.get("createtime"));
			List<Map<String, Object>> orderlist=this.warehouseMapper.getReturnByDate(data);
			order.put("orderlist", orderlist);
		}
		return list;
	}

	@Override
	public int getReturnOrderByDateNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getReturnOrderByDateNum(data);
	}

	@Override
	public List<Map<String, Object>> getReportlossOrderByDate(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=this.warehouseMapper.getReportlossOrderByDate(data);
		for(Map<String, Object> order:list){
			data.put("createtime", order.get("createtime"));
			List<Map<String, Object>> orderlist=this.warehouseMapper.getReportlossByDate(data);
			order.put("orderlist", orderlist);
		}
		return list;
	}

	@Override
	public int getReportlossOrderByDateNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getReportlossOrderByDateNum(data);
	}

	@Override
	public List<Map<String, Object>> getMaterialGroupList(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		
		List<Map<String, Object>> groupList=new ArrayList<Map<String,Object>>();
		//用料所占 type=1
		Map<String, Object> usedMater=this.warehouseMapper.getUsePrice(data);
		usedMater.put("type",1);
		usedMater.put("name","用料");
		//退货所占 type=2
		Map<String, Object> returnMater=this.warehouseMapper.getReturnPrice(data);
		returnMater.put("type", 2);
		returnMater.put("name","退货");
		//报损所占 type=3
		Map<String, Object> reportLossMater=this.warehouseMapper.getReportLossPrice(data);
		reportLossMater.put("type", 3);
		reportLossMater.put("name", "报损");
		
		//库存所在信息type=0
		Map<String, Object> stockMater=this.warehouseMapper.getStockPrice(data);
		stockMater.put("type", 4);
		stockMater.put("name", "库存");
		groupList.add(usedMater);
		groupList.add(returnMater);
		groupList.add(reportLossMater);
		groupList.add(stockMater);
		
		return groupList;
	}

	@Override
	public List<Map<String, Object>> getStockMaterialDetail(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		//用料使用物料
		if(data.containsKey("type") && String.valueOf(data.get("type")).equals("1")){
			List<Map<String, Object>> materlist=this.warehouseMapper.getUsedTypeList(data);
			return materlist;
		}
		//退货使用物料
		else if(data.containsKey("type") && String.valueOf(data.get("type")).equals("2")){
			List<Map<String, Object>> materlist=this.warehouseMapper.getReturnTypeList(data);
			return materlist;
			
		}
		//报损使用物料
		else if(data.containsKey("type") && String.valueOf(data.get("type")).equals("3")){
			List<Map<String, Object>> materlist=this.warehouseMapper.getReportLossTypeList(data);
			return materlist;
		}
		//库存
		else{
			List<Map<String, Object>> materlist=this.warehouseMapper.getPurchaseTypeList(data);
			return materlist;
		}
	}

	@Override
	public List<Map<String, Object>> getStockOrderByStatistics(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		//用料使用物料
		if(data.containsKey("type") && String.valueOf(data.get("type")).equals("1")){
			List<Map<String, Object>> list=this.warehouseMapper.getUsedOrderList(data);
			return list;
		}
		//退货使用物料
		else if(data.containsKey("type") && String.valueOf(data.get("type")).equals("2")){
			List<Map<String, Object>> list=this.warehouseMapper.getReturnOrder(data);
			return list;
			
		}
		//报损使用物料
		else if(data.containsKey("type") && String.valueOf(data.get("type")).equals("3")){
			List<Map<String, Object>> list=this.warehouseMapper.getReportLossOrderList(data);
			return list;
		}
		//库存
		else{
			List<Map<String, Object>> list=this.warehouseMapper.getPurchaseOrderList(data);
			return list;
		}
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
	public Map<String, Object> getSendNameByForadid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.warehouseMapper.getSendNameByForadid(map);
	} 
}
