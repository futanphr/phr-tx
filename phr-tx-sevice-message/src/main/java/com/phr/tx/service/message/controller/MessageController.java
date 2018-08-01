package com.phr.tx.service.message.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.phr.common.dto.ResultData;
import com.phr.common.utils.ResultDataUtil;
import com.phr.tx.service.message.api.RpTransactionMessageService;
import com.phr.tx.service.message.entity.request.MessageRequestEntity;
import com.phr.tx.service.message.model.entity.PhrTransactionMessageEntity;

@RestController
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private RpTransactionMessageService rpTransactionMessageService;
	/*@Resource
	private MqSenderImpl mqSenderImpl;*/
	@RequestMapping(value="/insert",method = RequestMethod.POST)
	public ResultData insert(MessageRequestEntity requestOrderEntity) {
		
		ResultData resultData=ResultDataUtil.successResult("初始化成功！"); 
		
		try {
			PhrTransactionMessageEntity message=new PhrTransactionMessageEntity();
			message.setVersion(111);
			message.setConsumerQueue(requestOrderEntity.getConsumerQueue());
			message.setMessageBody(requestOrderEntity.getMessageBody());
			rpTransactionMessageService.saveAndSendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
       
		return resultData;
	}
}
