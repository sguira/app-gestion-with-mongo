package com.application.mongo.app_e_feray.controller.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.controller.controller_user.verifier;
import com.application.mongo.app_e_feray.email.BodyEmail;
import com.application.mongo.app_e_feray.email.EmailServiceImp;
import com.application.mongo.app_e_feray.entities.Abonnement;
import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.entities.VitrineEmail;
import com.application.mongo.app_e_feray.repository.AbonnementR;
import com.application.mongo.app_e_feray.repository.UserRepositori;
import com.application.mongo.app_e_feray.services.JWTUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class Auth {

    private final UserRepositori usersR;

    private final JWTUtils jwtUtils;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final EmailServiceImp emailService;

    private final AbonnementR abonnement;

    @PostMapping(path = "/login")
    ResponseEntity<Object> login(@RequestBody Users u) {
        Map<String, String> res = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(u.getEmail(), u.getPassword()));
            if (authentication.isAuthenticated()) {

                Users user = usersR.findByEmail(u.getEmail());
                // String encode = passwordEncoder.encode(u.getPassword());
                System.out.println("Login appelé \n\n");
                System.out.println("username:" + u.getEmail() + "\nusername:" + user.getEmail());
                if (user != null) {
                    if (!user.getPassword().equals("")) {
                        if (user.getEmail().equals(u.getEmail()) &&
                                passwordEncoder.matches(u.getPassword(), user.getPassword())
                                && user.getRecuperation().equals("")) {

                            if (user.isConfirmed()) {
                                res.put("token", jwtUtils.generateToken(u.getEmail()));
                                res.put("code", "1");
                                return new ResponseEntity<>(res, HttpStatus.OK);
                            } else {
                                res.put("code", "-3");
                                return new ResponseEntity<>(res, HttpStatus.OK);
                            }
                        }
                    } else if (user.getRecuperation() != null) {
                        // System.out.println("\n\n" + result.get(i).getEmail());
                        if (user.getEmail().equals(u.getEmail())
                                && passwordEncoder.matches(u.getPassword(), user.getRecuperation())) {
                            res.put("code", "-2");
                            return new ResponseEntity<>(res, HttpStatus.OK);
                        } else {
                            res.put("code", "-1");
                            return new ResponseEntity<>(res, HttpStatus.OK);
                        }

                    }

                }
                res.put("code", "-1");
                return new ResponseEntity<>(res, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Donnée incorrect");
            }
        } catch (Exception e) {
            // e.printStackTrace();
            res.put("code", "-1");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

    }

    @GetMapping("get_user")
    ResponseEntity<String> extract_user_name(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String userName = jwtUtils.extractUsername(token);
            return new ResponseEntity<>(userName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("ERROR", HttpStatus.OK);
        }
    }

    @RequestMapping("validate_token")
    public boolean validate(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            return jwtUtils.validateToken(token);
        } catch (Exception e) {
            return false;
        }
    }

    String generateCode() {
        Random rand = new Random();
        String numero = "0123456789";
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int val = rand.nextInt(numero.length());
            stringBuilder.append(numero.charAt(val));
        }
        return stringBuilder.toString();

    }

    @PostMapping(path = "/register")
    ResponseEntity<Users> ajouterUtilisateur(@RequestBody Users u) {

        Users user = usersR.findByEmail(u.getEmail());

        if (user == null) {
            u.setConfirmCode(generateCode());
            String password = passwordEncoder.encode(u.getPassword());
            u.setPassword(password);
            u.setRole("ROLE_USER");
            BodyEmail email = new BodyEmail();

            email.setRecipient(u.getEmail());
            email.setBody("Création de compte");
            String res = emailService.sendHtlmlMail(email, creerHtmlBody(u, "Monsieur/Madame" + u.getName() +
                    "Veuillez confirmer la création de votre compte sur L'application E-Ferray, <br>" +
                    "avec le code suivant <u style='color: red;font-size: 26px;'>" + u.getConfirmCode() + "</u>"

            ));
            System.out.println(res);
            if (res.equals("Mail Sent Successfully...")) {
                return new ResponseEntity<>(usersR.save(u), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<Users>(HttpStatus.BAD_GATEWAY);
            }

        }

        return new ResponseEntity<Users>(HttpStatus.CONFLICT);
    }

    String creerHtmlBody(Users users, String message) {
        String body = "";
        body += "<!DOCTYPE html>" +
                "<html lang='fr'>" +
                "<body><div style='margin: 12px auto;max-width: 550px;display: contents;flex-direction: column;flex-wrap: nowrap;'>"
                +
                "<div style='background-color: rgb(56, 109, 109);width: 500px;padding: 22px;border-radius: 8px;'>" +
                "<h2 style='color: white;text-align: center;''>Bienvenue Sur E-Ferray</h2>" +
                "</div><div style='margin: 22px auto;position:relative;left:20px;'>" +
                message +
                "</div><div style='width: 150px;height: 200px;display: contents;justify-content: center;'>" +
                // "<img
                // src='http://drive.google.com/uc?export=view&id=1ExYFBFWAFWu-IVaRdgwHf4FWHGcnktYB'
                // alt='' >" +
                "<img src='cid:imageId' alt='Logo' >" +
                "</div>" + "</div></body></html>";

        return body;
    }

    @PostMapping(path = "/recuperation_password")
    String recuperation(@RequestParam(name = "email") String email,
            @RequestParam(name = "password1") String recuperation, @RequestParam(name = "password2") String password) {

        try {

            Users u = new Users();
            for (var i = 0; i < usersR.findAll().size(); i++) {
                if (usersR.findAll().get(i).getEmail().equals(email)) {
                    u = usersR.findAll().get(i);
                    break;
                }
            }
            String passwordEncode = passwordEncoder.encode(recuperation);
            u.setRecuperation(passwordEncode);
            u.setPassword("");
            usersR.save(u);
            BodyEmail body = new BodyEmail();
            body.setMessage(
                    "Votre mot de passe à bien été modifier\n vous pouvez utiliser le nouveau mot de passe génerer automatiquement pour vous connecter. N'oubliez pas de le modifier après connecion\n Mot de passe Recupération:"
                            + recuperation);
            body.setBody("Recupération Mot de passe! ");
            body.setRecipient(u.getEmail());
            emailService.sendSimpleMessage(body);
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERREUR";
        }

    }

    @PostMapping(path = "confirmation-compte/{email}/{code}")
    String confirmationCreation(@PathVariable(name = "email") String email, @PathVariable(name = "code") String code) {
        List<Users> users = usersR.findAll();
        for (var u : users) {
            if (u.getEmail().equals(email) && u.getConfirmCode().equals(code)) {
                u.setConfirmed(true);
                u.setCode("");
                usersR.save(u);
                BodyEmail email_ = new BodyEmail();
                email_.setBody("Compte crée.");
                email_.setRecipient(email);
                String res = emailService.sendHtlmlMail(email_, creerHtmlBody(u, "Félicitation, "
                        + u.getName() +
                        " Vous pouvez maintenant vous connecter à votre compte sur<br> l'application <u style='color:red;font-size:22px' >E-FERRAY</u>"
                        +
                        "Le nom d'utilisateur est votre mail: " + u.getEmail()));
                System.out.println(res);
                if (res.equals("Mail Sent Successfully...")) {
                    return "OK";
                } else {
                    return "INCORRECT";
                }

            }
        }
        return "INCORRECT";
    }

    @PostMapping("vitrine/contact")
    public ResponseEntity<String> postMethodName(@RequestBody VitrineEmail entity) {

        try {
            BodyEmail body = new BodyEmail();
            body.setBody(entity.getSubject());
            body.setRecipient("sguira96@gmail.com");
            body.setMessage(entity.getMessage());

            String envoi = emailService.sendSimpleMessage(body, entity.getEmail());
            if (envoi == "Mail Sent Successfully...") {
                return new ResponseEntity<String>("OK", HttpStatus.OK);
            }
            return new ResponseEntity<String>("ERROR", HttpStatus.CONFLICT);

        } catch (Exception e) {
            return new ResponseEntity<String>("ERROR", HttpStatus.CONFLICT);
        }

    }

    @GetMapping(path = "/abonnement")
    ResponseEntity<List<Abonnement>> getAbonnement() {
        return new ResponseEntity<List<Abonnement>>(abonnement.findAll(), HttpStatus.OK);
    }

}
