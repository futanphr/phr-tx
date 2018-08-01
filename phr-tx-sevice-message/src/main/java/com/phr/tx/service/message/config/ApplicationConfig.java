package com.phr.tx.service.message.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;


import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;


/**
 * springboot 总的配置类
 * 
 * @author pegnhuari
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.phr.*" }, includeFilters = @Filter(classes = org.springframework.stereotype.Controller.class, type = FilterType.ANNOTATION))
@Import({DataSourceConfig.class})
@EnableCaching //开启缓存
public class ApplicationConfig {

	

}
