package com.linkevin.springbootmall.dao;

import com.linkevin.springbootmall.dto.BuyItem;
import com.linkevin.springbootmall.dto.NewOrderRequest;
import com.linkevin.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

}
