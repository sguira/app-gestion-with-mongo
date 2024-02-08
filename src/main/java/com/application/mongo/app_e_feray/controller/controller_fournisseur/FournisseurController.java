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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.entities.Achat;
import com.application.mongo.app_e_feray.entities.Fournisseur;
import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.repository.AchatR;
import com.application.mongo.app_e_feray.repository.FournisseurRepo;
import com.application.mongo.app_e_feray.repository.UserRepositori;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/backend/fournisseurs/")
public class FournisseurController {

    @Autowired(required = true)
    private FournisseurRepo fournisseurRepo;

    @Autowired(required = true)
    private UserRepositori usersR;

    @Autowired(required = true)
    private AchatR achatR;

    @PostMapping(path = "/add_fournisseur/{id}")
    ResponseEntity<Fournisseur> add_fournisseur(@RequestBody Fournisseur f, @PathVariable String id) {
        Users u = usersR.findById(id).get();
        Fournisseur f_ = fournisseurRepo.save(f);
        u.ajouter_fournisseur(f_);
        usersR.save(u);
        return new ResponseEntity<Fournisseur>(f_, HttpStatus.CREATED);
    }

    @PostMapping(path = "/add_achat/{id_1}/{id_2}")
    ResponseEntity<Achat> ajouter_depense(@PathVariable(name = "id_1") String id,
            @PathVariable(name = "id_2") String id_,
            @RequestBody Achat a) {

        if (!id_.equals("-1")) {
            Users u = usersR.findById(id).get();
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
            Users u = usersR.findById(id).get();
            a.setNomFournisseur("Inconnu");
            Achat a_ = achatR.save(a);
            u.ajouter_achats(a_);
            usersR.save(u);
            return new ResponseEntity<Achat>(achatR.save(a), HttpStatus.CREATED);
        }
    }

    // Fonction pour modifier un client
    @PutMapping(path = "/update_fournisseur/{id}")
    ResponseEntity<Fournisseur> update_fournisseur(@PathVariable(name = "id") String id, @RequestBody Fournisseur c) {
        Users u = usersR.findById(id).get();

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

    @DeleteMapping("/delete_fournisseur/{id}/{idF}")
    ResponseEntity<Long> deleteFournisseur(@PathVariable(name = "id") String id,
            @PathVariable(name = "idF") String id2) {
        // Users u = usersR.findById(id).get();
        fournisseurRepo.deleteById(id2);
        return new ResponseEntity<Long>(HttpStatus.OK);
    }

    @GetMapping(path = "/all_achat/{id}")
    List<?> all_achats(@PathVariable(name = "id") String id) {
        List<Achat> v = usersR.findById(id).get().getAchats();
        return v;

    }

    @GetMapping(path = "achats/{id}")
    ResponseEntity<List<Achat>> find_achats(@PathVariable(name = "id") String id) {
        return new ResponseEntity<List<Achat>>(usersR.findById(id).get().getAchats(), HttpStatus.OK);
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
