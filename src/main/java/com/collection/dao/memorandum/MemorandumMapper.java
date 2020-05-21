package com.collection.dao.memorandum;
import java.util.List;
import java.util.Map;
/**
 * 备忘录管理
 * @author silence
 *
 */
public interface MemorandumMapper {
	/**
	 * 获取今天备忘录列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getMemorandumList(Map<String, Object> data);
	/**
	 * 获取所有的备忘录日期
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getAllMemorandumTime(Map<String, Object> data);
	/**
	 * 获取今天备忘录列表数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getMemorandumListNum(Map<String, Object> data);
	/**
	 * 添加备忘录
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertMemorandum(Map<String, Object> data);
	/**
	 * 添加分享
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertShare(Map<String, Object> data);
	/**
	 * 查询分享列表
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getShareList(Map<String, Object> data);
	/**
	 * 查询分享列表数量
	 * @param data
	 * @return
	 * @author silence
	 */
	int getShareListNum(Map<String, Object> data);
	/**
	 * 获取点赞信息
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getPraiseInfo(Map<String, Object> data);
	/**
	 * 添加点赞
	 * @param data
	 * @return
	 * @author silence
	 */
	void insertPraise(Map<String, Object> data);
	/**
	 * 修改点赞
	 * @param data
	 * @return
	 * @author silence
	 */
	void updatePraise(Map<String, Object> data);
	/**
	 * 修改点赞
	 * @param data
	 * @return
	 * @author silence
	 */
	void addNotreadNum(Map<String, Object> data);
	
	/**
	 * 清空未读数量
	 * @param map
	 */
	void shareNotReadClearNum(Map<String,Object> map);
	
	/**
	 * 查询备忘录信息
	 * @param data
	 * @return
	 * @author silence
	 */
	Map<String, Object> getMemorandumInfo(Map<String, Object> data);
	/**
	 * 修改备忘录信息
	 * @param data
	 * @return
	 * @author silence
	 */
	void updateMemorandumInfo(Map<String, Object> data);
	/**
	 * 查询所有1分钟之内的备忘录信息
	 * @param data
	 * @return
	 * @author silence
	 */
	List<Map<String, Object>> getAllMemorandumList();
	
	/**
	 * 某人未读的分享圈的总数
	 * @param map
	 * @return
	 */
	int getShareNotReadSumNum(Map<String,Object> map);
}
