package com.collection.service.impl.memorandum;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IndexMapper;
import com.collection.dao.UserInfoMapper;
import com.collection.dao.memorandum.MemorandumMapper;
import com.collection.dao.purchase.PurchaseMapper;
import com.collection.dao.starassess.StarAssessMapper;
import com.collection.service.memorandum.MemorandumService;
import com.collection.service.purchase.PurchaseService;
import com.collection.service.starassess.StarAssessService;
/**
 * 备忘录管理
 * @author silence
 *
 */
public class MemorandumServiceImpl implements MemorandumService{

	@Autowired MemorandumMapper memorandumMapper;
	@Resource IndexMapper indexMapper;
	@Resource UserInfoMapper userinfoMapper;
	@Override
	public List<Map<String, Object>> getMemorandumList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.memorandumMapper.getMemorandumList(data);
	}
	@Override
	public int getMemorandumListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.memorandumMapper.getMemorandumListNum(data);
	}
	@Override
	public void insertMemorandum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("memorandumid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime", new Date());
		data.put("delflag", 0);
		this.memorandumMapper.insertMemorandum(data);
	}
	@Override
	public void insertShare(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String shareid=UUID.randomUUID().toString().replaceAll("-", "");
		data.put("shareid", shareid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		this.memorandumMapper.insertShare(data);
		
		//添加到图片表关系表
		String [] images=(String[]) data.get("images");
		if(images != null && images.length>0){
			for(int i=0;i<images.length;i++){
				Map<String, Object> filemap=new HashMap<String, Object>();
				filemap.put("fileid", UUID.randomUUID().toString().replaceAll("-",""));
				filemap.put("companyid", data.get("companyid"));
				filemap.put("resourceid", shareid);
				filemap.put("visiturl", images[i]);
				filemap.put("resourcetype", 26);
				filemap.put("type",1);
				filemap.put("delflag",0);
				filemap.put("createtime", new Date());
				this.indexMapper.insertfile(filemap);
			}
		}
		//添加到用户分享未读数表
		Map<String, Object> notreadmap=new HashMap<String, Object>();
		notreadmap.put("notreadid", UUID.randomUUID().toString().replaceAll("-", ""));
		notreadmap.put("shareid",shareid);
		notreadmap.put("num",0);
		notreadmap.put("createtime", new Date());
		notreadmap.put("delflag", 0);
		notreadmap.put("createid", data.get("createid"));
		this.indexMapper.insertShareNotread(notreadmap);
	}
	@Override
	public List<Map<String, Object>> getShareList(Map<String, Object> data) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> sharelist=this.memorandumMapper.getShareList(data);
		for(Map<String, Object> share:sharelist){
			//查询图片
			List<Map<String, Object>> imagelist=new ArrayList<Map<String,Object>>();
			Map<String, Object> imagemap=new HashMap<String, Object>();
			imagemap.put("resourceid", share.get("shareid"));
			imagemap.put("resourcetype", 26);
			imagelist=this.indexMapper.getFileList(imagemap);
			share.put("imagelist", imagelist);
			//查询点赞
			List<Map<String, Object>> praiselist=new ArrayList<Map<String,Object>>();
			Map<String, Object> praisemap=new HashMap<String, Object>();
			praisemap.put("shareid", share.get("shareid"));
			praisemap.put("ispraise", 1);
			praiselist=this.indexMapper.getPraiseList(praisemap);
			share.put("praiselist", praiselist);
			//查询评论
			List<Map<String, Object>> commentlist=new ArrayList<Map<String,Object>>();
			Map<String, Object> commentmap=new HashMap<String, Object>();
			commentmap.put("resourceid", share.get("shareid"));
			commentmap.put("resourcetype", 26);
			commentlist=this.indexMapper.getCommentList(commentmap);
			share.put("commentlist", commentlist);
		}
		return sharelist;
	}
	@Override
	public int getShareListNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.memorandumMapper.getShareListNum(data);
	}
	@Override
	public Map<String, Object> getPraiseInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.memorandumMapper.getPraiseInfo(data);
	}
	@Override
	public void insertPraise(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("createtime", new Date());
		data.put("ispraise", 1);
		data.put("delflag", 0);
		this.memorandumMapper.insertPraise(data);
	}
	@Override
	public void updatePraise(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.memorandumMapper.updatePraise(data);
	}
	@Override
	public void insertShareNotread(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//判断是否已有数据
		Map<String, Object> notreadmap=this.indexMapper.getNotreadMap(data);
		if(notreadmap == null || notreadmap.size() == 0){
			data.put("notreadid", UUID.randomUUID().toString().replaceAll("-", ""));
			data.put("createtime", new Date());
			data.put("delflag", 0);
			this.indexMapper.insertShareNotread(data);
		}
		//给其他的评论者未读数加1
		Map<String, Object> addNotread=new HashMap<String, Object>();
		
		addNotread.put("shareid", data.get("shareid"));
		addNotread.put("createid", data.get("createid"));
		this.memorandumMapper.addNotreadNum(addNotread);
	}
	@Override
	public Map<String, Object> getMemorandumInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.memorandumMapper.getMemorandumInfo(data);
	}
	@Override
	public void updateMemorandumInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		this.memorandumMapper.updateMemorandumInfo(data);
	}
	@Override
	public List<Map<String, Object>> getAllMemorandumTime(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.memorandumMapper.getAllMemorandumTime(data);
	}
	@Override
	public List<Map<String, Object>> getAllMemorandumList() {
		// TODO Auto-generated method stub
		return this.memorandumMapper.getAllMemorandumList();
	}
	
	/**
	 * 清空未读数量
	 * @param map
	 */
	@Override
	public void shareNotReadClearNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.memorandumMapper.shareNotReadClearNum(map);
	}
	@Override
	public int getShareNotReadSumNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.memorandumMapper.getShareNotReadSumNum(map);
	}
}
