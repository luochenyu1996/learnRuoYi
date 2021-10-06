package com.chenyu.framework.web.service;

import com.chenyu.common.core.domain.model.LoginUser;
import com.chenyu.common.utils.SecurityUtils;
import com.chenyu.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * @program: learnRuoYi
 * @description: 自定义权限实现
 * @author: chen yu
 * @create: 2021-10-05 14:57
 */
@Service("ss")
public class PermissionService {

    //所有权限标识
    private static final String ALL_PERMISSION = "*:*:*";


    // 管理员角色权限标识
    private static final String SUPER_ADMIN = "admin";


    //分割符号
    private static final String ROLE_DELIMETER = ",";

    //分隔符
    private static final String PERMISSION_DELIMETER = ",";

    /**
     * 验证用户是否具备某种权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某种权限
     */
    public boolean hasPermi(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        //使用springSecurity返回当前用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //当前用户不存在 或则权限为空
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        return hasPermissions(loginUser.getPermissions(), permission);
    }


    /**
     * 判断是否包含权限
     *
     * @param permissions 用户中存在的权限列表
     * @param permission  接口要求的权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }


}