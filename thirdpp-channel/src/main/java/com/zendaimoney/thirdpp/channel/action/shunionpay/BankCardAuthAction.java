package com.zendaimoney.thirdpp.channel.action.shunionpay;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zendaimoney.thirdpp.channel.action.Action;
import com.zendaimoney.thirdpp.channel.annotations.ReqActionAnnotation;
import com.zendaimoney.thirdpp.channel.communication.Message;
import com.zendaimoney.thirdpp.channel.communication.shunionpay.ShunionpayCommunication;
import com.zendaimoney.thirdpp.channel.dto.req.ReqDto;
import com.zendaimoney.thirdpp.channel.dto.req.shunionpay.bankCardAuth.BankCardAuthReq;
import com.zendaimoney.thirdpp.channel.dto.resp.RespDto;
import com.zendaimoney.thirdpp.channel.dto.resp.shunionpay.bankCardAuth.BankCardAuthResp;
import com.zendaimoney.thirdpp.channel.entity.CollectInfo;
import com.zendaimoney.thirdpp.channel.entity.MessageInfo;
import com.zendaimoney.thirdpp.channel.entity.Request;
import com.zendaimoney.thirdpp.channel.entity.SysInfoCategory;
import com.zendaimoney.thirdpp.channel.entity.SysThirdChannelInfo;
import com.zendaimoney.thirdpp.channel.entity.TradeResult;
import com.zendaimoney.thirdpp.channel.exception.PlatformErrorCode;
import com.zendaimoney.thirdpp.channel.exception.PlatformException;
import com.zendaimoney.thirdpp.channel.pub.vo.BankCardAuthReqVo;
import com.zendaimoney.thirdpp.channel.pub.vo.BizReqVo;
import com.zendaimoney.thirdpp.channel.service.MessageService;
import com.zendaimoney.thirdpp.channel.service.RequestService;
import com.zendaimoney.thirdpp.channel.service.TradeResultService;
import com.zendaimoney.thirdpp.channel.util.ConfigUtil;
import com.zendaimoney.thirdpp.channel.util.Constants;
import com.zendaimoney.thirdpp.channel.util.Constants.MessageStatus;
import com.zendaimoney.thirdpp.channel.util.ExceptionUtil;
import com.zendaimoney.thirdpp.channel.util.JSONHelper;
import com.zendaimoney.thirdpp.channel.util.LogPrn;
import com.zendaimoney.thirdpp.channel.util.ThirdPPCacheContainer;
import com.zendaimoney.thirdpp.channel.util.shunionpay.ShunionpayUtil;
import com.zendaimoney.thirdpp.common.enums.BizType;
import com.zendaimoney.thirdpp.common.enums.ChannelCategory;
import com.zendaimoney.thirdpp.common.enums.ThirdType;
import com.zendaimoney.thirdpp.common.vo.Response;

/**
 * 银行卡验证
 * 
 * @author 00231257
 * 
 */
@ReqActionAnnotation(thirdType = ThirdType.SHUNIONPAY_ACCOUNT_AUTH, bizType = BizType.CERTIFICATION, channelCategory = ChannelCategory.AUTH)
@Component("com.zendaimoney.thirdpp.channel.action.shunionpay.BankCardAuthAction")
public class BankCardAuthAction extends Action {

	// 日志工具类
	private static final LogPrn logger = new LogPrn(BankCardAuthAction.class);

	@Autowired
	private ShunionpayCommunication shunionpayCommunication;

	@Autowired
	private RequestService requestService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private TradeResultService tradeResultService;

	public BankCardAuthAction() {
		super();
	}

	@Override
	protected ReqDto preProcess(BizReqVo vo) throws PlatformException {
		ReqDto dto = null;
		Request request = null;
		try {
			dto = new BankCardAuthReq(vo);
		} catch (Exception e) {
			logger.error("====", e);
			// 设置request-状态为“请求报文解析失败”
			request = new Request(vo.getChannelReqId(),
					MessageStatus.SEND_REQUEST_PARSE_ERROR.getCode(),
					ExceptionUtil.getExceptionPartyMessage(e));
			requestService.update(request);
			throw new PlatformException(PlatformErrorCode.VO_2_DTO_ERROR,
					ExceptionUtil.getExceptionMessage(e));
		}
		return dto;
	}

