package com.chenyu.web.controller.system;

import com.chenyu.common.core.controller.BaseController;
import com.chenyu.common.core.domain.entity.SysUser;
import com.chenyu.common.core.page.TableDataInfo;
import com.chenyu.system.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: learnRuoYi
 * @description: 用户信息
 * @author: chen yu
 * @create: 2021-10-05 12:52
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController  extends BaseController {

    @Autowired
    private SysUserServiceImpl userService;


    /**
     *  获取用户列表
     * ”@PreAuthorize("@ss.hasPermi('system:user:list')")“  进行接口权限控制
     * @param user 系统用户
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        //使用插件进行分页
        startPage();
        //去数据库中进行查询
        List<SysUser> list = userService.selectUserList(user);
        //对返回的分页好的数据重新进行封装返回给前端
        return getDataTable(list);
    }

}