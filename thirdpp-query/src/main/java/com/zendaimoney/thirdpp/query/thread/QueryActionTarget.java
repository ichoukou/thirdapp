package com.zendaimoney.thirdpp.query.thread;

import java.lang.reflect.Field;

import com.zendaimoney.thirdpp.common.enums.BizType;
import com.zendaimoney.thirdpp.query.action.Action;

@SuppressWarnings("rawtypes")
public class QueryActionTarget {

	/**
	 * 
	 */
	private Class<? extends Action> actionClazz;
	
	//类属性集合
	private Field[] fields;

	//业务类型
	private BizType bizType;


	public QueryActionTarget(Class<? extends Action> actionClazz,BizType bizType) {
		this.actionClazz = actionClazz;
		this.bizType = bizType;
		setFields();
	}

	private void setFields() {
		this.fields = actionClazz.getDeclaredFields();
		for (Field field : fields)
			field.setAccessible(true);
	}

	public Class<? extends Action> getActionClazz() {
		return actionClazz;
	}


	public BizType getBizType() {
		return bizType;
	}


	public Field[] getFields() {
		return fields;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bizType == null) ? 0 : bizType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryActionTarget other = (QueryActionTarget) obj;
		if (bizType != other.bizType)
			return false;
		return true;
	}

}