	@Override
	protected Message buildMessage(ReqDto dto, BizReqVo vo)
			throws PlatformException {
		Message message = new Message();
		Request request = null;

		BankCardAuthReq req = (BankCardAuthReq) dto;
		try {
			// 报文请求地址
			message.setUrl(ConfigUtil.getInstance().getShunionpayConfig().getAuthUrl());

			// 根据信息类别编码去查询信息类别表
			SysInfoCategory infoCategory = ThirdPPCacheContainer.sysInfoCategoryMap
					.get(vo.getInfoCategoryCode());

			// 根据通道编号 + 商户类型 取得通道信息对象
			SysThirdChannelInfo channelInfo = ThirdPPCacheContainer.sysThirdChannelInfoMap
					.get(vo.getThirdType().getCode()
							+ infoCategory.getMerchantType());
			
			// 签名之后的报文
			String sign = ShunionpayUtil.shunionAuthSign(ShunionpayUtil.xmlToMap(req.encode()), channelInfo.getCertPwd());
			req.setSignature(sign);

			// 请求报文，需要加密
			message.setRequestInfo(req.encode());

			// 请求参数对象Map
			Map<String, String> paraMap = ShunionpayUtil.xmlToMap(message
					.getRequestInfo());
			message.setMessageMap(paraMap);

			// 把TPP通道reqid放到Message.提供respHandler方法中
			message.setChannelReqId(dto.getChannelReqId());

			// 设置认证流水号
			message.setTradeSn(((BankCardAuthReqVo) vo).getAuthFlow());
		} catch (Exception e) {
			logger.error("====", e);
			// 设置request-状态为“请求报文解析失败”
			request = new Request(dto.getChannelReqId(),
					MessageStatus.SEND_REQUEST_PARSE_ERROR.getCode(),
					ExceptionUtil.getExceptionPartyMessage(e));
			requestService.update(request);
			throw new PlatformException(PlatformErrorCode.DTO_ENCODE_ERROR,
					ExceptionUtil.getExceptionMessage(e));
		}
		return message;
	}

	@Override
	protected Message process(BizReqVo vo, ReqDto dto) throws PlatformException {
		Message message = null;
		Request request = null;
		MessageInfo messageInfo = null;
		// 拼装报文
		message = buildMessage(dto, vo);
		// 准备发送报文前相关处理(修改Request状态-请求报文已发送、记录发送报文)
		try {
			// 设置request-状态为“请求报文已发送”
			request = new Request(dto.getChannelReqId(),
					MessageStatus.SEND_REQUEST.getCode());
			requestService.update(request);
			
			BankCardAuthReqVo bankCardAuthReqVo = (BankCardAuthReqVo) vo;
			// 记录请求报文
			messageInfo = new MessageInfo(dto.getChannelReqId(),
					MessageInfo.MSG_TYPE_Q, message.getRequestInfo(), dto
							.getThirdType().getCode(), bankCardAuthReqVo.getAuthFlow());
			// 如果报文需要记录的话
			if (Constants.MSG_IN_STORAGE_YES.equals(ConfigUtil.getInstance()
					.getShunionpayConfig().getMsgInStorage())) {
				messageService.insert(messageInfo);
				// 记录日志
			} else {
				logger.info("channelCategory:" + ChannelCategory.AUTH
						+ ",messageInfo:" + JSONHelper.bean2json(messageInfo));
			}
		} catch (Exception e) {
			logger.error("====", e);
			throw new PlatformException(PlatformErrorCode.DB_ERROR,
					ExceptionUtil.getExceptionMessage(e));
		}
		// 把报文发送第三方
		try {
			message = shunionpayCommunication.sendMessageByHttp(message, "UTF-8");
		} catch (Exception e) {
			logger.error("=================", e);
			// 设置http请求发送弄个异常状态码
			message.setHttpStatusCode(Constants.CommunicationStatus.HTTP_SEND_EXCEPTION
					.getCode());
			return message;
		}

		return message;
	}

	/**
	 * 响应处理
	 */
	@Override
	protected Response respHandler(Message message, BizReqVo bizReqVo)
			throws PlatformException {
		BankCardAuthResp dto = null;
		// 如果http請求操作成功的話
		if (Constants.CommunicationStatus.HTTP_OK_STATUS.getCode().equals(
				message.getHttpStatusCode())) {
			// 如果是报文解析异常
			try {
				dto = parseMessage(message, bizReqVo);
			} catch (PlatformException e) {
				// 设置状态:响应报文解析错误
				message.setHttpStatusCode(Constants.CommunicationStatus.PARSE_MESSAGE_ERROR
						.getCode());
				// 初始化异常信息
				message.setMessage(Constants.CommunicationStatus.PARSE_MESSAGE_ERROR
						.getDesc() + ",状态码:" + message.getHttpStatusCode());
				return parseMessageFailHandler(message, bizReqVo);
			}
			return communicationSuccessHandler(dto, message, bizReqVo);
			// 如果http请求发送异常
		} else if (Constants.CommunicationStatus.HTTP_SEND_EXCEPTION.getCode()
				.equals(message.getHttpStatusCode())) {
			// 初始化异常信息
			message.setMessage(Constants.CommunicationStatus.HTTP_SEND_EXCEPTION
					.getDesc() + ",状态码:" + message.getHttpStatusCode());
			return communicationThrowExceptionHandler(message, bizReqVo);
			// 调用第三方HTTP接口返回状态码不正确
		} else {
			// 初始化异常信息
			message.setMessage(Constants.CommunicationStatus.HTTP_NOT_OK_STATUS
					.getDesc() + ",状态码:" + message.getHttpStatusCode());
			return communicationFailHandler(message, bizReqVo);
		}
	}

