package com.zendaimoney.thirdpp.transfer.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.zendaimoney.thirdpp.common.enums.BizType;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface TransferActionAnnotation {
	/**
	 * 业务类型
	 * @return
	 */
	public BizType bizType();
	

}
