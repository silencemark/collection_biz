package com.collection.service.impl.managebackstage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.managebackstage.CustomerFileMapper;
import com.collection.service.managebackstage.CustomerFileService;

public class CustomerFileServiceImpl implements CustomerFileService{

	@Autowired CustomerFileMapper customerFileMapper;
	
	/**
	 * 添加文件上传记录
	 * @param map
	 */
	@Override
	public void insertCustomerFileInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("fileid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("createtime", new Date());
		if(map.containsKey("size") && !"".equals(map.get("size"))){
			int size = Math.round(Integer.parseInt(String.valueOf(map.get("size")))/1000);
			map.put("size", size);
		}
		if(map.containsKey("type") && !"".equals(map.get("type")) && !"null".equals(map.get("type"))){
			map.put("type", Integer.parseInt(String.valueOf(map.get("type"))));
		}else{
			map.put("type",99);
		}
		this.customerFileMapper.insertCustomerFileInfo(map);
		
		//修改公司的磁盘空间大小
		Map<String,Object> paramMap = new HashMap<String, Object>();
		if(map.containsKey("type") && !"".equals(map.get("type"))){
			String filetype = String.valueOf(map.get("type"));
			if("7".equals(filetype)){
				paramMap.put("qywpdmemory", map.get("size"));
			}else{
				paramMap.put("shujudmemory", map.get("size"));
			}
		}
		paramMap.put("usedmemory", map.get("size"));
		paramMap.put("companyid", map.get("companyid"));
	}

}
