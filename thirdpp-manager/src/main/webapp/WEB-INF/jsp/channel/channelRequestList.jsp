<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/base.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>证大财富统一支付管理系统</title>
		<meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<%@ include file="/common/jsCssInclude.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
<body class="easyui-layout">
			<!--搜索栏开始-->
			<div class="easyui-panel" title="查询条件" style="padding:5px;height:120px;margin: 0px;" data-options="region:'north'">
				<form id="searchForm" name="searchForm" method="post" >
					<table cellpadding="5">
						<tr>
						<td style="text-align:right">交易流水号：</td>
						<td>
							<input type="text" id="search_transfer_flow" name="search_transfer_flow" class="easyui-textbox" value="${search_transfer_flow}" style="width:100px;"/>
						</td>
						<td style="text-align:right">请求状态：</td>
						<td>
							<input id="search_status" class="easyui-combobox" name="search_status" data-options="
								editable:false,valueField: 'value',
								textField: 'name',
								//url是下拉框请求显示的数据
								url:'${path }/enumset/channelRequestStatusList' " style="width:100px;"/>  
						</td>
						<td style="text-align:right">查询日期：</td>
						<td>
							<input type="text" id="search_begin_date" name="search_begin_date"  value="${search_begin_date}" class="easyui-datebox" style="width:100px;" data-options="required:false,editable:false" />
							－
							<input type="text" id="search_end_date" name="search_end_date"  value="${search_end_date}" class="easyui-datebox" style="width:100px;" data-options="required:false,editable:false" />
						</td>
						
						
						</tr>
						<tr>
						<td style="text-align:right">TPP通道请求流水号：</td>
						<td>
							<input type="text" id="search_req_id" name="search_req_id" class="easyui-textbox" value="${search_req_id}" style="width:100px;"/>
						</td>
						<td style="text-align:right">业务系统：</td>
						<td>
							<input id="search_biz_sys_no" class="easyui-combobox" name="search_biz_sys_no" data-options="
								editable:false,valueField: 'value',
								textField: 'name',
								//url是下拉框请求显示的数据
								url:'${path }/enumset/bizSysNoList' " style="width:100px;"/>  
						</td>
						<td style="text-align:right">第三方支付平台：</td>
						<td>
							<input id="search_pay_sys_no" class="easyui-combobox" name="search_pay_sys_no" data-options="
								editable:false,valueField: 'value',
								textField: 'name',
								//url是下拉框请求显示的数据
								url:'${path }/enumset/dictionary/3' " style="width:100px;"/>  
						</td>
						<td style="text-align:right">业务类型：</td>
						<td>
							<input id="search_biz_type" class="easyui-combobox" name="search_biz_type" data-options="
								editable:false,valueField: 'value',
								textField: 'name',
								//url是下拉框请求显示的数据
								url:'${path }/enumset/dictionary/4' " style="width:100px;"/>  
						</td>
						</tr>
					</table>
				</form>
			</div>
			<!--搜索栏结束-->
			
			<!--搜索结果开始-->
			<div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
				<table  id="dg_tppChannelTRequest" class="easyui-datagrid" 
					data-options="rownumbers:true,pageSize:20,singleSelect:true,collapsible:true,sortName:'',sortOrder:'desc',pagination:'true',url:'${path }/channel/channelRequestlistData/null',method:'post',toolbar:toolbar,fit:'true',onLoadSuccess:function(){loadQueryParam();$(this).datagrid('fixRownumber');},onBeforeLoad:function(param){loadBefore(param)},onClickRow: function (rowIndex, rowData) {$(this).datagrid('unselectRow', rowIndex);}">
					<thead>
						<tr>
						 	<th data-options="field:'DETAIL_MESSAGE',width:60,align:'center'">详细信息</th>
						 	<!-- <th data-options="field:'DETAIL_RESULT',width:60,align:'center'">查看结果</th> -->
							<th data-options="field:'REQ_ID',width:150,sortable:'true',align:'left'">TPP通道请求流水号</th>
							<th data-options="field:'TRANSFER_FLOW',width:120,sortable:'true',align:'left'">交易流水号</th>
							<th data-options="field:'PAY_TRANS_FLOW',width:120,sortable:'true',align:'left'">第三方支付平台交易流水号</th>
							<th data-options="field:'CREATE_TIME',width:120,sortable:'true',align:'left'">创建时间</th>
							<th data-options="field:'UPDATE_TIME',width:120,sortable:'true',align:'left'">修改时间</th>
							<th data-options="field:'STATUS',width:120,sortable:'true',align:'left'">请求状态</th>
							<!-- <th data-options="field:'PAY_SYS_NO',width:120,sortable:'true',align:'left'">支付渠道编码</th> -->
							<th data-options="field:'PAY_SYS_NAME',width:120,sortable:'true',align:'left'">第三方支付平台</th>
							<th data-options="field:'BIZ_TYPE',width:120,sortable:'true',align:'left'">业务类型</th>
							<!-- <th data-options="field:'BIZ_SYS_NO',width:120,sortable:'true',align:'left'">业务系统编码</th> -->
							<th data-options="field:'APP_NAME',width:120,sortable:'true',align:'left'">业务系统</th>
							<!-- <th data-options="field:'SPARE1',width:120,sortable:'true',align:'left'">备注1</th>
							<th data-options="field:'SPARE2',width:120,sortable:'true',align:'left'">备注2</th> -->
							<!-- <th data-options="field:'INFO_CATEGORY_CODE',width:120,sortable:'true',align:'left'">信息类别编码</th> -->
							<th data-options="field:'INFO_CATEGORY_NAME',width:120,sortable:'true',align:'left'">信息类别</th>
							<th data-options="field:'FAIL_REASON',width:1000,sortable:'true',align:'left'">失败原因</th>
							
						</tr>
					</thead>
				</table>
			</div>
			<!--搜索栏结果end-->

