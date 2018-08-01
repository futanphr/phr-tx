package com.snailf.tools;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snailf.utils.DateUtil;

public class ServiceUtil {

	/**
	 * 构造mapper
	 * 
	 * @return
	 */

	public static Map<String, Object> createMapper(String basePackage, String mPackage, String entityName,
			String tableName, List<Map<String, Object>> columns, Map<String, Object> pkMap) {
		Map<String, Object> ret = new HashMap<String, Object>();
		String pkKey = String.valueOf(pkMap.get("column_name")).toLowerCase();
		String pkVal = ColumnUtil.mysqlColumnToJavaColumn(String.valueOf(pkMap.get("column_name")).toLowerCase());
		String firsetLowerEntityName = ColumnUtil.lowerFirstChar(entityName);
		String nameSpace = basePackage + "." + mPackage + ".mapper." + entityName + "Mapper";
		String resultType = basePackage + "." + mPackage + ".entity." + entityName + "Entity";

		ret.put("nameSpace", nameSpace);
		ret.put("resultType", resultType);
		ret.put("className", entityName);
		ret.put("firsetLowerClassName", firsetLowerEntityName);
		ret.put("tableName", tableName);
		ret.put("basepackage", basePackage);
		ret.put("mpackage", mPackage);
		ret.put("createTime", DateUtil.DateToString(new Date(), DateUtil.DateStyle.YYYY_MM_DD_HH_MM_SS_CN));

		return ret;
	}

}
