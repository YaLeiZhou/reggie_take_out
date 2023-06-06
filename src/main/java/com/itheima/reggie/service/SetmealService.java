package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    //删除套餐，同时需要删除套餐菜品表的关联数据
    public void removeWithDish(List<Long> ids);

    //新增套餐 ，同时保存套餐和菜品的关联关系
    // 将套餐关联的基本信息和菜品信息 分别在 套餐表和套餐菜品表
    public void saveWithDish(SetmealDto setmealDto);

}
