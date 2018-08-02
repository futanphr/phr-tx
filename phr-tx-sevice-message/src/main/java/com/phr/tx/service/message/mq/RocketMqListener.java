package com.phr.tx.service.message.mq;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * MQ监听器
 */
@Service
public class RocketMqListener implements MessageListenerConcurrently {

	private Logger logger = LoggerFactory.getLogger(RocketMqListener.class);
	@Autowired
	DefaultMQPushConsumer defaultMQPushConsumer;
	@Autowired
	DefaultMQPushConsumer defaultMQPushConsumer2;

	/**
	 * 注册此listener到consumer中，并启动消费者。
	 * 一个defaultMQPushConsumer对应一个rocketMq服务器，对应一个Listener，对应多个topic.
	 * 不能一个defaultMQPushConsumer对应多个个Listener。因为会在下面的方法中调用消费者的start方法。
	 */
	@PostConstruct
	public void registerListener() {
		defaultMQPushConsumer.registerMessageListener(this);
		defaultMQPushConsumer2.registerMessageListener(this);
		try {
			defaultMQPushConsumer.start();
			defaultMQPushConsumer2.start();
		} catch (MQClientException e) {
			logger.error("【MQ】监听MQ消息", "", "rocketMq消费者启动异常，请处理!", "", e);
		}
	}

	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		logger.warn("【MQ】监听MQ消息开始", "");
		for (MessageExt messageExt : msgs) {
			logger.warn("【MQ】监听MQ消息{}", messageExt.getTags());
//            //接受授信推送过来的风控回流信息
			if ("auth_tags".equals(messageExt.getTags())) {
				logger.warn("【MQ】监听MQ消息 开始处理{}", "处理授信逻辑");
				return dealRiskMqMessage(messageExt);
			}
//          //接受信审推送过来的风控回流信息
			if ("check".equals(messageExt.getTags())) {
				logger.warn("【MQ】监听MQ消息开始", "处理信审逻辑");
				return dealCheckMqMessage(messageExt);
			}
		}
		logger.warn("【MQ】监听MQ消息结束", "");
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

	/**
	 * 处理授信推送的风控回流的消息
	 * 
	 * @param msg
	 * @return
	 */
	private ConsumeConcurrentlyStatus dealRiskMqMessage(MessageExt msg) {
		String customerId = null;
		try {
			String msgId = msg.getMsgId();
			byte[] bytes = msg.getBody();
			String data = new String(bytes);
			logger.warn("【MQ】接收授信推送的风控消息{}", "返回信息msgId=【" + msgId + "】");
			Map<String, String> map = JSON.parseObject(data, new TypeReference<Map<String, String>>() {
			});
			customerId = map.get("customerId");
			logger.info("customerId=【" + customerId + "】");
//			acceptRiskBackFlowService.dealRiskMqMessage(msgId, Long.valueOf(customerId), flowId, processKey, Long.valueOf(channelId), Long.valueOf(applyTime),proId,
//					riskRata);
			System.out.println("===>auth,customerId=【" + customerId + "】");
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		} catch (Exception e) {
			logger.error("【MQ】接收授信推送的风控消息", e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
	}

	/**
	 * 处理信审审核结果推送的消息
	 * 
	 * @param msg
	 * @return
	 */
	private ConsumeConcurrentlyStatus dealCheckMqMessage(MessageExt msg) {
		String customerId = null;
		try {
			String msgId = msg.getMsgId();
			byte[] bytes = msg.getBody();
			String data = new String(bytes);
			logger.warn("【MQ】接收信审审核结果消息{}", "返回信息msgId=【" + msgId + "】,data=【" + data + "】");
			Map<String, String> map = JSON.parseObject(data, new TypeReference<Map<String, String>>() {
			});
			customerId = map.get("userId");
			logger.info("customerId=【" + customerId + "】");
//			acceptRiskBackFlowService.dealRiskMqMessage(msgId, Long.valueOf(customerId), flowId, processKey, Long.valueOf(channelId), Long.valueOf(applyTime),proId,
//					riskRata);
			System.out.println("===>check,customerId=【" + customerId + "】");
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		} catch (Exception e) {
			logger.error("【MQ】接收信审审核结果消息", e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
//		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
