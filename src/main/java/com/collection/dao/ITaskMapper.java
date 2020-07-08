package com.collection.dao;

import java.util.List;
import java.util.Map;

/**
 * 定时任务查询相关
 * @author silence
 *
 */
public interface ITaskMapper {
	/**
	 * 初始化获取所有的有效定时任务数据
	 * @param data
	 * @return
	 */
	List<Map<String, Object>> initialize();
	
	/**
	 * 获取定时任务
	 * @param taskid
	 * @return
	 */
	List<Map<String, Object>> getTasks(Map<String, Object> data);
	
	/**
	 * 修改定时任务
	 */
	void update(Map<String, Object> data);
	
	/**
	 * 定时任务
	 * @param data
	 * @return
	 */
	int add(Map<String, Object> data);
	
	/**
	 * 删除有效期大于等于新有效期和明天的任务表达式
	 * @param data
	 */
	void deleteCrontab(Map<String, Object> data);
	
	/**
	 * 修改前一任务表达式的失效日期和状态
	 * @param data
	 */
	void updateCrontab(Map<String, Object> data);
	
	/**
	 * 添加新的任务表达式 c_t_crontab
	 * @param data
	 */
	void addCrontab(Map<String, Object> data);
	
	/**
	 * 获取定时任务信息
	 * @param data
	 * @return
	 */
	Map<String, Object> getCrontab(Map<String, Object> data);
	
	/**
	 * 修改定时任务信息
	 * @param data
	 */
	void updateCrontabStatus(Map<String, Object> data);
}
