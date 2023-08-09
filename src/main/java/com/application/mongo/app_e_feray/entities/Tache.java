package com.application.mongo.app_e_feray.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Tache {
    @Id
    protected String id;
    String description;
    String date_;
    String heure;
}
