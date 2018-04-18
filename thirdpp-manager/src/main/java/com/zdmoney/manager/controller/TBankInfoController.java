package com.zdmoney.manager.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 




import javax.validation.groups.Default;

import net.sf.json.JSONArray;
 
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
 
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
 
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

 
 
 
 







import com.zdmoney.manager.Validate.Validate;
import com.zdmoney.manager.models.TBankInfo;
 
 
import com.zdmoney.manager.service.TBankInfoService;
 
import com.zdmoney.manager.utils.ConverNullString;
import com.zdmoney.manager.utils.JsonDateFormatUtil;
import com.zdmoney.manager.utils.Page;
import com.zdmoney.manager.utils.PermissionUtil;
import com.zdmoney.manager.utils.StringUtil;
import com.zdmoney.manager.vo.Response;
 


/** 
*
* @author wyj
* @version 2015年7月3日 下午1:59:18 
*/
@Controller
@RequestMapping(value = "/bim/bankInfo")
public class TBankInfoController {
	private final Logger log = Logger.getLogger(TSymPermissionController.class);
	@Autowired
	private TBankInfoService tBankInfoservice; 
	@Autowired
	private Page<TBankInfo> page;
	@RequestMapping(value = "/bankInfoList")
	public String list(HttpServletRequest request, ModelMap modelMap){
		try {
			HttpSession session = request.getSession();
			/*if(!PermissionUtil.isHavePermission(session, "/bim/bankInfo/bankInfoList")){
				return "/errorpage/permissionError";
			}*/
			/*Map<String,Object> params = new HashMap<String,Object>();
			//分页
			int pageNo = StringUtil.parseInteger(request.getParameter("pageNo"), 1);
			int pageSize = StringUtil.parseInteger(request.getParameter("pageSize"), 15);
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			//查询条件
			//封装查询条件
			String bankName = request.getParameter("bankName"); 
			String bankCode = request.getParameter("bankCode"); 
			String beginTime = request.getParameter("beginTime");
			String endTime = request.getParameter("endTime");
			if(!StringUtil.isEmpty(bankName)){
				params.put("bankName", bankName);
				modelMap.put("bankName", bankName);
			}
			if(!StringUtil.isEmpty(bankCode)){
				params.put("bankCode", bankCode);
				modelMap.put("bankCode", bankCode);
			}
			if(!StringUtil.isEmpty(beginTime)){
				modelMap.put("beginTime", beginTime);
				beginTime=beginTime.replaceAll("-", "");
				params.put("beginTime", beginTime);
			
			}
			if(!StringUtil.isEmpty(endTime)){
				modelMap.put("endTime", endTime);
				endTime=endTime.replaceAll("-", "");
				params.put("endTime", endTime);
			
			}
			params.put("page", page);
			
			//查询银行信息列表
			List<TBankInfo> tBankList=tBankInfoservice.getBankInfoList(params);
			page.setResults(tBankList);		
			modelMap.put("tBankList", tBankList);
			modelMap.put("page", page);*/
			return "/bim/bankInfo/bankInfoList";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			modelMap.put("errorMsg", e.getMessage());
			return "/errorpage/error";
		}
	}
	@RequestMapping(value = "/bankInfoListData")
	@ResponseBody
	public  String  listData(HttpServletRequest request, ModelMap modelMap){
		
		try {
			// 1、设置查询参数
			Map<String,Object> params = new HashMap<String,Object>();
			String beginTime =request.getParameter("beginTime");
			String endTime =request.getParameter("endTime");
			this.datagridParam(request, params);
			if(!StringUtil.isEmpty(beginTime)){
				beginTime=beginTime.replaceAll("-", "");
				params.put("beginTime", beginTime);
			}
			if(!StringUtil.isEmpty(endTime)){
				endTime=endTime.replaceAll("-", "");
				params.put("endTime", endTime);
			}
			params.put("bankName", request.getParameter("sebankName"));		 
			// 2.遍历增加及转换自定义内容
			List<Map> rows =tBankInfoservice.getBankInfoList(params);
			int count = tBankInfoservice.getBankInfoListCount(params);
			for (Map d : rows) {
				// 2.自定义按钮设置在此处
			
				/*/d.put("PAYER_BANK_CARD_TYPE", d.get("PAYER_BANK_CARD_TYPE") == null ? "" : BankCardTypeEnum.getEnumDesc(d.get("PAYER_BANK_CARD_TYPE").toString()));
				d.put("PAYER_ID_TYPE", d.get("PAYER_ID_TYPE") == null ? "" : IdTypeEnum.getEnumDesc(d.get("PAYER_ID_TYPE").toString()));
				d.put("CURRENCY", d.get("CURRENCY") == null ? "" : CurrencyEnum.getEnumDesc(d.get("CURRENCY").toString()));
				d.put("PRIORITY", d.get("PRIORITY") == null ? "" : PriorityEnum.getEnumDesc(d.get("PRIORITY").toString()));
				d.put("STATUS", d.get("STATUS") == null ? "" : SendStatusEnum.getEnumDesc(d.get("STATUS").toString()));
				d.put("IS_SEPARATE", d.get("IS_SEPARATE") == null ? "" : IsSeparateEnum.getEnumDesc(d.get("IS_SEPARATE").toString()));
				d.put("BIZ_TYPE", d.get("BIZ_TYPE") == null ? "" : BizTypeEnum.getEnumDesc(d.get("BIZ_TYPE").toString()));
				d.put("IS_NEED_PUSH", d.get("IS_NEED_PUSH") == null ? "" : NeedPushEnum.getEnumDesc(d.get("IS_NEED_PUSH").toString()));
				d.put("TRADE_STATUS", d.get("TRADE_STATUS") == null ? "" : TradeStatusEnum.getEnumDesc(d.get("TRADE_STATUS").toString()));
				d.put("IS_NEED_SPILT", d.get("IS_NEED_SPILT") == null ? "" : IsNeedSpiltEnum.getEnumDesc(d.get("IS_NEED_SPILT").toString()));
				d.put("AMOUNT" , d.get( "AMOUNT" )==null ? "0.00" : BigDecimalFormatUtil.switchMoneyFormat((BigDecimal)d.get( "AMOUNT" )) );
				d.put("FEE" , d.get( "FEE" )==null ? "0.00" : BigDecimalFormatUtil.switchMoneyFormat((BigDecimal)d.get( "FEE" )) );
				d.put("TRADE_SUCCESS_AMOUNT" , d.get( "TRADE_SUCCESS_AMOUNT" )==null ? "0.00" : BigDecimalFormatUtil.switchMoneyFormat((BigDecimal)d.get( "TRADE_SUCCESS_AMOUNT" )) );*/
				 
				d.put("update","<a href='javascript:return void(0);' onClick=\"updateBankInfo('"+ d.get("ID")+ "');\"><i class='icon-edit'></i></a>");
			}

			// 3.组合输出列表查询所需数据
			// JsonConfig 用于解析转换的设置
			JsonConfig config = new JsonConfig();
			JsonDateFormatUtil.formatDateForJsonConfig_type1(config);

			JSONArray json_rows = JSONArray.fromObject(rows, config);
			String json = "{\"total\":\""
					+ count
					+ "\",\"rows\":" + json_rows.toString() + "}";
			return json;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private void datagridParam(HttpServletRequest request, Map<String,Object> params) throws Exception {
		int pageNumber = StringUtils.isBlank(request.getParameter("page")) ? 1: Integer.parseInt(request.getParameter("page"));
		int pageSize = StringUtils.isBlank(request.getParameter("rows")) ? 10: Integer.parseInt(request.getParameter("rows"));
		
		int rowBegin = (pageNumber-1) * pageSize + 1;
		int rowEnd = (pageNumber-1) * pageSize + pageSize;
		String sortName = request.getParameter("sort");
		String sortOrder = StringUtils.isBlank(request.getParameter("order")) ? "asc" : request.getParameter("order");
		if (StringUtils.isNotBlank(sortName) && StringUtils.isNotBlank(sortOrder)) {
			params.put("orderStr", " order by " + sortName + " " + sortOrder + " ");
		}
		params.put("rowBegin", rowBegin);
		params.put("rowEnd", rowEnd);
	}
	@RequestMapping(value = "/bankInfoDelete/{ids}")
	@ResponseBody
	public String delete(@PathVariable("ids") String[] ids,HttpServletRequest request, ModelMap modelMap) {
		
		 
			if(null != ids && ids.length>0){
				List<Integer> list = new ArrayList<Integer>();
				for(String infoIdStr : ids){
					Integer infoId = Integer.valueOf(infoIdStr);
					list.add(infoId);
				}
				tBankInfoservice.batchDeleteInfo(list);
			}
		 
		return null;
		
	}
	/**
	 * 银行信息编辑页面
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/bankInfoEditUI")
	public String editUI(HttpServletRequest request, ModelMap modelMap) {
		TBankInfo tb = null;
		String id = request.getParameter("id");
 		if(!StringUtil.isEmpty(id)){
			//权限验证
			HttpSession session = request.getSession();
			System.out.println( session.getAttribute("permMap"));
			/*if(!PermissionUtil.isHavePermission(session, "/bim/bankInfo/editBankInfo")){
				return "/errorpage/permissionError";
			}*/
			
			tb = tBankInfoservice.selectBankInfoByID(StringUtil.parseLong(id));
			tb.setNoteNo(tb.getBankCode());
			modelMap.put("readOnly", true);
		}else{
			//权限验证
			HttpSession session = request.getSession();
			System.out.println(session.getAttribute("permMap"));
			modelMap.put("readOnly", false);
			/*if(!PermissionUtil.isHavePermission(session, "/bim/bankInfo/addBankInfo")){
				return "/errorpage/permissionError";
			}*/
			
			tb = new TBankInfo();
		}
		if(null != tb){
			modelMap.put("bankInfo", tb);
		}

		return "bim/bankInfo/bankInfoEdit";
	}
	
