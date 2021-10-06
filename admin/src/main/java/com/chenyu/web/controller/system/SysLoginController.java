package com.chenyu.web.controller.system;

import com.chenyu.common.constant.Constants;
import com.chenyu.common.core.domain.AjaxResult;
import com.chenyu.common.core.domain.model.LoginBody;
import com.chenyu.framework.web.service.SysLoginService;
import com.chenyu.framework.web.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: learnRuoYi
 * @description: 系统登录接口
 * @author: chen yu
 * @create: 2021-10-01 12:57
 */
@Slf4j
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    /**
     *  登录接口
     *
     * @param  loginBody 登录用户
     * @return 返回给前端的结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        log.info("登录用户信息:{}",loginBody);
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        // code 是验证码
        String code = loginBody.getCode();
        String uuid = loginBody.getUuid();
        String jwtToken = loginService.login(username, password, code, uuid);
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put(Constants.TOKEN, jwtToken);
        return ajaxResult;
    }

}