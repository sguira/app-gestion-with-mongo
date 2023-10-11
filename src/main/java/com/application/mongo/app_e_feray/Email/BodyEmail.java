package com.application.mongo.app_e_feray.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BodyEmail {
    public String body;
    public String message;
    public String recipient;
    public String attachement;
}
