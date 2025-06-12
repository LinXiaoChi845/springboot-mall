package com.linkevin.springbootmall.controller;

import com.linkevin.springbootmall.dto.UserLoginRequest;
import com.linkevin.springbootmall.dto.UserRegisterRequest;
import com.linkevin.springbootmall.model.Users;
import com.linkevin.springbootmall.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<Users> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        Integer userId = userService.register(userRegisterRequest);

        Users users = userService.getUserById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    @PostMapping("/users/login")
    public ResponseEntity<Users> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {

        Users users = userService.login(userLoginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
