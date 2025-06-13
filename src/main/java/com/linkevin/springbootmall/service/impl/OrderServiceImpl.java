package com.linkevin.springbootmall.service.impl;

import com.linkevin.springbootmall.dao.OrderDao;
import com.linkevin.springbootmall.dao.ProductDao;
import com.linkevin.springbootmall.dao.UserDao;
import com.linkevin.springbootmall.dto.BuyItem;
import com.linkevin.springbootmall.dto.NewOrderRequest;
import com.linkevin.springbootmall.model.OrderItem;
import com.linkevin.springbootmall.model.Orders;
import com.linkevin.springbootmall.model.Product;
import com.linkevin.springbootmall.model.Users;
import com.linkevin.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Orders getOrderByOrderId(Integer orderId) {
        Orders orders = orderDao.getOrderByOrderId(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemByOrderId(orderId);

        orders.setOrderItemList(orderItemList);

        return orders;
    }

    @Transactional
    @Override
    public Integer newOrder(Integer userId, NewOrderRequest newOrderRequest) {
        Users users = userDao.getUserById(userId);

        if (users == null) {
            log.warn("該 userId ({}) 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Integer totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : newOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            if (product == null) {
                log.warn("商品 ({}) 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {
                if (product.getStock() < buyItem.getQuantity()) {
                    log.warn("商品 ({}) 庫存數量不足，無法購買，剩餘庫存 {}，欲購買庫存 {}",
                            buyItem.getProductId(), product.getStock(), buyItem.getQuantity());
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
            }

            // 扣除商品庫存
            productDao.updateStock(buyItem.getProductId(), product.getStock() - buyItem.getQuantity());

            // 計算總價錢
            Integer amount = buyItem.getQuantity() * product.getPrice();
            totalAmount = totalAmount + amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        // 創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);

        return orderId;
    }
}
