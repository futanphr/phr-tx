package com.phr.tx.service.message.entity.request;


public class MessageRequestEntity {
	 /**
     * 主键ID
     */
    private String id;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 修改者
     */
    private String editor;
    /**
     * 创建者
     */
    private String creater;
    /**
     * 消息ID
     */
    private String messageId;
    /**
     * 消息内容
     */
    private String messageBody;
    /**
     * 消息数据类型
     */
    private String messageDataType;
    /**
     * 消费队列
     */
    private String consumerQueue;
    /**
     * 消息重发次数
     */
    private Integer messageSendTimes;
    /**
     * 是否死亡
     */
    private String areadlyDead;
    /**
     * 状态
     */
    private String status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 扩展字段1
     */
    private String field1;
    /**
     * 扩展字段2
     */
    private String field2;
    /**
     * 扩展字段3
     */
    private String field3;

	/**
	 * 设置主键ID
	 */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * 获取主键ID
     */
    public String getId() {
        return this.id;
    }
	/**
	 * 设置版本号
	 */
    public void setVersion(Integer version) {
        this.version = version;
    }
    /**
     * 获取版本号
     */
    public Integer getVersion() {
        return this.version;
    }
	/**
	 * 设置修改者
	 */
    public void setEditor(String editor) {
        this.editor = editor;
    }
    /**
     * 获取修改者
     */
    public String getEditor() {
        return this.editor;
    }
	/**
	 * 设置创建者
	 */
    public void setCreater(String creater) {
        this.creater = creater;
    }
    /**
     * 获取创建者
     */
    public String getCreater() {
        return this.creater;
    }
	/**
	 * 设置消息ID
	 */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    /**
     * 获取消息ID
     */
    public String getMessageId() {
        return this.messageId;
    }
	/**
	 * 设置消息内容
	 */
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
    /**
     * 获取消息内容
     */
    public String getMessageBody() {
        return this.messageBody;
    }
	/**
	 * 设置消息数据类型
	 */
    public void setMessageDataType(String messageDataType) {
        this.messageDataType = messageDataType;
    }
    /**
     * 获取消息数据类型
     */
    public String getMessageDataType() {
        return this.messageDataType;
    }
	/**
	 * 设置消费队列
	 */
    public void setConsumerQueue(String consumerQueue) {
        this.consumerQueue = consumerQueue;
    }
    /**
     * 获取消费队列
     */
    public String getConsumerQueue() {
        return this.consumerQueue;
    }
	/**
	 * 设置消息重发次数
	 */
    public void setMessageSendTimes(Integer messageSendTimes) {
        this.messageSendTimes = messageSendTimes;
    }
    /**
     * 获取消息重发次数
     */
    public Integer getMessageSendTimes() {
        return this.messageSendTimes;
    }
	/**
	 * 设置是否死亡
	 */
    public void setAreadlyDead(String areadlyDead) {
        this.areadlyDead = areadlyDead;
    }
    /**
     * 获取是否死亡
     */
    public String getAreadlyDead() {
        return this.areadlyDead;
    }
	/**
	 * 设置状态
	 */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * 获取状态
     */
    public String getStatus() {
        return this.status;
    }
	/**
	 * 设置备注
	 */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     * 获取备注
     */
    public String getRemark() {
        return this.remark;
    }
	/**
	 * 设置扩展字段1
	 */
    public void setField1(String field1) {
        this.field1 = field1;
    }
    /**
     * 获取扩展字段1
     */
    public String getField1() {
        return this.field1;
    }
	/**
	 * 设置扩展字段2
	 */
    public void setField2(String field2) {
        this.field2 = field2;
    }
    /**
     * 获取扩展字段2
     */
    public String getField2() {
        return this.field2;
    }
	/**
	 * 设置扩展字段3
	 */
    public void setField3(String field3) {
        this.field3 = field3;
    }
    /**
     * 获取扩展字段3
     */
    public String getField3() {
        return this.field3;
    }
    public void addSendTimes() {
		messageSendTimes++;
	}

}
