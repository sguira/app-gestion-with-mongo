package com.application.mongo.app_e_feray.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.application.mongo.app_e_feray.entities.DetailAbonnement;

public interface DetailAbonnementRepo extends MongoRepository<DetailAbonnement, String> {

}
