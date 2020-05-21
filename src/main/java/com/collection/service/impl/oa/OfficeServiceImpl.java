package com.collection.service.impl.oa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.IndexMapper;
import com.collection.dao.UserInfoMapper;
import com.collection.dao.oa.OfficeMapper;
import com.collection.dao.personal.PersonalMapper;
import com.collection.service.oa.OfficeService;
import com.collection.util.Constants;

/**
 * oa办公管理
 * 
 * @author silence
 *
 */
public class OfficeServiceImpl implements OfficeService {

	@Autowired
	OfficeMapper officeMapper;
	@Autowired
	IndexMapper indexMapper;
	@Autowired
	UserInfoMapper userinfoMapper;
	@Autowired PersonalMapper personalMapper;

	@Override
	public void insertCompanyModule(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("moduleid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("createtime", new Date());
		map.put("delflag", 0);
		this.officeMapper.insertCompanyModule(map);
	}

	@Override
	public List<Map<String, Object>> getCompanyModuleList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> modulelist = this.officeMapper
				.getCompanyModuleList(map);
		for (Map<String, Object> module : modulelist) {
			Map<String, Object> briefmap = new HashMap<String, Object>();
			briefmap.put("resourcetype", 7);
			briefmap.put("moduleid", module.get("moduleid"));
			briefmap.put("userid", map.get("userid"));
			briefmap.put("isread", 0);
			int briefnum = this.officeMapper.getBriefNotreadNum(briefmap);
			module.put("briefnum", briefnum);
		}
		return modulelist;
	}

