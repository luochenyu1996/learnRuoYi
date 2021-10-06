package com.chenyu.web.controller.system;

import com.chenyu.common.core.controller.BaseController;
import com.chenyu.common.core.domain.AjaxResult;
import com.chenyu.common.core.domain.model.RegisterBody;

import com.chenyu.framework.web.service.SysRegisterService;
import com.chenyu.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.chenyu.common.core.domain.AjaxResult.error;
import static com.chenyu.common.core.domain.AjaxResult.success;

/**
 * @program: learnRuoYi
 * @description: 注册接口
 * @author: chen yu
 * @create: 2021-10-02 15:50
 */
@RestController
public class SysRegisterController extends BaseController {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysRegisterService registerService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody registerBody) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(registerBody);
        //返回同意结果
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}