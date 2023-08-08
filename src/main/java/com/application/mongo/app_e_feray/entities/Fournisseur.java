package com.application.mongo.app_e_feray.entities;

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
public class Fournisseur {

    @Id
    private String id;
    private String name;
    private String number;
    private String email;

    @DBRef
    private List<Achat> achats = new ArrayList<>();

    public void ajouter_achat(Achat a) {
        achats.add(a);
    }

}
