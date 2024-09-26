package com.example.demo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogTableDAO {
    
    private final String jdbcURL      = "jdbc:sqlserver://webapiangulardemo.mssql.somee.com:1433;databaseName=webapiangulardemo;encrypt=false";
    private final String jdbcUsername = "aperezNWO_SQLLogin_1";
    private final String jdbcPassword = "aperezNWO_SQLLogin_1";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    // Create User
    public void insertUser(String name, String email) throws SQLException {
        String sql = "INSERT INTO Users (name, email) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        }
    }

    // Get All Users
    public List<User> selectAllUsers() throws SQLException {
        String sql = " SELECT ID_column , PageName , AccessDate, IpValue FROM dbo.accessLogs";
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                long id = rs.getLong("ID_column");
                String name = rs.getString("PageName");
                String email = rs.getString("IpValue");
                users.add(new User(id, name, email));
            }
        }
        return users;
    }

    // Get User by ID
    public User selectUserById(long id) throws SQLException {
        String sql = "SELECT * FROM Users WHERE id = ?";
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                user = new User(id, name, email);
            }
        }
        return user;
    }

    // Update User
    public void updateUser(long id, String name, String email) throws SQLException {
        String sql = "UPDATE Users SET name = ?, email = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();
        }
    }

    // Delete User
    public void deleteUser(long id) throws SQLException {
        String sql = "DELETE FROM Users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }
}


