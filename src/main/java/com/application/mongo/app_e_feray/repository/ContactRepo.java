package com.application.mongo.app_e_feray.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.application.mongo.app_e_feray.entities.Contact;

public interface ContactRepo extends MongoRepository<Contact, String> {

}
