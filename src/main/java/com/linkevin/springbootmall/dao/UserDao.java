package com.linkevin.springbootmall.dao;

import com.linkevin.springbootmall.dto.UserRegisterRequest;
import com.linkevin.springbootmall.model.Users;

public interface UserDao {

    Integer createUser(UserRegisterRequest userRegisterRequest);

    Users getUserById(Integer userId);

    Users getUserByEmail(String email);
}
