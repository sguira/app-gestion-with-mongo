package com.application.mongo.app_e_feray.controller.controller_fournisseur;

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

import com.application.mongo.app_e_feray.entities.Achat;
import com.application.mongo.app_e_feray.entities.Fournisseur;
import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.repository.AchatR;
import com.application.mongo.app_e_feray.repository.FournisseurRepo;
import com.application.mongo.app_e_feray.repository.UserRepositori;
import com.application.mongo.app_e_feray.services.JWTUtils;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/api/v1/fournisseurs/")
public class FournisseurController {

    @Autowired(required = true)
    private FournisseurRepo fournisseurRepo;

    @Autowired(required = true)
    private UserRepositori usersR;

    @Autowired(required = true)
    private AchatR achatR;

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

    @PostMapping(path = "/add_fournisseur", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<Fournisseur> add_fournisseur(@RequestBody Fournisseur f,
            @RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
                Fournisseur f_ = fournisseurRepo.save(f);
                u.ajouter_fournisseur(f_);
                usersR.save(u);
                return new ResponseEntity<Fournisseur>(f_, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/add_achat/{id_2}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<Achat> ajouter_depense(@RequestHeader("Authorization") String token,
            @PathVariable(name = "id_2") String id_,
            @RequestBody Achat a) {

        try {
            token = tokenValide(token);
            if (token != null) {

                if (!id_.equals("-1")) {
                    Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
                    Fournisseur f = fournisseurRepo.findById(id_).get();
                    String val = a.getDate() + a.getDesignation() + a.getEspece();
                    String remboursement = a.getEspece() + "," + a.getDate();
                    a.addAchat(remboursement);
                    a.setNomFournisseur(f.getName());
                    Achat a_ = achatR.save(a);
                    u.ajouter_achats(a_);
                    usersR.save(u);

                    f.ajouter_achat(a_);
                    fournisseurRepo.save(f);
                    return new ResponseEntity<Achat>(a_, HttpStatus.CREATED);
                } else {
                    Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
                    a.setNomFournisseur("Inconnu");
                    Achat a_ = achatR.save(a);
                    u.ajouter_achats(a_);
                    usersR.save(u);
                    return new ResponseEntity<Achat>(achatR.save(a), HttpStatus.CREATED);
                }

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Fonction pour modifier un client
    @PutMapping(path = "/update_fournisseur", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<Fournisseur> update_fournisseur(@RequestHeader("Authorization") String token,
            @RequestBody Fournisseur c) {
        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = usersR.findByEmail(jwtUtils.extractUsername(token));

                List<Fournisseur> f = u.getFournisseurs();
                Fournisseur fournisseur = fournisseurRepo.findById(c.getId()).get();
                for (int i = 0; i < f.size(); i++) {
                    if (f.get(i).getId().equals(c.getId())) {
                        fournisseur.setEmail(c.getEmail());
                        fournisseur.setName(c.getName());
                        fournisseur.setNumber(c.getNumber());
                        u.getFournisseurs().set(i, fournisseur);
                        break;
                    }
                }
                usersR.save(u);
                return new ResponseEntity<Fournisseur>(fournisseurRepo.save(fournisseur), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete_fournisseur/{idF}")
    ResponseEntity<Long> deleteFournisseur(@RequestHeader("Authorization") String token,
            @PathVariable(name = "idF") String id2) {
        token = tokenValide(token);
        if (token != null) {
            fournisseurRepo.deleteById(id2);
            return new ResponseEntity<Long>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @GetMapping(path = "/all_achat/{id}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    List<?> all_achats(@PathVariable(name = "id") String id) {
        List<Achat> v = usersR.findById(id).get().getAchats();
        return v;

    }

    @GetMapping(path = "achats", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<List<Achat>> find_achats(@RequestHeader("Authorization") String token) {
        token = tokenValide(token);
        if (token != null) {
            return new ResponseEntity<List<Achat>>(usersR.findByEmail(jwtUtils.extractUsername(token)).getAchats(),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    double get_achats_total(List<Achat> v, String date) {
        double res = 0d;
        for (int i = 0; i < v.size(); i++) {
            if (v.get(i).getDate().toString().equals(date.toString())) {
                res += v.get(i).getEspece();
            }
        }
        return res;
    }

}
