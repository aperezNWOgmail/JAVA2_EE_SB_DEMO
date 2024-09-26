package com.example.demo;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DemoEndPoint {
    //
    private final AccessLogDAO accessLogDAO = new AccessLogDAO();
    //
    @GetMapping("/hello")
    public String hello() {
        return "Hello, world!";
    }
    // Get All Users
    @GetMapping("/getAllLogs")
    public ResponseEntity<List<AccessLog>> getAllLogs() 
    {
        try 
        {
            //
            List<AccessLog> accessLog = accessLogDAO.getAllLogs();
            return ResponseEntity.ok(accessLog);
        } 
        catch (SQLException e) 
        {
            return ResponseEntity.status(500).body(null);
        }
    }
}
