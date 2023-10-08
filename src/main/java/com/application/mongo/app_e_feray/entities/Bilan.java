package com.application.mongo.app_e_feray.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bilan implements Serializable {

    public Double montant_vente;
    public Double paye_vente;
    public Double montant_achat;
    public Double paye_achat;
    public Double rentree;
    public Double depense;

}
