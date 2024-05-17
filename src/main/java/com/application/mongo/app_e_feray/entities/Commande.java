package com.application.mongo.app_e_feray.entities;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document
public class Commande implements Serializable {

    @Id
    public String reference;

    public String ref;
    public String date;
    public double montant;

    public String idC;
    public String description;
    public boolean terminer;
    public List<String> descriptions;
    public String dateLivraison = "";
    public String heureLivraison = "";
    // @OneToMany(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    // public List<ventes> ventes=new ArrayList<ventes>();

    // public void ajouterCommande(ventes v){
    // ventes.add(v);
    // }
}
