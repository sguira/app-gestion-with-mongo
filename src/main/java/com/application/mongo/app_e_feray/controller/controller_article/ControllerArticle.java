package com.application.mongo.app_e_feray.controller.controller_article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.entities.Categorie;
import com.application.mongo.app_e_feray.entities.Produit;
import com.application.mongo.app_e_feray.repository.CategorieRepositori;
import com.application.mongo.app_e_feray.repository.ProduitRepository;
import com.application.mongo.app_e_feray.services.JWTUtils;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/backend/articles")
public class ControllerArticle {

    @Autowired(required = true)
    ProduitRepository pr;

    @Autowired(required = true)
    private CategorieRepositori catR;

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

    @PostMapping(path = "/ajouter_produit/{id}")
    ResponseEntity<Produit> ajouter_produitf(@RequestBody Produit p, @PathVariable String id,
            @RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                Categorie cat = catR.findById(id).get();
                Produit produi = pr.save(p);
                cat.ajouterProduit(p);
                catR.save(cat);
                return new ResponseEntity<Produit>(produi, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/add_article/{id}")
    ResponseEntity<Produit> ajouter_article(@RequestBody Produit p, @PathVariable(name = "id") String id,
            @RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                Categorie cat = catR.findById(id).get();
                for (var i = 0; i < cat.getProduits().size(); i++) {
                    if (p.getName().toLowerCase().equals(cat.getProduits().get(i).getName().toLowerCase())) {
                        Produit prod = cat.getProduits().get(i);
                        prod.setQuantite(prod.getQuantite() + p.getQuantite());
                        cat.getProduits().set(i, prod);
                        catR.save(cat);
                        return new ResponseEntity<Produit>(pr.save(prod), HttpStatus.CREATED);
                    }
                }
                Produit produit = pr.save(p);
                cat.ajouterProduit(produit);
                catR.save(cat);
                return new ResponseEntity<Produit>(produit, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/update_article/{id_}")
    ResponseEntity<Produit> modifier_article(@RequestBody Produit p, @RequestHeader("Authorization") String token,
            @PathVariable(name = "id_") String id_) {

        try {
            token = tokenValide(token);
            if (token != null) {
                Categorie cat = catR.findById(id_).get();

                List<Produit> prod = cat.getProduits();
                for (var i = 0; i < prod.size(); i++) {
                    if (prod.get(i).getId() == p.getId()) {
                        System.out.println("okok\n\n");
                        prod.get(i).setName(p.getName());
                        prod.get(i).setPrix(p.getPrix());
                        prod.get(i).setDescription(p.getDescription());
                    }
                }

                catR.save(cat);
                return new ResponseEntity<Produit>(pr.save(p), HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/save_produit/{id_2}")
    ResponseEntity<?> save_article(@PathVariable(name = "id_2") String id, @RequestHeader("Authorization") String token,
            Produit produit) {

        try {
            token = tokenValide(token);
            if (token != null) {
                Produit p_ = pr.save(produit);
                // create the product

                Categorie cat = catR.findById(id).get();
                cat.ajouterProduit(p_);
                catR.save(cat);
                return new ResponseEntity<>(p_, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/ajouter_produit2/{id}")
    ResponseEntity<?> save_produit2(@PathVariable(name = "id") String id, @RequestBody Produit p,
            @RequestHeader("Authorization") String token) {

        try {
            token = tokenValide(token);
            if (token != null) {
                Categorie cat = catR.findById(id).get();
                Produit p_ = pr.save(p);
                cat.ajouterProduit(p_);
                catR.save(cat);

                return new ResponseEntity<>(p_, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // supprimer un produit
    @DeleteMapping(path = "/delete_product/{id}")
    ResponseEntity effacer_produit(@PathVariable String id, @RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                pr.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // delete
    @DeleteMapping(path = "/delete/{id}")
    int delete(@PathVariable(name = "id") String id, @PathVariable(name = "parent_id") String parent,
            @PathVariable(name = "choice") int choice) {
        switch (choice) {
            case 1: {
                Produit p = pr.findById(id).get();
                System.out.println("\n\n id cat:" + parent);
                Categorie cat = catR.findById(parent).get();
                // cat.effacer_produit(p);
                for (var i = 0; i < cat.getProduits().size(); i++) {
                    if (cat.getProduits().get(i).getId().equals(p.getId())) {
                        System.out.println("produit trouvé \n\n");
                        cat.getProduits().remove(i);
                    }
                }
                catR.save(cat);
                pr.deleteById(id);
                return 1;
            }
            case 2: {
                pr.deleteById(id);
                return 1;
            }
        }
        return -1;
    }

}
