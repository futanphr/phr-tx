/*
 * ====================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/details/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ====================================================================
 */
package com.phr.tx.service.message.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.phr.tx.service.message.api.RpTransactionMessageService;
import com.phr.tx.service.message.enums.MessageStatusEnum;
import com.phr.tx.service.message.exceptions.MessageBizException;
import com.phr.tx.service.message.model.entity.PhrTransactionMessageEntity;
import com.phr.tx.service.message.model.entity.RpTransactionMessage;
import com.phr.tx.service.message.model.service.PhrTransactionMessageService;
import com.phr.tx.service.message.mq.MqSenderImpl;
import com.roncoo.pay.common.core.enums.PublicEnum;
import com.roncoo.pay.common.core.page.PageBean;
import com.roncoo.pay.common.core.page.PageParam;
import com.roncoo.pay.common.core.utils.PublicConfigUtil;
import com.roncoo.pay.common.core.utils.StringUtil;

/**
 * <b>功能说明: </b>
 *
 * @author Peter <a href="http://www.roncoo.com">龙果学院(www.roncoo.com)</a>
 */
@Service("rpTransactionMessageService")
public class RpTransactionMessageServiceImpl implements RpTransactionMessageService {

	private static final Log log = LogFactory.getLog(RpTransactionMessageServiceImpl.class);

	@Autowired
	private PhrTransactionMessageService phrTransactionMessageService;

	@Autowired
	private JmsTemplate notifyJmsTemplate;
	
	@Resource
	private MqSenderImpl mqSenderImpl;

	@Override
	public int saveMessageWaitingConfirm(PhrTransactionMessageEntity message) throws MessageBizException {

		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
		}

		if (StringUtil.isEmpty(message.getConsumerQueue())) {
			throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
		}

