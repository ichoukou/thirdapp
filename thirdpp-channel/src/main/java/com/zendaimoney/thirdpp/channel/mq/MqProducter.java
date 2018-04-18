package com.zendaimoney.thirdpp.channel.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zendaimoney.thirdpp.channel.util.JSONHelper;

@Component
public class MqProducter {

	@Autowired
	private AmqpTemplate amqpTemplate;

	/**
	 * 
	 * @param keyName
	 * @param message
	 */
	public void sendMessage(String keyName, TaskMergeVo vo) {
		if (vo != null) {
			amqpTemplate.convertAndSend(keyName, JSONHelper.bean2json(vo));
		}

	}

}
