package com.phr.tx.service.message.mq;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.phr.common.utils.StringUtils;

/**
 * FLUSH_DISK_TIMEOUT：表示如果Broker配置了FlushDiskType（刷盘策略）为SYNC_FLUSH（同步刷盘），并且Broker在默认时间（syncFlushTimeout刷盘超时时间）内没有完成刷新磁盘则返回该状态。
 * FLUSH_SLAVE_TIMEOUT：表示如果Broker配置了SYNC_MASTER，没有在默认时间（syncFlushTimeout刷盘超时时间）内与主Broker完成同步，则返回该状态。
 * SLAVE_NOT_AVAILABLE：表示Broker配置了SYNC_MASTER，但是没有配置从库，则返回该状态。
 * SEND_OK：表示成功。但是它不代表这可靠，为了确保不会丢失任何消息，您还应该启用SYNC_MASTER或SYNC_FLUSH（指刷盘策略）。
 * Duplication or Missing
 * <p>
 * 如果投递消息失败，返回状态为：FLUSH_DISK_TIMEOUT 或者 FLUSH_SLAVE_TIMEOUT
 * <p>
 * 如果选择继续执行程序，则消息丢失。
 * 否则选择重发该消息，但是可能会导致消息重复。建议重发，因为可以通过其他手段来规避消息重复消费问题。
 */
@Service
public class MqSenderImpl {

    private Logger LOGGER = LoggerFactory.getLogger(MqSenderImpl.class);
    public static final String DEFAULT_CHARSET = "UTF-8";
    @Resource
    private DefaultMQProducer producer;


    /**
     * 发送消息
     *
     * @param data    消息内容
     * @param topic   主题
     * @param tags    标签
     * @param keys    唯一主键
     */
    public boolean sendMessage(String data, String topic, String tags,String userId) {
        SendResult result = null;
        try {
            byte[] messageBody = data.getBytes(DEFAULT_CHARSET);

            Message mqMsg = new Message();
            mqMsg.setTopic(topic);
            if (StringUtils.isNotNull(tags)) {
                mqMsg.setTags(tags);
            }
            mqMsg.setBody(messageBody);
//            LogUtil.warn(LogConstant.ID_CUSTOMERID, userId, LogConstant.MODEL_QUOTA, "MQ通知定制化推送额度", "开始......");
            result = producer.send(mqMsg);
//            LogUtil.warn(LogConstant.ID_CUSTOMERID, userId, LogConstant.MODEL_QUOTA, "MQ通知定制化推送额度", "返回结果"+result);
            return true;
        } catch (Exception e) {
            LOGGER.error("MQ: 生产者发送消息 发生异常,发送内容={}", data, e);
        } finally {
            LOGGER.warn("MQ: 生产者发送消息 topic={},tags={},发送内容={},结果={}", topic, tags, data, result);
        }
        return false;
    }
    /**
     * 发送广播消息
     *
     * @param data    消息内容
     * @param topic   主题
     * @param tags    标签
     * @param keys    唯一主键
     */
    public boolean sendBroadcastMessage(String data, String topic,String userId) {
        SendResult result = null;
        try {
            byte[] messageBody = data.getBytes(DEFAULT_CHARSET);

            Message mqMsg = new Message();
            mqMsg.setTopic(topic);
           /* if (StringUtils.isNotNull(tags)) {
                mqMsg.setTags(tags);
            }*/
            mqMsg.setBody(messageBody);
//            LogUtil.warn(LogConstant.ID_CUSTOMERID, userId, LogConstant.MODEL_QUOTA, "MQ广播通知定制化推送额度", "开始......");
            result = producer.send(mqMsg);
//            LogUtil.warn(LogConstant.ID_CUSTOMERID, userId, LogConstant.MODEL_QUOTA, "MQ广播通知定制化推送额度", "返回结果"+result);
            return true;
        } catch (Exception e) {
            LOGGER.error("MQ: 广播生产者发送消息 发生异常,发送内容={}", data, e);
        } finally {
            LOGGER.warn("MQ: 广播生产者发送消息 topic={},tags={},发送内容={},结果={}", topic, "", data, result);
        }
        return false;
    }

    @PreDestroy
    public void stop() {
        if (producer != null) {
            producer.shutdown();
            //LOGGER.info("MQ：关闭生产者");
        }
    }

}