package com.collection.dao.purchase;
import java.util.List;
import java.util.Map;
/**
 * 供应商管理
 * @author silence
 *
 */
public interface SupplierMapper {
	/**
	 * 供应商列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getSupplierList(Map<String, Object> data);
	/**
	 * 供应商数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getSupplierListNum(Map<String, Object> data);
	/**
	 * 供应商今日数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getSupplierNumByCompany(Map<String, Object> data);
	/**
	 * 添加供应商
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertSupplier(Map<String, Object> data);
	/**
	 * 查询供应商详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getSupplierInfo(Map<String, Object> data);
	/**
	 * 修改供应商
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateSupplier(Map<String, Object> data);
}
