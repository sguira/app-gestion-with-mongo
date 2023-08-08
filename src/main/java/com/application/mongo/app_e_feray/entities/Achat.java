package com.application.mongo.app_e_feray.entities;

import java.io.Serializable;
import java.sql.Date;

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

}
