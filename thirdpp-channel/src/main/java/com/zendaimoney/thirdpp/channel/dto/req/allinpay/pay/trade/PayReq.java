package com.zendaimoney.thirdpp.channel.dto.req.allinpay.pay.trade;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.zendaimoney.thirdpp.channel.dto.req.ReqDto;
import com.zendaimoney.thirdpp.channel.pub.vo.BizReqVo;
import com.zendaimoney.thirdpp.channel.pub.vo.PayReqVo;
import com.zendaimoney.thirdpp.channel.util.ConfigUtil;
import com.zendaimoney.thirdpp.channel.util.JaxbBinder;
import com.zendaimoney.thirdpp.channel.util.ThirdPPCacheContainer;
import com.zendaimoney.thirdpp.channel.util.allinpay.AllinpayUtil;

/**
 * 代付业务请求数据传输对象
 * 
 * @author 00233197
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document")
@XmlRootElement(name = "AIPG")
public class PayReq extends ReqDto {

	/**
	 * 请求头
	 */
	@XmlElement(name = "INFO")
	private ReqHeader header;

	/**
	 * 请求体
	 */
	@XmlElement(name = "TRANS")
	private ReqBody body;

	public PayReq() {
		super();
	}

	/**
	 * 构造方法
	 * 
	 * @param collectReqVo
	 */
	public PayReq(BizReqVo vo) {

		PayReqVo payReqVo = (PayReqVo) vo;
		
		String merchantType = ThirdPPCacheContainer.sysInfoCategoryMap.get(
				vo.getInfoCategoryCode()).getMerchantType();
		// 报文头
		header = new ReqHeader();
		header.setLevel(ConfigUtil.getInstance().getAllinpayConfig().getLevel());
		header.setReqSn(payReqVo.getTradeFlow());
		// 用户名需要根据不同的第三方支付通道编码和商户号类型进行判断
		header.setUserName(ThirdPPCacheContainer.sysThirdChannelInfoMap.get(
				vo.getThirdType().getCode() + merchantType).getUserName());
		// 密码需要根据不同的第三方支付通道编码和商户号类型进行判断
		header.setUserPass(ThirdPPCacheContainer.sysThirdChannelInfoMap.get(
				vo.getThirdType().getCode() + merchantType).getUserPwd());
		header.setVersion(ConfigUtil.getInstance().getAllinpayConfig()
				.getVersion());
		// 报文体
		body = new ReqBody();
		
		// 第三方的业务类型
		body.setBusinessCode(ConfigUtil.getInstance().getAllinpayConfig().getPayforBusinessCode());
		body.setAccountName(payReqVo.getReceiverName());
		body.setAccountNo(payReqVo.getReceiverBankCardNo());
		// 将TPP系统银行卡类型转化为相应第三方平台银行卡类型
		body.setAccountType(ThirdPPCacheContainer.tppBankCardTypeToThirdBankCardTypeMap
				.get(vo.getThirdType().getCode()
						+ payReqVo.getReceiverBankCardType()));
		body.setAmount(AllinpayUtil.yuanConvertFen(payReqVo.getAmount())
				.toString());
		body.setId(payReqVo.getReceiverId());
		body.setIdType(ThirdPPCacheContainer.tppIdTypeToThirdIdTypeMap.get(vo
				.getThirdType().getCode() + payReqVo.getReceiverIdType()));
		// 将TPP系统银行编码转化为相应第三方平台银行编码
		body.setBankCode(ThirdPPCacheContainer.tppBankCodeToThirdBankCodeMap
				.get(vo.getThirdType().getCode()
						+ payReqVo.getReceiverBankCode()));
		// 商户号需要根据不同的业务系统来判断
		body.setMerchantId(ThirdPPCacheContainer.sysThirdChannelInfoMap.get(
				vo.getThirdType().getCode() + merchantType).getMerchantNo());
		// 设置币种, 将TPP系统币种转化为相应第三方平台币种
		body.setCurrency(ThirdPPCacheContainer.tppCurrencyToThirdCurrencyMap
				.get(vo.getThirdType().getCode() + payReqVo.getCurrency()));
		
		body.setAccountProp(ThirdPPCacheContainer.tppUserTypeTothirdUserTypeMap
				.get(payReqVo.getThirdType().getCode()
				+ payReqVo.getReceiverType()));

		// 其他
		this.setBizSys(vo.getBizSys());
		this.setBizType(vo.getBizType());
		this.setThirdType(vo.getThirdType());
		this.setChannelReqId(vo.getChannelReqId());
	}

	public ReqHeader getHeader() {
		return header;
	}

	public void setHeader(ReqHeader header) {
		this.header = header;
	}

	public ReqBody getBody() {
		return body;
	}

	public void setBody(ReqBody body) {
		this.body = body;
	}

	/**
	 * 对象转XML报文
	 * 
	 * @return
	 */
	public String encode() {
		JaxbBinder binder = new JaxbBinder(this.getClass());
		String xml = binder.toXml(this, "GBK");
		// System.out.println("===========" + xml);
		// binder.doValidate(xml, this.msgType.getCode());

		//return xml.substring(xml.indexOf("\n") + 1);
		//报文头中需要替换掉standalone="yes",这个需要根据具体情况来确定
		return xml.replaceFirst(JaxbBinder.REPLACE_STANDALONE, "");
	}
	
	public static void main(String args[]){
		ReqHeader header = new ReqHeader();
		ReqBody body = new ReqBody();
		PayReq req = new PayReq();
		header.setLevel("1");
		req.setHeader(header);
		req.setBody(body);
		System.out.println(req.encode());
	}

}
