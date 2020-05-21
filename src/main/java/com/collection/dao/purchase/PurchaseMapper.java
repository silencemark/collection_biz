package com.collection.dao.purchase;
import java.util.List;
import java.util.Map;
/**
 * 采购管理
 * @author silence
 *
 */
public interface PurchaseMapper {
	/**
	 * 采购列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getPurchaseList(Map<String, Object> data);
	/**
	 * 采购数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getPurchaseListNum(Map<String, Object> data);
	/**
	 * 采购日期列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getPurchaseListByDate(Map<String, Object> data);
	/**
	 * 采购日期数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getPurchaseListByDateNum(Map<String, Object> data);
	/**
	 * 查询店铺
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getShopList(Map<String, Object> data);
	/**
	 * 添加采购(入库)单
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertPurchase(Map<String, Object> data);
	/**
	 * 添加采购物料
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertPurchaseMaterial(Map<String, Object> data);
	/**
	 * 采购(入库)单详细
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getPurchaseInfo(Map<String, Object> data);
	/**
	 * 采购物料列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getPurchaseMaterialList(Map<String, Object> data);
	List<Map<String, Object>> getPurchaseMaterialListnew(Map<String, Object> data);
	
	List<Map<String,Object>> getApplynameMaterialList(Map<String,Object> map);
	
	/**
	 * 采购统计 获取物料类别
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getMaterialType(Map<String, Object> data);

	/**
	 * 根据时间查询采购入库的总金额
	 * @param map
	 * @return
	 */
	Map<String,Object> getPurchasePayAmount(Map<String,Object> map);
	
	/**
	 * 采购评论列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getPurchaseCommentList(Map<String, Object> data);
	/**
	 * 评论数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getPurchaseCommentListNum(Map<String, Object> data);
	/**
	 * 添加转发信息
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertForword(Map<String, Object> data);
	/**
	 * 添加评论信息
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertComment(Map<String, Object> data);
	/**
	 * 审核 （修改）采购(入库)单
	 * @param data
	 * @return
	 * @author silence
	 */
	void updatePurchaseOrder(Map<String, Object> data);
	
	/* ------------------------------申购单 ---------------------------------------   */
	
	/**
	 * 申购列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getApplyOrderList(Map<String, Object> data);
	/**
	 * 申购数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getApplyOrderListNum(Map<String, Object> data);
	
	/**
	 * 添加申购单
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertApply(Map<String, Object> data);
	/**
	 * 添加申购物料
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertApplyMaterial(Map<String, Object> data);
	
	/**
	 * 申购单详细
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getApplyInfo(Map<String, Object> data);
	/**
	 * 申购物料列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getApplyMaterialList(Map<String, Object> data);
	/**
	 * 审核 （修改）申购单
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateApplyOrder(Map<String, Object> data);
	/**
	 * 获取物料明细
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getMaterialDetail(Map<String, Object> data);
	
	
	/**
	 * 获取物料明细数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getMaterialDetailNum(Map<String, Object> data);
	
	/**
	 * 查询采购(入库)单 根据物料
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getPurchaseStatisticsList(Map<String, Object> data);
	/**
	 * 查询采购(入库)单 根据物料 数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getPurchaseStatisticsListNum(Map<String, Object> data);
	/**
	 * 查询采购(入库)单今日 数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getPurchaseNumByCompany(Map<String, Object> data);
	/**
	 * 查询申购单今日 数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getApplyNumByCompany(Map<String, Object> data);
	/**
	 * 查询采购(入库)单时间 列表 根据物料/单价 
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getPurchaseDateByname(Map<String, Object> data);
	/**
	 * 查询采购(入库)单时间 列表 根据物料/单价  数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getPurchaseDateBynameNum(Map<String, Object> data);
	/**
	 * 根据时间查询采购(入库)单
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getOrderListByDate(Map<String, Object> data);
	
	/**
	 * 查询申购单时间 列表 已处理
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getApplyOrderByDate(Map<String, Object> data);
	/**
	 * 查询采购(入库)单时间 列表 已处理
	 * @param data
	 * @return
	 * @author silence
	 */
	int getApplyOrderByDateNum(Map<String, Object> data);
	
	/**
	 * 根据时间查询申购单
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getApplyListByDate(Map<String, Object> data);
	
	/**
	 * 根据时间查询采购(入库)单
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getPurchaseByDate(Map<String, Object> data);
	/**
	 * 查询今日库存
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getStockInfo(Map<String, Object> data);
	
	/**
	 * 查询查询库存的总金额
	 * @param map
	 * @return
	 */
	Map<String,Object> getStockPayAmountInfo(Map<String,Object> map);
	
	/**
	 * 查询新增申购单的采购类型
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getPurchaseTypeList(Map<String, Object> data);
	/**
	 * 查询新增采购单的采购类型
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> getApplyTypeList(Map<String, Object> data);
	Map<String,Object> getUsername(Map<String,Object> map);
}
