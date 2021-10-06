package com.chenyu.framework.security.filter;

import com.chenyu.common.core.domain.model.LoginUser;
import com.chenyu.common.utils.SecurityUtils;
import com.chenyu.common.utils.StringUtils;
import com.chenyu.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: learnRuoYi
 * @description: JWT过滤器
 * @author: chen yu
 * @create: 2021-10-05 09:25
 */
@Component
public class JwtAuthenticationTokenFilter  extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        LoginUser loginUser = tokenService.getLoginUser(httpServletRequest);
        //todo  StringUtils.isNull(SecurityUtils.getAuthentication()) 什么意思
        if (StringUtils.isNotNull(loginUser)&& StringUtils.isNull(SecurityUtils.getAuthentication())){
            tokenService.verifyTokenTime(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            //设置上下文  设置了上下文  后续处理器才能通过
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        //为什么这里依旧放行
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}