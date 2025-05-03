package com.application.mongo.app_e_feray.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.application.mongo.app_e_feray.dto.UserNameEmailProjection;
import com.application.mongo.app_e_feray.entities.Users;

public interface UserRenderRepository extends MongoRepository<Users, String> {

    @Query(value = "{}", fields = "{ 'name' : 1, 'email' : 1 ,'number' : 1, 'id' : 1, 'finAbonnement' : 1, 'description' : 1, 'dateCreation' : 1, 'suscription' : 1, 'confirmed' : 1 }")
    List<UserNameEmailProjection> findByAll();

}
