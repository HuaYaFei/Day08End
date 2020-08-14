package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Order;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    public void add(Order order);

    public List<Order> findByCondition(Order order);

    public Map findById4Detail(Integer id);

    public Integer findOrderCountByDate(String date);

    public Integer findOrderCountAfterDate(String date);

    public Integer findVisitsCountByDate(String date);

    public Integer findVisitsCountAfterDate(String date);

    public List<Map> findHotSetmeal();

    Page<Map<String, Object>> pageQuery(String queryString);

    void submit(Map<String, Object> data);

//    void findByCondition(Integer id, Date date, Integer setmeaId);
}
