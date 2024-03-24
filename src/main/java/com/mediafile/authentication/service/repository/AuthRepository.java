 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mediafile.authentication.service.repository;

import com.mediafile.rmi.classes.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author 000430063
 */
public class AuthRepository {
    
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    
    final Connection conn;
    final Statement stmt;

    public AuthRepository (String connectionString, String user, String password) throws Exception {
        try {
            Class.forName(JDBC_DRIVER);
            this.conn = DriverManager.getConnection(connectionString, user, password);
            this.stmt = conn.createStatement();
            System.out.println("[rmi-server] connected to mariadb");
        } catch (ClassNotFoundException | SQLException ex){
            System.out.println("[rmi-server] cannot connect to mariadb");
            throw new Exception();
        }
    }
    
    public AuthRepository (String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        this.conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/", user, password);
        this.stmt = conn.createStatement();
    }
    
    public User GetUserById(String userId) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public User GetUserByEmail(String email) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void InsertUser(String userId, String fullName, String email, String password) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
