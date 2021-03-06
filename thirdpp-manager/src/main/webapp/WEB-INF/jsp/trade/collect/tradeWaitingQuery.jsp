<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="content-body">
		<div class="search-panel-content">
			<form id="addTppTradeTWaitingQueryFrom" name="addTppTradeTWaitingQueryFrom" method="post">
				<div class="search-panel-bd">
				 <table class="search-table">
				 	<tr>
						<th class="wd-20"><label>交易流水号</label></th>
						<td>
							<input type="text" id="TRADE_FLOW" name="TRADE_FLOW" value="${collectInfo.TRADE_FLOW}" class="easyui-validatebox" readonly="true" style="width:200px;"/>
						</td>
					</tr>
					<tr>
						<th class="wd-20"><label>业务类型</label></th>
						<td>
							<input type="text" id="BIZ_TYPE" name="BIZ_TYPE" value="${collectInfo.BIZ_TYPE}" class="easyui-validatebox" readonly="true" style="width:200px;"/>
						</td>
					</tr>
					<tr>
						<th class="wd-20"><label>业务系统</label></th>
						<td>
							<input type="text" id="BIZ_SYS_NAME" name="BIZ_SYS_NAME" value="${collectInfo.BIZ_SYS_NAME}" class="easyui-validatebox" readonly="true" style="width:200px;"/>
						</td>
					</tr>
					<tr>
						<th class="wd-20"><label>第三方支付平台</label></th>
						<td>
							<input type="text" id="PAY_SYS_NAME" name="PAY_SYS_NAME" value="${collectInfo.PAY_SYS_NAME}" class="easyui-validatebox" readonly="true" style="width:200px;"/>
						</td>
					</tr>
					<tr>
						<th class="wd-20"><label>信息类别</label></th>
						<td>
							<input type="text" id="INFO_CATEGORY_NAME" name="INFO_CATEGORY_NAME" value="${collectInfo.INFO_CATEGORY_NAME}" class="easyui-validatebox" readonly="true" style="width:200px;"/>
						</td>
					</tr>
					<tr>
						<th class="wd-20"><label>付款方账户编号</label></th>
						<td>
							<input type="text" id="PAYER_ACCOUNT_NO" name="PAYER_ACCOUNT_NO" value="${collectInfo.PAYER_ACCOUNT_NO}" class="easyui-validatebox" readonly="true" style="width:200px;"/>
						</td>
					</tr>
					<tr>
						<th class="wd-20"><label>查询模块名称</label></th>
						<td>
							<input type="hidden" id="notify_query_trade_flow" name="notify_query_trade_flow"/>
							<!-- <input type="text" id="query_module_name" name="query_module_name" class="easyui-validatebox" data-options="required:true,missingMessage:'不能为空',novalidate: true,validType:'maxLength[50]'" style="width:200px;"/> -->
							<input id="query_module_name" class="easyui-combobox" name="query_module_name" data-options="
								editable:false,valueField: 'name',
								textField: 'name',
								//url是下拉框请求显示的数据
								url:'${path }/enumset/dictionary2/7',
								value:'请选择'" style="width:200px;"/> 
							<span style="color:red">*</span>
						</td>
					</tr>
					<tr>
						<th class="wd-20"><label>运营类型</label></th>
						<td>
							<input id="op_mode" class="easyui-combobox" name="op_mode" data-options="
								editable:false,valueField: 'value',
								textField: 'name',
								//url是下拉框请求显示的数据
								url:'${path }/enumset/OPModeList',
								value:'0'" style="width:200px;"/> 
							<span style="color:red">*</span>
						</td>
					</tr>
				  </table>
				</div>
			</form>
		</div>
</div>