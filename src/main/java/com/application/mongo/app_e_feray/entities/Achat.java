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
public class Achat implements Serializable {
    @Id
    private String id;
    private double montant;
    private String designation;
    private String Date;
    private Double espece;
    private String nomFournisseur;
    private String typeOperation;
    private double regleInialement;
    private String codeFacture;
    private List<String> articles_ = new ArrayList<>();

    private List<String> remboursement = new ArrayList<>();

    public void addAchat(String item) {
        this.remboursement.add(0, item);
    }

}
