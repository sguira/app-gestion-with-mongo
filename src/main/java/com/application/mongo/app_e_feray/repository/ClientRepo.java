package com.application.mongo.app_e_feray.repository;

import java.util.List;

import org.springframework.data.mongodb.core.aggregation.VariableOperators.Map;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.application.mongo.app_e_feray.entities.Client;
import com.application.mongo.app_e_feray.entities.ventes;

import jakarta.websocket.server.PathParam;

public interface ClientRepo extends MongoRepository<Client, String> {

    @Query(value = "SELECT c.name c from client c Where c.name like '%{#name}%'")
    List<Client> search_by_name(@PathParam("id") String id, @PathParam("name") String name);

    @Query(value = "SELECT V FROM users_ventes U_V,commande C, ventes V Where U_V.users_id={#id}and V.ventes_id=U_V.id,v.numero_commande={#name} ")
    List<ventes> getVenteCommande(@PathParam("id") Long id, @PathParam("name") String name);

    @Query(value="SELECT")
    Map getVenteByClient(@PathParam("id") String id);

}