	@Override
	protected BankCardAuthResp parseMessage(Message message, BizReqVo bizReqVo)
			throws PlatformException {
		Request request = null;
		BankCardAuthResp dto = null;
		try {
			dto = new BankCardAuthResp();
			dto = dto.decode(message.getResponseInfo());
			// 把业务系统编码设置到返回dto中
			dto.setBizSys(bizReqVo.getBizSys());
			// 把业务类型设置到返回dto中
			dto.setBizType(bizReqVo.getBizType());
			// 把第三方系统编码设置到返回dto中
			dto.setThirdType(bizReqVo.getThirdType());
			// 把通道操作流水号设置到返回dto中
			dto.setChannelReqId(bizReqVo.getChannelReqId());
			
			message.setResponseInfo(message.getResponseInfo());
		} catch (Exception e) {
			logger.error("====", e);
			// 设置request-状态为“响应报文解析失败”
			request = new Request(message.getChannelReqId(),
					MessageStatus.RESPONSE_PARSE_ERROR.getCode(),
					StringUtils.substring(e.getMessage(), 0 , 300));
			requestService.update(request);
			throw new PlatformException(PlatformErrorCode.DTO_DECODE_ERROR,
					ExceptionUtil.getExceptionMessage(e));
		}
		return dto;
	}

	@Override
	protected Response communicationFailHandler(Message message,
			BizReqVo bizReqVo) {
		// 返回调用方为认证处理中，对于这种状态来说需要TPP下一次明确告知对方(通过推送状态方式)该认证是否成功
		Response response = new Response(
				Constants.TppConstants.TRADE_STATE_FAILER.getCode(),
				Constants.ShunionAuthConstants.AUTH_MIDDLE_CODE.getDesc());
		// 设置操作流水号
		response.setFlowId(message.getChannelReqId());
		Request request = null;

		// 发送报文后相关处理(修改Request状态-请求报文发送失败)
		try {
			// 设置request-状态为“请求报文发送失败”
			request = new Request(message.getChannelReqId(),
					MessageStatus.SEND_REQUEST_ERROR.getCode(),
					message.getMessage());
			requestService.update(request);
		} catch (Exception e) {
			logger.error("==============", e);

		}
		return response;
	}

