/*
 * ====================================================================
 * 龙果学院： www.roncoo.com （微信公众号：RonCoo_com）
 * 超级教程系列：《微服务架构的分布式事务解决方案》视频教程
 * 讲师：吴水成（水到渠成），840765167@qq.com
 * 课程地址：http://www.roncoo.com/details/7ae3d7eddc4742f78b0548aa8bd9ccdb
 * ====================================================================
 */
package com.phr.tx.service.message.api;

import java.util.Map;

import com.phr.tx.service.message.exceptions.MessageBizException;
import com.phr.tx.service.message.model.entity.PhrTransactionMessageEntity;
import com.roncoo.pay.common.core.page.PageBean;
import com.roncoo.pay.common.core.page.PageParam;

/**
 * 
 * @Description:实时消息服务
 * @author: penghuari
 * @date:   2018年8月1日 下午12:02:19
 */
public interface RpTransactionMessageService {

	/**
	 * 预存储消息. 
	 */
	public int saveMessageWaitingConfirm(PhrTransactionMessageEntity phrTransactionMessageEntity) throws MessageBizException;
	
	
	/**
	 * 确认并发送消息.
	 */
	public void confirmAndSendMessage(String messageId) throws MessageBizException;

	
	/**
	 * 存储并发送消息.
	 */
	public int saveAndSendMessage(PhrTransactionMessageEntity phrTransactionMessageEntity) throws MessageBizException;

	
	/**
	 * 直接发送消息.
	 */
	public void directSendMessage(PhrTransactionMessageEntity phrTransactionMessageEntity) throws MessageBizException;
	
	
	/**
	 * 重发消息.
	 */
	public void reSendMessage(PhrTransactionMessageEntity phrTransactionMessageEntity) throws MessageBizException;
	
	
	/**
	 * 根据messageId重发某条消息.
	 */
	public void reSendMessageByMessageId(String messageId) throws MessageBizException;
	
	
	/**
	 * 将消息标记为死亡消息.
	 */
	public void setMessageToAreadlyDead(String messageId) throws MessageBizException;


	/**
	 * 根据消息ID获取消息
	 */
	public PhrTransactionMessageEntity getMessageByMessageId(String messageId) throws MessageBizException;

	/**
	 * 根据消息ID删除消息
	 */
	public void deleteMessageByMessageId(String messageId) throws MessageBizException;
	
	
	/**
	 * 重发某个消息队列中的全部已死亡的消息.
	 */
	public void reSendAllDeadMessageByQueueName(String queueName, int batchSize) throws MessageBizException;
	
	/**
	 * 获取分页数据
	 */
	PageBean listPage(PageParam pageParam, Map<String, Object> paramMap) throws MessageBizException;


}
