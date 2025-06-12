package com.linkevin.springbootmall.dao.impl;

import com.linkevin.springbootmall.dao.UserDao;
import com.linkevin.springbootmall.dto.UserRegisterRequest;
import com.linkevin.springbootmall.model.Users;
import com.linkevin.springbootmall.rowmapper.UserRowMapper;
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
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql="INSERT INTO users (email, password, created_date, last_modified_date ) " +
                "VALUES(:email, :password, :createdDate, :lastModifiedDate)";

        Date now = new Date();

        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        Integer userId = keyHolder.getKey().intValue();

        return userId;
    }

    @Override
    public Users getUserById(Integer userId) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date " +
                "FROM users " +
                "WHERE user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<Users> usersList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (usersList.size() > 0){
            return usersList.getFirst();
        } else {
            return null;
        }
    }

    @Override
    public Users getUserByEmail(String email) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date " +
                "FROM users " +
                "WHERE email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<Users> usersList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (usersList.size() > 0){
            return usersList.get(0);
        } else {
            return null;
        }
    }
}
