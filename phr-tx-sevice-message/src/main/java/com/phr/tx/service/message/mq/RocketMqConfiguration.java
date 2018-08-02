package com.phr.tx.service.message.mq;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;

/**
 * 配置spring 消费者和生产者
 *
 * @author :郝丹阳
 * @date: 2018-05-03
 */
@Configuration
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMqConfiguration {
	private static Logger logger = LoggerFactory.getLogger(RocketMqConfiguration.class);

	// mq服务器的地址
	private String namesrvAddr;
	// 监听的地址
	private String listenTopicsTags1;
	private String listenTopicsTags2;
	private int consumeThreadMin;
	private int consumeThreadMax;
	private String producerGroupName;
	private String mqConsumerGroupName;

	@Bean
	public DefaultMQProducer rockerMqProducer() {
		DefaultMQProducer producer = new DefaultMQProducer(producerGroupName);
		producer.setNamesrvAddr(namesrvAddr);
		return producer;
	}

	@Bean
	public DefaultMQPushConsumer defaultMQPushConsumer() {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(mqConsumerGroupName);
		consumer.setNamesrvAddr(namesrvAddr);
		// 程序第一次启动从消息队列头取数据
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		// 设置最小线程数
		consumer.setConsumeThreadMin(8);
		// 设置最大线程数
		consumer.setConsumeThreadMax(16);
		// 默认一次拿一条记录，为了控制事物
		consumer.setConsumeMessageBatchMaxSize(50);
		try {
			String[] listenTopicsTags = listenTopicsTags1.split("\\|");
			consumer.subscribe(listenTopicsTags[0], listenTopicsTags[1]);
		} catch (MQClientException e) {
			logger.error("【MQ】监听MQ消息", "设置消费者的时候发生异常", "", "", e);
		}
		return consumer;
	}

	@Bean
	public DefaultMQPushConsumer defaultMQPushConsumer2() {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("bangsheng_group");
		consumer.setNamesrvAddr(namesrvAddr);
		// 程序第一次启动从消息队列头取数据
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		// 设置最小线程数
		consumer.setConsumeThreadMin(8);
		// 设置最大线程数
		consumer.setConsumeThreadMax(16);
		// 默认一次拿一条记录，为了控制事物
		consumer.setConsumeMessageBatchMaxSize(50);
		try {
			String[] listenTopicsTags = listenTopicsTags2.split("\\|");
			consumer.subscribe(listenTopicsTags[0], listenTopicsTags[1]);
		} catch (MQClientException e) {
			logger.error("【MQ】监听MQ消息", "设置消费者的时候发生异常", "", "", e);
		}
		return consumer;
	}

	/**
	 * spring启动后会调用一次
	 */
	@PostConstruct
	public void start() {
		try {
			rockerMqProducer().start();
		} catch (MQClientException e) {
			logger.error("【MQ】监听MQ消息", "启动mq生产者的时候发生异常!!", "", "", e);
		}
	}

	@PreDestroy
	public void shutDown() {
		// This method shuts down this producer instance and releases related resources.
		rockerMqProducer().shutdown();

		// Shut down this client and releasing underlying resources.
		defaultMQPushConsumer().shutdown();

	}

	public String getNamesrvAddr() {
		return namesrvAddr;
	}

	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	public String getListenTopicsTags1() {
		return listenTopicsTags1;
	}

	public void setListenTopicsTags1(String listenTopicsTags1) {
		this.listenTopicsTags1 = listenTopicsTags1;
	}

	public String getListenTopicsTags2() {
		return listenTopicsTags2;
	}

	public void setListenTopicsTags2(String listenTopicsTags2) {
		this.listenTopicsTags2 = listenTopicsTags2;
	}

	public int getConsumeThreadMin() {
		return consumeThreadMin;
	}

	public void setConsumeThreadMin(int consumeThreadMin) {
		this.consumeThreadMin = consumeThreadMin;
	}

	public int getConsumeThreadMax() {
		return consumeThreadMax;
	}

	public void setConsumeThreadMax(int consumeThreadMax) {
		this.consumeThreadMax = consumeThreadMax;
	}

	public String getProducerGroupName() {
		return producerGroupName;
	}

	public void setProducerGroupName(String producerGroupName) {
		this.producerGroupName = producerGroupName;
	}

	public String getMqConsumerGroupName() {
		return mqConsumerGroupName;
	}

	public void setMqConsumerGroupName(String mqConsumerGroupName) {
		this.mqConsumerGroupName = mqConsumerGroupName;
	}
}
