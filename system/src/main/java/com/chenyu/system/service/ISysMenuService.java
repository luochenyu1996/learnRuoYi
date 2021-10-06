package com.chenyu.system.service;

import java.util.Set;

/**
 * @program: learnRuoYi
 * @description:
 * @author: chen yu
 * @create: 2021-10-01 12:26
 */
public interface ISysMenuService {
    public Set<String> selectMenuPermsByUserId(Long  userId);

}