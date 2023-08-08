package com.application.mongo.app_e_feray.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.application.mongo.app_e_feray.entities.Commande;

public interface CommandeRepositori extends MongoRepository<Commande, String> {

    @Query(value = "SELECT * from commande C where C.ref=:#{#ref}")
    Commande getCommandebyName(@Param("ref") String numeroCommande);
}
