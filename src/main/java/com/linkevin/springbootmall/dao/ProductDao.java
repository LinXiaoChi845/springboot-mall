package com.linkevin.springbootmall.dao;

import com.linkevin.springbootmall.constant.ProductCategory;
import com.linkevin.springbootmall.dto.ProductRequest;
import com.linkevin.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getProducts(ProductCategory category, String search);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
