package com.collection.service.impl.managebackstage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.managebackstage.CloudCapacityMapper;
import com.collection.service.managebackstage.CloudCapacityService;


public class CloudCapacityServiceImpl implements CloudCapacityService{

	@Autowired CloudCapacityMapper cloudCapacityMapper;

	/**
	 * 查询扩容申请记录
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCloudCapacityList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.cloudCapacityMapper.getCloudCapacityList(map);
		for(Map<String,Object> mm : list){
			String memory = String.valueOf(mm.get("memory"));
			String sjmemory = String.valueOf(mm.get("sjmemory"));
			if(!"".equals(memory) && mm.containsKey("memory")){
				String memory_float = (((Float.parseFloat(memory)*100)/(1024*1024))/100)+"";
				int dian = memory_float.indexOf(".")+3;
				if(dian > memory_float.length()){
					dian = memory_float.length();
				}
				memory_float =  memory_float.substring(0,dian);
				mm.put("memory", memory_float+" G");
			}
			if(!"".equals(sjmemory) && mm.containsKey("sjmemory")){
				String sjmemory_float = (((Float.parseFloat(sjmemory)*100)/(1024*1024))/100)+"";
				int dian = sjmemory_float.indexOf(".")+3;
				if(dian > sjmemory_float.length()){
					dian = sjmemory_float.length();
				}
				sjmemory_float = sjmemory_float.substring(0,dian);
				mm.put("sjmemory", sjmemory_float+" G");
			}
		}
		return list;
	}

	/**
	 * 查询扩容申请的总数
	 * @param map
	 * @return
	 */
	@Override
	public int getCloudCapacityListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.cloudCapacityMapper.getCloudCapacityListCount(map);
	}

	/**
	 * 查询扩容申请详情
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getCloudCapacityDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String,Object> mm = this.cloudCapacityMapper.getCloudCapacityDetail(map);
		String memory = String.valueOf(mm.get("memory"));
		String sjmemory = String.valueOf(mm.get("sjmemory"));
		if(!"".equals(memory) && mm.containsKey("memory")){
			String memory_float = (((Float.parseFloat(memory)*100)/(1024*1024))/100)+"";
			int dian = memory_float.indexOf(".")+3;
			if(dian > memory_float.length()){
				dian = memory_float.length();
			}
			memory_float =  memory_float.substring(0,dian);
			mm.put("memory", memory_float+" G");
		}
		if(!"".equals(sjmemory) && mm.containsKey("sjmemory")){
			String sjmemory_float = (((Float.parseFloat(sjmemory)*100)/(1024*1024))/100)+"";
			int dian = sjmemory_float.indexOf(".")+3;
			if(dian > sjmemory_float.length()){
				dian = sjmemory_float.length();
			}
			sjmemory_float = sjmemory_float.substring(0,dian);
			mm.put("sjmemory", sjmemory_float+" G");
		}
		return mm;
	}

	/**
	 * 审核扩容申请
	 * @param map
	 */
	@Override
	public void updateCloudCapacityInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
		map.put("updatetime", new Date());
		String sjmemory = String.valueOf(map.get("sjmemory"));
		if(map.containsKey("sjmemory") && !"".equals(sjmemory)){
			int memory = Integer.parseInt(sjmemory)*1024*1024;
			map.put("sjmemory", memory);
			this.updateCompanyCloudInfo(map);
		}
		this.cloudCapacityMapper.updateCloudCapacityInfo(map);
	}

	/**
	 * 修改扩容数量
	 */
	@Override
	public void updateCompanyCloudInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("sjmemory", Integer.parseInt(String.valueOf(map.get("sjmemory"))));
		this.cloudCapacityMapper.updateCompanyCloudInfo(map);
	}
	
}
