package com.itheima.service;

import com.itheima.pojo.User;

public interface UserService {
    /**
     * 用户服务接口
     *
     * @param username
     * @return
     */
    User findByUsername(String username);
}
