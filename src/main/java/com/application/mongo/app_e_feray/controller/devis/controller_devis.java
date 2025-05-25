package com.application.mongo.app_e_feray.controller.devis;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.dto.TypeDevis;
import com.application.mongo.app_e_feray.entities.Devis;
import com.application.mongo.app_e_feray.repository.DevisRepository;
import com.application.mongo.app_e_feray.repository.UserRepositori;
import com.application.mongo.app_e_feray.services.JWTUtils;

@RestController
@RequestMapping("/api/v1/devis")
@CrossOrigin("*")
class DevisController {
    @Autowired
    UserRepositori userR;

    @Autowired
    DevisRepository devisR;

    @Autowired
    JWTUtils jwtUtils;

    String tokenValide(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7, token.length());
                if (jwtUtils.validateToken(token)) {
                    return token;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping(path = "/devis")
    ResponseEntity<?> ajouterDevis(@RequestBody Devis devis, @RequestHeader("Authorization") String token) {

        try {
            token = tokenValide(token);
            if (token != null) {
                Users user = userR.findByEmail(jwtUtils.extractUsername(token));

                devisR.save(devis);
                user.ajouterDevis(devis);
                userR.save(user);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @GetMapping(path = "/devis/{id}")
    // public ResponseEntity<List<Devis>> getDevisByArticle(@PathVariable String id)
    // {
    // Users u = userR.findById(id).get();
    // return new ResponseEntity<>(u.getDevis(), HttpStatus.OK);
    // }

    @GetMapping(path = "/devis")
    ResponseEntity<List<Devis>> getDevis(@RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = userR.findByEmail(jwtUtils.extractUsername(token));
                if (u != null) {
                    List<Devis> devis = new ArrayList<>();
                    for (var e : devis) {
                        if (e.getType().equals("DEVIS")) {
                            devis.add(e);
                        }
                    }
                    return new ResponseEntity<>(devis, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/bcommande")
    ResponseEntity<List<Devis>> getBonCommande(@RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = userR.findByEmail(jwtUtils.extractUsername(token));
                if (u != null) {
                    List<Devis> devis = new ArrayList<>();
                    for (var e : devis) {
                        if (e.getType().equals("BON_COMMANDE")) {
                            devis.add(e);
                        }
                    }
                    return new ResponseEntity<>(devis, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
