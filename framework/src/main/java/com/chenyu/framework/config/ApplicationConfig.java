package com.chenyu.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.TimeZone;

/**
 * @program: learnRuoYi
 * @description: 整个程序中的注解进行配置
 * @author: chen yu
 * @create: 2021-10-04 10:48
 */
@Configuration
//表开启注解版本的aop
@EnableAspectJAutoProxy(exposeProxy = true)
//指定MyBatis要扫描的Mapper类的包的路径
@MapperScan("com.chenyu.**.mapper")
public class ApplicationConfig {
    /**
     * 时区配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }


}