	@Override
	protected Response communicationSuccessHandler(RespDto dto,
			Message message, BizReqVo bizReqVo) {
		Response response = new Response(
				Constants.TppConstants.TRADE_STATE_FAILER.getCode(),
				message.getMessage());
		// 设置操作流水号
		response.setFlowId(message.getChannelReqId());
		BankCardAuthResp res = (BankCardAuthResp) dto;

		Request request = null;
		MessageInfo messageInfo = null;
		// 发送报文后相关处理(修改Request状态-响应报文已收到、记录响应报文)
		try {
			// 设置request-状态为“收到响应报文”
			request = new Request(message.getChannelReqId(),
					MessageStatus.RECEIVED_RESPONSE.getCode());
			requestService.update(request);
			
			BankCardAuthReqVo bankCardAuthReqVo = (BankCardAuthReqVo) bizReqVo;
			// 记录响应报文
			messageInfo = new MessageInfo(message.getChannelReqId(),
					MessageInfo.MSG_TYPE_S, message.getResponseInfo(), dto
							.getThirdType().getCode(), bankCardAuthReqVo.getAuthFlow());
			// 如果报文需要记录的话
			if (Constants.MSG_IN_STORAGE_YES.equals(ConfigUtil.getInstance()
					.getShunionpayConfig().getMsgInStorage())) {
				
				messageService.insert(messageInfo);
			} else {
				logger.info("channelCategory:" + ChannelCategory.TRADE
						+ ",messageInfo:" + JSONHelper.bean2json(messageInfo));
			}

		} catch (Exception e) {
			logger.error("==============", e);

		}

		// 需要转换失败描述的响应code
		String[] convertStatus = Constants.ShunionAuthConstants.AUTH_MIDDLE_CODE.getCode().split(",");
		
		// 业务逻辑处理
		// 认证结果
		TradeResult tradeResult = null;

		try {
			// header通道返回码
			String retCode = res.getRespcode();
			// 通道返回信息
			String retMsg = res.getRespmsg();
			// 通道返回流水号
			String payTransFlow = "";

			// 如果认证通道返回成功(响应码和认证码都成功)
			if (Constants.ShunionAuthConstants.AUTH_SUCCESS_CODE.getCode().equals(retCode)) {
				// 返回状态为-认证成功
				response.setCode(Constants.TppConstants.TRADE_STATE_SUCCESS.getCode());
				
				// 返回第三方响应码
				response.setBankRepCode(retCode);
				
				// 初始化认证结果
				tradeResult = new TradeResult(res.getThirdType().getCode(),
						message.getTradeSn(), payTransFlow, res.getBizType()
								.getCode(), retCode,
						(retMsg != null ? StringUtils.substring(retMsg, 0, 150)
								: ""),
						Constants.TppConstants.TRADE_STATE_SUCCESS.getCode(),
						message.getChannelReqId());
				
				
				// 得到最终结果处理
				finalResultHandler(tradeResult, null, message,
						response, bizReqVo);
				// 其他定义认证失败
			} else {
				
				// 返回状态为-认证失败
				response.setCode(Constants.TppConstants.TRADE_STATE_FAILER.getCode());
				// 返回第三方响应码
				response.setBankRepCode(retCode);
				
				// 如果响应码为1111,2222,9916 或者响应信息为空，认证描述统一处理转换为：操作超时，请重试
				if (StringUtils.isBlank(retMsg) || Arrays.asList(convertStatus).contains(retCode)) {
					
					retMsg = Constants.ShunionAuthConstants.AUTH_MIDDLE_CODE.getDesc();
				}
				// 设置第三方通道返回错误信息
				response.setMsg((retMsg != null ? StringUtils.substring(retMsg,
						0, 150) : ""));
				// 初始化认证结果
				tradeResult = new TradeResult(res.getThirdType().getCode(),
						message.getTradeSn(), payTransFlow, res.getBizType()
								.getCode(), retCode,
						(retMsg != null ? StringUtils.substring(retMsg, 0, 150)
								: ""),
						Constants.TppConstants.TRADE_STATE_FAILER.getCode(),
						message.getChannelReqId());
				// 得到最终结果处理
				finalResultHandler(tradeResult, null, message,
						response, bizReqVo);
			}
		} catch (Exception e) {
			logger.error("==============", e);
		}
		return response;
	}

	/**
	 * 响应报文解析失败处理(此方法不能向外抛出异常，方法内部发生异常必须在方法内部捕获并以repsonse返回)
	 * 
	 * @param message
	 * @return
	 */
	@Override
	protected Response parseMessageFailHandler(Message message,
			BizReqVo bizReqVo) {
		// 返回调用方为认证处理中，对于这种状态来说需要TPP下一次明确告知对方(通过推送状态方式)该认证是否成功
		Response response = new Response(
				Constants.TppConstants.TRADE_STATE_FAILER.getCode(),
				Constants.ShunionAuthConstants.AUTH_MIDDLE_CODE.getDesc());
		// 设置操作流水号
		response.setFlowId(message.getChannelReqId());
		return response;

	}

	/**
	 * communication提交抛出异常处理(此方法不能向外抛出异常，方法内部发生异常必须在方法内部捕获并以repsonse返回)
	 * 
	 * @param message
	 * @return
	 */
	@Override
	protected Response communicationThrowExceptionHandler(Message message,
			BizReqVo bizReqVo) {
		// 返回调用方为认证处理中，对于这种状态来说需要TPP下一次明确告知对方(通过推送状态方式)该认证是否成功
		Response response = new Response(
				Constants.TppConstants.TRADE_STATE_FAILER.getCode(),
				Constants.ShunionAuthConstants.AUTH_MIDDLE_CODE.getDesc());
		// 设置操作流水号
		response.setFlowId(message.getChannelReqId());
		Request request = null;

		// 发送报文后相关处理(修改Request状态-请求报文发送失败)
		try {
			// 设置request-状态为“请求报文发送失败”
			request = new Request(message.getChannelReqId(),
					MessageStatus.SEND_REQUEST_ERROR.getCode(),
					message.getMessage());
			requestService.update(request);
		} catch (Exception e) {
			logger.error("==============", e);

		}

		return response;
	}
	
	/**
	 * 认证最终结果处理
	 * 
	 * @param tradeResult
	 * @param collectInfo
	 */
	private void finalResultHandler(TradeResult tradeResult,
			CollectInfo collectInfo, Message message, Response response,
			BizReqVo bizReqVo) {
		try {
			
			tradeResultService.insert(tradeResult);
		} catch (Exception e) {
			// 如果本次失败的话，返回业务端状态为-认证失败
			response.setCode(Constants.TppConstants.TRADE_STATE_FAILER.getCode());
			response.setMsg(Constants.ShunionAuthConstants.AUTH_MIDDLE_CODE.getDesc());
			logger.error("认证最终结果处理异常", e);
		}
	}

}
