package com.application.mongo.app_e_feray.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.repository.UserRenderRepository;
import com.application.mongo.app_e_feray.repository.UserRepositori;
import com.application.mongo.app_e_feray.repository.VenteRepo;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController()
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepositori userR;
    private final VenteRepo venteRepo;
    private final UserRenderRepository userRenderRepository;

    @GetMapping("/users")
    public ResponseEntity<Object> allUser() {
        return ResponseEntity.ok(userR.findAll());
    }

    @GetMapping(path = "/users/{id}")
    ResponseEntity getUserDetail(@PathVariable("id") String id) {
        return ResponseEntity.ok(userR.findById(id).orElse(null));
    }

    @DeleteMapping(path = "/delete-vente/{id}")
    ResponseEntity delteVente(@PathVariable String id) {
        try {
            venteRepo.deleteById(id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping(path = "/list-user")
    ResponseEntity<List<Users>> getListUser() {
        return ResponseEntity.ok(userRenderRepository.findByAll());
    }

}
