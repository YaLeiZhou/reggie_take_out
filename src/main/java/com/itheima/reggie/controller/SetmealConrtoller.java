package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealConrtoller {


    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;



    /**
     * 新增套餐
     */
     @PostMapping
     public R<String> save(@RequestBody SetmealDto setmealDto){
         log.info("setmeal:{}",setmealDto);
         setmealService.saveWithDish(setmealDto);
         return R.success("新增套餐成功");
     }

    /**
     * 分页显示套餐信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
     @GetMapping("/page")
     public R<Page> page(int page,int pageSize,String name){
         //分页器构造
         Page<Setmeal> pageInfo = new Page<>(page,pageSize);

         Page<SetmealDto> dtoPage = new Page<>(page,pageSize);



         LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
         //添加查询条件，根据name进行like模糊查询
         queryWrapper.like(name!=null,Setmeal::getName,name);

         //添加排序条件，根据更新时间降序排列
         queryWrapper.orderByDesc(Setmeal::getUpdateTime);

         setmealService.page(pageInfo,queryWrapper);


         BeanUtils.copyProperties(pageInfo,dtoPage,"records");
         List<Setmeal> records = pageInfo.getRecords();


         List<SetmealDto> list=records.stream().map((item)->{
             SetmealDto setmealDto = new SetmealDto();
             //对象拷贝
             BeanUtils.copyProperties(item,setmealDto);

             //分类id
             Long categoryId = item.getCategoryId();
             Category category = categoryService.getById(categoryId);
             if (category!=null){
                 //分类名称
                 String categoryName=category.getName();
                 setmealDto.setCategoryName(categoryName);
             }
             return setmealDto;
         }).collect(Collectors.toList());


        dtoPage.setRecords(list);

         return R.success(dtoPage);
     }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);
        setmealService.removeWithDish(ids);

        return R.success("套餐数据删除成功！");
    }


    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());

        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> setmealList = setmealService.list();

        return R.success(setmealList);

    }

}