	/**
	 * 查询企业简报模板列表总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getCompanyModuleListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getCompanyModuleListCount(map);
	}

	/**
	 * 修改企业简报栏目信息
	 * 
	 * @param map
	 */
	@Override
	public void updateCompanyModuleInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.officeMapper.updateCompanyModuleInfo(map);
	}

	@Override
	public String insertBrief(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String briefid = UUID.randomUUID().toString().replaceAll("-", "");
		data.put("briefid", briefid);
		data.put("delflag", 0);
		data.put("createtime", new Date());
		this.officeMapper.insertBrief(data);

		Map<String, Object> releaserangemap = new HashMap<String, Object>();
		releaserangemap.put("id",
				UUID.randomUUID().toString().replaceAll("-", ""));
		releaserangemap.put("delflag", 0);
		releaserangemap.put("createtime", new Date());
		releaserangemap.put("isread", 1);
		releaserangemap.put("companyid", data.get("companyid"));
		releaserangemap.put("resourceid", briefid);
		releaserangemap.put("resourcetype", 7);
		releaserangemap.put("userid", data.get("createid"));
		releaserangemap.put("createid", data.get("createid"));
		this.indexMapper.insertReleaseRangeUser(releaserangemap);

		return briefid;
	}

	@Override
	public List<Map<String, Object>> getBriefList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getBriefList(map);
	}

	@Override
	public int getBriefListNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getBriefListNum(map);
	}

	@Override
	public Map<String, Object> getBriefInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String, Object> briefInfo = this.officeMapper.getBriefInfo(map);
		// 查询发布范围
		Map<String, Object> rangemap = new HashMap<String, Object>();
		rangemap.put("resourcetype", 7);
		rangemap.put("resourceid", map.get("briefid"));
		List<Map<String, Object>> rangelist = this.indexMapper
				.getRangeList(rangemap);
		briefInfo.put("rangelist", rangelist);
		return briefInfo;
	}

	/**
	 * 修改企业简报信息
	 */
	@Override
	public void updateBriefInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.officeMapper.updateBriefInfo(map);
	}

	@Override
	public List<Map<String, Object>> getSystemCloudList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.officeMapper.getSystemCloudList(map);
		for(Map<String,Object> filemap : list){
			String fileurl = String.valueOf(filemap.get("fileurl"));
			String filetype = String.valueOf(filemap.get("filetype"));
			if("1".equals(filetype)){
				if(filemap.containsKey("fileurl") && !"".equals(fileurl)){
					String icon = queryfiletype(fileurl);
					filemap.put("file_icon", icon);
				}else{
					filemap.put("file_icon", queryfiletype(""));
				}
			}else{
				filemap.put("file_icon", "/userbackstage/images/public/file.png");
			}
		}
		return list;
	}
	
	public String queryfiletype(String url){
		String icon = "";
		String [] typecount = {".wmv.asf.asx.rm. rmvb.mpg.mpeg.mpe.3gp.mov.mp4.m4v.avi.dat.mkv.flv.vob",
				".jpeg.tiff.psd.png.swf.svg.pcx.dxf.wmf.emf.lic.eps.tga.bmp.jpg.jpeg2000.gif",
				".ascll.mime.txt.wtf.rtf.pdf.xml.odf.wtf",
				".0.000.001.7z.ace.ain.alz.apz.ar.arc.ari.arj.axx.bh.bhx.boo.bz.bza.bz2.bzip2.c00.c01.c02.cab.car.cbr.cbz.cp9.cpgz.cpt.dar.dd.deb.dgc.dist.ecs.efw.f.fdp.gca.gz.gzi.gzip.ha.hbc.hbc2.hbe.hki.hki1.hki2.hki3.hpk.hyp.ice.imp.ipg.ipk.ish.j.jar.jgz.jic.kgb.kz.lbr.lha.lnx.lqr.lzh.lzm.lzma.lzo.lzx.md.mint.mou.mpkg.mzp.nz.p7m.package.pae.pak.paq6.paq7.paq8.par.par2.pbi.pcv.pea.pf.pim.pit.piz.pkg.pup.pup.puz.pwa.qda.r00.r01.r02.r03.rar.rk.rnc.rpm.rte.rz.rzs.s00.s01.s02.s7z.sar.sdn.sea.sfs.sfx.sh.shar.shk.shr.sit.sitx.spt.sqx.sqz.tar.tbz2.tgz.tlz.uc2.uha.uue.vsi.wad.war.wot.xef.xez.xpi.xx.xxe.y.yz.z.zap.zfsendtotarget.zip.zipx.zix.zoo.zz.exe",
				".docx.dotx.doc.dot.docm.dotm",
				".xlsx.xlsm.xlsb.xlam.xls",
				"ppt,pptx"};
		
		String [] type_icon_url = {"/managebackstage/type_icon/shipin.png",
				"/managebackstage/type_icon/photo.png",
				"/managebackstage/type_icon/text.png",
				"/managebackstage/type_icon/yasuobao.png",
				"/managebackstage/type_icon/word.png",
				"/managebackstage/type_icon/excel.png",
				"/managebackstage/type_icon/ppt.png",
				"/managebackstage/type_icon/weizhi.png"};
		if(url != null && url != ""){
			String type = url.substring(url.indexOf("."), url.length());
			icon = type_icon_url[7];
			for(int i=0;i<7;i++){
				int tp  = typecount[i].indexOf(type);
				if(tp >= 0){
					icon = type_icon_url[i];
					break;
				}
			}
		}else{
			icon = type_icon_url[7];
		}
		return icon;
	}

	@Override
	public int getSystemCloudListNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getSystemCloudListNum(map);
	}

	@Override
	public void insertCompanyCloudModule(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("moduleid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("createtime", new Date());
		map.put("delflag", 0);
		this.officeMapper.insertCompanyCloudModule(map);
	}

	@Override
	public List<Map<String, Object>> getCompanyCloudModuleList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getCompanyCloudModuleList(map);
	}

	/**
	 * 查询企业文件夹总数量
	 * 
	 * @param map
	 * @return
	 */
	public int getCompanyCloudModuleListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getCompanyCloudModuleListCount(map);
	}

	@Override
	public List<Map<String, Object>> getCompanyCloudList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getCompanyCloudList(map);
	}

	@Override
	public int getCompanyCloudListNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getCompanyCloudListNum(map);
	}

	@Override
	public List<Map<String, Object>> getUserBirthdayList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> userlist = this.officeMapper
				.getUserBirthdayList(map);
		for (Map<String, Object> user : userlist) {
			Map<String, Object> userInfo = new HashMap<String, Object>();
			userInfo.put("userid", user.get("userid"));
			List<Map<String, Object>> organizelist = indexMapper
					.getOrganizeListByUser(userInfo);
			user.put("organizelist", organizelist);
		}
		return userlist;
	}

	@Override
	public int getUserBirthdayListNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getUserBirthdayListNum(map);
	}

	@Override
	public List<Map<String, Object>> getUserRewardList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getUserRewardList(map);
	}

	@Override
	public int getUserRewardListNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getUserRewardListNum(map);
	}

	@Override
	public String insertReserveAmount(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String reserveamountid = UUID.randomUUID().toString()
				.replaceAll("-", "");
		data.put("reserveamountid", reserveamountid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		data.put("status", 0);

		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo = this.userinfoMapper.getUserInfo(userInfo);
		// 添加到审批提醒表
		if (userInfo != null && userInfo.size() > 0) {
			Map<String, Object> remindmap = new HashMap<String, Object>();
			remindmap.put("remindid",
					UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid", data.get("companyid"));
			remindmap.put("userid", data.get("examineuserid"));
			remindmap.put("title", "审批");
			remindmap.put("content", userInfo.get("realname")+ "申请了一个备用金申请,请您审批");
			remindmap.put("linkurl", "oa/reserve_check.html?reserveamountid="+reserveamountid+"&userid="+data.get("examineuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getPcOaReserveDetail?reserveamountid="+reserveamountid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", reserveamountid);
			remindmap.put("resourcetype", 20);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			String level = String.valueOf(data.get("urgentlevel"));
			if(!"1".equals(level)){
				String jinji = "紧急";
				if("3".equals(level)){
					jinji = "非常紧急";
				}
				try {
					//推送信息
					String userid=data.get("examineuserid")+"";
					String title=userInfo.get("realname")+ "申请了一个"+jinji+"备用金申请,请您审批";
					String url="/oa/reserve_check.html?reserveamountid="+reserveamountid+"&userid="+userid;
					JPushAndriodAndIosMessage(userid, title, url);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		// 添加自己到转发表
		Map<String, Object> forwordmap = new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString()
				.replaceAll("-", ""));
		forwordmap.put("companyid", data.get("companyid"));
		forwordmap.put("resourceid", reserveamountid);
		forwordmap.put("receiveid", data.get("createid"));
		forwordmap.put("createid", data.get("createid"));
		forwordmap.put("isread", 1);
		forwordmap.put("resourcetype", 20);
		forwordmap.put("createtime", new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);

		// 添加审批人转发表
		forwordmap = new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString()
				.replaceAll("-", ""));
		forwordmap.put("companyid", data.get("companyid"));
		forwordmap.put("resourceid", reserveamountid);
		forwordmap.put("receiveid", data.get("examineuserid"));
		forwordmap.put("createid", data.get("createid"));
		forwordmap.put("isread", 0);
		forwordmap.put("resourcetype", 20);
		forwordmap.put("createtime", new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);

		String ccuserids = "";
		//添加抄送人到转发表
		if(data.containsKey("userlist") && !"".equals(data.get("userlist"))){
			JSONObject json=JSONObject.fromObject(data.get("userlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String,Object> user : userlist){
				try {
					String userid=String.valueOf(user.get("userid"));
					if(!userid.equals(data.get("examineuserid")) && !userid.equals(data.get("createid"))){
						forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",data.get("companyid"));
						forwordmap.put("resourceid",reserveamountid);
						forwordmap.put("receiveid",user.get("userid"));
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",20);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						
						String level = String.valueOf(data.get("urgentlevel"));
						if(!"1".equals(level)){
							String jinji = "紧急";
							if("3".equals(level)){
								jinji = "非常紧急";
							}
							//推送信息
							String title=userInfo.get("realname")+ "申请了一个"+jinji+"备用金申请,请查看！";
							String url="/oa/reserve_detail.html?reserveamountid="+reserveamountid+"&userid="+userid;
							JPushAndriodAndIosMessage(userid, title, url);
						}
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.officeMapper.insertReserveAmount(data);
		return reserveamountid;
	}

	@Override
	public List<Map<String, Object>> getReserveAmountList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getReserveAmountList(map);
	}

	@Override
	public int getReserveAmountListNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getReserveAmountListNum(map);
	}

	@Override
	public Map<String, Object> getReserveAmountInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String,Object> reserveAmountMap = this.officeMapper.getReserveAmountInfo(data);
		List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
		if(reserveAmountMap.containsKey("CCuserids")){
			String ccuserids = String.valueOf(reserveAmountMap.get("CCuserids"));
			String [] userids = ccuserids.split(",");
			for(String userid : userids){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userid", userid);
				Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
				ccuserlist.add(usermap);
			}
		}
		reserveAmountMap.put("ccuserlist", ccuserlist);
		return reserveAmountMap;
	}

	@Override
	public void updateReserveAmount(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("reserveamountid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		this.officeMapper.updateReserveAmount(data);
	}

	@Override
	public String insertTask(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String taskid = UUID.randomUUID().toString().replaceAll("-", "");
		data.put("taskid", taskid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		data.put("status", 0);
		this.officeMapper.insertTask(data);

		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo = this.userinfoMapper.getUserInfo(userInfo);
		// 添加到审批提醒表
		if (userInfo != null && userInfo.size() > 0) {
			Map<String, Object> remindmap = new HashMap<String, Object>();
			remindmap.put("remindid",
					UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid", data.get("companyid"));
			remindmap.put("userid", data.get("examineuserid"));
			remindmap.put("title", "审批");
			remindmap.put("content", userInfo.get("realname")+ "给您下达了一个任务,请您尽快处理");
			remindmap.put("linkurl", "oa/task_detail.html?taskid="+taskid+"&userid="+data.get("examineuserid")+"&cple=yes");
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getPcOaTaskDetail?taskid="+taskid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", taskid);
			remindmap.put("resourcetype", 19);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title= userInfo.get("realname")+ "给您下达了一个任务,请您尽快处理";
				String url="/oa/task_detail.html?taskid="+taskid+"&userid="+userid+"&cple=yes";
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// 添加自己到转发表
		Map<String, Object> forwordmap = new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString()
				.replaceAll("-", ""));
		forwordmap.put("companyid", data.get("companyid"));
		forwordmap.put("resourceid", taskid);
		forwordmap.put("receiveid", data.get("createid"));
		forwordmap.put("createid", data.get("createid"));
		forwordmap.put("isread", 1);
		forwordmap.put("resourcetype", 19);
		forwordmap.put("createtime", new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);

		// 添加审批人转发表
		forwordmap = new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString()
				.replaceAll("-", ""));
		forwordmap.put("companyid", data.get("companyid"));
		forwordmap.put("resourceid", taskid);
		forwordmap.put("receiveid", data.get("examineuserid"));
		forwordmap.put("createid", data.get("createid"));
		forwordmap.put("isread", 0);
		forwordmap.put("resourcetype", 19);
		forwordmap.put("createtime", new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);

		return taskid;
	}

	@Override
	public void insertTaskAssist(Map<String, Object> data) {
		// TODO Auto-generated method stub
		// 添加协办人员到转发表
		Map<String, Object> forwordmap = new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString()
				.replaceAll("-", ""));
		forwordmap.put("companyid", data.get("companyid"));
		forwordmap.put("resourceid", data.get("taskid"));
		forwordmap.put("receiveid", data.get("assistuserid"));
		forwordmap.put("createid", data.get("createid"));
		forwordmap.put("isread", 0);
		forwordmap.put("resourcetype", 19);
		forwordmap.put("createtime", new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);

		data.put("assistid", UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime", new Date());
		data.put("delflag", 0);
		// data.put("isread", 0);
		// data.put("status", 2);
		
		this.officeMapper.insertTaskAssist(data);
		
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo = this.userinfoMapper.getUserInfo(userInfo);
		// 添加到审批提醒表
		if (userInfo != null && userInfo.size() > 0) {
			
			try {
				//推送信息
				String userid=data.get("assistuserid")+"";
				String title= userInfo.get("realname")+ "下达了一个任务需要您协助,请您尽快处理";
				String url="/oa/task_detail.html?taskid="+data.get("taskid")+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	@Override
	public List<Map<String, Object>> getTaskList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> tasklist = this.officeMapper.getTaskList(map);
		for (Map<String, Object> task : tasklist) {
			List<Map<String, Object>> assistlist = this.officeMapper
					.getTaskAssisList(task);
			task.put("assistlist", assistlist);
		}
		return tasklist;
	}

	@Override
	public int getTaskListNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getTaskListNum(map);
	}

	/**
	 * 查询任务的时间的列表
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getTaskTimeList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = this.officeMapper.getTaskTimeList(map);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("companyid", map.get("companyid"));
		param.put("userid", map.get("userid"));
		if (map.containsKey("isoverdue") && !"".equals(map.get("isoverdue"))) {
			param.put("isoverdue", 1);
			param.put("status", 0);
		} else {
			param.put("status", 1);
		}
		for (Map<String, Object> mm : list) {
			param.put("starttime", mm.get("createtime"));
			param.put("endtime", mm.get("createtime"));
			List<Map<String, Object>> tasklist = this.getTaskList(param);
			mm.put("tasklist", tasklist);
		}
		return list;
	}

	/**
	 * 查询任务时间列表的总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getTaskTimsListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getTaskTimsListCount(map);
	}

	@Override
	public Map<String, Object> getTaskInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> taskInfo = this.officeMapper.getTaskInfo(data);

		Map<String, Object> rangemap = new HashMap<String, Object>();
		rangemap.put("resourceid", data.get("taskid"));
		// 查询图片和语音
		rangemap.put("resourcetype", 19);
		List<Map<String, Object>> filelist = this.indexMapper
				.getFileList(rangemap);

		List<Map<String, Object>> assistlist = this.officeMapper
				.getTaskAssisList(data);
		taskInfo.put("assistlist", assistlist);

		taskInfo.put("filelist", filelist);
		return taskInfo;
	}

	@Override
	public void updateTask(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("taskid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		this.officeMapper.updateTask(data);
		List<Map<String, Object>> assistlist = this.officeMapper
				.getTaskAssisList(data);
		for(Map<String,Object> mm : assistlist){
			try {
				//推送信息
				String userid=mm.get("userid")+"";
				String title= "您协助的任务已完成";
				String url="/oa/task_detail.html?taskid="+data.get("taskid")+"&userid="+userid+"&cple=success";
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		try {
			//推送信息
			String userid=data.get("createid")+"";
			String title= "您发布的任务已完成";
			String url="/oa/task_detail.html?taskid="+data.get("taskid")+"&userid="+userid+"&cple=success";
			JPushAndriodAndIosMessage(userid, title, url);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public String insertLeave(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String leaveid = UUID.randomUUID().toString().replaceAll("-", "");
		data.put("leaveid", leaveid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		data.put("status", 0);

		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo = this.userinfoMapper.getUserInfo(userInfo);
		// 添加到审批提醒表
		if (userInfo != null && userInfo.size() > 0) {
			Map<String, Object> remindmap = new HashMap<String, Object>();
			remindmap.put("remindid",
					UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid", data.get("companyid"));
			remindmap.put("userid", data.get("examineuserid"));
			remindmap.put("title", "审批");
			remindmap.put("content", userInfo.get("realname") + "申请了一个请假,请您审批");
			remindmap.put("linkurl", "oa/rest_check.html?leaveid="+leaveid+"&userid="+data.get("examineuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getPcOaRestDetail?leaveid="+leaveid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", leaveid);
			remindmap.put("resourcetype", 18);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title=userInfo.get("realname") + "申请了一个请假,请您审批";
				String url="/oa/rest_check.html?leaveid="+leaveid+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// 添加自己到转发表
		Map<String, Object> forwordmap = new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString()
				.replaceAll("-", ""));
		forwordmap.put("companyid", data.get("companyid"));
		forwordmap.put("resourceid", leaveid);
		forwordmap.put("receiveid", data.get("createid"));
		forwordmap.put("createid", data.get("createid"));
		forwordmap.put("isread", 1);
		forwordmap.put("resourcetype", 18);
		forwordmap.put("createtime", new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);

		// 添加审批人转发表
		forwordmap = new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString()
				.replaceAll("-", ""));
		forwordmap.put("companyid", data.get("companyid"));
		forwordmap.put("resourceid", leaveid);
		forwordmap.put("receiveid", data.get("examineuserid"));
		forwordmap.put("createid", data.get("createid"));
		forwordmap.put("isread", 0);
		forwordmap.put("resourcetype", 18);
		forwordmap.put("createtime", new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);

		String ccuserids = "";
		//添加抄送人到转发表
		if(data.containsKey("userlist") && !"".equals(data.get("userlist"))){
			JSONObject json=JSONObject.fromObject(data.get("userlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String,Object> user : userlist){
				try {
					String userid=String.valueOf(user.get("userid"));
					if(!userid.equals(data.get("examineuserid")) && !userid.equals(data.get("createid"))){
						forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",data.get("companyid"));
						forwordmap.put("resourceid",leaveid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",18);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname") + "申请了一个请假,请查看！";
						String url="/oa/rest_detail.html?leaveid="+leaveid+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.officeMapper.insertLeave(data);
		return leaveid;
	}

	@Override
	public List<Map<String, Object>> getLeaveList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getLeaveList(map);
	}

	@Override
	public int getLeaveListNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getLeaveListNum(map);
	}

	/**
	 * 查询请假单时间列表信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getLeaveTimesList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = this.officeMapper
				.getLeaveTimesList(map);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("companyid", map.get("companyid"));
		param.put("userid", map.get("userid"));
		param.put("status", map.get("status"));
		for (Map<String, Object> mm : list) {
			param.put("starttime", mm.get("createtime"));
			param.put("endtime", mm.get("createtime"));
			List<Map<String, Object>> leavelist = this.getLeaveList(param);
			mm.put("leavelist", leavelist);
		}
		return list;
	}

	/**
	 * 查询请假单时间列表的总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getLeaveTimesCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getLeaveTimesCount(map);
	}

	@Override
	public Map<String, Object> getLeaveInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> leaveinfo = this.officeMapper.getLeaveInfo(data);
		if (leaveinfo != null && leaveinfo.size() > 0) {
			// 查询文件
			List<Map<String, Object>> filelist = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resourceid", leaveinfo.get("leaveid"));
			map.put("resourcetype", 18);
			filelist = this.indexMapper.getFileList(map);
			leaveinfo.put("filelist", filelist);
			
			List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
			if(leaveinfo.containsKey("CCuserids")){
				String ccuserids = String.valueOf(leaveinfo.get("CCuserids"));
				String [] userids = ccuserids.split(",");
				for(String userid : userids){
					Map<String,Object> paramMap = new HashMap<String,Object>();
					paramMap.put("userid", userid);
					Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
					ccuserlist.add(usermap);
				}
			}
			leaveinfo.put("ccuserlist", ccuserlist);
		}
		return leaveinfo;
	}

	@Override
	public void updateLeave(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", map.get("updateid"));
		remindmap.put("resourceid", map.get("leaveid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		this.officeMapper.updateLeave(map);
	}

	@Override
	public int getExpenseNumByCompany(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.officeMapper.getExpenseNumByCompany(data);
	}

	@Override
	public String insertExpense(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String expenseid = UUID.randomUUID().toString().replaceAll("-", "");
		data.put("expenseid", expenseid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		data.put("status", 0);

		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("userid", data.get("createid"));
		userInfo = this.userinfoMapper.getUserInfo(userInfo);
		// 添加到审批提醒表
		if (userInfo != null && userInfo.size() > 0) {
			Map<String, Object> remindmap = new HashMap<String, Object>();
			remindmap.put("remindid",
					UUID.randomUUID().toString().replaceAll("-", ""));
			remindmap.put("companyid", data.get("companyid"));
			remindmap.put("userid", data.get("examineuserid"));
			remindmap.put("title", "审批");
			remindmap.put("content", userInfo.get("realname") + "申请了一个报销,请您审批");
			remindmap.put("linkurl", "oa/expenseaccount_check.html?expenseid="+expenseid+"&userid="+data.get("examineuserid"));
			remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getPcOaExpenseDetail?expenseid="+expenseid);
			remindmap.put("createid", data.get("createid"));
			remindmap.put("resourceid", expenseid);
			remindmap.put("resourcetype", 17);
			remindmap.put("createtime", new Date());
			remindmap.put("delflag", 0);
			this.indexMapper.insertRemindInfo(remindmap);
			
			try {
				//推送信息
				String userid=data.get("examineuserid")+"";
				String title=userInfo.get("realname") + "申请了一个报销,请您审批";
				String url="/oa/expenseaccount_check.html?expenseid="+expenseid+"&userid="+userid;
				JPushAndriodAndIosMessage(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// 添加自己到转发表
		Map<String, Object> forwordmap = new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString()
				.replaceAll("-", ""));
		forwordmap.put("companyid", data.get("companyid"));
		forwordmap.put("resourceid", expenseid);
		forwordmap.put("receiveid", data.get("createid"));
		forwordmap.put("createid", data.get("createid"));
		forwordmap.put("isread", 1);
		forwordmap.put("resourcetype", 17);
		forwordmap.put("createtime", new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);

		// 添加审批人转发表
		forwordmap = new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString()
				.replaceAll("-", ""));
		forwordmap.put("companyid", data.get("companyid"));
		forwordmap.put("resourceid", expenseid);
		forwordmap.put("receiveid", data.get("examineuserid"));
		forwordmap.put("createid", data.get("createid"));
		forwordmap.put("isread", 0);
		forwordmap.put("resourcetype", 17);
		forwordmap.put("createtime", new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);

		String ccuserids = "";
		//添加抄送人到转发表
		if(data.containsKey("userlist") && !"".equals(data.get("userlist"))){
			JSONObject json=JSONObject.fromObject(data.get("userlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String,Object> user : userlist){
				try {
					String userid=String.valueOf(user.get("userid"));
					if(!userid.equals(data.get("examineuserid")) && !userid.equals(data.get("createid"))){
						forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",data.get("companyid"));
						forwordmap.put("resourceid",expenseid);
						forwordmap.put("receiveid",user.get("userid"));
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",17);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						String title=userInfo.get("realname") + "申请了一个报销,请查看！";
						String url="/oa/expenseaccount_detail.html?expenseid="+expenseid+"&userid="+userid;
						JPushAndriodAndIosMessage(userid, title, url);
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.officeMapper.insertExpense(data);
		return expenseid;
	}

	@Override
	public void insertExpenseDetail(Map<String, Object> data) {
		// TODO Auto-generated method stub
		data.put("expensedetailid",
				UUID.randomUUID().toString().replaceAll("-", ""));
		data.put("createtime", new Date());
		data.put("delflag", 0);
		this.officeMapper.insertExpenseDetail(data);
	}

	@Override
	public List<Map<String, Object>> getExpenseList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getExpenseList(map);
	}

	@Override
	public int getExpenseListNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getExpenseListNum(map);
	}

	/**
	 * 查询报销单时间列表信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getExpenseTimesList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = this.officeMapper
				.getExpenseTimesList(map);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("companyid", map.get("companyid"));
		param.put("userid", map.get("userid"));
		param.put("status", map.get("status"));
		for (Map<String, Object> mm : list) {
			param.put("starttime", mm.get("createtime"));
			param.put("endtime", mm.get("createtime"));
			List<Map<String, Object>> expenselist = this.getExpenseList(param);
			mm.put("expenselist", expenselist);
		}
		return list;
	}

	/**
	 * 查询报销单时间列表的总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getExpenseTimesListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getExpenseTimesListCount(map);
	}

	@Override
	public Map<String, Object> getExpenseInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> expenseinfo = this.officeMapper
				.getExpenseInfo(data);
		if (expenseinfo != null && expenseinfo.size() > 0) {
			List<Map<String, Object>> expensedetaillist = this.officeMapper
					.getExpenseDetailList(expenseinfo);
			expenseinfo.put("expensedetaillist", expensedetaillist);
			
			List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
			if(expenseinfo.containsKey("CCuserids")){
				String ccuserids = String.valueOf(expenseinfo.get("CCuserids"));
				String [] userids = ccuserids.split(",");
				for(String userid : userids){
					Map<String,Object> paramMap = new HashMap<String,Object>();
					paramMap.put("userid", userid);
					Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
					ccuserlist.add(usermap);
				}
			}
			expenseinfo.put("ccuserlist", ccuserlist);
		}
		return expenseinfo;
	}

	@Override
	public void updateExpense(Map<String, Object> data) {
		// TODO Auto-generated method stub

		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("updateid"));
		remindmap.put("resourceid", data.get("expenseid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		this.officeMapper.updateExpense(data);
	}

	@Override
	public String insertRequest(Map<String, Object> data) {
		// TODO Auto-generated method stub
		String requestid = UUID.randomUUID().toString().replaceAll("-", "");
 		data.put("requestid", requestid);
		data.put("createtime", new Date());
		data.put("delflag", 0);
		data.put("status", 0);

 		String level = String.valueOf(data.get("urgentlevel"));
 		Map<String, Object> userInfo = new HashMap<String, Object>();
 		Map<String, Object> forwordmap = new HashMap<String, Object>();
 		if(!"1".equals(level)){
 			String jinji = "紧急";
 			if("3".equals(level)){
 				jinji = "非常紧急";
 			}
			userInfo.put("userid", data.get("createid"));
			userInfo = this.userinfoMapper.getUserInfo(userInfo);
			// 添加到审批提醒表
			if (userInfo != null && userInfo.size() > 0) {
				Map<String, Object> remindmap = new HashMap<String, Object>();
				remindmap.put("remindid",
						UUID.randomUUID().toString().replaceAll("-", ""));
				remindmap.put("companyid", data.get("companyid"));
				remindmap.put("userid", data.get("examineuserid"));
				remindmap.put("title", "审批");
				remindmap.put("content", userInfo.get("realname") + "申请了一个"+jinji+"请示,请您审批");
				remindmap.put("linkurl", "oa/ask_check.html?requestid="+requestid+"&userid="+data.get("examineuserid"));
				remindmap.put("pclinkurl", Constants.PROJECT_PATH+"/pc/getPcOaAskDetail?requestid="+requestid);
				remindmap.put("createid", data.get("createid"));
				remindmap.put("resourceid", requestid);
				remindmap.put("resourcetype", 16);
				remindmap.put("createtime", new Date());
				remindmap.put("delflag", 0);
				this.indexMapper.insertRemindInfo(remindmap);
				
				try {
					//推送信息
					String userid=data.get("examineuserid")+"";
					String title=userInfo.get("realname") + "申请了一个"+jinji+"请示,请您审批";
					String url="/oa/ask_check.html?requestid="+requestid+"&userid="+userid;
					JPushAndriodAndIosMessage(userid, title, url);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
 		}
		// 添加自己到转发表
		forwordmap.put("forwarduserid", UUID.randomUUID().toString()
				.replaceAll("-", ""));
		forwordmap.put("companyid", data.get("companyid"));
		forwordmap.put("resourceid", requestid);
		forwordmap.put("receiveid", data.get("createid"));
		forwordmap.put("createid", data.get("createid"));
		forwordmap.put("isread", 1);
		forwordmap.put("resourcetype", 16);
		forwordmap.put("createtime", new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);

		// 添加审批人转发表
		forwordmap = new HashMap<String, Object>();
		forwordmap.put("forwarduserid", UUID.randomUUID().toString()
				.replaceAll("-", ""));
		forwordmap.put("companyid", data.get("companyid"));
		forwordmap.put("resourceid", requestid);
		forwordmap.put("receiveid", data.get("examineuserid"));
		forwordmap.put("createid", data.get("createid"));
		forwordmap.put("isread", 0);
		forwordmap.put("resourcetype", 16);
		forwordmap.put("createtime", new Date());
		forwordmap.put("delflag", 0);
		this.indexMapper.insertForword(forwordmap);
		
		String ccuserids = "";
		//添加抄送人到转发表
		if(data.containsKey("userlist") && !"".equals(data.get("userlist"))){
			JSONObject json=JSONObject.fromObject(data.get("userlist")+"");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> userlist=(List<Map<String, Object>>)json.get("userlist");
			for(Map<String,Object> user : userlist){
				try {
					String userid=String.valueOf(user.get("userid"));
					if(!userid.equals(data.get("examineuserid")) && !userid.equals(data.get("createid"))){
						forwordmap=new HashMap<String, Object>();
						forwordmap.put("forwarduserid", UUID.randomUUID().toString().replaceAll("-", ""));
						forwordmap.put("companyid",data.get("companyid"));
						forwordmap.put("resourceid",requestid);
						forwordmap.put("receiveid",userid);
						forwordmap.put("createid",data.get("createid"));
						forwordmap.put("isread",0);
						forwordmap.put("resourcetype",16);
						forwordmap.put("createtime",new Date());
						forwordmap.put("delflag", 0);
						this.indexMapper.insertForword(forwordmap);
						//推送信息
						if(!"1".equals(level)){
				 			String jinji = "紧急";
				 			if("3".equals(level)){
				 				jinji = "非常紧急";
				 			}
							String title=userInfo.get("realname") + "申请了一个"+jinji+"请示,请查看！";
							String url="/oa/ask_detail.html?requestid="+requestid+"&userid="+userid;
							JPushAndriodAndIosMessage(userid, title, url);
						}
					}
					ccuserids += userid+",";
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			data.put("CCuserids", ccuserids.substring(0, (ccuserids.length() - 1)));
		}
		
		this.officeMapper.insertRequest(data);
		return requestid;
	}

	@Override
	public List<Map<String, Object>> getRequestList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getRequestList(map);
	}

	@Override
	public int getRequestListNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getRequestListNum(map);
	}

	/**
	 * 查询请示单时间列表信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getRequestTimesList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = this.officeMapper
				.getRequestTimesList(map);

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("companyid", map.get("companyid"));
		param.put("userid", map.get("userid"));
		param.put("status", 1);
		for (Map<String, Object> mm : list) {
			param.put("starttime", mm.get("createtime"));
			param.put("endtime", mm.get("createtime"));
			List<Map<String, Object>> requestlist = this.getRequestList(param);
			mm.put("requestlist", requestlist);
		}
		return list;
	}

	/**
	 * 查询请示单时间列表的总数量
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getRequestTimesCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getRequestTimesCount(map);
	}

	@Override
	public Map<String, Object> getRequestInfo(Map<String, Object> data) {
		// TODO Auto-generated method stub
		Map<String, Object> requestInfo = this.officeMapper
				.getRequestInfo(data);
		if (requestInfo != null && requestInfo.size() > 0) {
			// 查询文件
			List<Map<String, Object>> filelist = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resourceid", requestInfo.get("requestid"));
			map.put("resourcetype", 16);
			filelist = this.indexMapper.getFileList(map);
			requestInfo.put("filelist", filelist);
			
			List<Map<String,Object>> ccuserlist = new ArrayList<Map<String,Object>>();
			if(requestInfo.containsKey("CCuserids")){
				String ccuserids = String.valueOf(requestInfo.get("CCuserids"));
				String [] userids = ccuserids.split(",");
				for(String userid : userids){
					Map<String,Object> paramMap = new HashMap<String,Object>();
					paramMap.put("userid", userid);
					Map<String,Object> usermap = this.personalMapper.getVerificationPwd(paramMap);
					ccuserlist.add(usermap);
				}
			}
			requestInfo.put("ccuserlist", ccuserlist);
		}
		return requestInfo;
	}

	@Override
	public void updateRequest(Map<String, Object> data) {
		// TODO Auto-generated method stub
		//删除待办事项
		Map<String, Object> remindmap=new HashMap<String, Object>();
		remindmap.put("userid", data.get("userid"));
		remindmap.put("resourceid", data.get("requestid"));
		remindmap.put("delflag", 1);
		this.indexMapper.updateRemind(remindmap);
		
		this.officeMapper.updateRequest(data);
	}

	@Override
	public List<Map<String, Object>> getReserveAmountByDate(
			Map<String, Object> data) {
		// TODO Auto-generated method stub
		// List<Map<String, Object>>
		// list=this.purchaseMapper.getApplyOrderByDate(data);
		List<Map<String, Object>> list = this.officeMapper
				.getReserveAmountByDate(data);
		for (Map<String, Object> order : list) {
			data.put("createtime", order.get("createtime"));
			List<Map<String, Object>> orderlist = this.officeMapper
					.getReserveByDate(data);
			order.put("orderlist", orderlist);
		}
		return list;
	}

	@Override
	public int getReserveAmountByDateNum(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return this.officeMapper.getReserveAmountByDateNum(data);
	}

	/**
	 * 查询公司的企业网盘信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getCompanyCloudInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
//		Map<String,Object> cloudmap = this.officeMapper.getCompanyCloudInfo(map); 
//		String maxmemory = String.valueOf(cloudmap.get("maxmemory"));
//		String usedmemory = String.valueOf(cloudmap.get("usedmemory"));
//		if(cloudmap.containsKey("maxmemory") && !"".equals(maxmemory)){
//			String max = ((Float.parseFloat(maxmemory)*100)/(1024*1024))/100+"";
//			int num = max.indexOf(".")+3;
//			if(num>max.length()){
//				num=max.length();
//			}
//			cloudmap.put("maxmemory", max.substring(0,num));
//		}
//		if(cloudmap.containsKey("usedmemory") && !"".equals(usedmemory)){
//			String used = ((Float.parseFloat(usedmemory)*100)/(1024*1024))/100+"";
//			int num = used.indexOf(".")+3;
//			if(num>used.length()){
//				num=used.length();
//			}
//			cloudmap.put("usedmemory", used.substring(0,num));
//		}
		Map<String,Object> cloudmap = this.userinfoMapper.getCompanyMemory(map);
		return cloudmap;
	}

	/**
	 * 添加扩容信息
	 * 
	 * @param map
	 */
	@Override
	public void insertCloudCapacityInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("capacityid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("status", 1);
		map.put("delflag", 0);
		map.put("createtime", new Date());
		this.officeMapper.insertCloudCapacityInfo(map);
	}

	/**
	 * 查询扩容记录信息
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCloudCapacityList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = this.officeMapper
				.getCloudCapacityList(map);
		for (Map<String, Object> mm : list) {
			String memory = String.valueOf(mm.get("memory"));
			String sjmemory = String.valueOf(mm.get("sjmemory"));
			if (!"".equals(memory) && mm.containsKey("memory")) {
				String memory_float = (((Float.parseFloat(memory) * 100) / (1024 * 1024)) / 100)
						+ "";
				int dian = memory_float.indexOf(".") + 3;
				if (dian > memory_float.length()) {
					dian = memory_float.length();
				}
				memory_float = memory_float.substring(0, dian);
				mm.put("memory", memory_float + " G");
			}
			if (!"".equals(sjmemory) && mm.containsKey("sjmemory")) {
				String sjmemory_float = (((Float.parseFloat(sjmemory) * 100) / (1024 * 1024)) / 100)
						+ "";
				int dian = sjmemory_float.indexOf(".") + 3;
				if (dian > sjmemory_float.length()) {
					dian = sjmemory_float.length();
				}
				sjmemory_float = sjmemory_float.substring(0, dian);
				mm.put("sjmemory", sjmemory_float + " G");
			}
		}
		return list;
	}

	/**
	 * 查询扩容记录总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getCloudCapacityCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getCloudCapacityCount(map);
	}

	/**
	 * 查询公司申请的总的云空间大小
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public String getCloudCapacitySjmemoryCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String count = this.officeMapper.getCloudCapacitySjmemoryCount(map);
		if (count != null && count != "") {
			count = (((Float.parseFloat(count) * 100) / (1024 * 1024)) / 100)
					+ "";
			int dian = count.indexOf(".") + 3;
			if (dian > count.length()) {
				dian = count.length();
			}
			count = count.substring(0, dian);
		} else {
			count = "0";
		}
		return count;
	}

	/**
	 * 修改云盘文件夹信息
	 * 
	 * @param map
	 */
	@Override
	public void updateCompanyCloudModuleInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.officeMapper.updateCompanyCloudModuleInfo(map);
	}

	/**
	 * 查询云盘文件
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCompanyCloudFileList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getCompanyCloudFileList(map);
	}

	/**
	 * 查询云盘文件的总数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public int getCompanyCloudFileListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getCompanyCloudFileListCount(map);
	}

	/**
	 * 添加文件信息
	 * 
	 * @param map
	 */
	@Override
	public String insertCompanyCloudFileInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String fileid = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("fileid", fileid);
		map.put("filetype", 1);
		map.put("delflag", 0);
		map.put("createtime", new Date());
		this.officeMapper.insertCompanyCloudFileInfo(map);
		return fileid;
	}

	/**
	 * 修改文件信息
	 * 
	 * @param map
	 */
	@Override
	public void updateCompanyCloudFileInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.officeMapper.updateCompanyCloudFileInfo(map);
	}

	/**
	 * 查询文件详情
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getCompanyCloudFileInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		// 查询发布范围
		Map<String, Object> filemap = this.officeMapper
				.getCompanyCloudFileInfo(map);
		if(filemap.containsKey("memory") && !"".equals(filemap.get("memory"))){
			String memory = String.valueOf(filemap.get("memory"));
			if (filemap.containsKey("memory") && !"".equals(memory)) {
				int memo = Integer.parseInt(memory);
				filemap.put("kb_memory", memo);
				String[] dd = { "KB", "MB", "GB" };
				for (int i = 0; i <= 3; i++) {
					if (memo > 1024) {
						if (i == 3) {
							filemap.put("memory", memo + " GB");
							break;
						}
						memo = ((memo * 100) / 1024) / 100;
					} else {
						filemap.put("memory", memo + " " + dd[i]);
						break;
					}
				}
			} else {
				filemap.put("kb_memory", 0);
			}
		}
		Map<String, Object> rangemap = new HashMap<String, Object>();
		rangemap.put("resourcetype", 13);
		rangemap.put("resourceid", map.get("fileid"));
		List<Map<String, Object>> rangelist = this.indexMapper
				.getRangeList(rangemap);
		filemap.put("rangelist", rangelist);
		return filemap;
	}

	/**
	 * 修改内存大小
	 * 
	 * @param map
	 */
	@Override
	public void updateCompanyCloudUseMemoryInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		this.officeMapper.updateCompanyCloudUseMemoryInfo(map);
	}

	@Override
	public Map<String, Object> getLeaveDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getLeaveInfo(map);
	}

	/**
	 * 使用放后台查询企业简报信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBriefListInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getBriefListInfo(map);
	}

	/**
	 * 使用方后台查询查询企业简报总数
	 * @param map
	 * @return
	 */
	@Override
	public int getBriefListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.officeMapper.getBriefListCount(map);
	}

	/**
	 * 查询还有一个小时结束的任务信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOverTaskOneHour(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> tasklist = this.officeMapper.getOverTaskOneHour(map);
		if(tasklist != null && tasklist.size() > 0){
			for(Map<String,Object> mm : tasklist){
				List<Map<String,Object>> assislist  = this.officeMapper.getTaskAssisList(mm);
				if(assislist != null && assislist.size() > 0){
					mm.put("assislist", assislist);
				}
			}
		}
		return tasklist;
	}

	/**
	 * 推送通知栏信息
	 * @param userid
	 * @param title
	 * @param url
	 */
	public void JPushAndriodAndIosMessage(String userid, String title, String url){
		/*//获取推送的类型
		String type = Constants.JPUSHTYPE;
		//registrationid推送
		if(type.equals("registrationid")){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("userid", userid);
			//查询用户的registrationid
			try {
				String registrationid = this.indexMapper.getRegistrationIdByUserId(param);
				if(!"".equals(registrationid) && registrationid != null){
					//推送信息
					JPushRegIdUtil.PushUrlByRegId(registrationid, title, url);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}else if(type.equals("userid")){//userid 推送
			try {
				JPushAliaseUtil.PushUrlByAliase(userid, title, url);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}*/
	} 
	
}