<script>
var tppChannelTRequest_list ={};
var toolbar = [{
				id : 'search_btn',
				text : '查询',
				iconCls : 'icon-search'
			},'-',{
				id : 'clear_btn',
				text : '清除',
				iconCls : 'icon-remove'
			}];
			
function CurentTime()
{ 
    var now = new Date();
   
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日
   
    var hh = now.getHours();            //时
    var mm = now.getMinutes();          //分
   
    var clock = year + "-";
   
    if(month < 10)
        clock += "0";
   
    clock += month + "-";
   
    if(day < 10)
        clock += "0";
    clock += day ;
    return(clock); 
} 
function loadBefore(param) {
	var search_begin_date = $("#search_begin_date").datebox("getValue");
	var search_end_date = $("#search_end_date").datebox("getValue");
	if (search_begin_date == '' && search_end_date == '') {
		param.search_begin_date=CurentTime();
		param.search_end_date=CurentTime();
  	}
}
function loadQueryParam(){
	$('#dg_tppChannelTRequest').datagrid("options").queryParams={
		 'search_transfer_flow':$("input[name='search_transfer_flow']").val(),
		 'search_req_id':$("input[name='search_req_id']").val(),
		 'search_begin_date':$("#search_begin_date").datebox("getValue"),
		 'search_end_date':$("#search_end_date").datebox("getValue"),
		 'search_status':$('#search_status').combobox("getValue"),
		 'search_pay_sys_no':$('#search_pay_sys_no').combobox("getValue"),
		 'search_biz_sys_no':$('#search_biz_sys_no').combobox("getValue"),
		 'search_biz_type':$('#search_biz_type').combobox("getValue")
	};
  	
}
jQuery(function($){
	$("#search_begin_date").datebox('setValue', CurentTime());
	$("#search_end_date").datebox('setValue', CurentTime());

	//定义构造查询
	tppChannelTRequest_list.buildQueryParams=function(){
		$('#dg_tppChannelTRequest').datagrid("options").queryParams={
				 'search_transfer_flow':$("input[name='search_transfer_flow']").val(),
				 'search_req_id':$("input[name='search_req_id']").val(),
				 'search_begin_date':$("#search_begin_date").datebox("getValue"),
				 'search_end_date':$("#search_end_date").datebox("getValue"),
				 'search_status':$('#search_status').combobox("getValue"),
				 'search_pay_sys_no':$('#search_pay_sys_no').combobox("getValue"),
				 'search_biz_sys_no':$('#search_biz_sys_no').combobox("getValue"),
				 'search_biz_type':$('#search_biz_type').combobox("getValue")
		};
	}

	//重新按照条件刷新查询内容
	$('#search_btn').click(function(){
		var flag = checkDate();
		if (!flag) {
			return ;
		}
		tppChannelTRequest_list.buildQueryParams();
		$('#dg_tppChannelTRequest').datagrid('load');
	});
	
    //重置查询条件并刷新内容
	$('#clear_btn').click(function(){
		//清空查询条件
			$("input[name='search_transfer_flow']").val("");
			$("input[name='search_req_id']").val("");
			$("#search_begin_date").datebox('setValue', CurentTime());
			$("#search_end_date").datebox('setValue', CurentTime());
			$('#search_status').combobox('setValue','');
			$('#search_pay_sys_no').combobox('setValue','');
			$('#search_biz_sys_no').combobox('setValue','');
			$('#search_biz_type').combobox('setValue','');
		tppChannelTRequest_list.buildQueryParams();
		$('#dg_tppChannelTRequest').datagrid('load');
	});
	
	
	
	
});
//详细
function viewMessage (reqId){
	$('<div id="dialog_holder"></div>').dialog({
	    title: '报文明细',
	    width: 800,
	    height: 400,
	    href: '${path}/channel/message/channelMessageList/'+reqId,
	    modal: true,
	    method: "POST",
	    params:{},
		onClose: function(){
			$(this).dialog("destroy");
		},
	    buttons: [{
	    	text: "关闭",
	    	handler: function(e){
	    		$(this).dialog("close");
	    	}
	    }]
	});
}
//详细
function viewResult (tradeFlow){
	$('<div id="dialog_holder"></div>').dialog({
	    title: '查看结果',
	    width: 800,
	    height: 400,
	    href: '${path}/channel/tradeResult/tradeResultList/'+tradeFlow,
	    modal: true,
	    method: "POST",
	    params:{},
		onClose: function(){
			$(this).dialog("destroy");
		},
	    buttons: [{
	    	text: "关闭",
	    	handler: function(e){
	    		$(this).dialog("close");
	    	}
	    }]
	});
}
function checkDate(){
	var beginDate = $("#search_begin_date").datebox("getValue");
	var endDate = $("#search_end_date").datebox("getValue");
	if (beginDate != '' && beginDate != null) {
		if (endDate == '' || endDate == null) {
			$.messager.alert('提示','请选择结束日期'); 
			return false;
		}
	}
	if (endDate != '' && endDate != null) {
		if (beginDate == '' || beginDate == null) {
			$.messager.alert('提示','请选择开始日期');
			return false;
		}
	}
	if (beginDate != '' && beginDate != null && endDate != '' && endDate != null) {
		beginDate = new Date(beginDate);
		endDate = new Date(endDate);
		var day = endDate.diff(beginDate);
		if (day < 0) {
			$.messager.alert("提示","查询开始时间不能大于结束时间！");
			return false;
		}
		if (day > 7){
			$.messager.alert('提示','请查询七日内数据');
			return false;
		}
	}
	return true;
}
Date.prototype.diff = function(date){
  return (this.getTime() - date.getTime())/(24 * 60 * 60 * 1000);
}
</script>
</body>
</html>