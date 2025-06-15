package com.linkevin.springbootmall.dao.impl;

import com.linkevin.springbootmall.dao.OrderDao;
import com.linkevin.springbootmall.dto.OrderQueryParams;
import com.linkevin.springbootmall.dto.ProductQueryParams;
import com.linkevin.springbootmall.model.OrderItem;
import com.linkevin.springbootmall.model.Orders;
import com.linkevin.springbootmall.rowmapper.OrderItemRowMapper;
import com.linkevin.springbootmall.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = "SELECT COUNT(order_id) FROM orders WHERE 1 = 1";

        Map<String, Object> map = new HashMap<>();

        // 查詢條件
        sql = addFilteringSql(sql, map, orderQueryParams);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return total;
    }

    @Override
    public List<Orders> getOrders(OrderQueryParams orderQueryParams) {
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date " +
                "FROM orders WHERE 1 = 1";

        Map<String, Object> map = new HashMap<>();

        // 查詢條件
        sql = addFilteringSql(sql, map, orderQueryParams);

        // 排序
        sql = sql + " ORDER BY created_date DESC ";

        // 分頁 ( 因為 SQL SERVER 沒有 LIMIT 指令，要用 FETCH 指令 )
        sql = sql + " OFFSET :offset ROWS ";
        map.put("offset", orderQueryParams.getOffset());

        if (orderQueryParams.getLimit() > 0 ) {
            sql = sql + " FETCH FIRST :limit ROWS ONLY";
            map.put("limit", orderQueryParams.getLimit());
        }

        List<Orders> ordersList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        return ordersList;
    }

    @Override
    public Orders getOrderByOrderId(Integer orderId) {
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date " +
                " FROM orders " +
                " WHERE order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<Orders> ordersList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        if (!ordersList.isEmpty()) {
            return ordersList.getFirst();
        } else {
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemByOrderId(Integer orderId) {
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, " +
                " p.product_name, p.image_url " +
                " FROM order_item oi " +
                " LEFT JOIN product p ON oi.product_id = p.product_id " +
                " WHERE oi.order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());

        return orderItemList;
    }

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {
        String sql = "INSERT INTO orders (user_id, total_amount, created_date, last_modified_date) " +
                " VALUES(:userId, :totalAmount, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("totalAmount", totalAmount);

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        Integer orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount) " +
                " VALUES(:orderId, :productId, :quantity, :amount)";

        // 使用 for loop 一條一條 sql 加入數據，效率較低
//        for (OrderItem orderItem : orderItemList) {
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("orderId", orderId);
//            map.put("productId", orderItem.getProductId());
//            map.put("quantity", orderItem.getQuantity());
//            map.put("amount", orderItem.getAmount());
//
//            namedParameterJdbcTemplate.update(sql, map);
//        }

        // 使用 batchupdate 一次性加入數據，效率較高
        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[orderItemList.size()];

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);

            parameterSources[i] = new MapSqlParameterSource();
            parameterSources[i].addValue("orderId", orderId);
            parameterSources[i].addValue("productId", orderItem.getProductId());
            parameterSources[i].addValue("quantity", orderItem.getQuantity());
            parameterSources[i].addValue("amount", orderItem.getAmount());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
    }

    // 拼接查詢條件
    private String addFilteringSql(String sql, Map<String, Object> map, OrderQueryParams orderQueryParams) {

        if (orderQueryParams.getUserId() != null) {
            sql = sql + " AND orders.user_id = :userId ";
            map.put("userId", orderQueryParams.getUserId());
        }

        return sql;
    }
}
