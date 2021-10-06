package com.chenyu.framework.web.service;

import com.chenyu.common.core.domain.entity.SysUser;
import com.chenyu.system.service.ISysRoleService;
import com.chenyu.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: learnRuoYi
 * @description: 用户权限处理
 * @author: chen yu
 * @create: 2021-10-01 12:24
 */
@Service
public class SysPermissionService {

    @Autowired
    private ISysMenuService systemMenuService;

    @Autowired
    private ISysRoleService sysRoleService;



    /**
     *  获取用户菜单权限
     *
     * @param
     * @return
     */
    public Set<String> getMenuPermission(SysUser user){
        HashSet<String> menuPerms = new HashSet<String>();
        //管理员拥有所有权限
        if(user.isAdmin()){
            menuPerms.add("*:*:*");
        }else {
            menuPerms.addAll(systemMenuService.selectMenuPermsByUserId(user.getUserId()));
        }
        return  menuPerms;
    }



}