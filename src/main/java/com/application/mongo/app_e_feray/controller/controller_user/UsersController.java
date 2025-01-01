package com.application.mongo.app_e_feray.controller.controller_user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.email.BodyEmail;
import com.application.mongo.app_e_feray.email.EmailServiceImp;
import com.application.mongo.app_e_feray.entities.Abonnement;
import com.application.mongo.app_e_feray.entities.DetailAbonnement;
import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.entities.VitrineEmail;
import com.application.mongo.app_e_feray.entities.userRender;
import com.application.mongo.app_e_feray.repository.AbonnementR;
import com.application.mongo.app_e_feray.repository.DetailAbonnementRepo;
import com.application.mongo.app_e_feray.repository.UserRepositori;
import com.application.mongo.app_e_feray.services.JWTUtils;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/backend/users")
public class UsersController {

    @Autowired
    private UserRepositori usersR;

    @Autowired
    private EmailServiceImp emailService;

    // @Autowired
    // private AbonnementR abonnementR;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private DetailAbonnementRepo detailAbonnementR;

    @Autowired
    private JWTUtils jwtUtils;

    String tokenValide(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7, token.length());
                if (jwtUtils.validateToken(token)) {
                    return token;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping(path = "/updatePassword")
    ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> body) {

        try {
            token = tokenValide(token);
            if (token != null) {
                Users user = usersR.findByemail(jwtUtils.extractUsername(token));
                if (passwordEncoder.matches(body.get("hold"), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(body.get("password")));
                    usersR.save(user);
                    return new ResponseEntity<>("OK", HttpStatus.OK);
                }
                return new ResponseEntity<>("ERROR", HttpStatus.OK);

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/login")
    ResponseEntity<Map> login(@RequestBody Users u) {

        // List<Users> result = usersR.findAll();
        // for (var i : result) {
        // System.out.println(i.getEmail() + "\n\n");
        // }

        // ajouté
        Map<String, String> res = new HashMap<>();
        Users user = usersR.findByemail(u.getEmail());
        String encode = passwordEncoder.encode(u.getPassword());

        if (user != null) {
            if (!user.getPassword().equals("")) {
                if (user.getEmail().equals(u.getEmail()) &&
                        passwordEncoder.matches(u.getPassword(), user.getPassword())
                        && user.getRecuperation().equals("")) {
                    // System.out.println("\n\n" + result.get(i).getEmail());
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

        // for (int i = 0; i < result.size(); i++) {

        // if (!result.get(i).getPassword().equals("")) {
        // if (result.get(i).getEmail().equals(u.getEmail()) &&
        // passwordEncoder.matches(u.getPassword(), result.get(i).getPassword())
        // && result.get(i).getRecuperation().equals("")) {
        // // System.out.println("\n\n" + result.get(i).getEmail());
        // if (result.get(i).isConfirmed()) {
        // return result.get(i).getId();
        // } else {
        // return "-3";
        // }
        // }
        // } else if (result.get(i).getRecuperation() != null) {
        // // System.out.println("\n\n" + result.get(i).getEmail());
        // if (result.get(i).getEmail().equals(u.getEmail())
        // && passwordEncoder.matches(u.getPassword(), result.get(i).getRecuperation()))
        // {
        // return "-2";
        // } else {
        // return "-1";
        // }

        // }
        // }
        // return "-1";
    }

    @PostMapping(path = "/adduser")
    ResponseEntity<Users> ajouterUtilisateur(@RequestBody Users u) {

        if (!verifier.addUsers(usersR.findAll(), u.getEmail())) {
            u.setConfirmCode(generateCode());
            String password = passwordEncoder.encode(u.getPassword());
            u.setPassword(password);
            BodyEmail email = new BodyEmail();
            // email.setRecipient(u.getEmail());
            // email.setBody("Création de compte");
            // email.setMessage(
            // "Bienvenue sur Notre application\nVotre Compte a bien été crée avec les
            // coordonnées suivante.\nNom:"
            // + u.getName() + "\tEmail:" + u.getEmail() +
            // "\t\u001B31 Nom entreprise:" + u.getInfo().getName() + "\tAddresse:"
            // + u.getInfo().getAddresse() + "");
            // // email.setRecipient(u.getEmail());
            // // email.setBody("Création de compte");
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

    @PutMapping(path = "/update_user")
    ResponseEntity<Users> modifierUtilisateur(@RequestBody Users u, @RequestHeader("Authorization") String token) {

        try {
            token = tokenValide(token);
            if (token != null) {
                Users user = usersR.findByemail(jwtUtils.extractUsername(token));
                if (u.getEmail().equals(user.getEmail())) {
                    user.setName(u.getName());
                    user.setEmail(u.getEmail());
                    user.setLast_name(u.getLast_name());
                    user.setNumber(u.getNumber());
                    user.setInfo(u.getInfo());
                    // user.setPassword(u.getPassword());
                    return new ResponseEntity<Users>(usersR.save(user), HttpStatus.CREATED);
                }

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // ancien
    // @GetMapping(path = "/get_user/{id}")
    // ResponseEntity<Users> getUser(@PathVariable(name = "id") String id) {
    // return new ResponseEntity<Users>(usersR.findById(id).get(), HttpStatus.OK);
    // }

    // nouveau
    @GetMapping(path = "/get_user")
    ResponseEntity<Users> getUser(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
                String username = jwtUtils.extractUsername(token);
                Users u = usersR.findByemail(username);
                if (u != null) {
                    return new ResponseEntity<Users>(u, HttpStatus.OK);
                } else {
                    return new ResponseEntity<Users>(HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // verifierToken
    @GetMapping(path = "/verifier_token")
    ResponseEntity<String> verifierToken(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (jwtUtils.validateToken(token)) {
                    return new ResponseEntity<String>("token valide", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("token non valide", HttpStatus.CONFLICT);
                }
            }
            return new ResponseEntity<String>("token non valide", HttpStatus.CONFLICT);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // check_mail
    @GetMapping("/check_mail/{email}")
    ResponseEntity<?> verification(@PathVariable String email) {

        try {
            Users u = usersR.findByemail(email);
            if (u != null) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

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

    @PostMapping(path = "/updatePDP")
    String modificationMDP(@RequestParam(name = "recuperation") String recuperation,
            @RequestParam(name = "password") String password, @RequestParam(name = "email") String email) {

        for (var user : usersR.findAll()) {
            if (user.getEmail().equals(email) && passwordEncoder.matches(recuperation, user.getRecuperation())) {
                user.setPassword(passwordEncoder.encode(password));
                user.setRecuperation("");
                usersR.save(user);
                System.out.println(" \n\nmodifier\n\n");
                return "OK";
            }

        }

        return "ERROR";
    }

    @GetMapping(path = "/updateSuscription/{dateDebut}/{dateFin}/{prix}/{number}/{duree}")
    ResponseEntity<Users> updatSuscription(@RequestHeader("Authorization") String token,
            @PathVariable(name = "prix") double montant,
            @PathVariable(name = "dateDebut") String dateAbonnement,
            @PathVariable(name = "dateFin") String finAbonnement,
            @PathVariable(name = "number") String numero,
            @PathVariable(name = "duree") int duree

    ) {
        try {

        } catch (Exception e) {
            System.out.println("Erreur abonnement.");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = usersR.findByemail(jwtUtils.extractUsername(token));
                u.setMontantAbonnement(montant);
                u.setSuscription(true);
                u.setDateAbonnement(dateAbonnement);
                u.setFinAbonnement(finAbonnement);
                DetailAbonnement abonnement = new DetailAbonnement();
                abonnement.setDateAbonnement(dateAbonnement);
                abonnement.setFinAbonnement(finAbonnement);
                abonnement.setNumero(numero);
                abonnement.setPrix(montant);
                abonnement.setDuree(duree);
                // abonnement.setLabel(numero);
                abonnement = detailAbonnementR.save(abonnement);

                u.addAbonnement(abonnement);
                System.out.println("Abonnement");
                return new ResponseEntity<>(usersR.save(u), HttpStatus.CREATED);

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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

    @GetMapping(path = "reset-code/{email}")
    ResponseEntity<String> sendCodeMail(@PathVariable(name = "email") String email) {
        List<Users> users = usersR.findAll();
        for (var u : users) {
            if (email.equals(u.getEmail())) {
                BodyEmail emailBody = new BodyEmail();
                String code = resetCode(email);
                if (code != null) {
                    emailBody.setRecipient(email);
                    emailBody.setBody("Code de confirmation de Compte sur E-Ferray");
                    emailBody.setMessage(
                            "Ce Message est le code de confirmation de la création de votre compte sur Eferray. \n code:"
                                    + code);
                    emailService.sendSimpleMessage(emailBody);
                    u.setConfirmCode(code);
                    u.setConfirmed(true);
                    usersR.save(u);
                    return new ResponseEntity<String>("OK", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("INTROUVABLE", HttpStatus.OK);
                }

            }
        }
        return new ResponseEntity<String>("INTROUVABLE", HttpStatus.OK);
    }

    @Transactional
    String resetCode(String email) {
        for (var user : usersR.findAll()) {
            if (user.getEmail().equals(email)) {
                String code = generateCode();
                user.setConfirmCode(code);
                // usersR.save(user);
                return code;
            }
        }
        return null;
    }

    @GetMapping(path = "/users")
    ResponseEntity<List<Users>> alluser() {
        return new ResponseEntity<List<Users>>(usersR.findAll(), HttpStatus.OK);
    }

    boolean verifieToken(String token) {
        try {
            boolean res = jwtUtils.validateToken(token);
            if (res == true) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    @GetMapping(path = "/get_user_by_id")
    ResponseEntity<userRender> get_users(@RequestHeader("Authorization") String token) {

        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (verifieToken(token)) {
                    Users u = usersR.findByemail(jwtUtils.extractUsername(token));

                    userRender u_ = new userRender(u.getId(), u.getName(), u.getEmail(), u.getNumber(), u.getInfo(),
                            u.getDateAbonnement(), u.getFinAbonnement(), u.getNumeroCompte(), u.getAbonnements());

                    return new ResponseEntity<userRender>(u_, HttpStatus.OK);

                }
                return new ResponseEntity<userRender>(HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<userRender>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<userRender>(HttpStatus.INTERNAL_SERVER_ERROR);
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

    // @PostMapping("/backend/")
    // public ResponseEntity<String> contactByEmailVitrine(RequestBody VitrineEmai
    // messageBody){
    // return new ResponseEntity<String>(HttpStatus.OK,"ENVOYER");
    // }

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

    String customMail(VitrineEmail email) {
        return "<html><body>" +
                "<h1>Mail Envoyé par: " + email.getName() + " </h>" +
                "<div>" + email.getMessage() + "</div>" +
                "</body></html>";
    }

    // ajouter un article favoris
    @PostMapping(path = "add_favoris/{id_article}")
    ResponseEntity<String> addArticleFavoris(@RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "id_article") String id) {
        try {
            token = tokenValide(token);

            if (token != null) {
                Users user = usersR.findByemail(jwtUtils.extractUsername(token));
                List<String> articlesFreq = new ArrayList<>();
                boolean exist = false;
                for (int i = 0; i < articlesFreq.size(); i++) {
                    if (id.equals(articlesFreq.get(i))) {
                        exist = true;
                    }
                }
                if (exist == false) {
                    user.ajouterArticleFrequent(id);
                }
                usersR.save(user);
                return new ResponseEntity<String>("OK", HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ajouter un article favoris
    @PostMapping(path = "add_favoris_multiple")
    ResponseEntity<String> addMultipleFrequent(@RequestHeader(name = "Authorization") String token,
            @RequestBody List<String> ids) {

        try {
            token = tokenValide(token);

            if (token != null) {
                Users user = usersR.findByemail(jwtUtils.extractUsername(token));
                List<String> articlesFreq = user.getArticlesFrequent();
                // boolean exist=false;

                for (int i = 0; i < ids.size(); i++) {
                    if (checkFavExist(ids.get(i), articlesFreq) == false) {
                        user.ajouterArticleFrequent(ids.get(i));
                    }
                }
                usersR.save(user);
                return new ResponseEntity<>("OK", HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ajouter un article favoris
    @GetMapping(path = "delete_favoris/{id_article}")
    ResponseEntity<?> supprimerFav(@RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "id_article") String id) {

        try {
            token = tokenValide(token);

            if (token != null) {
                Users user = usersR.findByemail(jwtUtils.extractUsername(token));
                List<String> articlesFreq = user.getArticlesFrequent();

                for (int i = 0; i < articlesFreq.size(); i++) {
                    if (checkFavExist(id, articlesFreq)) {
                        user.getArticlesFrequent().remove(i);
                    }
                }
                usersR.save(user);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    boolean checkFavExist(String id, List<String> frequents) {
        for (String freq : frequents) {
            if (freq.equals(id)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping(path = "frequences/{id}")
    ResponseEntity<List<String>> getfrequents(@RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                return new ResponseEntity<List<String>>(
                        usersR.findByemail(jwtUtils.extractUsername(token)).getArticlesFrequent(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
