package com.linkevin.springbootmall.dao;

import com.linkevin.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);

}