	/**
	 * 保存银行数据信息
	 * @param request
	 * @param modelMap
	 * @return
	 */ 
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bankInfoSave",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String bankInfoSave(@ModelAttribute("bankInfoSave")  TBankInfo bankInfo,HttpServletRequest request,HttpServletResponse responsem, ModelMap modelMap) {	 
		try{
			ConverNullString sr=new  ConverNullString();
			sr.nullConverNullString(bankInfo);
			String response =  Validate.getInstance().validate(bankInfo,
					com.zdmoney.manager.Validate.InsertCheck.class, Default.class);
			JSONObject jb=new JSONObject();
		
			if(!StringUtils.isBlank(response)){			 	 				
				jb.put("valmsg", response);
				return    jb.toString();
			}
			int count=tBankInfoservice.getBankCodeCount(bankInfo.getBankCode());
			if(count>0){
				if(StringUtil.isEmpty(bankInfo.getId())){
					response="银行编码已存在";
					jb.put("valmsg",response);
					return    jb.toString();
				}else{
					if(!bankInfo.getBankCode().equals(bankInfo.getNoteNo())){
						response="银行编码已存在";
						jb.put("valmsg",response);
						return    jb.toString();
					}
				}
				
			}
				if(StringUtil.isEmpty(bankInfo.getId())){
					//新增	
					log.info("新增+："+bankInfo.getId());
					tBankInfoservice.insert(bankInfo);
				}else{
					//修改		
					log.info("修改+："+bankInfo.getId());
					tBankInfoservice.updateBankInfo(bankInfo);
				} 
					log.info("保存成功");
					return    jb.toString();
			 
		}catch(Exception e){
			log.error(e.getMessage(), e);
			modelMap.put("errorMsg", e.getMessage());
			return "/errorpage/error";
		}
	}
}
