package com.application.mongo.app_e_feray.controller.rentree_controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.entities.Client;
import com.application.mongo.app_e_feray.entities.Commande;
import com.application.mongo.app_e_feray.entities.Produit;
import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.entities.ventes;
import com.application.mongo.app_e_feray.repository.ClientRepo;
import com.application.mongo.app_e_feray.repository.CommandeRepositori;
import com.application.mongo.app_e_feray.repository.ProduitRepository;
import com.application.mongo.app_e_feray.repository.UserRepositori;
import com.application.mongo.app_e_feray.repository.VenteRepo;
import com.application.mongo.app_e_feray.services.JWTUtils;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/backend/rentree")
public class RentreeController {

    @Autowired(required = true)
    VenteRepo venteRepo;

    @Autowired(required = true)
    private UserRepositori usersR;

    @Autowired(required = true)
    private ProduitRepository pr;

    @Autowired(required = true)
    private CommandeRepositori commandeRepo;

    @Autowired(required = true)
    private ClientRepo clientR;

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

    @PostMapping(path = "/add_ventes/{id_clients}")
    ResponseEntity<ventes> ajouter_ventes(@RequestBody ventes v, @RequestHeader("Authorization") String token,
            @PathVariable(name = "id_clients") String id_2) {
        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = usersR.findByemail(jwtUtils.extractUsername(token));

                String item = v.getEspece() + "," + v.getDate();
                v.addVentes(item);
                Commande commande = new Commande();
                v.getArticles_().forEach((article) -> {
                    List<Produit> produit = pr.findAll();
                    for (var i = 0; i < produit.size(); i++) {
                        if (article.split(",")[0].equals(produit.get(i).getName())) {
                            Produit p = produit.get(i);
                            p.setQuantite(p.getQuantite() - Integer.parseInt(article.split(",")[1]));
                            pr.save(p);
                        }
                    }
                });
                if (!id_2.equals("-1")) {
                    try {

                        if (!v.getNumeroCommande().equals("")) {
                            commande = commandeRepo.getCommandebyName(v.getNumeroCommande());
                        }
                        String name = clientR.findById(id_2).get().getName();
                        v.setNomClient(name);
                        if (commande != null) {

                            commandeRepo.save(commande);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ventes v2 = venteRepo.save(v);

                    Client c = clientR.findById(id_2).get();
                    c.ajouter_ventes(v2);
                    u.ajouter_ventes(v2);
                    clientR.save(c);
                    usersR.save(u);
                    return new ResponseEntity<ventes>(v2, HttpStatus.CREATED);
                } else {
                    ventes v2 = venteRepo.save(v);
                    v.setNomClient("Inconnu");
                    u.ajouter_ventes(v2);
                    usersR.save(u);
                    return new ResponseEntity<ventes>(v2, HttpStatus.CREATED);
                }

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(path = "vente_client/{id}")
    ResponseEntity<List<ventes>> vente_by_client(@PathVariable String id) {
        return new ResponseEntity<List<ventes>>(clientR.findById(id).get().getVentes(), HttpStatus.OK);
    }

}
