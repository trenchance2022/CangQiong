package com.sky.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前商品已加入购物车，如果已经加入，number+1，否则插入一条
        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId= BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list=shoppingCartMapper.list(shoppingCart);

        //查询结果只可能0或1条，因为这个方法只能插入一个菜品/套餐，而这一个菜品/套餐如果添加多次只会修改number而不会占多项
        if (list!=null && list.size()>0){
            ShoppingCart cart=list.get(0);
            cart.setNumber(cart.getNumber()+1);
            shoppingCartMapper.updateNumberById(cart);
        } else {
            //判断是菜品还是套餐
            Long dishId=shoppingCartDTO.getDishId();
            if(dishId!=null){
                Dish dish=dishMapper.getByID(dishId);
                shoppingCart.setNumber(1);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setCreateTime(LocalDateTime.now());
            } else {
                Setmeal setmeal=setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setNumber(1);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setCreateTime(LocalDateTime.now());
            }
            shoppingCartMapper.insert(shoppingCart);
        }

    }


    public List<ShoppingCart> showShoppingCart() {
        Long userId=BaseContext.getCurrentId();
        ShoppingCart shoppingCart=ShoppingCart.builder()
                .userId(userId)
                .build();
        return shoppingCartMapper.list(shoppingCart);
    }

    public void cleanShoppingCart() {
        Long userId=BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list=shoppingCartMapper.list(shoppingCart);
        if(list!=null&&list.size()>0){
            shoppingCart=list.get(0);
            Integer number=shoppingCart.getNumber();
            if(number==1){
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }else {
                shoppingCart.setNumber(number-1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
    }
}
