package com.linkevin.springbootmall.service;

import com.linkevin.springbootmall.dto.BuyItem;
import com.linkevin.springbootmall.dto.NewOrderRequest;
import com.linkevin.springbootmall.model.Orders;

public interface OrderService {

    Orders getOrderByOrderId(Integer orderId);

    Integer newOrder(Integer userId, NewOrderRequest newOrderRequest);
}
