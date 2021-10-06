package com.chenyu.common.utils.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @program: learnRuoYi
 * @description: spring 的工具类，方便在 非 spring 环境中获取  bean
 * @author: chen yu
 * @create: 2021-10-01 20:33
 */
public final class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware  {
    //spring 应用上下文环境
    private static ConfigurableListableBeanFactory beanFactory;

    private static ApplicationContext applicationContext;


    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        SpringUtils.beanFactory = configurableListableBeanFactory;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }


    /**
     *  获取bean对象
     *
     * @param  name bean的名字
     * @return Object 一个以所给名字注册的bean的实例
     */

    public  static   <T> T getBean(String  name) throws BeansException {
        return (T) beanFactory.getBean(name);
    }


    /**
     * 根据类型获取bean
     *
     * @param  clz 指定的类型
     * @return Object 一个以所给名字注册的bean的实例
     */

    public static  <T> T getBean(Class<T> clz) throws  BeansException{
        return beanFactory.getBean(clz);
    }



}