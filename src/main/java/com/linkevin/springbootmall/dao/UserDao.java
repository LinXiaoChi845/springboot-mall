package com.linkevin.springbootmall.dao;

import com.linkevin.springbootmall.dto.UserRegisterRequest;
import com.linkevin.springbootmall.model.User;

public interface UserDao {

    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);
}
