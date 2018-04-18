package com.zendaimoney.thirdpp.account.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface TranslateAnnotation {
	/**
	 * 文件后缀
	 * @return
	 */
	public String value();
	
}
