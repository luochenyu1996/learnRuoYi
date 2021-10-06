package com.chenyu.framework.aspectj;

import com.chenyu.common.core.domain.BaseEntity;
import com.chenyu.common.core.domain.entity.SysRole;
import com.chenyu.common.core.domain.entity.SysUser;
import com.chenyu.common.core.domain.model.LoginUser;
import com.chenyu.common.utils.SecurityUtils;
import com.chenyu.common.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.chenyu.common.annotation.DataScope;

import java.lang.reflect.Method;

/**
 * @program: learnRuoYi
 * @description:
 * @author: chen yu
 * @create: 2021-10-05 17:42
 */
@Aspect
@Component
public class DataScopeAspect {

    //全部数据权限
    public static final String DATA_SCOPE_ALL = "1";

    //自定义数据权限
    public static final String DATA_SCOPE_CUSTOM = "2";

    //部门数据权限
    public static final String DATA_SCOPE_DEPT = "3";

    //部门及以下数据权限
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    //仅本人数据权限
    public static final String DATA_SCOPE_SELF = "5";

    // 数据权限过滤关键字
    public static final String DATA_SCOPE = "dataScope";


    /**
     *  配置切入点
     *
     */
    @Pointcut("@annotation(com.chenyu.common.annotation.DataScope)")
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()") //表示要在哪些切点上进行增强
    public  void deBefore(JoinPoint point){
        //增强的内容
        clearDataScope(point);
        handleDataScope(point);
    }

    /**
     * 切入点切入的内容 单独抽取成一个方法
     *
     */
    protected void handleDataScope(final JoinPoint joinPoint) {
        // 去切入点 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null) {//只有有注解才进行增强
            return;
        }
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getUser();
            // 如果是超级管理员，则不过滤数据
            if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin()) {
                //进行数据过滤的 sql 拼接
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias());
            }
        }
    }


    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }

    /**
     * 拼接权限SQL前先清空 params.dataScope参数防止注入
     *
     */
    private  void clearDataScope(final JoinPoint joinPoint ){
        System.out.println("");
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }

    }


    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user      用户
     * @param userAlias 别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias, String userAlias) {
        StringBuilder sqlString = new StringBuilder();

        for (SysRole role : user.getRoles()) {
            //数据库表 sys_role中定义了数据范围
            String dataScope = role.getDataScope();

            if (DATA_SCOPE_ALL.equals(dataScope)) {//全部数据权限  那么就不进行拼接
                sqlString = new StringBuilder();
                break;
            } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {//自定义数据权限
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias,
                        role.getRoleId()));
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {//部门数据范围
                sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {//部门及以下数据权限
                sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                        deptAlias, user.getDeptId(), user.getDeptId()));
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {//自己的数据权限
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                } else {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(" OR 1=0 ");
                }
            }
        }

        if (StringUtils.isNotBlank(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) params;
                //进行一个数据范围的拼接 然后放到这个对象中去
                baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
            }
        }
    }








}