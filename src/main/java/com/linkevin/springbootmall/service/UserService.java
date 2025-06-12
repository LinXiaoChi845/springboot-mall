package com.linkevin.springbootmall.service;

import com.linkevin.springbootmall.dto.UserLoginRequest;
import com.linkevin.springbootmall.dto.UserRegisterRequest;
import com.linkevin.springbootmall.model.Users;

public interface UserService {

    Users getUserById(Integer userId);

    Integer register(UserRegisterRequest userRegisterRequest);

    Users login(UserLoginRequest userLoginRequest);
}
