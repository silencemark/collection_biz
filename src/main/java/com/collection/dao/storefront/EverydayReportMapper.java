package com.collection.dao.storefront;
import java.util.List;
import java.util.Map;
/**
 * 每日报表
 * @author silence
 *
 */
public interface EverydayReportMapper {
	
	/**
	 * 添加每日报表
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertEverydayReport(Map<String, Object> data);
	/**
	 * 每日报表列表（未处理）
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getEverydayReportList(Map<String, Object> data);
	/**
	 * 每日报表列表（已处理）
	 * @param data
	 * @return
	 * @author silence
	 */
	int getEverydayReportListNum(Map<String, Object> data);
	
	/**
	 * 每日报表时间列表（已处理）
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getEverydayReportByDate(Map<String, Object> data);
	/**
	 * 每日报表时间数量（已处理）
	 * @param data
	 * @return
	 * @author silence
	 */
	int getEverydayReportByDateNum(Map<String, Object> data);
	/**
	 * 每日报表列表（按时间查询）
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportByDate(Map<String, Object> data);
	/**
	 * 每日报表模版列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getEverydayReportModule(Map<String, Object> data);
	
	/**
	 * 每日报表字典列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getEverydayReportDictList(Map<String, Object> data);
	/**
	 * 添加每日报表扩展表
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertReportExtend(Map<String, Object> data);
	/**
	 * 每日报表详情
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getEverydayReportInfo(Map<String, Object> data);
	/**
	 * 每日报表扩展表列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getExtendList(Map<String,Object> data);
	/**
	 * 修改每日报表
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateEverydayReport(Map<String, Object> data);
	
	/**
	 * 查询营业额 收入
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getIncomeChartByConsume(Map<String, Object> data);
	
	/**
	 * 查询营业额 使用
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getIncomeChartByUse(Map<String, Object> data);
	/**
	 * 查询 统计客流量 数据
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportFlowChart(Map<String, Object> data);
	/**
	 * 查询 统计人均消费 数据
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportConsumptionChart(Map<String, Object> data);
	
	/**
	 * 查询每日报表的数据条数
	 * @param map
	 * @return
	 */
	int getReportConsumptionChartCount(Map<String,Object> map);
	
	/**
	 * 查询 报表 满意度
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportSatisfyChart(Map<String, Object> data);
	
	/**
	 * 查询顾客满意度的评价的次数
	 * @param map
	 * @return
	 */
	int getReportSatisfyChartCount(Map<String,Object> map);
	
	/**
	 * 查询 报表 出勤 人数
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportAttendanceChart(Map<String, Object> data);
	/**
	 * 查询 报表 出勤总 人数
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportAttendanceChartAll(Map<String, Object> data);
	
	/**
	 * 查询每日报表模版
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getReportModuleList(Map<String, Object> data);
	
	/**
	 * 查询所有报表字典类型信息
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getTypeList();
	
	/**
	 * 查询所有报表字典信息
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getDictList(Map<String, Object> data);
	/**
	 * 添加模版
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertTemplate(Map<String, Object> data); 
	
	/**
	 * 添加报表模版扩展表
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertReportTemplateExtend(Map<String, Object> data);
	
	/**
	 * 修改模版信息
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateTemplate(Map<String, Object> data);
	
	/**
	 * 查询模版信息
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getTemplateInfo(Map<String, Object> data);
	/**
	 * 查询模版扩展表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getTemplateExtend(Map<String, Object> data);
	
	/**
	 * 删除模版扩展表
	 * @param data
	 * @return
	 * @author silence
	 */
	void deleteTemplateExtend(Map<String, Object> data);
	
	/**
	 * 根据组织id查询组织信息
	 * @param map
	 * @return
	 */
	Map<String,Object> getOrganizeByOrganizeid(Map<String,Object> map);
	
	/**
	 * 查询今日每日报表
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getTodayReport(Map<String, Object> data);
	/**
	 * 查询上一次最后的编制人数
	 * @param data
	 * @return
	 * @author silence
	 */
	String getLastBianjiNum(Map<String, Object> data);
	
	/**
	 * 查询每日报表——每条数据唯一
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getEverydayReportListOnlyOne(Map<String,Object> map);
	
	/**
	 * 查询每日报表总数量--每条数据唯一
	 * @param map
	 * @return
	 */
	int getEverydayReportListNumOnlyOne(Map<String,Object> map);
}