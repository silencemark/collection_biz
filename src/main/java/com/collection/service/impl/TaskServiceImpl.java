package com.collection.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.ITaskMapper;
import com.collection.service.ITaskService;
/**
 * 定时任务相关操作
 * @author silence
 *
 */
public class TaskServiceImpl implements ITaskService{

	@Autowired ITaskMapper taskMapper ;

	private Logger logger = Logger.getLogger(TaskServiceImpl.class);
	
	@Override
	public List<Map<String, Object>> initialize() {
		return taskMapper.initialize();
	}

	@Override
	public List<Map<String, Object>> getTasks(Map<String, Object> data) {
		return taskMapper.getTasks(data);
	}

	@Override
	public void update(Map<String, Object> data) {
		this.taskMapper.update(data);
	}

	@Override
	public int add(Map<String, Object> data) {
		this.taskMapper.add(data);
		if(data.get("taskid") != null) {
			logger.info("定时任务唯一taskid："+data.get("taskid"));
			return Integer.parseInt(data.get("taskid").toString());
		}
		return 0;
	}

	@Override
	public void updateCrontab(Map<String, Object> data) {
		//删除有效期大于等于新有效期和明天的任务表达式
		taskMapper.deleteCrontab(data);
		//修改前一任务表达式的失效日期和状态
		taskMapper.updateCrontab(data);
	}

	@Override
	public void addCrontab(Map<String, Object> data) {
		this.taskMapper.addCrontab(data);
	}

	@Override
	public Map<String, Object> getCrontab(Map<String, Object> data) {
		return this.taskMapper.getCrontab(data);
	}

	@Override
	public void updateCrontabStatus(Map<String, Object> data) {
		this.taskMapper.updateCrontabStatus(data);
	}
}
