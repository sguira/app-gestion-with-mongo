package com.application.mongo.app_e_feray.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
public class VitrineEmail {

    private String name;
    private String subject;
    private String email;
    private String message;
    private String phone;

}
