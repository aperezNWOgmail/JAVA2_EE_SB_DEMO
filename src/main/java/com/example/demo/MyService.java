package com.example.demo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    @Autowired
    //private JdbcTemplate jdbcTemplate;

    public List<String> getNames() {
        //String sql = "SELECT name FROM users";
        return null; //jdbcTemplate.queryForList(sql, String.class);
    }
}