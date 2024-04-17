/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mediafile.authentication.service.repository;

import com.mediafile.authentication.service.utils.ServiceError;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Diego
 */
public class RepositoryBase {
    
    final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    final String connectionString;
    
    private Connection conn;
    private Statement stmt;

    public Connection getConn() {
        return conn;
    }

    public Statement getStmt() {
        return stmt;
    }
    
    public RepositoryBase(String connectionString) throws Exception {
        try {
            this.connectionString = connectionString;
            this.setConnection();
            System.out.println("[rmi-server] connected to mariadb");
        } catch (SQLException ex){
            System.out.println("[rmi-server] cannot connect to mariadb");
            throw ex;
        }
    }
    
    private void setConnection() throws SQLException {
        this.conn = DriverManager.getConnection(this.connectionString);
        this.stmt = conn.createStatement();
    }
    
    public ResultSet execute(String sql) throws SQLException {
        ResultSet rs;
        try {
            if(conn.isClosed() || stmt.isClosed()){
                setConnection();
            }
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println("[rmi-server] sql exeption: " + ex);
            throw ex;
        }
        return rs;
    }

}
