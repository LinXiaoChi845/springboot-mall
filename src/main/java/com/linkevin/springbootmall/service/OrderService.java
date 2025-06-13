package com.linkevin.springbootmall.service;

import com.linkevin.springbootmall.dto.BuyItem;
import com.linkevin.springbootmall.dto.NewOrderRequest;
import com.linkevin.springbootmall.dto.OrderQueryParams;
import com.linkevin.springbootmall.model.Orders;

import java.util.List;

public interface OrderService {

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Orders> getOrders(OrderQueryParams orderQueryParams);

    Orders getOrderByOrderId(Integer orderId);

    Integer newOrder(Integer userId, NewOrderRequest newOrderRequest);
}
