package com.linkevin.springbootmall.controller;

import com.linkevin.springbootmall.dto.NewOrderRequest;
import com.linkevin.springbootmall.dto.OrderQueryParams;
import com.linkevin.springbootmall.model.Orders;
import com.linkevin.springbootmall.service.OrderService;
import com.linkevin.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Orders>> getOrders(@PathVariable Integer userId,
                                                  @RequestParam(defaultValue = "10") @Max(100) @Min(0) Integer limit,
                                                  @RequestParam(defaultValue = "0") @Min(0) Integer offset) {
        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        // 取得 Order List
        List<Orders> ordersList = orderService.getOrders(orderQueryParams);

        // 取得 Order 總數
        Integer count = orderService.countOrder(orderQueryParams);

        // 分頁
        Page<Orders> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(ordersList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<Orders> newOrder(@PathVariable Integer userId,
                                      @RequestBody @Valid NewOrderRequest newOrderRequest) {

        // 創建訂單
        Integer orderId = orderService.newOrder(userId, newOrderRequest);

        // 讀取訂單資料
        Orders orders = orderService.getOrderByOrderId(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(orders);
    }
}
