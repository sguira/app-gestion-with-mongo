package com.application.mongo.app_e_feray.repository;

import java.sql.SQLException;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.application.mongo.app_e_feray.entities.Tache;

public interface TacheRepositorie extends MongoRepository<Tache, String> {

    @Query(value = " SELECT * from tache t,users_taches u WHERE u.users_id=:#{#id},t.date=:#{#date}")
    List<Tache> findTacheNow(@Param("date") String date, @Param("id") Long id) throws SQLException;

}
