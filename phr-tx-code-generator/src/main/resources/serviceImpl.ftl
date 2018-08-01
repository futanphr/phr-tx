<#--<#include "/java_copyright.include"> -->
package ${basepackage}.${mpackage}.service.impl;

<#--<#include "/java_imports.include">  -->
import ${basepackage}.${mpackage}.entity.${className}Entity;
import ${basepackage}.${mpackage}.service.${className}Service;
import ${basepackage}.${mpackage}.mapper.${className}Mapper;
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
 * @time ${createTime}
 * @version 1.0
 *
 **/
@Service("${firsetLowerClassName}Service")
public class ${className}ServiceImpl  implements ${className}Service{

	@Autowired
	private ${className}Mapper ${firsetLowerClassName}Mapper;
  	/**
	 * 通过主键id 删除
	 * @param id
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = RuntimeException.class)
	public int deleteByPrimaryKey(Long id){
		return	${firsetLowerClassName}Mapper.deleteByPrimaryKey(id);
	}
	/**
	 * 插入实体
	 * @param record
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = RuntimeException.class)
	public int insertSelective(${className}Entity record){
		return ${firsetLowerClassName}Mapper.insertSelective(record);
	}
	/**
	 * 通过主键id 获取实体对象
	 * @param id
	 * @return
	 */
	public ${className}Entity selectByPrimaryKey(Long id){
		return ${firsetLowerClassName}Mapper.selectByPrimaryKey(id);
	}
	/**
	 * 通过map 获取实体对象
	 * @param id
	 * @return
	 */
	public ${className}Entity selectByKeys(Map<String,Object> params){
		return ${firsetLowerClassName}Mapper.selectByKeys(params);
	}
	/**
	 * 通过主键id 更新实体
	 * @param record
	 * @return 1成功  其它失败
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = RuntimeException.class)
	public int updateByPrimaryKeySelective(${className}Entity record){
		return ${firsetLowerClassName}Mapper.updateByPrimaryKeySelective(record);
	}
	/**
	 * 通过map参数获取列表
	 * @param params
	 * @return List<${className}Entity>
	 */
	public List<${className}Entity> getList(Map<String,Object> params){
		return  ${firsetLowerClassName}Mapper.getList(params);
	}
	/**
	 * 通过map参数获取列表 分页
	 * @param params
	 * @return PageInfo<${className}Entity>
	 */

	public PageInfo<${className}Entity> getListByPage(Map<String,Object> params,Integer currentPage,Integer pageSize){
	    currentPage =( currentPage == null || currentPage == 0 ) ? 1 : currentPage;
        pageSize = ( pageSize == null || pageSize == 0 ) ? 10 : pageSize;
		PageHelper.startPage(currentPage,pageSize);
		List<${className}Entity> list = ${firsetLowerClassName}Mapper.getList(params);
		Integer total = ${firsetLowerClassName}Mapper.getListCount(params);
		PageInfo<${className}Entity> pageInfo = new PageInfo<${className}Entity>(currentPage, pageSize, total);
		pageInfo.setItems(list);
		return pageInfo;
	}
	/**
	 * 通过map参数获取 总数
	 * @param params
	 * @return int
	 */
	public int getListCount(Map<String,Object> params){
		return  ${firsetLowerClassName}Mapper.getListCount(params);
	}

}