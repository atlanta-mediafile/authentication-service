/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mediafile.authentication.service.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mediafile.authentication.service.repository.AuthRepository;
import com.mediafile.authentication.service.rmi.RMIServer;
import com.mediafile.rmi.classes.Response;
import com.mediafile.rmi.classes.User;
import com.mediafile.rmi.classes.args.LoginArgs;
import com.mediafile.rmi.classes.args.RegisterArgs;
import com.mediafile.rmi.interfaces.IAuthProvider;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Dego
 */
public class AuthProviderIT {
    
    private static IAuthProvider authProvider;
    private static RMIServer server;
    
    @BeforeAll
    public static void setUpClass() throws ClassNotFoundException, SQLException, NotBoundException, MalformedURLException, RemoteException {
        String host = "localhost";
        int port = 3001;
        
        // Start the server
        AuthProviderIT.server = new RMIServer(new AuthProvider(new AuthRepository("root", "root")), host, port);
        
        // Start client
        AuthProviderIT.authProvider = (IAuthProvider) Naming.lookup(String.format("rmi://%s:%d/AuthProvider", host, port));
    }
    
    @AfterAll
    public static void tearDownClass() {
        // Close server
        server.close();
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @org.junit.jupiter.api.Test
    public void testAuth() throws Exception {
        String uuid = UUID.randomUUID().toString();
        
        // REGISTER
        RegisterArgs regArgs = new RegisterArgs(uuid, "user name", "user2024@gmail.com", "user2024pass");
        Response<String> responseRegister = authProvider.Register(regArgs);

        DecodedJWT decoded = JWT.decode(responseRegister.getData());
        String userId = decoded.getClaim("userId").asString();
        String email = decoded.getClaim("email").asString();
        decoded.getClaim("fullname").asString();
        
        assertTrue(responseRegister.isSuccess());
        assertEquals(0, responseRegister.getErrors().length);
        assertTrue(userId.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));
        assertTrue(email.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"));
        
        // AUTH REGISTER JWT
        Response<Boolean> responseAuthRegister = authProvider.Auth(responseRegister.getData());
        
        assertTrue(responseAuthRegister.isSuccess());
        assertEquals(0, responseAuthRegister.getErrors().length);
        assertTrue(responseAuthRegister.getData());
        
        // LOGIN
        Response<String> responseLogin = authProvider.Login(new LoginArgs("user2024@gmail.com", "user2024pass"));
        
        DecodedJWT _decoded = JWT.decode(responseRegister.getData());
        String _userId = _decoded.getClaim("userId").asString();
        String _email = _decoded.getClaim("email").asString();
        decoded.getClaim("fullname").asString();
        
        assertTrue(responseRegister.isSuccess());
        assertEquals(0, responseRegister.getErrors().length);
        assertTrue(_userId.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));
        assertTrue(_email.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"));
        
        // AUTH LOGIN JWT
        Response<Boolean> responseAuthLogin = authProvider.Auth(responseLogin.getData());
        
        assertTrue(responseAuthLogin.isSuccess());
        assertEquals(0, responseAuthLogin.getErrors().length);
        assertTrue(responseAuthLogin.getData());
        
        // GET USER BY ID
        Response<User> responseGetUser = authProvider.GetUser(uuid);
        User user = responseGetUser.getData();
        
        assertTrue(responseGetUser.isSuccess());
        assertEquals(0, responseGetUser.getErrors().length);
        assertEquals(user.getId(), uuid);
        assertEquals(user.getFullName(), "user name");
        assertEquals(user.getEmail(), "user2024@gmail.com");
        
    }
    
}
