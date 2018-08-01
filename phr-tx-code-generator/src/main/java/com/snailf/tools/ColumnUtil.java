package com.snailf.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snailf.utils.DateUtil;

public class ColumnUtil {

	public static Map<String, Object> CloumnToMap(List<Map<String, Object>> columns, String tableName,
			String entityRemark, String basepackage, String mpackage) {
		boolean hasDateType = false;
		boolean hasBigDecimal = false;
		TableInfo tableInfo = new TableInfo();
		tableInfo.setClassName(tableName);
		tableInfo.setClassNameFirstLower(tableName);
		tableInfo.setEntityRemark(entityRemark);
		tableInfo.setCreateTime(DateUtil.DateToString(new Date(), DateUtil.DateStyle.YYYY_MM_DD_HH_MM_SS_CN));

		tableInfo.setColumns(columns);

		if (!hasDateType) {
			hasDateType = hasDateType(columns);
		}
		if (!hasBigDecimal) {
			hasBigDecimal = hasBigDecimal(columns);
		}
		// 构造填充数据的Map
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table", tableInfo);
		map.put("basepackage", basepackage);
		map.put("mpackage", mpackage);
		// if (hasDateType) {
		map.put("import", hasDateType);
		map.put("hasBigDecimal", hasBigDecimal);
		// }

		return map;
	}

	public static List<Map<String, Object>> mysqlColumnsToJavaColumns(List<Map<String, Object>> columns) {
		List<Map<String, Object>> realColumns = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> temp : columns) {
			Map<String, Object> column = new HashMap<String, Object>();
			column.put("simpleJavaType", mysqlTypeToJavaType(temp.get("type_name")));
			column.put("mysqlType", temp.get("type_name"));
			column.put("columnNameLower", (mysqlColumnToJavaColumn(temp.get("column_name"))));
			column.put("columnName", captureName(mysqlColumnToJavaColumn(temp.get("column_name"))));
			column.put("column", String.valueOf(temp.get("column_name")).toLowerCase());
			column.put("remarks", temp.get("remarks"));
			column.put("isDateTimeColumn", false);
			realColumns.add(column);

		}

		return realColumns;
	}

	// mysql 字段名称转换成java 字段
	public static String mysqlColumnToJavaColumn(Object mysqlColumn) {
		StringBuffer sbBuffer = new StringBuffer();
		String[] arr = String.valueOf(mysqlColumn).split("_");
		for (int i = 0; i < arr.length; i++) {
			if (i == 0) {
				sbBuffer.append(arr[i].toLowerCase());
			} else {
				sbBuffer.append(captureName(arr[i].toLowerCase()));
			}
		}
		return sbBuffer.toString();
	}

	/**
	 * 判断是否含有日期字段
	 * 
	 * @param mysqlType
	 * @return
	 */
	public static boolean hasDateType(List<Map<String, Object>> columns) {
		for (Map<String, Object> map : columns) {

			Object mysqlType = map.get("simpleJavaType");

			if (String.valueOf(mysqlType).toLowerCase().trim().equals("date")) {
				return true;
			}

		}
		return false;
	}
	/**
	 * 判断是否含有日期自字段
	 * @param columns
	 * @return
	 */
	public static boolean hasBigDecimal(List<Map<String, Object>> columns) {
		for (Map<String, Object> map : columns) {
			
			Object mysqlType = map.get("simpleJavaType");
			
			if (String.valueOf(mysqlType).toLowerCase().trim().equals("bigdecimal")) {
				return true;
			}
			
		}
		return false;
	}

	// mysql 字段名称转换成java 字段
	public static String mysqlTypeToJavaType(Object mysqlType) {
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("char")) {
			return "String";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("longtext")) {
			return "String";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("varchar")) {
			return "String";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("date")) {
			return "Date";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("datetime")) {
			return "Date";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("timestamp")) {
			return "Date";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("smallint")) {
			return "Integer";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("bigint")) {
			return "Long";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("tinyint")) {
			return "Integer";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("int")) {
			return "Integer";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("blob")) {
			return "String";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("tinyblob")) {
			return "String";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("longblob")) {
			return "String";
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("decimal")) {
			return "BigDecimal" ;
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("decimal unsigned")) {
			return "BigDecimal" ;
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("numeric")) {
			return "BigDecimal" ;
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("float")) {
			return "BigDecimal" ;
		}
		if (String.valueOf(mysqlType).toLowerCase().trim().equals("double")) {
			return "BigDecimal" ;
		}
		return null;
	}

	// 首字母大写
	public static String captureName(String str) {
		char[] cs = str.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);

	}

	// 首字母小写
	public static String lowerFirstChar(String str) {
		char[] chars = new char[1];
		chars[0] = str.charAt(0);
		String temp = new String(chars);
		if (chars[0] >= 'A' && chars[0] <= 'Z') {
			return str.replaceFirst(temp, temp.toLowerCase());
		}
		return str;
	}

	public static String getEntityName(String tableName) {

		tableName = tableName.replace("_tb", "").replace("t_wk_", "").replace("t_", "");
		return ColumnUtil.captureName(ColumnUtil.mysqlColumnToJavaColumn(tableName));

	}

}
