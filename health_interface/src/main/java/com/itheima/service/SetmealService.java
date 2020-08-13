package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void add(Setmeal setmeal, Integer[] integers);

    List<Setmeal> findAll();

    Setmeal findById(int id);

    List<Map<String, Object>> findSetmealCount();
}
