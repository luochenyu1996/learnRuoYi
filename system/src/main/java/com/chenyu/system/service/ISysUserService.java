package com.chenyu.system.service;


import com.chenyu.common.core.domain.entity.SysUser;

import java.util.List;


public interface ISysUserService {

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */

    public List<SysUser> selectUserList(SysUser user);


    /**
     * 根据用户名查询用户
     *
     * @param userName
     * @return SysUser
     */
    public SysUser selectUserByUserName(String userName);


    /**
     * 修改用户基本信息
     *
     * @param user 系统用户
     * @return
     */
    public int updateUserProfile(SysUser user);


    /**
     * 查询和用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public String checkUserNameUnique(String userName);


    /**
     * 用户注册
     *
     * @param sysUser 系统用户
     * @return 结果
     */

    public boolean registerUser(SysUser sysUser);


}


