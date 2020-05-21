package com.collection.dao.warehouse;
import java.util.List;
import java.util.Map;
/**
 * 仓库管理
 * @author silence
 *
 */
public interface WarehouseMapper {
	/**
	 * 用料列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getMaterialList(Map<String, Object> data);
	/**
	 * 用料数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getMaterialListNum(Map<String, Object> data);
	/**
	 * 添加用料单
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertMaterial(Map<String,Object> data);
	/**
	 * 添加用料单物料明细
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertUsedMaterial(Map<String,Object> data);
	/**
	 * 减库存
	 * @param data
	 * @return
	 * @author silence
	 */
	void reducePurchaseNum(Map<String,Object> data);
	
	/**
	 * 今日用料单数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getMaterialNumByCompany(Map<String, Object> data);
	/**
	 * 用料单详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getMaterialInfo(Map<String, Object> data);
	/**
	 * 用料单物料列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUsedMaterialList(Map<String, Object> data);
	/**
	 * 审核用料单 （修改）
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateMaterialOrder(Map<String, Object> data);
	/*---------------------------------------退货单--------------------------------------------------*/
	
	/**
	 * 退货单列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReturnOrderList(Map<String, Object> data);
	/**
	 *  退货单数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getReturnOrderListNum(Map<String, Object> data);
	/**
	 *  今日退货单数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getReturnNumByCompany(Map<String, Object> data);
	/**
	 * 添加退货单
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertReturn(Map<String,Object> data);
	/**
	 * 添加退货单物料明细
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertReturnMaterial(Map<String,Object> data);
	/**
	 * 用料单详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getReturnInfo(Map<String, Object> data);
	/**
	 * 用料单物料列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReturnMaterialList(Map<String, Object> data);
	/**
	 * 修改退货单
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateReturnOrder(Map<String,Object> data);
	
	/*---------------------------------------报损单--------------------------------------------------*/
	
	/**
	 * 退货单列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportlossOrderList(Map<String, Object> data);
	/**
	 *  退货单数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getReportlossOrderListNum(Map<String, Object> data);
	/**
	 *  今日退货单数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getReportlossNumByCompany(Map<String, Object> data);
	/**
	 * 添加退货单
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertReportloss(Map<String,Object> data);
	/**
	 * 添加退货单物料明细
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertReportlossMaterial(Map<String,Object> data);
	/**
	 * 退货单详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getReportlossInfo(Map<String, Object> data);
	/**
	 * 退货单物料列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportlossMaterialList(Map<String, Object> data);
	/**
	 * 审核（修改）退货单
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateReportlossOrder(Map<String,Object> data);
	/**
	 * 查询用料 （已处理）
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getMaterialOrderByDate(Map<String,Object> data);
	/**
	 * 查询用料 （已处理）数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getMaterialOrderByDateNum(Map<String, Object> data);
	/**
	 * 查询用料 按时间查询
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getMaterialByDate(Map<String,Object> data);
	
	/**
	 * 查询退货 （已处理）
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReturnOrderByDate(Map<String,Object> data);
	/**
	 * 查询退货 （已处理）数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getReturnOrderByDateNum(Map<String, Object> data);
	/**
	 * 查询退货 按时间查询
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReturnByDate(Map<String,Object> data);
	/**
	 * 查询报损 （已处理）
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportlossOrderByDate(Map<String,Object> data);
	/**
	 * 查询报损 （已处理）数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getReportlossOrderByDateNum(Map<String, Object> data);
	/**
	 * 查询报损 按时间查询
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportlossByDate(Map<String,Object> data);
	/**
	 * 修改库存
	 * @param data
	 * @return
	 * @author silence
	 */
	void updatePurchaseMaterial(Map<String, Object> data);
	
	/**
	 * 获取用料单的 金额
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getUsePrice(Map<String, Object> data);
	
	/**
	 * 获取退货单的 金额
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getReturnPrice(Map<String, Object> data);
	/**
	 * 获取报损单的  金额
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getReportLossPrice(Map<String, Object> data);
	
	/**
	 * 获取库存的  金额
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getStockPrice(Map<String, Object> data);
	/**
	 * 获取用料 使用的 类型及 金额
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUsedTypeList(Map<String, Object> data);
	/**
	 * 获取退货 使用的 类型及 金额
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReturnTypeList(Map<String, Object> data);
	/**
	 * 获取报损 使用的 类型及 金额
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportLossTypeList(Map<String, Object> data);
	/**
	 * 获取村口 剩余 类型及 金额
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getPurchaseTypeList(Map<String, Object> data);
	
	/**
	 * 获取用料 使用的 订单列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getUsedOrderList(Map<String, Object> data);
	
	/**
	 * 获取退货 使用的 订单列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReturnOrder(Map<String, Object> data);
	/**
	 * 获取报损 使用的 订单列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportLossOrderList(Map<String, Object> data);
	/**
	 * 获取库存 剩余 订单列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getPurchaseOrderList(Map<String, Object> data);
	/**
	 * 查询采购的物料相详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getMaterial(Map<String, Object> data);
	Map<String,Object> getSendNameByForadid(Map<String,Object> map);
}
