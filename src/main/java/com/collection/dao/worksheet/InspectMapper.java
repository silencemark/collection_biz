package com.collection.dao.worksheet;
import java.util.List;
import java.util.Map;
/**
 * 检查管理
 * @author silence
 *
 */
public interface InspectMapper {
	/**
	 * 查询检查评分表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getInspectStarList(Map<String, Object> data);
	/**
	 * 添加检查评分
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertInspectStar(Map<String, Object> data);
	
	/**
	 * 查询检查模版
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getInspectTemplateList(Map<String, Object> data);
	/**
	 * 查询检查模版项目列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getTemplateProjectList(Map<String, Object> data);
	/**
	 * 添加离店检查
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertLeaveshop(Map<String, Object> data);
	/**
	 * 离店检查列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getLeaveShopList(Map<String, Object> data);
	/**
	 * 离店检查数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getLeaveShopListNum(Map<String, Object> data);
	
	/**
	 * 查询离店时间列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getLeaveShopListTimes(Map<String,Object> map);
	
	/**
	 * 查询离店检查时间列表总数
	 * @param map
	 * @return
	 */
	int getLeaveShopListTimesCount(Map<String,Object> map);
	
	/**
	 * 离店检查详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getLeaveShopInfo(Map<String, Object> data);
	/**
	 * 修改 离店检查 （抄送意见）
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateLeaveShop(Map<String, Object> data);
	
	/*---------------------------------------------餐前检查--------------------------------------------*/
	
	/**
	 * 添加餐前检查
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertBeforeMeal(Map<String, Object> data);
	/**
	 * 餐前检查列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getBeforeMealList(Map<String, Object> data);
	/**
	 * 餐前检查数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getBeforeMealListNum(Map<String, Object> data);
	
	/**
	 * 查询餐前检查列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getBeforeMealListTimes(Map<String,Object> map);
	
	/**
	 * 查询餐前检查列表总数
	 * @param map
	 * @return
	 */
	int getBeforeMealListTimesCount(Map<String,Object> map);
	
	/**
	 * 餐前检查详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getBeforeMealInfo(Map<String, Object> data);
	/**
	 * 修改 餐前检查 （抄送意见）
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateBeforeMeal(Map<String, Object> data);
	
	/*---------------------------------------------厨房检查--------------------------------------------*/
	
	/**
	 * 添加厨房检查
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertKitchen(Map<String, Object> data);
	/**
	 * 厨房检查列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getKitchenList(Map<String, Object> data);
	/**
	 * 厨房检查数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getKitchenListNum(Map<String, Object> data);
	
	/**
	 * 查询厨房检查时间列表信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getKitchenListTimes(Map<String,Object> map);
	
	/**
	 * 查询厨房检查时间列表信息的总数
	 * @param map
	 * @return
	 */
	int getKitchenListTimesCount(Map<String,Object> map);
	
	/**
	 * 厨房检查详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getKitchenInfo(Map<String, Object> data);
	/**
	 * 修改 厨房检查 （抄送意见）
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateKitchen(Map<String, Object> data);
	
	/**
	 * 查询餐前检查数据
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getReportPrecheckInfo(Map<String,Object> map);
	
	/**查询厨房检查数据
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getReportKitChenCheckInfo(Map<String,Object> map);
	
	/**
	 * 查询离店报告数据
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getReportLeaveShopInfo(Map<String,Object> map);
	
	/**
	 * 查询厨房检查表单--表单唯一
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getKitchenListOnlyOne(Map<String,Object> map);
	
	/**
	 * 查询厨房检查单总数--表单唯一
	 * @param map
	 * @return
	 */
	int getKitchenListNumOnlyOne(Map<String,Object> map);
	
	/**
	 * 查询离店报告单--表单唯一
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getLeaveShopListOnlyOne(Map<String,Object> map);
	
	/**
	 * 查询离店报告单总数-=表单唯一
	 * @param map
	 * @return
	 */
	int getLeaveShopListNumOnlyOne(Map<String,Object> map);
	
	/**
	 * 查询餐前检查表单--表单唯一
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getBeforeMealListOnlyOne(Map<String,Object> map);
	
	/**
	 * 查询餐前检查表单--表单唯一
	 * @param map
	 * @return
	 */
	int getBeforeMealListNumOnlyOne(Map<String,Object> map);
}
