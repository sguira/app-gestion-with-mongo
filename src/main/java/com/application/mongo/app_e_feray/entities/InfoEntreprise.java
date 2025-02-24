package com.application.mongo.app_e_feray.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class InfoEntreprise implements Serializable {

    String name;
    String telephone;
    String addresse;
    String logo;
    String codeCnn;
    String ncr;
    String description = "";
    String phraseAcroche = "";

    // @I
    // LocalDateTime createdAt;

}
