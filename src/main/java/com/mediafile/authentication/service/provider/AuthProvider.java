/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mediafile.authentication.service.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.mediafile.authentication.service.repository.AuthRepository;
import com.mediafile.rmi.classes.Response;
import com.mediafile.rmi.classes.User;
import com.mediafile.rmi.classes.args.LoginArgs;
import com.mediafile.rmi.classes.args.RegisterArgs;
import com.mediafile.rmi.interfaces.IAuthProvider;

import java.rmi.RemoteException;
import java.util.Date;
import java.security.MessageDigest;

/**
 *
 * @author 000430063
 */
public class AuthProvider implements IAuthProvider {
    
    private final AuthRepository repositorio;
    private String secretKey;
    
    public AuthProvider(AuthRepository repositorio) throws RemoteException, InterruptedException {
        this.repositorio = repositorio;
        this.secretKey = System.getenv("SECRET_KEY");
        if (secretKey == null) {
            secretKey = "Sistemas distribuidos";
        }
    }

    // me envian el token, verifico la informacion y regreso un booleano
    @Override
    public Response<Boolean> Auth(String string) throws RemoteException {
        // print token
        System.out.println("[rmi-server] token recibido: " + string);
        String token = string;
        // decodificar el token
        DecodedJWT decoded = JWT.decode(token);
        String userId = decoded.getSubject();
		try {
			User user = repositorio.GetUserById(userId);
            String email = user.getEmail();
            String fullName = user.getFullName();
            try {
                Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
                // JWT token to verify
                JWTVerifier verifier = JWT.require(algorithm)
                        .withSubject(userId)
                        .withClaim("email", email)
                        .withClaim("username", fullName)
                        .build();
                DecodedJWT jwt = verifier.verify(token);
                // print verification
                System.out.println("[rmi-server] token verificado: " + jwt.getToken());
                verifier.verify(token);
                System.out.println("[rmi-server] token verificado correctamente");
                return new Response<Boolean>(true);
            } catch (Exception e) {
                return new Response<Boolean>(false);
            }
		} catch (Exception e) {
			// regresar un error por usuario no existente
            return new Response<>(new String[] {"usuario no existente"});
        }
    }

    // me envian el id y me regreso el usuario
    @Override
    public Response<User> GetUser(String userId) throws RemoteException {
        try {
            return new Response<User>(repositorio.GetUserById(userId));
        } catch (Exception e) {
            return new Response<>(new String[] {"Ocurrio un error"});
        }
    }

    // me envian informacion para login y me regreso un token
    @Override
    public Response<String> Login(LoginArgs la) throws RemoteException {
        String email = la.getEmail();
        String password = la.getPassword();

        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        try {
            String passwordLogin = new String(java.util.Base64.getEncoder().encode(MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8"))));
            User user = repositorio.GetUserByEmail(email);
            String passwordUser = user.getPassword();
            if (passwordLogin.equals(passwordUser)) {
                // generar token con email con jwt
                String token = JWT.create()
                        .withSubject(user.getId())
                        .withClaim("email", email)
                        .withClaim("username", user.getFullName())
                        // time to expire of 1 week in seconds
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 1000))
                        // .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                        .sign(algorithm);
                System.out.println("[rmi-server] token generado para usuario logeado: " + token);
                return new Response<String>(token);
            } else {
                return new Response<>("contraseña incorrecta");
            }
        } catch (Exception e) {
            return new Response<>("Ocurrio un error");
        }
    }


    // me envian informacion para registrar y me regreso un token
    @Override
    public Response<String> Register(RegisterArgs ra) throws RemoteException {
        String userId = ra.getId();
        String fullName = ra.getFullname();
        String email = ra.getEmail();
        String password = ra.getPassword();
        // algoritmo de encriptacion
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        try {
            repositorio.InsertUser(userId, fullName, email, password);
            // generar token con email con jwt
            String token = JWT.create()
                    .withSubject(userId)
                    .withClaim("email", email)
                    .withClaim("username", fullName)
                    // time to expire of 1 week in seconds
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                    .sign(algorithm);
            System.out.println("[rmi-server] token generado para usuario registrado: " + token);
            return new Response<String>(token);
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            System.out.println("[rmi-server] token expirado: " + e.getMessage());
            return new Response<>("El token expiro" + e.getMessage());
        } catch (Exception e) {
            return new Response<>("Ocurrio un error");
        }
    }
    
}
