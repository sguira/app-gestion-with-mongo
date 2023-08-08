package com.application.mongo.app_e_feray.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class userRender implements Serializable {

    public String id;
    public String name;
    public String email;
    public String number;
    public InfoEntreprise info;
    public String dateFin;
    public String dateDebut;

}
