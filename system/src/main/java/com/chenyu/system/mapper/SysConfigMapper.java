package com.chenyu.system.mapper;

import com.chenyu.system.domain.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: learnRuoYi
 * @description: 系统参数配置  数据层
 * @author: chen yu
 * @create: 2021-10-03 15:21
 */
//@Mapper
//@Repository
public interface SysConfigMapper {
    /**
     * 查询参数配置信息
     *
     * @param config 参数配置信息
     * @return 参数配置信息
     */
    public SysConfig selectConfig(SysConfig config);



    /**
     *
     * 查询参数配置列表
     */
   public List<SysConfig> selectConfigList(SysConfig config);


}