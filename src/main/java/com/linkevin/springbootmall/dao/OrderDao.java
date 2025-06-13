package com.linkevin.springbootmall.dao;

import com.linkevin.springbootmall.dto.BuyItem;
import com.linkevin.springbootmall.dto.NewOrderRequest;
import com.linkevin.springbootmall.dto.OrderQueryParams;
import com.linkevin.springbootmall.model.OrderItem;
import com.linkevin.springbootmall.model.Orders;

import java.util.List;

public interface OrderDao {

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Orders> getOrders(OrderQueryParams orderQueryParams);

    Orders getOrderByOrderId(Integer orderId);

    List<OrderItem> getOrderItemByOrderId(Integer orderId);

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

}
