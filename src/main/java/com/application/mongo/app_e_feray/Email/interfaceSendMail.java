package com.application.mongo.app_e_feray.email;

public interface interfaceSendMail {

    public String sendSimpleMessage(BodyEmail email);

    public String sendEmailWithAttachment(BodyEmail email);
}
