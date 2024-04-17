/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mediafile.authentication.service.repository;

import com.mediafile.authentication.service.utils.ServiceError;
import com.mediafile.rmi.classes.User;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author 000430063
 */
public class AuthRepository extends RepositoryBase {

    public AuthRepository (String connectionString) throws Exception {
        super(connectionString);
    }
    
    public User GetUserById(String userId) throws ServiceError {
        String sql = "SELECT * FROM User WHERE id = '" + userId + "';";
        ResultSet rs;
        boolean next;
        try {
            rs = this.execute(sql);
            next = rs.next();
        } catch (SQLException ex) {
            throw new ServiceError("Server error");
        }
        
        if (next) {
            // recibe los datos del resultado y crea un objeto de usuario
            User user;
            try {
                user = new User(rs.getString("id"), rs.getString("fullName"), rs.getString("email"), rs.getString("password"));
            } catch (SQLException ex) {
                throw new ServiceError("Server error");
            }

            return user;
        } else {
            // no usuario encontrado con el id
            System.out.println("No user found with id: " + userId);
            throw new ServiceError("Usuario no encontrado");
        }
    }
    
    public User GetUserByEmail(String email) throws ServiceError {
        String sql = "SELECT * FROM User WHERE email = '" + email + "';";
        ResultSet rs;
        boolean next;
        try {
            rs = this.execute(sql);
            next = rs.next();
        } catch (SQLException ex) {
            throw new ServiceError("Server error");
        }
    
        // reverifica si hay resultados
        if (next) {
            // recibe los datos del resultado y crea un objeto de usuario
            User user;
            try {
                user = new User(rs.getString("id"), rs.getString("fullName"), rs.getString("email"), rs.getString("password"));
            } catch (SQLException ex) {
                throw new ServiceError("Server error");
            }

            System.out.println("[rmi-server] " + user.getId() + " - " + user.getFullName() + " - " + user.getEmail());

            return user;
        } else {
            // no usuario encontrado con el email
            System.out.println("No user found with email: " + email);
            throw new ServiceError("Usuario no encontrado");
        }
    }

    public void InsertUser(String userId, String fullName, String email, String password) throws ServiceError {
        String pwd = password;
        // parametros de email
        if (email.indexOf('@') == -1 || email.indexOf('.') == -1) {
            System.out.println("[rmi-server] Error al crear usuario, correo no valido");
            throw new ServiceError("Correo invalido");
        }

        try {
            // metodo para encriptar contraseña
            String passwordHash = new String(java.util.Base64.getEncoder().encode(MessageDigest.getInstance("SHA-256").digest(pwd.getBytes("UTF-8"))));
            // sql query para insertar un usuario con userId, fullName, email y password
            String sql = "INSERT INTO User (id, fullName, email, password) VALUES ('" + userId + "', '" + fullName + "', '" + email + "', '" + passwordHash + "');";
            this.execute(sql);
            System.out.println("[rmi-server] usuario insertado");
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("[rmi-server] Error al crear usuario, correo o UUID ya existen");
            throw new ServiceError("Error al crear usuario, correo o UUID ya existen");
        } catch (java.sql.SQLSyntaxErrorException e) {
            System.out.println("[rmi-server] Error al crear usuario, algun dato es incorrecto");
            throw new ServiceError("Error al crear usuario, algun dato es incorrecto");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | SQLException e) {
            throw new ServiceError("Server error");
        }
    }
    
}
