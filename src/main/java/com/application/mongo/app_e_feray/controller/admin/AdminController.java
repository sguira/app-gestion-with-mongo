package com.application.mongo.app_e_feray.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.repository.UserRepositori;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController()
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepositori userR;

    @GetMapping("/users")
    public ResponseEntity<Object> allUser() {
        return ResponseEntity.ok(userR.findAll());
    }

}
