package com.linkevin.springbootmall.service.impl;

import com.linkevin.springbootmall.dao.UserDao;
import com.linkevin.springbootmall.dto.UserLoginRequest;
import com.linkevin.springbootmall.dto.UserRegisterRequest;
import com.linkevin.springbootmall.model.Users;
import com.linkevin.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public Users getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        // 檢查註冊的 email (因為 Email 不能重複)
        Users users = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if (users != null) {
            log.warn("該 Email ({}) 已經被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        // 創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public Users login(UserLoginRequest userLoginRequest) {
        Users users = userDao.getUserByEmail(userLoginRequest.getEmail());

        // 檢查 user 是否存在
        if (users == null) {
            log.warn("該 email ({}) 尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        // 比較密碼
        if (users.getPassword().equals(hashedPassword)) {
            return users;
        } else {
            log.warn("該 email ({}) 的密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
