package com.application.mongo.app_e_feray.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.application.mongo.app_e_feray.entities.Payement;

public interface PayementRepository extends MongoRepository<Payement, String> {

    Payement findByTransactionId(String transactionId);

    Payement findByNumero(String numero);

    Payement findByEmail(String email);

    Payement findByUserId(String userId);

}
