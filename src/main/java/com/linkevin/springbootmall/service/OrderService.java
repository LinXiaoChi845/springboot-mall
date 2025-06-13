package com.linkevin.springbootmall.service;

import com.linkevin.springbootmall.dto.BuyItem;
import com.linkevin.springbootmall.dto.NewOrderRequest;

public interface OrderService {

    Integer newOrder(Integer userId, NewOrderRequest newOrderRequest);
}
