package com.application.mongo.app_e_feray.entities;

import java.io.File;
import java.io.Serializable;
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
public class Categorie implements Serializable {

    @Id
    private String id;

    private String name;
    private String description;

    @DBRef
    private List<Produit> produits = new ArrayList<Produit>();

    public void ajouterProduit(Produit p) {
        this.produits.add(p);
    }

    public void effacer_produit(Produit p) {
        this.produits.remove(p);
    }

}
