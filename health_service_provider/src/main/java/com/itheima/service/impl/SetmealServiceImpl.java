package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> setmeals = setmealDao.pageQuery(queryString);
        return new PageResult(setmeals.getTotal(), setmeals);
    }

    //    新增套餐
    @Override
    public void add(Setmeal setmeal, Integer[] integers) {
        setmealDao.add(setmeal);
        if (integers != null && integers.length > 0) {
            //添加多对多绑定
            setSetmealAndCheckGroup(setmeal.getId(), integers);
        }
//        添加到redis中
        saveRedis(setmeal.getImg());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    //    图片名添加到redis中
    private void saveRedis(String imgNmae) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, imgNmae);

    }

    //多对多绑定
    private void setSetmealAndCheckGroup(Integer id, Integer[] ids) {
        for (Integer integer : ids) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmeal_id", id);
            map.put("checkgroup_id", integer);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }
}
