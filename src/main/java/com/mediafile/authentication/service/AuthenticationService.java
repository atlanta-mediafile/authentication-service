/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mediafile.authentication.service;

import com.mediafile.authentication.service.provider.AuthProvider;
import com.mediafile.authentication.service.repository.AuthRepository;
import com.mediafile.authentication.service.rmi.RMIServer;
import com.mediafile.rmi.interfaces.IAuthProvider;

/**
 *
 * @author 000430063
 */
public class AuthenticationService {

    public static void main(String[] args) throws Exception {
        String host = System.getenv("HOST");
        String port = System.getenv("PORT");
        
        String mariaCs = System.getenv("MARIA_CONNECTION_STRING");
        String mariaUser = System.getenv("MARIA_USER");
        String mariaPass = System.getenv("MARIA_PASSWORD");
        
        if(host == null) {
            host = "localhost";
        }
        if(port == null) {
            port = "3000";
        }
        if(mariaUser == null){
            mariaUser = "root";
        }
        if(mariaPass == null){
            mariaPass = "root";
        }
        
        AuthRepository repository;
        
        if(mariaCs == null) {
            repository = new AuthRepository(mariaUser, mariaPass);
        } else {
            repository = new AuthRepository(mariaCs, mariaUser, mariaPass);
        }
        
        IAuthProvider authProvider = new AuthProvider(repository);
        
        RMIServer server = new RMIServer(authProvider, host, Integer.parseInt(port));
        
        server.start();
    }
}
