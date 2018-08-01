package com.snailf.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerUtils {
	private Configuration cfg; // 模版配置对象

	public void init(String classPath) throws Exception {
		// 初始化FreeMarker配置
		// 创建一个Configuration实例
		cfg = new Configuration();

		// 设置FreeMarker的模版文件夹位置
		cfg.setClassForTemplateLoading(this.getClass(),"/");
//		cfg.setDirectoryForTemplateLoading(new File(classPath));

		cfg.setObjectWrapper(new DefaultObjectWrapper());
	}

	public void process(Map<String, Object> map, String projectPath, String outFileName, String templateName)
			throws Exception {

		// 创建模版对象
		Template t = cfg.getTemplate(templateName);
		// 在模版上执行插值操作，并输出到制定的输出流中
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(projectPath + outFileName)));
		t.process(map, out);
	}

	// public static void main(String[] args) throws Exception {
	//
	// }
}
