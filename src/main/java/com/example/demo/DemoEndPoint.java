package com.example.demo;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DAO.AccessLogDAO;
import com.example.DAO.personasDAO;
import com.example.entity.accessLog;
import com.example.entity.personaTable;

@RestController
public class DemoEndPoint {
    //
    private final AccessLogDAO accessLogDAO  = new AccessLogDAO();
    private final personasDAO  _personasDAO  = new personasDAO();
    //
    @GetMapping("/hello")
    public String hello() {
        return "Hello, world!";
    }

    // Get All Users
    @GetMapping("/getAllLogs")
    public ResponseEntity<List<accessLog>> getAllLogs() {
        try {
            //
            List<accessLog> accessLog = accessLogDAO.getAllLogs();
            return ResponseEntity.ok(accessLog);
        } catch (SQLException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    // Get All Users
    @GetMapping("/getAllPersons")
    public ResponseEntity<List<personaTable>> getPersons() {
        try {
            //
            List<personaTable> personas = _personasDAO.getAllPersons();
            return ResponseEntity.ok(personas);
        } catch (SQLException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
