package com.chenyu.system.mapper;

import com.chenyu.common.core.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @program: learnRuoYi
 * @description: 用户表 数据层
 * @author: chen yu
 * @create: 2021-09-30 15:12
 */
//@Mapper
//@Repository
public interface SysUserMapper {

    /**
     * 通过用户名查询用户对象
     *
     * @param userName
     * @return
     */
    public SysUser selectUserByUserName(String userName);


    /**
     *  更新系统用户信息
     *
     * @param user 系统用户
     * @return 修改结果
     */

    public int updateUser(SysUser user);


}