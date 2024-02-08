package com.application.mongo.app_e_feray.controller.controller_achat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.entities.Achat;
import com.application.mongo.app_e_feray.repository.AchatR;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/backend/achat/")
public class AchatController {

    @Autowired(required = true)
    AchatR achatR;

    @PostMapping(path = "update_achats/{id}/{montant}/{date}")
    ResponseEntity<Achat> updateAchat(@PathVariable(name = "id") String id,
            @PathVariable(name = "montant") double montant, @PathVariable(name = "date") String date) {
        String val = montant + "," + date;
        Achat achat = achatR.findById(id).get();
        achat.setEspece(achat.getEspece() + montant);
        achat.addAchat(val);
        return new ResponseEntity<Achat>(achatR.save(achat), HttpStatus.CREATED);
    }

    @GetMapping(path = "get_achat_by_id/{id}")
    ResponseEntity<Achat> getSingleAchat(@PathVariable String id) {
        return new ResponseEntity<Achat>(achatR.findById(id).get(), HttpStatus.OK);
    }
}
