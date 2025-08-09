package com.application.mongo.app_e_feray.entities;

import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Payement {

    private String id;
    private String numero;
    private Instant date;
    private double montant;
    private String transactionId;
    private String email;
    private String userId;
    private String status = "PENDING";
    private String abonnementId;

}
