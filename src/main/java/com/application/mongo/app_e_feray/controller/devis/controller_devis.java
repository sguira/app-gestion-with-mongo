package com.application.mongo.app_e_feray.controller.devis;

import java.net.http.HttpResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.entities.Devis;
import com.application.mongo.app_e_feray.repository.DevisRepository;
import com.application.mongo.app_e_feray.repository.UserRepositori;

@RestController
@RequestMapping("/devis")
@CrossOrigin("*")
class DevisController {
    @Autowired
    UserRepositori userR;

    @Autowired
    DevisRepository devisR;

    @PostMapping(path = "/ajouter/{idUser}")
    ResponseEntity<?> ajouterDevis(@RequestBody Devis devis, @RequestParam String idUser) {

        Users user = userR.findById(idUser).get();

        devisR.save(devis);
        user.ajouterDevis(devis);
        userR.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/devis/{id}")
    public ResponseEntity<List<Devis>> getDevisByArticle(@PathVariable String id) {
        Users u = userR.findById(id).get();
        return new ResponseEntity<>(u.getDevis(), HttpStatus.OK);
    }

}
