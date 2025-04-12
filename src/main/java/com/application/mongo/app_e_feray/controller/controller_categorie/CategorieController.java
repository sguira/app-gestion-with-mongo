package com.application.mongo.app_e_feray.controller.controller_categorie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.entities.Categorie;
import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.repository.CategorieRepositori;
import com.application.mongo.app_e_feray.repository.UserRepositori;
import com.application.mongo.app_e_feray.services.JWTUtils;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/api/v1/categories/")
public class CategorieController {

    @Autowired(required = true)
    private CategorieRepositori catR;

    @Autowired(required = true)
    private UserRepositori usersR;

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

    @PostMapping(path = "/addcategorie")
    ResponseEntity<Categorie> ajouterCategorie(@RequestBody Categorie cat,
            @RequestHeader("Authorization") String token) {

        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
                System.out.println("Client " + u.getName());
                List<Categorie> cats = (List<Categorie>) (u.getCategories());
                if (cats.size() > 0) {
                    for (int i = 0; i < cats.size(); i++) {
                        if (cats.get(i) != null) {
                            if (cats.get(i).getName() != null) {
                                if (cats.get(i).getName().equals(cat.getName())) {
                                    System.out.println(" CONFLICT >>>>>>>>>>>>> ");
                                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                                }
                            }
                        }
                    }
                }
                Categorie categorie = catR.save(cat);
                u.ajouterCategorie(categorie);
                usersR.save(u);
                // catR.save(categorie);
                return new ResponseEntity<Categorie>(categorie, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @DeleteMapping(path = "/delete_categorie/{id}")
    ResponseEntity<?> deletecategorie(@RequestHeader("Authorization") String token, @PathVariable String id)
            throws Exception {
        try {
            token = tokenValide(token);
            if (token != null) {
                catR.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/update_categorie")
    ResponseEntity<Categorie> updateCategorie(@RequestHeader("Authorization") String token, @RequestBody Categorie cat_)
            throws Exception {
        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = usersR.findByEmail(jwtUtils.extractUsername(token));

                Categorie cat = catR.findById(cat_.getId()).get();
                cat.setName(cat_.getName());
                cat.setDescription(cat_.getDescription());

                return new ResponseEntity<Categorie>(catR.save(cat), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            System.out.println("error" + e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(path = "/all_categories")
    ResponseEntity<List<Categorie>> get_categorie_by_user(@RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                return new ResponseEntity<List<Categorie>>(usersR.findByEmail(token).getCategories(),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
