package com.application.mongo.app_e_feray.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

import java.util.List;

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
public class ventes implements Serializable {

    @Id
    private String id;
    private String code;
    private String date;
    private double prix;
    private Double espece;
    private String numeroCommande;
    private String nomClient;
    private String typeOperation;
    private String typePayement = "espece";
    private String numeroCompte = "espece";
    private String designation = "vente d'article";
    private double montantRemise = 0;
    private double regleInialement;
    private double tva = 0;
    private String dateEcheance;
    private List<String> articles_ = new ArrayList<>();

    private List<String> remboursement = new ArrayList<>();

    public void addVentes(String item) {
        this.remboursement.add(0, item);
    }

    // @OneToMany()
    // private List<SingleArticle> article2 = new ArrayList<>();

    // public void addArticleVente(SingleArticle s) {
    // this.article2.add(0, s);
    // }

}

@Data
// @Entity
@AllArgsConstructor
@NoArgsConstructor
class SingleArticle implements Serializable {
    // @Id
    // private int id;

    private String nom;
    private int quantite;
    private double prix;
}

// class ElementConvertor implements AttributeConverter<List<SingleArticle>,
// String> {

// private final ObjectMapper objectMapper = new ObjectMapper();

// @Override
// public String convertToDatabaseColumn(List<SingleArticle> attribute) {
// try {
// return objectMapper.writeValueAsString(attribute);
// } catch (JsonProcessingException e) {
// return null;
// }

// }

// @Override
// public List<SingleArticle> convertToEntityAttribute(String dbData) {
// try {
// return objectMapper.readValue(dbData, new
// TypeReference<List<SingleArticle>>() {

// });
// } catch (JsonProcessingException e) {
// return null;
// }
// }

// }