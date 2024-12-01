package com.application.mongo.app_e_feray.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.application.mongo.app_e_feray.entities.Devis;

import jakarta.annotation.Resource;

@Resource
public interface DevisRepository extends MongoRepository<Devis, String> {

}
