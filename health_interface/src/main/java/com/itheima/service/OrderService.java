package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.Result;

import java.util.Map;

public interface OrderService {
    Result order(Map map) throws Exception;

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    Map finyById(Integer id) throws Exception;

    Result add(Map data) throws Exception;
}
