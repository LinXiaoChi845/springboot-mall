package com.linkevin.springbootmall.service;

import com.linkevin.springbootmall.dto.UserRegisterRequest;
import com.linkevin.springbootmall.model.User;

public interface UserService {

    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);
}
