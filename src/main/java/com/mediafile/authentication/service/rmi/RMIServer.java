/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mediafile.authentication.service.rmi;

import com.mediafile.rmi.interfaces.IAuthProvider;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author 000430063
 */
public class RMIServer extends Thread {
    private final String host;
    private final int port;
    private final IAuthProvider authProvider;
    
    public RMIServer(IAuthProvider authProvider){
        this.authProvider = authProvider;
        this.host = "localhost";
        this.port = 3000;
    }
    public RMIServer(IAuthProvider authProvider, String host, int port){
        this.authProvider = authProvider;
        this.host = host;
        this.port = port;
    }
    
    @Override
    public void run() {
        try {
            System.setProperty("java.rmi.server.hostname", this.host);
            IAuthProvider stub = (IAuthProvider) UnicastRemoteObject.exportObject(this.authProvider, this.port);
            Registry registry = LocateRegistry.createRegistry(this.port);
            registry.bind("AuthProvider", stub);
            System.out.println(String.format("[rmi-server] rmi server started on %s:%d", this.host, this.port));
        } catch (AlreadyBoundException | RemoteException ex) {
            System.out.println("[rmi-server] cannot create the rmi server: " + ex);
            this.interrupt();
        }
    }
    
    public void close() {
        this.interrupt();
    }
}