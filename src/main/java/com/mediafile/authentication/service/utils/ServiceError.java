/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mediafile.authentication.service.utils;

/**
 *
 * @author Diego
 */
public class ServiceError extends Exception {
    
    public ServiceError(String error) {
        super(error);
    }
    
    public String getError() {
        return this.getMessage();
    }
    
}
