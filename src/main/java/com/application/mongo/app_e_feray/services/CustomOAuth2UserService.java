package com.application.mongo.app_e_feray.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.repository.UserRepositori;

// import lombok.RequiredArgsConstructor;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    // private final OAuth2UserService<OAuth2UserRequest, OAuth2User>
    // oAuth2UserService;

    @Autowired
    private UserRepositori userRepositori;

    // private PasswordEncoder passwordEncoder=new ;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User auth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // recuperation des informations
        String oauthProvider = userRequest.getClientRegistration().getRegistrationId();
        String oauthId = auth2User.getAttribute("sub");
        String name = auth2User.getAttribute("name");
        String email = auth2User.getAttribute("email");
        if (oauthId == null) {
            oauthId = auth2User.getAttribute("id");
        }

        Users users = userRepositori.findByEmail(email);
        if (users == null) {
            users = new Users();
            users.setEmail(email);
            users.setName(name);
            users.setOauthProvider(oauthProvider);
            users.setOauthId(oauthId);
            // users.setPassword(passwordEncoder.encode(email));
            userRepositori.save(users);
        }

        return auth2User;

    }

}
