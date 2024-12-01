package com.application.mongo.app_e_feray.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.application.mongo.app_e_feray.entities.Users;

public interface UserRepositori extends MongoRepository<Users, String> {

    @Query(value = "SELECT sum(A.montant) from achat A,users U where A.date between :#{#date1} and :#{#date2} and U.id=:#{#id}")
    double get_bilan_achat_montant(@Param("date2") String date2, @Param("date1") String date1, @Param("id") String id);

    @Query(value = "SELECT sum(A.espece) from ventes A,users U where A.date between :#{#date1} and :#{#date2} and U.id=:#{#id} ")
    double get_bilan_ventes_montant(@Param("date2") String date2, @Param("date1") String date1, @Param("id") String id);

    @Query(value = "SELECT sum(A.espece) from achat A,users U where A.date between :#{#date1} and :#{#date2} and U.id=:#{#id} ")
    double get_bilan_achat_paye(@Param("date2") String date2, @Param("date1") String date1, @Param("id") String id);

    @Query(value = "SELECT sum(A.espece) from ventes A,users U where A.date between :#{#date1} and :#{#date2} and U.id=:#{#id} ")
    double get_bilan_ventes_paye(@Param("date2") String date2, @Param("date1") String date1, @Param("id") String id);

    @Query(value = "SELECT sum(V.payee) from ventes V,users U where  V.date=:#{#date_} U.id=:#{#id}")
    Double get_ventes_journaliere(@Param("date_") String date, @Param("id") String id);

    @Query(value = " SELECT DAYOFWEEK(now()); ")
    List ventes_de_la_semaine();

    @Query(value = "SELECT date,code,prix FROM ventes V,users_ventes U_V, WHERE V.id=U_V.ventes_id , U_V.users_id = :#{#id} ")
    List<?> all_ventes(@Param("id") Long id);

    @Query(value = "SELECT * from ventes join users where users_id=:#{#id}")
    Object ventes_by_clients(@Param("id") String id);

    Users findByemail(String username);

}
