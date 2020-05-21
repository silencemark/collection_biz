package com.collection.service.impl.managebackstage;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.collection.dao.managebackstage.SystemCloudMapper;
import com.collection.service.managebackstage.SystemCloudService;

public class SystemCloudServiceImpl implements SystemCloudService{

	@Autowired SystemCloudMapper systemCloudMapper;

	/**
	 * 查询管理方文件列表信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getSystemCloudListInfo(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> list = this.systemCloudMapper.getSystemCloudListInfo(map);
		for(Map<String,Object> filemap : list){
			String memory = String.valueOf(filemap.get("memory"));
			if(filemap.containsKey("memory") && !"".equals(memory)){
				int memo = Integer.parseInt(memory);
				filemap.put("kb_memory", memo);
				String[] dd ={"KB","MB","GB","TB"};
				for(int i=0;i<=4;i++){
					if(memo > 1024){
						if(i==4){
							filemap.put("memory", memo+" TB");
							break;
						}
						memo = ((memo*100)/1024)/100;
					}else{
						filemap.put("memory", memo+" "+dd[i]);
						break;
					}
				}
			}else{
				filemap.put("kb_memory", 0);
			}
			
			String fileurl = String.valueOf(filemap.get("fileurl"));
			String filetype = String.valueOf(filemap.get("filetype"));
			if("1".equals(filetype)){
				if(filemap.containsKey("fileurl") && !"".equals(fileurl)){
					String icon = queryfiletype(fileurl);
					filemap.put("file_icon", icon);
				}else{
					filemap.put("file_icon", queryfiletype(""));
				}
			}
		}
		return list;
	}

	/**
	 * 查询管理方文件列表的总数
	 * @param map
	 * @return
	 */
	@Override
	public int getSystemCloudListCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemCloudMapper.getSystemCloudListCount(map);
	}

	/**
	 * 查询子文件夹的父文件夹的parentid
	 * @param map
	 * @return
	 */
	@Override
	public String getFolderParentid(Map<String,Object> map){
		// TODO Auto-generated method stub
		return this.systemCloudMapper.getFolderParentid(map);
	}
	
	/**
	 * 修改管理方文件信息
	 * @param map
	 */
	@Override
	public void updateSystemCloudInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("updatetime", new Date());
		this.systemCloudMapper.updateSystemCloudInfo(map);
	}

	/**
	 * 添加文件
	 * @param map
	 */
	@Override
	public void insertSystemCloudInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		map.put("fileid", UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("createtime", new Date());
		map.put("delflag", 0);
		this.systemCloudMapper.insertSystemCloudInfo(map);
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

	/**
	 * 查询文件的详细信息
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getSystemCloudDetailInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.systemCloudMapper.getSystemCloudDetailInfo(map);
	}
	
}
