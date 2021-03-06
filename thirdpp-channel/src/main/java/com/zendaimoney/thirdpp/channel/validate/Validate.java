package com.zendaimoney.thirdpp.channel.validate;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.zendaimoney.thirdpp.channel.exception.PlatformErrorCode;
import com.zendaimoney.thirdpp.common.vo.Response;

public class Validate<T> {

	private static Validate uniqueInstance = null;

	private static ValidatorFactory factory = null;

	private static Validator validator = null;


	/**
	 * 定义私有构造方法.
	 */
	private Validate() {

	}

	/**
	 * 单例模式.
	 * 
	 * @return
	 */
	public static Validate getInstance() {

		if (uniqueInstance == null) {
			factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			uniqueInstance = new Validate();
		}

		return uniqueInstance;

	}

	/**
	 * 合法行校验
	 * 
	 * @param arg0
	 *            T
	 * @param systemCode
	 *            进行检验系统编码
	 * @param arg1
	 *            Class<T>
	 */
	public Response validate(T arg0, Class<T>... arg1) {
		Response response = new Response(Response.SUCCESS_RESPONSE_CODE);
		if (arg0 == null) {
			response = new Response(PlatformErrorCode.VALIDATE_ISNULL.getErrorCode(),
					"Object is null");
			return response;
		}
		String message = "";
		String[] array = null;

		Set<ConstraintViolation<T>> constraintViolations = validator.validate(
				arg0, arg1);
		for (ConstraintViolation<T> constraintViolation : constraintViolations) {
			message = constraintViolation.getMessage();
			if (message != null) {
				array = message.split(",");
				if (array != null && array.length == 2) {
					response = new Response(array[0].trim(),
							constraintViolation.getPropertyPath() + " "
									+ array[1]);
				} else {
					response = new Response(PlatformErrorCode.ERROR_CODE_MESSAGE_FORMAT_BAD.getErrorCode(),
							constraintViolation.getPropertyPath() + " "
									+ PlatformErrorCode.ERROR_CODE_MESSAGE_FORMAT_BAD.getDefaultMessage());
				}
			} else {
				response = new Response(PlatformErrorCode.ERROR_CODE_MESSAGE_NULL.getErrorCode(),
						constraintViolation.getPropertyPath() + " "
								+ PlatformErrorCode.ERROR_CODE_MESSAGE_NULL.getErrorCode());
			}
			return response;
		}
		return response;
	}
}
