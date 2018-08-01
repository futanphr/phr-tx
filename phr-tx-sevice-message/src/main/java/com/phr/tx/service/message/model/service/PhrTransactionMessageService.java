package com.phr.tx.service.message.model.service;

import com.phr.tx.service.message.model.entity.PhrTransactionMessageEntity;
import java.util.Map;
import java.util.List;
import com.phr.core.entity.PageInfo;
/**
 *
 * @time 2018年08月01日 13:54:02
 * @version 1.0
 *
 **/

public interface PhrTransactionMessageService  {
  	/**
	 * 通过主键id 删除
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Long id);
	/**
	 * 插入实体
	 * @param record
	 * @return
	 */
	int insertSelective(PhrTransactionMessageEntity record);
	/**
	 * 通过主键id 获取实体对象
	 * @param id
	 * @return
	 */
	PhrTransactionMessageEntity selectByPrimaryKey(Long id);
	/**
	 * 通过map参数 获取实体对象
	 * @param id
	 * @return
	 */
	PhrTransactionMessageEntity selectByKeys(Map<String,Object> params);
	/**
	 * 通过主键id 更新实体
	 * @param record
	 * @return 1成功  其它失败
	 */
	int updateByPrimaryKeySelective(PhrTransactionMessageEntity record);
	/**
	 * 通过map参数获取列表
	 * @param params
	 * @return List<PhrTransactionMessageEntity>
	 */
	List<PhrTransactionMessageEntity> getList(Map<String,Object> params);
	/**
	 * 通过map参数获取列表 分页
	 * @param params
	 * @return PageInfo<PhrTransactionMessageEntity>
	 */
	PageInfo<PhrTransactionMessageEntity> getListByPage(Map<String,Object> params,Integer currentPage,Integer pageSize);
	/**
	 * 通过map参数获取 总数
	 * @param params
	 * @return int
	 */
	int getListCount(Map<String,Object> params);

}