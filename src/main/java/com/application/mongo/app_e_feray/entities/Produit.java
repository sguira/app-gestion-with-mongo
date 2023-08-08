package com.application.mongo.app_e_feray.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
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
public class Produit implements Serializable {

    @Id
    private String id;
    private String name;
    private String description;
    private double prix;

    private int quantite;
    private String reference;

    // @Lob
    // private Byte[] image;

    // @ManyToOne
    // private Categorie cat;

}
