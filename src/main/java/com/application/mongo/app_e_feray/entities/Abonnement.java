package com.application.mongo.app_e_feray.entities;

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
public class Abonnement {

    @Id
    private String id;
    private String label;
    private String dateAbonnement;
    private String finAbonnement;
    // private int jourRestant = 30;
    private int duree;

}
