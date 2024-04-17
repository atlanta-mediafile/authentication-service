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
        
        if(host == null) {
            host = "localhost";
        }
        if(port == null) {
            port = "3000";
        }
        if(mariaCs == null){
            mariaCs = "jdbc:mariadb://localhost:3306/Auth?user=root&password=root";
        }
        
        AuthRepository repository = new AuthRepository(mariaCs);
        
        IAuthProvider authProvider = new AuthProvider(repository);
        
        RMIServer server = new RMIServer(authProvider, host, Integer.parseInt(port));
        
        server.start();
    }
}
