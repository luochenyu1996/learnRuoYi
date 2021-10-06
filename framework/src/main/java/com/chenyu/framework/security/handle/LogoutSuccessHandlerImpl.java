package com.chenyu.framework.security.handle;

import com.alibaba.fastjson.JSON;
import com.chenyu.common.constant.HttpStatus;
import com.chenyu.common.core.domain.AjaxResult;
import com.chenyu.common.core.domain.model.LoginUser;
import com.chenyu.common.utils.ServletUtils;
import com.chenyu.common.utils.StringUtils;
import com.chenyu.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: learnRuoYi
 * @description: 退出处理
 * @author: chen yu
 * @create: 2021-10-05 11:24
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Autowired
    private TokenService tokenService;
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(httpServletRequest);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            //  todo 记录用户退出日志
        }
        ServletUtils.renderString(httpServletResponse, JSON.toJSONString(AjaxResult.error(HttpStatus.SUCCESS, "退出成功")));
    }
}