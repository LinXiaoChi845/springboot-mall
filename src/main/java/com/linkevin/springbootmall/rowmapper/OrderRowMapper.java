package com.linkevin.springbootmall.rowmapper;

import com.linkevin.springbootmall.model.Orders;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<Orders> {

    @Override
    public Orders mapRow(ResultSet rs, int rowNum) throws SQLException {
        Orders orders = new Orders();

        orders.setOrderId(rs.getInt("order_id"));
        orders.setUserId(rs.getInt("user_id"));
        orders.setTotalAmount(rs.getInt("total_amount"));
        orders.setCreatedDate(rs.getTimestamp("created_date"));
        orders.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

        return orders;
    }
}
