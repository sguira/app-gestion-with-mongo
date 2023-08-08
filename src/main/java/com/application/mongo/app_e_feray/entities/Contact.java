package com.application.mongo.app_e_feray.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Contact implements Serializable {
    @Id
    private String id;
    private String name;
    private String numero;

    @DBRef
    Users user;
}
