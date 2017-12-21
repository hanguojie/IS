package com.tospur.is.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tospur.is.service.OAUserService;
import com.tospur.is.service.ProcessService;
import com.tospur.is.vo.RunforVo;
import com.tospur.is.vo.Vote;
import com.tospur.utils.DateUtil;
import com.tospur.utils.response.Resp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/process")
@Api(tags = "IS-后台接口")
public class ProcessController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessController.class);
	
	@Resource
	private ProcessService processService;
	
	@Resource
	//OA获取数据
	private OAUserService oaUserService;
	

	@Value("${upload.path}") 
	private String uploadPath;
	
	
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	@ApiImplicitParams({
		@ApiImplicitParam(required = true, name = "token", value = "token", paramType = "query", dataType = "string")})
	public Resp uploadImage(@RequestParam("file") MultipartFile [] files,HttpServletRequest request,String token){
		String imageUrls="";
		String requrl=request.getRequestURL().substring(0,request.getRequestURL().indexOf("IS"));
		final String workNo=token.substring(token.indexOf("#")+1);
		if(files.length>5){
			return Resp.failed("文件数量大于5个");
		}
		if(files!=null&&files.length>0){
            //循环获取file数组中得文件  
            for(int i = 0;i<files.length;i++){
                MultipartFile file = files[i];  
                //保存文件  
                String s = UUID.randomUUID().toString();
        		String name = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
        		
                String imageUrl=saveFile(file,workNo,name,requrl);  
                if(!"".equals(imageUrl)&&i==files.length-1){
                	imageUrls+=imageUrl;
                }else if(!"".equals(imageUrl)){
                	imageUrls+=imageUrl+"|";
                }
            }
        }
		Resp resp = Resp.success();
		Map<String, String> imageMap=new HashMap<String, String>();
		imageMap.put("imageURL", imageUrls);
		resp.setData(imageMap);

		return resp;
	}
	
	private String saveFile(MultipartFile file,String singleId,String icon,String requrl) {  
        // 判断文件是否为空  
        if (!file.isEmpty()) {
            try {  
                // 文件保存路径  
                String filePath = singleId +"/"+icon
                        + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));  
                //创建文件夹
                createdir(uploadPath+singleId);
                // 转存文件  
                
                file.transferTo(new File(uploadPath+filePath));  
                
                return requrl+"images/"+filePath;  
            } catch (Exception e) {  
            	LOGGER.error("保存文件失败"+e);
    			return "";
            }  
        }else{
        	LOGGER.info("文件为空");
        	return "";
        }
    } 

	public boolean createdir(String path) {
		try{
	 		 File file=new File(path);
	         if (file.exists()) {
	        	 LOGGER.info("文件夹已存在");
	        	 
	         }else{
	        	 file.mkdir();
	         }
	         return true;
		}catch(Exception e){
			LOGGER.error("创建文件夹失败"+e);
			return false;
		}
		 
	}
	
	
	@ApiOperation(value = "报名竞选", notes = "报名竞选")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(required = true, name = "X-Token", value = "令牌", paramType = "header", dataType = "string") })
	public Resp addFault (@RequestBody RunforVo runforVo,HttpServletRequest request) {
		final String token=request.getHeader("X-Token");
		final String workNo=token.substring(token.indexOf("#")+1);
		if(runforVo.getWorkNo()==null||runforVo.getType()==null
				||runforVo.getEnrollType()==null){
			return Resp.failed("缺少参数!");
		}
		//查询推荐的人是否存在OA
		Map<String, String> userInfo=oaUserService.getUserInfoById(runforVo.getWorkNo());
		if(userInfo!=null){
			runforVo.setDepartment(userInfo.get("department"));
			runforVo.setName(userInfo.get("name"));
		}else{
			return Resp.failed("推荐人工号不存在!");
		}
		System.out.println(DateUtil.dateToString(new Date(), "yyyyMMddHHmmss"));
		
		if(processService.getByWorkNo(workNo).size()!=0){
			return Resp.failed("该员工已报名，请勿重复自荐或他荐!");
		}
		try{
			processService.addrunFor(runforVo); 
			
			Resp resp = Resp.success();
			return resp;
		}catch(Exception e){
			LOGGER.error("添加报障信息出错" + e.getMessage(), e);
			return Resp.failed("添加报障信息出错!");
		}
	}
	
	
	@ApiOperation(value = "获取四同标杆", response = RunforVo.class, notes = "获取四同标杆")
	@RequestMapping(value = "/getPInfoByType", method = RequestMethod.GET)
	@ApiImplicitParams({
			@ApiImplicitParam(required = true, name = "type", value = "竞选类型", paramType = "query", dataType = "string") })
	public Resp getPInfoByType(String type) {
		try{
		List<RunforVo> RunforList=processService.getPInfoByType(type);
		Resp resp=Resp.success();
		resp.setData(RunforList);
		return resp;
		} catch (Exception e) {
			LOGGER.error("获取四同标杆出错" + e.getMessage(), e);
			return Resp.failed("网络错误");
		}
	}
	
	@ApiOperation(value = "获取四同投票人员列表", response = RunforVo.class, notes = "获取四同投票人员列表")
	@RequestMapping(value = "/findPListByType", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(required = true, name = "type", value = "竞选类型", paramType = "query", dataType = "string") })
	public Resp findPListByType(String type) {
		try{
		List<RunforVo> RunforList=processService.findPListByType(type);
		Resp resp=Resp.success();
		resp.setData(RunforList);
		return resp;
		} catch (Exception e) {
			LOGGER.error("获取报名人员信息出错" + e.getMessage(), e);
			return Resp.failed("网络错误");
		}
	}
	
	@ApiOperation(value = "获取报名人员列表", response = RunforVo.class, notes = "获取报名人员列表")
	@RequestMapping(value = "/findAddList", method = RequestMethod.GET)
	public Resp findAddList() {
		try{
		List<RunforVo> RunforList=processService.findAddList();
		Resp resp=Resp.success();
		resp.setData(RunforList);
		return resp;
		} catch (Exception e) {
			LOGGER.error("获取报名人员信息出错" + e.getMessage(), e);
			return Resp.failed("网络错误");
		}
	}
	
	@ApiOperation(value = "投票", response = Vote.class, notes = "投票")
	@RequestMapping(value = "/addVote", method = RequestMethod.POST)
	@ApiImplicitParams({
	@ApiImplicitParam(required = true, name = "X-Token", value = "令牌", paramType = "header", dataType = "string") })
	public Resp addVote(@RequestBody Vote vote,HttpServletRequest request) {
		try{
			vote.setDay(DateUtil.dateToString(new Date(), "yyyy-MM-dd"));
			if(processService.findVoteNum(vote).size()!=0){
				return Resp.failed("同一竞选类型当天只能投一票");
			}
			
			processService.addVote(vote);
			
			Resp resp=Resp.success();
		return resp;
		} catch (Exception e) {
			LOGGER.error("投票出错" + e.getMessage(), e);
			return Resp.failed("网络错误");
		}
	}
	
	@ApiOperation(value = "后台更改人员状态接口", response = RunforVo.class, notes = "更改人员状态接口")
	@RequestMapping(value = "/updatetatus", method = RequestMethod.PUT)
	@ApiImplicitParams({
			@ApiImplicitParam(required = true, name = "X-Token", value = "令牌", paramType = "header", dataType = "string") })
	public Resp updateStatus(@RequestBody RunforVo runforVo,HttpServletRequest request) {
		//简单权限控制状态的更改，上线时放开注释
		final String token=request.getHeader("X-Token");
		final String workNo=token.substring(token.indexOf("#")+1);
		
		if(workNo!="40158"||workNo!="10777"){
			return Resp.failed("用户无权进行更改状态!");
		}
		try{
			processService.updateStatus(runforVo);
					
			Resp resp = Resp.success();
			return resp;
		}catch(Exception e){
			LOGGER.error("更改状态失败" + e.getMessage(), e);
			return Resp.failed("更改状态失败!");
		}
	}
	
	@ApiOperation(value = "获取票数排行榜", response = RunforVo.class, notes = "获取票数排行榜")
	@RequestMapping(value = "/findRankingByType", method = RequestMethod.GET)
	@ApiImplicitParams({
		@ApiImplicitParam(required = true, name = "type", value = "竞选类型", paramType = "query", dataType = "string") })
	public Resp findRankingByType(String type) {
		try{
		List<RunforVo> RunforList=processService.findRankingByType(type);
		Resp resp=Resp.success();
		resp.setData(RunforList);
		return resp;
		} catch (Exception e) {
			LOGGER.error("获取票数排行榜出错" + e.getMessage(), e);
			return Resp.failed("网络错误");
		}
	}
	
	/**
	 * 获取四同任务总榜列表
	 * @return
	 */
	@ApiOperation(value = "获取四同任务总榜列表", response = RunforVo.class, notes = "获取四同任务总榜列表")
	@RequestMapping(value = "/findAllList", method = RequestMethod.GET)
	public Resp findAllList() {
		try {
		List<RunforVo> AllList = processService.findAllList();
		Resp resp = Resp.success();
		resp.setData(AllList);
		return resp;
		} catch (Exception e) {
			LOGGER.error("获取四同任务总榜列表出错" + e.getMessage(), e);
			return Resp.failed("网络错误");
		}
	}
}
