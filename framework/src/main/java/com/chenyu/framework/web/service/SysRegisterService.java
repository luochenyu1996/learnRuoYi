package com.chenyu.framework.web.service;

import com.chenyu.common.constant.Constants;
import com.chenyu.common.constant.UserConstants;
import com.chenyu.common.core.domain.entity.SysUser;
import com.chenyu.common.core.domain.model.RegisterBody;
import com.chenyu.common.core.redis.RedisCache;
import com.chenyu.common.exception.user.CaptchaException;
import com.chenyu.common.exception.user.CaptchaExpireException;
import com.chenyu.common.utils.SecurityUtils;
import com.chenyu.common.utils.StringUtils;
import com.chenyu.system.service.ISysConfigService;
import com.chenyu.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: learnRuoYi
 * @description: 注册相关的服务层
 * @author: chen yu
 * @create: 2021-10-02 17:06
 */
@Component
public class SysRegisterService {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;


    /**
     * 注册服务方法
     *
     * @param registerBody 注册用的用户信息
     * @return 是否成功
     */
    public String register(RegisterBody registerBody) {
        String msg = "";
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        String code = registerBody.getCode();
        String uuid = registerBody.getUuid();
        boolean captchaOnOff = configService.selectCaptchaOnOff();
        if (captchaOnOff) {//进行验证码验证
            validateCaptcha(username, code, uuid);
        }
        if (StringUtils.isNull(username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isNull(password)) {
            msg = "用户密码不能为控";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";

        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在2到20个字符之间";
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(username))) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else { //到这里可以放行注册
            SysUser sysUser = new SysUser();
            sysUser.setUserName(username);
            sysUser.setNickName(username);
            //对密码进行加密存储
            sysUser.setPassword(SecurityUtils.encryptPassword(registerBody.getPassword()));
            //进行注册
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                //对注册成功的用户进行日志记录  todo
            }
        }
        return msg;
    }


    /**
     * 校验验证码  和登录时候的校验验证的区别是 这里没有进行日志的存储
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果 这里返回为控的原因是：如果校验失败那么抛出了异常  进行了全局异常处理
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        //进行了获取 那么从缓存中删除
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!captcha.equals(code)) {
            throw new CaptchaException();
        }
    }

}