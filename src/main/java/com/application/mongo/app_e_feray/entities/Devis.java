package com.application.mongo.app_e_feray.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Devis {

    @Id
    private String id;
    private String date;
    private String idClient;
    private String description;

    List<String> article = new ArrayList<>();
}