		message.setEditTime(new Date());
		message.setStatus(MessageStatusEnum.WAITING_CONFIRM.name());
		message.setAreadlyDead(PublicEnum.NO.name());
		message.setMessageSendTimes(0);
		return phrTransactionMessageService.insertSelective(message);
	}

	@Override
	public void confirmAndSendMessage(String messageId) {
		final PhrTransactionMessageEntity message = getMessageByMessageId(messageId);
		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
		}

		message.setStatus(MessageStatusEnum.SENDING.name());
		message.setEditTime(new Date());
		phrTransactionMessageService.updateByPrimaryKeySelective(message);

		notifyJmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
		notifyJmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message.getMessageBody());
			}
		});
	}

	@Override
	public int saveAndSendMessage(final PhrTransactionMessageEntity message) {

		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
		}

		if (StringUtil.isEmpty(message.getConsumerQueue())) {
			throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
		}
		message.setCreateTime(new Date());
		message.setStatus(MessageStatusEnum.SENDING.name());
		message.setAreadlyDead(PublicEnum.NO.name());
		message.setMessageSendTimes(0);
		message.setEditTime(new Date());
		int result = phrTransactionMessageService.insertSelective(message);

		/*notifyJmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
		notifyJmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message.getMessageBody());
			}
		});*/
		mqSenderImpl.sendMessage(JSON.toJSONString(message), "bank_topic", "auth_tags", "13120039229");

		return result;
	}

	@Override
	public void directSendMessage(final PhrTransactionMessageEntity message) {

		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
		}

		if (StringUtil.isEmpty(message.getConsumerQueue())) {
			throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
		}

		notifyJmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
		notifyJmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message.getMessageBody());
			}
		});
	}

	@Override
	public void reSendMessage(final PhrTransactionMessageEntity message) {

		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
		}

		if (StringUtil.isEmpty(message.getConsumerQueue())) {
			throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
		}

		message.addSendTimes();
		message.setEditTime(new Date());
		phrTransactionMessageService.updateByPrimaryKeySelective(message);

		notifyJmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
		notifyJmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message.getMessageBody());
			}
		});
	}

	@Override
	public void reSendMessageByMessageId(String messageId) {
		final PhrTransactionMessageEntity message = getMessageByMessageId(messageId);
		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
		}

		int maxTimes = Integer.valueOf(PublicConfigUtil.readConfig("message.max.send.times"));
		if (message.getMessageSendTimes() >= maxTimes) {
			message.setAreadlyDead(PublicEnum.YES.name());
		}

		message.setEditTime(new Date());
		message.setMessageSendTimes(message.getMessageSendTimes() + 1);
		phrTransactionMessageService.updateByPrimaryKeySelective(message);

		notifyJmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
		notifyJmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message.getMessageBody());
			}
		});
	}

	@Override
	public void setMessageToAreadlyDead(String messageId) {
		PhrTransactionMessageEntity message = getMessageByMessageId(messageId);
		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
		}

		message.setAreadlyDead(PublicEnum.YES.name());
		message.setEditTime(new Date());
		phrTransactionMessageService.updateByPrimaryKeySelective(message);
	}
	@Override
	public PhrTransactionMessageEntity getMessageByMessageId(String messageId) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("messageId", messageId);

		return phrTransactionMessageService.selectByKeys(paramMap);
	}
	@Override
	public void deleteMessageByMessageId(String messageId) {
		PhrTransactionMessageEntity message = getMessageByMessageId(messageId);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("messageId", messageId);
		phrTransactionMessageService.deleteByPrimaryKey(Long.valueOf(message.getId()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void reSendAllDeadMessageByQueueName(String queueName, int batchSize) {/*
		log.info("==>reSendAllDeadMessageByQueueName");

		int numPerPage = 1000;
		if (batchSize > 0 && batchSize < 100) {
			numPerPage = 100;
		} else if (batchSize > 100 && batchSize < 5000) {
			numPerPage = batchSize;
		} else if (batchSize > 5000) {
			numPerPage = 5000;
		} else {
			numPerPage = 1000;
		}

		int pageNum = 1;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("consumerQueue", queueName);
		paramMap.put("areadlyDead", PublicEnum.YES.name());
		paramMap.put("listPageSortType", "ASC");

		Map<String, RpTransactionMessage> messageMap = new HashMap<String, RpTransactionMessage>();
		List<Object> recordList = new ArrayList<Object>();
		int pageCount = 1;

		PageBean pageBean = rpTransactionMessageDao.listPage(new PageParam(pageNum, numPerPage), paramMap);
		recordList = pageBean.getRecordList();
		if (recordList == null || recordList.isEmpty()) {
			log.info("==>recordList is empty");
			return;
		}
		pageCount = pageBean.getTotalPage();
		for (final Object obj : recordList) {
			final RpTransactionMessage message = (RpTransactionMessage) obj;
			messageMap.put(message.getMessageId(), message);
		}

		for (pageNum = 2; pageNum <= pageCount; pageNum++) {
			pageBean = rpTransactionMessageDao.listPage(new PageParam(pageNum, numPerPage), paramMap);
			recordList = pageBean.getRecordList();

			if (recordList == null || recordList.isEmpty()) {
				break;
			}

			for (final Object obj : recordList) {
				final RpTransactionMessage message = (RpTransactionMessage) obj;
				messageMap.put(message.getMessageId(), message);
			}
		}

		recordList = null;
		pageBean = null;

		for (Map.Entry<String, RpTransactionMessage> entry : messageMap.entrySet()) {
			final RpTransactionMessage message = entry.getValue();

			message.setEditTime(new Date());
			message.setMessageSendTimes(message.getMessageSendTimes() + 1);
			rpTransactionMessageDao.update(message);

			notifyJmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
			notifyJmsTemplate.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(message.getMessageBody());
				}
			});
		}

	*/}

	@SuppressWarnings("unchecked")
	@Override
	public PageBean<RpTransactionMessage> listPage(PageParam pageParam, Map<String, Object> paramMap) {
//		return rpTransactionMessageDao.listPage(pageParam, paramMap);
		return null;
	}

}
