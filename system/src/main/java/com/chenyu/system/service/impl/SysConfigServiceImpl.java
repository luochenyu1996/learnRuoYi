package com.chenyu.system.service.impl;

import com.chenyu.common.constant.Constants;
import com.chenyu.common.core.redis.RedisCache;
import com.chenyu.common.core.text.Convert;
import com.chenyu.common.utils.StringUtils;
import com.chenyu.system.domain.SysConfig;
import com.chenyu.system.mapper.SysConfigMapper;
import com.chenyu.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @program: learnRuoYi
 * @description: 系统服务层
 * @author: chen yu
 * @create: 2021-10-03 10:08
 */
@Slf4j
@Service
public class SysConfigServiceImpl implements ISysConfigService {

    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private RedisCache redisCache;


    /**
     * 项目启动的时候初始化 一部分数据到缓存中
     *
     * @PostContruct: 是Java自带的注解, 在方法上加该注解会在项目启动的时候执行该方法，也可以理解为在spring容器初始化的时候执行该方法
     */
    @PostConstruct
    public void init() {
        loadingConfigCache();
    }


    public SysConfig selectConfigById(Long configId) {
        return null;
    }


    /**
     * 根据系统配置参数的键查询其键值
     *
     * @param configKey 系统配置的 Key
     * @return 参数键值
     */

    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        //缓存中不为空则返回
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        //缓存中为空  则去数据库查找
        SysConfig sysConfig = new SysConfig();
        sysConfig.setConfigKey(configKey);
        //todo  这个查询为什么要封装成整个对象进行查询
        SysConfig retSysConfig = configMapper.selectConfig(sysConfig);
        //如果数据库中不为空  查询出来后  进行缓存设置
        if (StringUtils.isNotNull(retSysConfig)) {
            redisCache.setCacheObject(getCacheKey(configKey), retSysConfig.getConfigValue());
        }
        //这里重新去设置了缓存，因此返回一个空
        return StringUtils.EMPTY;
    }


    /**
     * 查询验证码的开关状态
     *
     * @return true 打开, false 关闭
     */
    public boolean selectCaptchaOnOff() {
        String captchaOnOff = selectConfigByKey("sys.account.captchaOnOff");
        if (StringUtils.isEmpty(captchaOnOff)) {
            return true;
        }
        return Convert.toBool(captchaOnOff);
    }


    public List<SysConfig> selectConfigList(SysConfig config) {
        return null;
    }

    public int insertConfig(SysConfig config) {
        return 0;
    }

    public int updateConfig(SysConfig config) {
        return 0;
    }

    public void deleteConfigByIds(Long[] configIds) {

    }

    /**
     * 从数据库中加载参数到redis缓存中
     *
     */
    public void loadingConfigCache() {
        List<SysConfig> sysConfigList = configMapper.selectConfigList(new SysConfig());
        for (SysConfig config : sysConfigList) {
            log.info("系统默认配置信息:{}", config);
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    public void clearConfigCache() {

    }

    public void resetConfigCache() {

    }

    public String checkConfigKeyUnique(SysConfig config) {
        return null;
    }



    /**
     *  拼接配置文件中配置项在配置redis中的key
     *
     * @param  configKey  配置文件中配置项在数据库中存储的key
     * @return 配置项在redis中的钥匙
     */

    private String getCacheKey(String configKey) {
        return Constants.CAPTCHA_CODE_KEY + configKey;
    }

}