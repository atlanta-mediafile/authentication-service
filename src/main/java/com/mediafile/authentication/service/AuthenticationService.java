/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mediafile.authentication.service;

import com.mediafile.authentication.service.rmi.RMIServer;

/**
 *
 * @author 000430063
 */
public class AuthenticationService {

    public static void main(String[] args) {
        String host = System.getenv("HOST");
        String port = System.getenv("PORT");
        
        if(host == null) {
            host = "localhost";
        }
        if(port == null) {
            port = "3000";
        }
        
        RMIServer server = new RMIServer(host, Integer.parseInt(port));
        server.start();
    }
}
