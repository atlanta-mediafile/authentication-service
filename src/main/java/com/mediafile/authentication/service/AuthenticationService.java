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
        RMIServer server = new RMIServer("localhost", 3000);
        server.start();
    }
}
