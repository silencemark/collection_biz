package com.collection.dao.oa;
import java.util.List;
import java.util.Map;
/**
 * 
 * 通知Mapper
 *
 */
public interface NoticeMapper {
	
	/**
	 * app 消息通知 列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getNoticeList(Map<String,Object> map);
	
	/**
	 * app 消息通知 总记录数
	 * @param map
	 * @return
	 */
	int getNoticeListCount(Map<String,Object> map);
	
	/**
	 * 新增消息通知基本信息 
	 * @param map
	 * @return
	 */
	int addNotice(Map<String,Object> map);
	/**
	 * 删除通知
	 * @param map
	 * @return
	 */
	void updatenotice(Map<String, Object> map);
	
 
}
