package com.example.demo;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    private final AccessLogDAO accessLogDAO = new AccessLogDAO();
    private final personasDAO _personasDAO = new personasDAO();

    //
    @GetMapping("/hello")
    public String hello() {

        // String originalString = "Hello, World! & ? / ■";
        // String encodedString = URLEncoder.encode(originalString,
        // StandardCharsets.UTF_8);

        // return encodedString;
        // return originalString;

        // Get a Charset object for UTF-8 encoding
        Charset utf8Charset = StandardCharsets.UTF_8;

        // Encode a string into a byte array
        String text = "Hello, World! &#x25A0; &#x2261;";

        byte[] encodedBytes = text.getBytes(utf8Charset);

        // Decode a byte array into a string
        String decodedText = new String(encodedBytes, utf8Charset);

        return decodedText;
    }

    // Get All LOGS
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

    // Get All PERSONS
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

    // GenerateRandomVertex_SpringBoot
    //
    @GetMapping("/GenerateRandomVertex_SpringBoot")
    public String GenerateRandomVertex_SpringBoot() {

        // Get a Charset object for UTF-8 encoding
        Charset utf8Charset = StandardCharsets.US_ASCII;

        // Encode a string into a byte array
        // String text = "Hello, World! &#x25A0; &#x2261;";

        String text = "[2,20]|[15,22]|[1,17]|[8,19]|[14,6]|[13,8]|[5,12]|[4,14]|[22,5]&#x25A0;{0,0,0,6,18,16,0,0,0}|{0,0,14,0,0,0,0,0,18}|{0,14,0,0,0,0,6,0,24}|{6,0,0,0,0,12,0,6,0}|{18,0,0,0,0,0,0,0,0}|{16,0,0,12,0,0,8,0,0}|{0,0,6,0,0,8,0,2,18}|{0,0,0,6,0,0,2,0,0}|{0,18,24,0,0,0,18,0,0}&#x25A0;00&lt;[2;20]&gt;-00-<br/>01&lt;[15;22]&gt;-34-[0;3]&#x2261;[3;7]&#x2261;[7;6]&#x2261;[6;2]&#x2261;[2;1]&#x2261;<br/>02&lt;[1;17]&gt;-20-[0;3]&#x2261;[3;7]&#x2261;[7;6]&#x2261;[6;2]&#x2261;<br/>03&lt;[8;19]&gt;-06-[0;3]&#x2261;<br/>04&lt;[14;6]&gt;-18-[0;4]&#x2261;<br/>05&lt;[13;8]&gt;-16-[0;5]&#x2261;<br/>06&lt;[5;12]&gt;-14-[0;3]&#x2261;[3;7]&#x2261;[7;6]&#x2261;<br/>07&lt;[4;14]&gt;-12-[0;3]&#x2261;[3;7]&#x2261;<br/>08&lt;[22;5]&gt;-32-[0;3]&#x2261;[3;7]&#x2261;[7;6]&#x2261;[6;8]&#x2261;";

        byte[] encodedBytes = text.getBytes(utf8Charset);

        // Decode a byte array into a string
        String decodedText = new String(encodedBytes, utf8Charset);

        return decodedText;

    }
}
