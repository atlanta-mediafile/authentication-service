/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mediafile.authentication.service.provider;

import com.mediafile.authentication.service.rmi.RMIServer;
import com.mediafile.rmi.classes.Response;
import com.mediafile.rmi.classes.User;
import com.mediafile.rmi.classes.args.LoginArgs;
import com.mediafile.rmi.classes.args.RegisterArgs;
import com.mediafile.rmi.interfaces.IAuthProvider;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
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
    public static void setUpClass() throws RemoteException, NotBoundException {
        // Start the server
        AuthProviderIT.server = new RMIServer("localhost", 3000);
        server.start();
        
        // Start client
        Registry registry = LocateRegistry.getRegistry("localhost", 3000);
        AuthProviderIT.authProvider = (IAuthProvider) registry.lookup("AuthProvider");
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
        Response<String> responseRegister = authProvider.Register(new RegisterArgs("user2024", "user2024@gmail.com", "user2024pass"));

        assertTrue(responseRegister.isSuccess());
        assertEquals(0, responseRegister.getErrors().length);
        assertTrue(responseRegister.getData().matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));
        
        Response<Boolean> responseAuthRegister = authProvider.Auth(responseRegister.getData());
        
        assertTrue(responseAuthRegister.isSuccess());
        assertEquals(0, responseAuthRegister.getErrors().length);
        assertTrue(responseAuthRegister.getData());
        
        Response<String> responseLogin = authProvider.Login(new LoginArgs("user2024", "user2024pass"));
        
        assertTrue(responseLogin.isSuccess());
        assertEquals(0, responseLogin.getErrors().length);
        assertTrue(responseLogin.getData().matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));
        
        Response<Boolean> responseAuthLogin = authProvider.Auth(responseLogin.getData());
        
        assertTrue(responseAuthLogin.isSuccess());
        assertEquals(0, responseAuthLogin.getErrors().length);
        assertTrue(responseAuthLogin.getData());
    }
    
}
