package com.chenyu.framework.web.service;


import com.chenyu.common.core.domain.entity.SysUser;
import com.chenyu.common.core.domain.model.LoginUser;
import com.chenyu.common.core.redis.RedisCache;
import com.chenyu.common.exception.ServiceException;
import com.chenyu.common.exception.user.CaptchaException;
import com.chenyu.common.exception.user.CaptchaExpireException;
import com.chenyu.common.exception.user.UserPasswordNotMatchException;
import com.chenyu.common.utils.DateUtils;
import com.chenyu.common.utils.ServletUtils;
import com.chenyu.common.utils.ip.IpUtils;

import com.chenyu.system.service.ISysConfigService;
import com.chenyu.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import com.chenyu.common.constant.Constants;

/**
 * @program: learnRuoYi
 * @description: 登录校验方法
 * @author: chen yu
 * @create: 2021-10-01 17:45
 */
@Component
public class SysLoginService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;


    /**
     * 验证用户  生成令牌
     *
     * @param userName 用户名
     * @param password 密码
     * @param password 验证码
     * @return 令牌
     */
    public String login(String userName, String password, String code, String uuid) {
        //验证码  看验证码是否打开
        boolean captchaOnOff = configService.selectCaptchaOnOff();
        //验证 验证码
        if (captchaOnOff) {
            validateCaptcha(userName, code, uuid);
        }

        //使用SpringSecurity进行用户验证
        Authentication authenticated = null;
        //返回被授权后的对象
        try {
            //!!! 这里应该只是做了验证  没有设置上下文   因为采用的是  token 形式
            authenticated = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                //记录日志  todo
                //抛出异常
                throw new UserPasswordNotMatchException();
            } else {
                //记录日志  todo
                //抛出异常
                throw new ServiceException(e.getMessage());
            }
        }
        //记录日志 todo
        LoginUser loginUser = (LoginUser) authenticated.getPrincipal();
        //记录登录用户的信息
        recordLoginInfo(loginUser.getUser());
        // 生成token！！！一个关键点
        return tokenService.createToken(loginUser);
    }


    /**
     * 验证验证码
     *
     * @param userName 用户名
     * @param code     状态码
     * @param uuid     用户唯一标识
     */

    public void validateCaptcha(String userName, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        //从缓存中去取
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            //异步处理 进行日志打印 todo
            throw new CaptchaExpireException();
        }
        if (!code.equals(captcha)) {
            //异步处理 进行日志打印  todo
            throw new CaptchaException();
        }
    }


    /**
     * 记录用户登录信息
     *
     * @param sysUser 系统用户
     */
    public void recordLoginInfo(SysUser sysUser) {
        //1.登录用户的IP信息
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        //2.登录时间
        sysUser.setLoginDate(DateUtils.getNowDate());

        userService.updateUserProfile(sysUser);
    }


}