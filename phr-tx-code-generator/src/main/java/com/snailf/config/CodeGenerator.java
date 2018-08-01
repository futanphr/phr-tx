package com.snailf.config;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.snailf.tools.ColumnUtil;
import com.snailf.tools.DBMSMetaUtil;
import com.snailf.tools.FreemarkerUtils;
import com.snailf.tools.MapUtil;
import com.snailf.tools.MapperUtil;
import com.snailf.tools.ServiceUtil;

public class CodeGenerator {

	private static Map<String, String> paramsMap = new HashMap<String, String>();
	public static ResourceBundle bundle = null;

	private String basepackage;
	private String mpackage;

	private String ip;
	private String port;
	private String dbname;
	// dbname = "csoedb";
	private String username;
	// = "root";
	private String password;
	//
	private static String databasetype = "mysql";
	private String tableName;
	private String projectPath = System.getProperty("user.dir");
	private String classPath = projectPath + "\\src\\main\\java\\";
	private String resourcesPath = projectPath + "\\src\\main\\resources\\";

	// public CodeGenerator() {
	// init();
	// }

	/**
	 * 
	 * @param ip
	 *            数据ip
	 * @param port
	 *            数据库端口号
	 * @param userName
	 *            数据库用户名
	 * @param password
	 *            数据库密码
	 * @param dbName
	 *            数据库名称
	 * @param tableName
	 *            表名
	 * @param basePackage
	 *            基础包名
	 * @param servicePackage
	 *            业务包名
	 */
	public CodeGenerator(String ip, String port, String userName, String password, String dbName, String tableName,
			String basePackage, String servicePackage) {
		this.ip = ip;
		this.port = port;
		this.username = userName;
		this.password = password;
		this.dbname = dbName;
		this.tableName = tableName;
		this.basepackage = basePackage;
		this.mpackage = servicePackage;
		//
		// String curPath = System.getProperty("user.dir");
		// curPath = curPath.substring(0, curPath.lastIndexOf("\\"));
		//
		// this.projectPath = curPath + File.separator + projectName;
		// this.classPath = projectPath + "\\src\\main\\java\\";
		// this.resourcesPath = projectPath + "\\src\\main\\resources\\";
	}

	public void init() {
		bundle = ResourceBundle.getBundle("code-config");
		Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			paramsMap.put(key, bundle.getString(key));
		}

		basepackage = paramsMap.get("base.package");
		mpackage = paramsMap.get("service.package");
		mpackage = paramsMap.get("service.package");
		ip = paramsMap.get("db.ip");
		port = paramsMap.get("db.port");
		dbname = paramsMap.get("db.name");
		username = paramsMap.get("db.userName");
		password = paramsMap.get("db.password");
		tableName = paramsMap.get("table.name");
	}

	/**
	 * 通过key 获取pwd
	 * 
	 * @param key
	 * @return
	 */
	public static String getVal(String key) {
		return paramsMap.get(key);
	}

	public void run() {

		Map<String, Object> tableMap = DBMSMetaUtil.selectTable(databasetype, ip, port, dbname, username, password,
				tableName);
		List<Map<String, Object>> columns = DBMSMetaUtil.listColumns(databasetype, ip, port, dbname, username,
				password, tableName);
		List<Map<String, Object>> pkColumns = DBMSMetaUtil.listPkColumn(databasetype, ip, port, dbname, username,
				password, tableName);

		//
		tableMap = MapUtil.convertKey2LowerCase(tableMap);
		columns = MapUtil.convertKeyList2LowerCase(columns);
		pkColumns = MapUtil.convertKeyList2LowerCase(pkColumns);

		String entityPackage = mkdirPackage();
		String entityName = ColumnUtil.getEntityName(tableName);
		String entityRemark = String.valueOf(tableMap.get("remarks"));

		columns = ColumnUtil.mysqlColumnsToJavaColumns(columns);
		Map<String, Object> map = ColumnUtil.CloumnToMap(columns, entityName, entityRemark, basepackage, mpackage);

		FreemarkerUtils hf = new FreemarkerUtils();

		try {
			hf.init(resourcesPath);
			// 生成实体对象
			if (isExistFile(entityPackage + "\\entity\\" + entityName + "Entity.java")) {
				System.out.println(entityPackage + "\\entity\\" + entityName + "Entity.java" + "  已经存在");
			} else {
				hf.process(map, entityPackage + "\\entity\\", entityName + "Entity.java", "pojo.ftl");
			}
			String mapperPackage = mkdirPackage();
			if (isExistFile(mapperPackage + "\\mapper\\" + entityName + "Mapper.java")) {
				System.out.println(mapperPackage + "\\mapper\\" + entityName + "Mapper.java 已经存在");
			} else {
				hf.process(map, mapperPackage + "\\mapper\\", entityName + "Mapper.java", "mapper.ftl");
			}
			Map<String, Object> mapperMap = MapperUtil.createMapper(basepackage, mpackage, entityName, tableName,
					columns, pkColumns.get(0));
			if (isExistFile(mapperPackage + "\\mapper\\" + entityName + "Mapper.xml")) {
				System.out.println(mapperPackage + "\\mapper\\" + entityName + "Mapper.xml 已经存在");
			} else {
				hf.process(mapperMap, mapperPackage + "\\mapper\\", entityName + "Mapper.xml", "mapperXml.ftl");
			}
			// 生成service
			Map<String, Object> serviceMap = ServiceUtil.createMapper(basepackage, mpackage, entityName, tableName,
					columns, pkColumns.get(0));
			if (isExistFile(mapperPackage + "\\service\\" + entityName + "Service.java")) {
				System.out.println(mapperPackage + "\\service\\" + entityName + "Service.java 已经存在");
			} else {
				hf.process(serviceMap, mapperPackage + "\\service\\", entityName + "Service.java", "service.ftl");
			}
			if (isExistFile(mapperPackage + "\\service\\impl\\" + entityName + "ServiceImpl.java")) {
				System.out.println(mapperPackage + "\\service\\impl\\" + entityName + "ServiceImpl.java 已经存在");
			} else {
				hf.process(serviceMap, mapperPackage + "\\service\\impl\\", entityName + "ServiceImpl.java",
						"serviceImpl.ftl");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构建包
	 */
	public String mkdirPackage() {
		String[] baseArrs = basepackage.split("\\.");
		String[] mpackageArrs = mpackage.split("\\.");
		String packagePath = classPath;

		if (baseArrs != null) {
			for (String path : baseArrs) {
				packagePath = packagePath + File.separator + path;
			}
		}
		if (mpackageArrs != null) {
			for (String path : mpackageArrs) {
				packagePath = packagePath + File.separator + path;
			}

		}
		packagePath = packagePath + File.separator;
		createDir(packagePath + "entity" + File.separator);
		createDir(packagePath + "mapper" + File.separator);
		createDir(packagePath + "service" + File.separator);
		createDir(packagePath + "service\\impl" + File.separator);
		return packagePath;

	}

	public static boolean isExistFile(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	// 创建目录
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {// 判断目录是否存在
			System.out.println("包名已经存在！[" + destDirName + "]");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
			destDirName = destDirName + File.separator;
		}
		if (dir.mkdirs()) {// 创建目标目录
			System.out.println("创建目录成功！[" + destDirName + "]");
			return true;
		} else {
			System.out.println("创建目录失败！");
			return false;
		}
	}
}
