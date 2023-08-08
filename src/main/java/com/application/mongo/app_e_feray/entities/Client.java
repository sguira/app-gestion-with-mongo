package com.application.mongo.app_e_feray.entities;

import java.io.Serializable;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client implements Serializable {
    @Id
    private String id;
    private String name;
    private String number;
    private String email;

    // @JsonProperty(access = Access.WRITE_ONLY)
    @DBRef
    List<ventes> ventes = new ArrayList<>();

    @DBRef
    List<Commande> commandes = new ArrayList<>();

    public void ajouter_ventes(ventes v) {
        ventes.add(v);
    }

    public void ajouterCommande(Commande c) {
        this.commandes.add(0, c);
    }

}
