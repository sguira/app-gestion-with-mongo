package com.application.mongo.app_e_feray.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.application.mongo.app_e_feray.entities.Fournisseur;

public interface FournisseurRepo extends MongoRepository<Fournisseur, String> {

    @Query(value = "SELECT count(id) from fournisseur")
    public int get_fournisseur();

    // @Query(value="select sum(montant) from achat where id")

}
