package com.collection.service.impl.purchase;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.purchase.PurchaseMapper;
import com.collection.dao.purchase.SupplierMapper;
import com.collection.service.purchase.PurchaseService;
import com.collection.service.purchase.SupplierService;
/**
 * 供应商管理
 * @author silence
 *
 */
public class SupplierServiceImpl implements SupplierService{

	@Autowired SupplierMapper supplierMapper;

	@Override
	public List<Map<String, Object>> getSupplierList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.supplierMapper.getSupplierList(data);
	}

	@Override
	public int getSupplierListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.supplierMapper.getSupplierListNum(data);
	}

	@Override
	public int getSupplierNumByCompany(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.supplierMapper.getSupplierNumByCompany(data);
	}

	@Override
	public void insertSupplier(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("supplierid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime", new Date());
		data.put("delflag", 0);
		this.supplierMapper.insertSupplier(data);
	}

	@Override
	public Map<String, Object> getSupplierInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.supplierMapper.getSupplierInfo(data);
	}

	@Override
	public void updateSupplier(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.supplierMapper.updateSupplier(data);
	}
	
	
}
