package com.example.demo;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DemoEndPoint {

    private final LogTableDAO userDAO = new LogTableDAO();

    @GetMapping("/hello")
    public String hello() {
        return "Hello, world!";
    }

    @GetMapping("/data")
    public MyData getData() {
        MyData data = new MyData();
        data.setName("John Doe");
        data.setAge(30);
        return data;
    }

    // Get All Users
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userDAO.selectAllUsers();
            return ResponseEntity.ok(users);
        } catch (SQLException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
