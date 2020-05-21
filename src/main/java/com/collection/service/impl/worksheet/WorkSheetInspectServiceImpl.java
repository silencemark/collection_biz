package com.collection.service.impl.worksheet;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IndexMapper;
import com.collection.dao.worksheet.WorkSheetInspectMapper;
import com.collection.service.worksheet.WorkSheetInspectService;

public class WorkSheetInspectServiceImpl implements WorkSheetInspectService{

	@Autowired
	WorkSheetInspectMapper workSheetInspectMapper;
	
	@Autowired
	IndexMapper indexMapper;
	
	@Override
	public List<Map<String, Object>> getInspectTemplateList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.workSheetInspectMapper.getInspectTemplateList(map);
//		for(Map<String,Object> mm : list){
//			Map<String,Object> param = new HashMap<String, Object>();
//			param.put("templateid", mm.get("templateid"));
//			List<Map<String,Object>> templatelist = this.getTemplateProjectList(param);
//			mm.put("templatelist", templatelist);
//		}
		return list;
	}

	@Override
	public int getInspectTemplateCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.workSheetInspectMapper.getInspectTemplateCount(map);
	}

	@Override
	public List<Map<String, Object>> getTemplateProjectList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.workSheetInspectMapper.getTemplateProjectList(map);
	}

	@Override
	public int getTemplateProjectCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.workSheetInspectMapper.getTemplateProjectCount(map);
	}
	
	@Override
	public String insertInspectTemplate(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String templateid = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("templateid", templateid);
		map.put("createtime", new Date());
		map.put("delflag", 0);
		this.workSheetInspectMapper.insertInspectTemplate(map);
		return templateid;
	}

	@Override
	public void insertTemplateProject(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("projectid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("createtime", new Date());
		map.put("delflag", 0);
		this.workSheetInspectMapper.insertTemplateProject(map);
	}

	@Override
	public void updateInspectTemplate(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.workSheetInspectMapper.updateInspectTemplate(map);
	}

	@Override
	public void updateTemplateProject(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.workSheetInspectMapper.updateTemplateProject(map);
	}

	/**
	 * 查询组织架构信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOrganizeList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.workSheetInspectMapper.getOrganizeList(map);
	}

	/**
	 * 查询用户所拥有的权限
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOrganizeListByUserid(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.workSheetInspectMapper.getOrganizeListByUserid(map);
	}

	/**
	 * 查询组织下的人员
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getUserInfoByOrganizeid(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.workSheetInspectMapper.getUserInfoByOrganizeid(map);
	}

	/**
	 * 删除发布范围信息
	 * @param map
	 */
	@Override
	public void deleteReleaseRangeInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.workSheetInspectMapper.deleteReleaseRangeInfo(map);
	}

	/**
	 * 查询组织下的人员的总数
	 * @param map
	 * @return
	 */
	@Override
	public int getUserInfoCountByOrganizeid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.workSheetInspectMapper.getUserInfoCountByOrganizeid(map);
	}

	/**
	 * 删除检查项目
	 * @param map
	 */
	public void deleteTemplateProjectInfo(Map<String,Object> map){
		// TODO Auto-generated method stub
		this.workSheetInspectMapper.deleteTemplateProjectInfo(map);
	}

	/**
	 * 查询默认的检查模板信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getDefaultTemplateList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.workSheetInspectMapper.getDefaultTemplateList(map);
	}

	/**
	 * 查询默认的检查项目信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getDefaultProjectList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.workSheetInspectMapper.getDefaultProjectList(map);
	}

	/**
	 * 查询组织架构的datacode
	 * @param map
	 * @return
	 */
	@Override
	public Map<String,Object> getOrganizeDataCodeInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.workSheetInspectMapper.getOrganizeDataCodeInfo(map);
	}

	/**
	 * 查询组织机构下面模板名称是否重复
	 * @param map
	 * @return
	 */
	@Override
	public int getTemplateNameIsExists(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.workSheetInspectMapper.getTemplateNameIsExists(map);
	}
}
