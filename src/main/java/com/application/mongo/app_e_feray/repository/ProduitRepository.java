package com.application.mongo.app_e_feray.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.application.mongo.app_e_feray.entities.Produit;

public interface ProduitRepository extends MongoRepository<Produit, String> {

    @Query(value = "Select p from produit, where p.name Like '%:#{#mc%'")
    public List<Produit> findByNameContains(@Param("mc") String ck, @Param("id") Long id);

}
