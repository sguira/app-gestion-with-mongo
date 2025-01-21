package com.application.mongo.app_e_feray.controller.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.repository.UserRepositori;
import com.application.mongo.app_e_feray.services.JWTUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping(path = "/auth/")
public class Auth {
    @Autowired
    UserRepositori userR;

    @Autowired
    JWTUtils jwtUtils;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping(path = "login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        String username = user.getEmail();
        String password = user.getPassword();

        Users u = userR.findByEmail(username);

        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        if (!passwordEncoder.matches(password, u.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error");
        }

        String token = jwtUtils.generateToken(username);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("get_user")
    ResponseEntity<String> extract_user_name(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String userName = jwtUtils.extractUsername(token);
            return new ResponseEntity<>(userName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("ERROR", HttpStatus.OK);
        }
    }

    @RequestMapping("validate_token")
    public boolean validate(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            return jwtUtils.validateToken(token);
        } catch (Exception e) {
            return false;
        }
    }

}
