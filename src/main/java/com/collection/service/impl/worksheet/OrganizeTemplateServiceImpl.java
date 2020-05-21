package com.collection.service.impl.worksheet;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.worksheet.OrganizeTemplateMapper;
import com.collection.service.worksheet.OrganizeTemplateService;

public class OrganizeTemplateServiceImpl implements OrganizeTemplateService{

	@Autowired
	OrganizeTemplateMapper organizeTemplateMapper;
	
	@Override
	public String insertOrganizeTemplateInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String templateid = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("templateid", templateid);
		map.put("delflag", 0);
		map.put("createtime", new Date());
		this.organizeTemplateMapper.insertOrganizeTemplateInfo(map);
		
		return templateid;
	}

	@Override
	public String insertOrganizeTemplateProjectInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String projectid = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("projectid", projectid);
		map.put("delflag", 0);
		map.put("createtime", new Date());
		this.organizeTemplateMapper.insertOrganizeTemplateProjectInfo(map);
		
		return projectid;
	}

	@Override
	public void updateOrganizeTemplateInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		this.organizeTemplateMapper.updateOrganizeTemplateInfo(map);
	}

	@Override
	public void updateOrganizeTemplateProjectInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		this.organizeTemplateMapper.updateOrganizeTemplateProjectInfo(map);
	}

	@Override
	public List<Map<String, Object>> getOrganizeTemplateListInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.organizeTemplateMapper.getOrganizeTemplateListInfo(map);
	}

	@Override
	public List<Map<String, Object>> getOrganizeTemplateProjectListInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.organizeTemplateMapper.getOrganizeTemplateProjectListInfo(map);
	}

	@Override
	public int getOrganizeTemplateMaxPriority(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.organizeTemplateMapper.getOrganizeTemplateMaxPriority(map);
	}

	@Override
	public int getOrganizeTemplateProjectMaxPriority(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.organizeTemplateMapper.getOrganizeTemplateProjectMaxPriority(map);
	}

	/**
	 * 查询模板名称是否重复
	 * @param map
	 * @return
	 */
	@Override
	public int getOrganizeTemplateNameIsExsit(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.organizeTemplateMapper.getOrganizeTemplateNameIsExsit(map);
	}

}
