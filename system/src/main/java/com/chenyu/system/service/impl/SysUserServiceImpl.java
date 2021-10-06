package com.chenyu.system.service.impl;

import com.chenyu.common.core.domain.entity.SysUser;
import com.chenyu.system.mapper.SysUserMapper;
import com.chenyu.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: learnRuoYi
 * @description:
 * @author: chen yu
 * @create: 2021-09-30 15:09
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    @Autowired
    private SysUserMapper userMapper;


    public List<SysUser> selectUserList(SysUser user) {
        return null;
    }


    /**
     *  根据用户名从数据库中查询系统用户
     *
     * @param userName 用户名
     * @return 系统用户
     *
     */

    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);

    }


    public int updateUserProfile(SysUser user) {
        userMapper.updateUser(user);
        return 0;
    }

    @Override
    public String checkUserNameUnique(String userName) {
        return null;
    }

    @Override
    public boolean registerUser(SysUser sysUser) {
        return false;
    }


}