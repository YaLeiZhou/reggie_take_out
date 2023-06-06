package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {


    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);


    //修改菜品回显，根据dish表id查询相应的  dish表和dishFlavor表
    DishDto getByIdWithFlavor(Long id);


    //更新菜品信息，同时更新对应的口味信息
    void updateWithFlavor(DishDto dishDto);

    //单个删除菜品和批量删除
    boolean deleteInSetmeal(List<Long> ids);

}
