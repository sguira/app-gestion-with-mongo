package com.application.mongo.app_e_feray.dto;

import java.time.LocalDate;

public interface UserNameEmailProjection {
    String getName();

    String getEmail();

    String getId();

    String getNumber();

    String getFinAbonnement();

    String getDescription();

    LocalDate getDateCreation();
}
