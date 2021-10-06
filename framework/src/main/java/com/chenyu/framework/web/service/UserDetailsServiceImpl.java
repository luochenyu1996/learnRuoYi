package com.chenyu.framework.web.service;

import com.chenyu.common.core.domain.entity.SysUser;
import com.chenyu.common.core.domain.model.LoginUser;
import com.chenyu.common.enums.UserStats;
import com.chenyu.common.utils.StringUtils;
import com.chenyu.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @program: learnRuoYi
 * @description: springSecurity 用户验证处理
 * @author: chen yu
 * @create: 2021-09-30 14:06
 */

@Slf4j
@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysPermissionService  permissionService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("自定义的loadUserByUsername()方法被调用,查询的用户名为:{}",username);
        SysUser sysUser = sysUserService.selectUserByUserName(username);
        log.info("数据库中查询出来的登录用户:{}",sysUser);

        if (StringUtils.isNull(sysUser)){
            log.info("登录用户：{}不存在",username);
            //todo 抛出一个异常

        }else if (UserStats.DISABLE.getCode().equals(sysUser.getStatus())){
            log.info("登录用户：{}已经被停用",username);
            //todo  抛出一个异常
        }else if (UserStats.DELETED.getCode().equals(sysUser.getStatus())){
            log.info("登录用户：{}已经被删除",username);
            //todo  抛出一个异常
        }
        return creatLoginUser(sysUser);

    }

    private UserDetails creatLoginUser(SysUser user){
        return new LoginUser(user.getUserId(),user.getDeptId(),user,permissionService.getMenuPermission(user));
    }

}