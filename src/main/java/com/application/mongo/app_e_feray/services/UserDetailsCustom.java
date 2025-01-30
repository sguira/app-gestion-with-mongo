package com.application.mongo.app_e_feray.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.repository.UserRepositori;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Component
public class UserDetailsCustom implements UserDetailsService {

    final UserRepositori userRepositori;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users u = userRepositori.findByEmail(username);
        if (u == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPassword(),
                mapRolesToAuthorities(u.getRole()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(String roles) {
        roles = roles.equals("") ? "ROLE_USER" : roles;
        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
