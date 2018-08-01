package com.phr.tx.service.message.model.service.impl;

import com.phr.tx.service.message.model.entity.PhrTransactionMessageEntity;
import com.phr.tx.service.message.model.service.PhrTransactionMessageService;
import com.phr.tx.service.message.model.mapper.PhrTransactionMessageMapper;
import java.util.Map;
import java.util.List;
import com.phr.core.entity.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
//import org.apache.ibatis.session.RowBounds;
/**
 *
 * @time 2018年08月01日 13:54:02
 * @version 1.0
 *
 **/
@Service("phrTransactionMessageService")
public class PhrTransactionMessageServiceImpl  implements PhrTransactionMessageService{

	@Autowired
	private PhrTransactionMessageMapper phrTransactionMessageMapper;
  	/**
	 * 通过主键id 删除
	 * @param id
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = RuntimeException.class)
	public int deleteByPrimaryKey(Long id){
		return	phrTransactionMessageMapper.deleteByPrimaryKey(id);
	}
	/**
	 * 插入实体
	 * @param record
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = RuntimeException.class)
	public int insertSelective(PhrTransactionMessageEntity record){
		return phrTransactionMessageMapper.insertSelective(record);
	}
	/**
	 * 通过主键id 获取实体对象
	 * @param id
	 * @return
	 */
	public PhrTransactionMessageEntity selectByPrimaryKey(Long id){
		return phrTransactionMessageMapper.selectByPrimaryKey(id);
	}
	/**
	 * 通过map 获取实体对象
	 * @param id
	 * @return
	 */
	public PhrTransactionMessageEntity selectByKeys(Map<String,Object> params){
		return phrTransactionMessageMapper.selectByKeys(params);
	}
	/**
	 * 通过主键id 更新实体
	 * @param record
	 * @return 1成功  其它失败
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = RuntimeException.class)
	public int updateByPrimaryKeySelective(PhrTransactionMessageEntity record){
		return phrTransactionMessageMapper.updateByPrimaryKeySelective(record);
	}
	/**
	 * 通过map参数获取列表
	 * @param params
	 * @return List<PhrTransactionMessageEntity>
	 */
	public List<PhrTransactionMessageEntity> getList(Map<String,Object> params){
		return  phrTransactionMessageMapper.getList(params);
	}
	/**
	 * 通过map参数获取列表 分页
	 * @param params
	 * @return PageInfo<PhrTransactionMessageEntity>
	 */

	public PageInfo<PhrTransactionMessageEntity> getListByPage(Map<String,Object> params,Integer currentPage,Integer pageSize){
	    currentPage =( currentPage == null || currentPage == 0 ) ? 1 : currentPage;
        pageSize = ( pageSize == null || pageSize == 0 ) ? 10 : pageSize;
		PageHelper.startPage(currentPage,pageSize);
		List<PhrTransactionMessageEntity> list = phrTransactionMessageMapper.getList(params);
		Integer total = phrTransactionMessageMapper.getListCount(params);
		PageInfo<PhrTransactionMessageEntity> pageInfo = new PageInfo<PhrTransactionMessageEntity>(currentPage, pageSize, total);
		pageInfo.setItems(list);
		return pageInfo;
	}
	/**
	 * 通过map参数获取 总数
	 * @param params
	 * @return int
	 */
	public int getListCount(Map<String,Object> params){
		return  phrTransactionMessageMapper.getListCount(params);
	}

}