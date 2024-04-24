package com.application.mongo.app_e_feray.controller.controller_user;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.email.BodyEmail;
import com.application.mongo.app_e_feray.email.EmailServiceImp;
import com.application.mongo.app_e_feray.entities.Abonnement;
import com.application.mongo.app_e_feray.entities.DetailAbonnement;
import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.entities.userRender;
import com.application.mongo.app_e_feray.repository.AbonnementR;
import com.application.mongo.app_e_feray.repository.DetailAbonnementRepo;
import com.application.mongo.app_e_feray.repository.UserRepositori;

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

    @Autowired
    private DetailAbonnementRepo detailAbonnementR;

    @PostMapping(path = "/updatePassword/{id}")
    ResponseEntity<String> updatePassword(@PathVariable String id, @RequestBody Map<String, String> body) {
        Users user = usersR.findById(id).get();
        if (user.getPassword().equals(body.get("hold"))) {
            user.setPassword(body.get("password"));
            usersR.save(user);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR", HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    String login(@RequestBody Users u) {

        List<Users> result = usersR.findAll();
        // for (var i : result) {
        // System.out.println(i.getEmail() + "\n\n");
        // }
        for (int i = 0; i < result.size(); i++) {

            if (result.get(i).getEmail().equals(u.getEmail()) &&
                    result.get(i).getPassword().equals(u.getPassword())) {
                // System.out.println("\n\n" + result.get(i).getEmail());
                if (result.get(i).isConfirmed()) {
                    return result.get(i).getId();
                } else {
                    return "-3";
                }
            } else if (result.get(i).getRecuperation() != null) {
                // System.out.println("\n\n" + result.get(i).getEmail());
                if (result.get(i).getEmail().equals(u.getEmail()) &&
                        result.get(i).getRecuperation().equals(u.getPassword())) {
                    return "-2";
                }

            }
        }
        return "-1";
    }

    @PostMapping(path = "/adduser")
    ResponseEntity<Users> ajouterUtilisateur(@RequestBody Users u) {

        if (!verifier.addUsers(usersR.findAll(), u.getEmail())) {
            u.setConfirmCode(generateCode());

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
    ResponseEntity<Users> modifierUtilisateur(@RequestBody Users u) {

        Users user = usersR.findById(u.getId()).get();
        user.setName(u.getName());
        user.setEmail(u.getEmail());
        user.setLast_name(u.getLast_name());
        user.setNumber(u.getNumber());
        user.setInfo(u.getInfo());
        // user.setPassword(u.getPassword());
        return new ResponseEntity<Users>(usersR.save(user), HttpStatus.CREATED);

    }

    @GetMapping(path = "/get_user/{id}")
    ResponseEntity<Users> getUser(@PathVariable(name = "id") String id) {
        return new ResponseEntity<Users>(usersR.findById(id).get(), HttpStatus.OK);
    }

    // check_mail
    @GetMapping("/check_mail/{email}")
    String verification(@PathVariable(name = "email") String email) {
        List<Users> users = usersR.findAll();
        for (var i : users) {
            if (i.getEmail().toLowerCase().equals(email.toLowerCase())) {
                return "1";
            }
        }
        return "0";
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
            // LocalDateTime now = LocalDateTime.now();

            // String chaine = email + now.toString();
            // System.out.println("\n\n" + chaine);
            // StringBuffer recuperation = new StringBuffer();
            // for (int i = 0; i < chaine.length(); i++) {
            // int index = (int) (Math.random() * (chaine.length() - 1));
            // recuperation.append(chaine.charAt(index));
            // }
            u.setRecuperation(password);
            usersR.save(u);
            BodyEmail body = new BodyEmail();
            body.setMessage(
                    "Votre mot de passe à bien été modifier\n vous pouvez utiliser le nouveau mot de passe génerer automatiquement pour vous connecter. N'oubliez pas de le modifier après connecion\n Mot de passe Recupération:"
                            + recuperation.toString());
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
            if (user.getEmail().equals(email) && user.getRecuperation().equals(recuperation)) {
                user.setPassword(password);
                usersR.save(user);
                System.out.println(" \n\nmodifier\n\n");
                return "OK";
            }

        }

        return "ERROR";
    }

    @GetMapping(path = "updateSuscription/{id}/{dateDebut}/{dateFin}/{prix}/{number}/{duree}")
    ResponseEntity<Users> updatSuscription(@PathVariable String id, @PathVariable(name = "prix") double montant,
            @PathVariable(name = "dateDebut") String dateAbonnement,
            @PathVariable(name = "dateFin") String finAbonnement,
            @PathVariable(name = "number") String numero,
            @PathVariable(name = "duree") int duree

    ) {
        try {
            Users u = usersR.findById(id).get();
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
        } catch (Exception e) {
            System.out.println("Erreur abonnement.");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
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

    @GetMapping(path = "users")
    ResponseEntity<List<Users>> alluser() {
        return new ResponseEntity<List<Users>>(usersR.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/get_user_by_id/{id}")
    ResponseEntity<userRender> get_users(@PathVariable(name = "id") String id) {

        Users u = usersR.findById(id).get();
        userRender u_ = new userRender(u.getId(), u.getName(), u.getEmail(), u.getNumber(), u.getInfo(),
                u.getDateAbonnement(), u.getFinAbonnement(), u.getNumeroCompte(), u.getAbonnements());

        return new ResponseEntity<userRender>(u_, HttpStatus.OK);
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
}
