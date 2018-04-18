package com.zendaimoney.thirdpp.channel.entity;

import java.io.Serializable;

import com.zendaimoney.thirdpp.channel.pub.vo.*;
import com.zendaimoney.thirdpp.channel.util.Constants.MessageStatus;
import com.zendaimoney.thirdpp.channel.util.IDGenerateUtil;

/**
 * TPP通道请求对象
 * 
 * @author 00231257
 *
 */
public class Request implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7731958054978050377L;

	/**
	 * 请求流水号
	 */
	private String reqId;

	/**
	 * 交易流水号
	 */
	private String transferFlow;

	/**
	 * 支付通道流水号
	 */
	private String payTransFlow;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 修改时间
	 */
	private String updateTime;

	/**
	 * 状态(0 初始化 1 请求报文解析失败 2 请求报文已发送 3 请求报文发送失败 4 收到相应报文 5 响应报文解析失败 6 响应报文已发送 7
	 * 响应报文发送失败 )
	 */
	private String status;

	/**
	 * 支付渠道编码
	 */
	private String paySysNo;

	/**
	 * 业务类型
	 */
	private String bizType;

	/**
	 * 业务系统编号
	 */
	private String bizSysNo;

	/**
	 * 备用字段1
	 */
	private String spare1;

	/**
	 * 备用字段2
	 */
	private String spare2;
	
	/**
	 * 发送失败原因
	 */
	private String failReason;
	
	/**
	 * 信息类别
	 */
	private String infoCategoryCode;
	
	public Request(){
		
	}

	public Request(CollectReqVo collectReqVo) {
		// 业务系统编号
		bizSysNo = collectReqVo.getBizSys().getCode();
		// 业务类型
		bizType = collectReqVo.getBizType().getCode();
		// 支付渠道编码
		paySysNo = collectReqVo.getThirdType().getCode();
		// 记录操作流水号
		reqId = IDGenerateUtil.createReqId();
		// 设置状态
		status = MessageStatus.MESSAGE_INIT.getCode();
		// 记录交易流水号
		transferFlow = collectReqVo.getTradeFlow();
		// 把通道请求ID设置到业务对象中方便下一步对request状态更新操作
		collectReqVo.setChannelReqId(reqId);
		
		//设置信息类别
		infoCategoryCode = collectReqVo.getInfoCategoryCode();

	}
	
	public Request(QueryReqVo queryReqVo) {
		// 业务系统编号
		bizSysNo = queryReqVo.getBizSys().getCode();
		// 业务类型
		bizType = queryReqVo.getBizType().getCode();
		// 支付渠道编码
		paySysNo = queryReqVo.getThirdType().getCode();
		// 记录操作流水号
		reqId = IDGenerateUtil.createReqId();
		// 设置状态
		status = MessageStatus.MESSAGE_INIT.getCode();
		// 记录交易流水号
		transferFlow = queryReqVo.getTradeFlow();
		// 把通道请求ID设置到业务对象中方便下一步对request状态更新操作
		queryReqVo.setChannelReqId(reqId);
		
		//设置信息类别
		infoCategoryCode = queryReqVo.getInfoCategoryCode();

	}
	
	public Request(BankCardAuthReqVo bankCardAuthReqVo) {
		// 业务系统编号
		bizSysNo = bankCardAuthReqVo.getBizSys().getCode();
		// 业务类型
		bizType = bankCardAuthReqVo.getBizType().getCode();
		// 支付渠道编码
		paySysNo = bankCardAuthReqVo.getThirdType().getCode();
		// 记录操作流水号
		reqId = IDGenerateUtil.createReqId();
		// 设置状态
		status = MessageStatus.MESSAGE_INIT.getCode();
		// 记录认证流水号
		transferFlow = bankCardAuthReqVo.getAuthFlow();
		// 把通道请求ID设置到业务对象中方便下一步对request状态更新操作
		bankCardAuthReqVo.setChannelReqId(reqId);
		
		//设置信息类别
		infoCategoryCode = bankCardAuthReqVo.getInfoCategoryCode();

	}
	
	public Request(BankCardBinQueryReqVO binQueryReqVO) {
		// 业务系统编号
		bizSysNo = binQueryReqVO.getBizSys().getCode();
		// 业务类型
		bizType = binQueryReqVO.getBizType().getCode();
		// 支付渠道编码
		paySysNo = binQueryReqVO.getThirdType().getCode();
		// 记录操作流水号
		reqId = IDGenerateUtil.createReqId();
		// 设置状态
		status = MessageStatus.MESSAGE_INIT.getCode();
		// 查询流水号
		transferFlow = binQueryReqVO.getQueryFlow();
		// 把通道请求ID设置到业务对象中方便下一步对request状态更新操作
		binQueryReqVO.setChannelReqId(reqId);
		
		
		//设置信息类别
		infoCategoryCode = binQueryReqVO.getInfoCategoryCode();

	}

	public Request(String reqId, String status) {
		this.reqId = reqId;
		this.status = status;
	}
	
	public Request(String reqId, String status,String failReason) {
		this.reqId = reqId;
		this.status = status;
		this.failReason = failReason;
	}
	
	public Request(PayReqVo payReqVo) {
		// 业务系统编号
		bizSysNo = payReqVo.getBizSys().getCode();
		// 业务类型
		bizType = payReqVo.getBizType().getCode();
		// 支付渠道编码
		paySysNo = payReqVo.getThirdType().getCode();
		// 记录操作流水号
		reqId = IDGenerateUtil.createReqId();
		// 设置状态
		status = MessageStatus.MESSAGE_INIT.getCode();
		// 记录交易流水号
		transferFlow = payReqVo.getTradeFlow();
		// 把通道请求ID设置到业务对象中方便下一步对request状态更新操作
		payReqVo.setChannelReqId(reqId);

		// 设置信息类别
		infoCategoryCode = payReqVo.getInfoCategoryCode();
	}

	public Request(SignMsgReqVo signMsgReqVo){
		// 业务系统编号
		this.bizSysNo = signMsgReqVo.getBizSys().getCode();
		// 业务类型
		this.bizType = signMsgReqVo.getBizType().getCode();
		//设置信息类别
		this.infoCategoryCode = signMsgReqVo.getInfoCategoryCode();
		// 支付渠道编码
		this.paySysNo = signMsgReqVo.getThirdType().getCode();
		// 请求流水号
		this.reqId = IDGenerateUtil.createReqId();
		// TPP交易流水号
		this.transferFlow = signMsgReqVo.getTradeFlow();
		// 请求状态
		this.status = MessageStatus.MESSAGE_INIT.getCode();
		// 把通道请求ID设置到业务对象中方便下一步对request状态更新操作
		signMsgReqVo.setChannelReqId(this.reqId);
	}

	public Request(SignReqVo signReqVo){
		// 业务系统编号
		this.bizSysNo = signReqVo.getBizSys().getCode();
		// 业务类型
		this.bizType = signReqVo.getBizType().getCode();
		//设置信息类别
		this.infoCategoryCode = signReqVo.getInfoCategoryCode();
		// 支付渠道编码
		this.paySysNo = signReqVo.getThirdType().getCode();
		// 请求流水号
		this.reqId = IDGenerateUtil.createReqId();
		// TPP交易流水号
		this.transferFlow = signReqVo.getTradeFlow();
		// 请求状态
		this.status = MessageStatus.MESSAGE_INIT.getCode();
		// 把通道请求ID设置到业务对象中方便下一步对request状态更新操作
		signReqVo.setChannelReqId(this.reqId);
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getTransferFlow() {
		return transferFlow;
	}

	public void setTransferFlow(String transferFlow) {
		this.transferFlow = transferFlow;
	}

	public String getPayTransFlow() {
		return payTransFlow;
	}

	public void setPayTransFlow(String payTransFlow) {
		this.payTransFlow = payTransFlow;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaySysNo() {
		return paySysNo;
	}

	public void setPaySysNo(String paySysNo) {
		this.paySysNo = paySysNo;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getBizSysNo() {
		return bizSysNo;
	}

	public void setBizSysNo(String bizSysNo) {
		this.bizSysNo = bizSysNo;
	}

	public String getSpare1() {
		return spare1;
	}

	public void setSpare1(String spare1) {
		this.spare1 = spare1;
	}

	public String getSpare2() {
		return spare2;
	}

	public void setSpare2(String spare2) {
		this.spare2 = spare2;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	
}
