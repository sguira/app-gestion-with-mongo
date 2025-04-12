package com.application.mongo.app_e_feray.controller;

// import java.io.File;
// import java.io.FileOutputStream;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Random;

// import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
// import org.springframework.web.multipart.MultipartFile;

import com.application.mongo.app_e_feray.email.BodyEmail;
import com.application.mongo.app_e_feray.email.EmailServiceImp;
import com.application.mongo.app_e_feray.entities.Abonnement;
import com.application.mongo.app_e_feray.entities.Achat;
import com.application.mongo.app_e_feray.entities.Bilan;
// import com.application.mongo.app_e_feray.entities.Categorie;
import com.application.mongo.app_e_feray.entities.Client;
import com.application.mongo.app_e_feray.entities.Commande;
import com.application.mongo.app_e_feray.entities.Contact;
// import com.application.mongo.app_e_feray.entities.Fournisseur;
// import com.application.mongo.app_e_feray.entities.InfoEntreprise;
// import com.application.mongo.app_e_feray.entities.Produit;
import com.application.mongo.app_e_feray.entities.Tache;
import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.entities.userRender;
import com.application.mongo.app_e_feray.entities.ventes;
import com.application.mongo.app_e_feray.repository.AbonnementR;
import com.application.mongo.app_e_feray.repository.AchatR;
import com.application.mongo.app_e_feray.repository.CategorieRepositori;
import com.application.mongo.app_e_feray.repository.ClientRepo;
import com.application.mongo.app_e_feray.repository.CommandeRepositori;
import com.application.mongo.app_e_feray.repository.ContactRepo;
import com.application.mongo.app_e_feray.repository.FournisseurRepo;
import com.application.mongo.app_e_feray.repository.ProduitRepository;
import com.application.mongo.app_e_feray.repository.TacheRepositorie;
import com.application.mongo.app_e_feray.repository.UserRepositori;
import com.application.mongo.app_e_feray.repository.VenteRepo;
import com.application.mongo.app_e_feray.services.JWTUtils;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/api/v1")
public class controller {

    @Autowired(required = true)
    UserRepositori usersR;

    @Autowired(required = true)
    CategorieRepositori catR;

    @Autowired(required = true)
    ProduitRepository pr;

    @Autowired(required = true)
    ClientRepo clientR;

    @Autowired(required = true)
    ContactRepo contactRepo;

    @Autowired(required = true)
    FournisseurRepo fournisseurRepo;

    @Autowired(required = true)
    VenteRepo venteRepo;

    @Autowired(required = true)
    CommandeRepositori commandeRepo;

    @Autowired(required = true)
    AchatR achatR;

    @Autowired(required = true)
    TacheRepositorie tacheR;

    @Autowired(required = true)
    AbonnementR abonnement;

    @Autowired
    EmailServiceImp emailService = new EmailServiceImp();

    @Autowired
    JWTUtils jwtUtils;

    @PostMapping(path = "/sendMail")
    String envoyerMail(@RequestBody BodyEmail body) {
        try {
            String status = emailService.sendSimpleMessage(body);
            return status;
        } catch (Exception e) {
            System.out.println(e.toString());
            return "erreur";
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

    // @PostMapping(path = "/ajouter_user")
    // ResponseEntity<Users> ajouter_utilisateur2(
    // @RequestParam(name = "name") String name,
    // @RequestParam(name = "email") String email,
    // @RequestParam(name = "password") String password,
    // @RequestParam(name = "number") String number,
    // @RequestParam(name = "image") MultipartFile image,
    // @RequestParam(name = "entreprise") String n_en,
    // @RequestParam(name = "addresse") String addrese,
    // @RequestParam(name = "num") String num,
    // @RequestParam(name = "codeCnn") String codeCnn) {

    // Users users = new Users();
    // users.setName(name);
    // users.setEmail(email);
    // users.setNumber(number);
    // users.setPassword(password);
    // users.setImage_url(image.getOriginalFilename());
    // InfoEntreprise info = new InfoEntreprise(n_en, num, addrese,
    // image.getOriginalFilename(), codeCnn);
    // System.out.println("info info:" + info.getName());
    // users.setInfo(info);
    // try {
    // File ul = new File("D://GESTION CLIENT APP//images//image_profit//",
    // image.getOriginalFilename());
    // ul.createNewFile();
    // FileOutputStream fout = new FileOutputStream(ul);
    // fout.write(image.getBytes());
    // fout.close();
    // } catch (Exception e) {
    // System.out.print("mon erreru" + e.toString());
    // } finally {
    // if (!verifier.addUsers(usersR.findAll(), email)) {

    // return new ResponseEntity<>(usersR.save(users), HttpStatus.CREATED);
    // }

    // return new ResponseEntity<Users>(HttpStatus.CONFLICT);
    // }

    // }

    @PostMapping(path = "/update_user/{id}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<Users> modifierUser(@RequestBody Users u, @PathVariable String id) {
        Users u_ = usersR.findById(id).get();

        System.out.println("appel..\n");
        u_.setName(u.getName());
        u_.setEmail(u.getEmail());
        u_.setNumber(u.getNumber());
        u_.setInfo(u.getInfo());
        return new ResponseEntity<>(usersR.save(u_), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete_aboonement/{id}")
    ResponseEntity<?> deleteAbonnement(@PathVariable String id) {
        abonnement.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(path = "/save_abonnement", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<Abonnement> saveAbonnement(@RequestBody Abonnement abonnement_) {
        return new ResponseEntity<Abonnement>(abonnement.save(abonnement_), HttpStatus.CREATED);
    }

    @GetMapping(path = "/all_contact/{id}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<List<Contact>> contacts(@PathVariable(name = "id") String id) {
        return new ResponseEntity<List<Contact>>((List<Contact>) usersR.findById(id).get().getContacts(),
                HttpStatus.OK);
    }

    @GetMapping(path = "/get_bilan/{date1}/{date2}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<Bilan> bilan_ventes(@PathVariable(name = "date2") String date2,
            @PathVariable(name = "date1") String date1, @RequestHeader(name = "Authorization") String token) {
        Bilan bilan = new Bilan();

        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
                double achat = 0;
                double vente = 0;
                double payeV = 0;
                double payeA = 0;
                double rentree = 0;
                double depense = 0;
                for (var element : u.getAchats()) {
                    if (element.getDate().split(" ")[0].compareTo(date1) >= 0
                            && element.getDate().split(" ")[0].compareTo(date2) <= 0) {
                        if (element.getTypeOperation() != null) {
                            if (element.getTypeOperation().equals("DEPENSE")) {
                                depense += element.getEspece();

                            } else {

                                achat += element.getMontant() + element.getTva() - element.getMontantRemise();
                                payeA += element.getEspece();
                                if (!element.getRemboursement().isEmpty()) {
                                    for (var i = 0; i < element.getRemboursement().size() - 1; i++) {
                                        if (element.getRemboursement().get(i).split(",")[1].split(" ")[0]
                                                .compareTo(date1) >= 0
                                                && element.getRemboursement().get(i).split(",")[1].split(" ")[0]
                                                        .compareTo(date2) <= 0) {
                                            payeA += Double
                                                    .parseDouble(element.getRemboursement().get(i).split(",")[0]);

                                        }
                                    }
                                }
                            }

                        }

                    }
                }
                for (var element : u.getVentes()) {
                    if (element.getDate().split(" ")[0].compareTo(date1) >= 0
                            && element.getDate().split(" ")[0].compareTo(date2) <= 0) {
                        if (element.getTypeOperation() != null) {
                            if (!element.getTypeOperation().equals("RENTREE")) {
                                vente += element.getPrix() + element.getTva() - element.getMontantRemise();
                                payeV += element.getEspece();
                            } else {

                                rentree += element.getEspece();

                                if (!element.getRemboursement().isEmpty()) {
                                    for (var i = 1; i < element.getRemboursement().size() - 1; i++) {
                                        if (element.getRemboursement().get(i).split(",")[1].split(" ")[0]
                                                .compareTo(date1) >= 0
                                                && element.getRemboursement().get(i).split(",")[1].split(" ")[0]
                                                        .compareTo(date2) <= 0) {
                                            payeA += Double
                                                    .parseDouble(element.getRemboursement().get(i).split(",")[0]);

                                        }
                                    }
                                }
                            }

                        }

                    }
                }
                bilan.setMontant_achat(achat);
                bilan.setPaye_achat(payeA);
                bilan.setMontant_vente(vente);
                bilan.setPaye_vente(payeV);
                bilan.setDepense(depense);
                bilan.setRentree(rentree);

                return new ResponseEntity<Bilan>(bilan, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // supprimer une commande
    @DeleteMapping(path = "/deleteCommande/{id_comande}")
    ResponseEntity<?> deleteCommande(@PathVariable(name = "id_comande") String idCommande,
            @RequestHeader("Authorization") String token) {
        try {
            // Users user = usersR.findById(idUser).get();
            token = tokenValide(token);
            if (token != null) {
                commandeRepo.deleteById(idCommande);
                System.out.println("Commande supprimée:" + idCommande);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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

    @GetMapping(path = "vente_semaine_2/{date}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<List<Double>> getVenteSemaine2(@RequestHeader("Authorization") String token,
            @PathVariable(name = "date") String date) {
        List<Double> montant = new ArrayList<Double>();
        token = tokenValide(token);
        if (token != null) {

            Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
            String[] date_ = date.split(",");
            for (var d : date_) {
                System.out.println("Date: " + d + '\n');
                double montantT = 0;
                for (var v : u.getVentes()) {
                    if (v.getDate().split(" ")[0].equals(d)) {
                        montantT += v.getEspece();
                    }
                    if (!v.getRemboursement().isEmpty()) {
                        for (int i = 0; i < v.getRemboursement().size() - 1; i++) {
                            try {
                                if (v.getRemboursement().get(i).split(",")[1].split(" ")[0].equals(d)) {
                                    montantT += Double
                                            .parseDouble(v.getRemboursement().get(i).toString().split(",")[0]);
                                }
                            } catch (Exception ex) {
                                System.out.println("Erreur: " + ex.getMessage());
                            }
                        }
                    }

                }
                montant.add(montantT);
            }
            for (var d : date_) {
                double montantI = 0;
                for (var a : u.getAchats()) {
                    if (a.getDate().split(" ")[0].equals(d)) {
                        montantI += a.getEspece();
                    }
                    if (!a.getRemboursement().isEmpty()) {
                        for (var i = 0; i < a.getRemboursement().size() - 1; i++) {
                            try {
                                if (a.getRemboursement().get(i).split(",")[1].split(" ")[0].equals(d)) {
                                    montantI += Double
                                            .parseDouble(a.getRemboursement().get(i).toString().split(",")[0]);
                                }
                            } catch (Exception ex) {

                            }
                        }
                    }
                }
                montant.add(montantI);
            }

            return new ResponseEntity<List<Double>>(montant, HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);

    }

    @GetMapping(path = "/get_ventes_journaliere/{id}/{choice}/{date1}/{date2}/{date3}/{date4}/{date5}/{date6}/{date7}")
    List<Double> ventes_journaliere(@PathVariable(name = "id") String id,
            @PathVariable(name = "date1") String date1,
            @PathVariable(name = "choice") int choice,
            @PathVariable(name = "date2") String date2,
            @PathVariable(name = "date3") String date3,
            @PathVariable(name = "date4") String date4,
            @PathVariable(name = "date5") String date5,
            @PathVariable(name = "date6") String date6,
            @PathVariable(name = "date7") String date7) {

        List<Double> data = new ArrayList<>();
        switch (choice) {
            case 1: {
                List<ventes> v = usersR.findById(id).get().getVentes();

                data.add(get_ventes_total(v, date1));

                return data;
            }
            case 2: {
                List<ventes> v = usersR.findById(id).get().getVentes();
                data.add(
                        get_ventes_total(v, date7));
                data.add(
                        get_ventes_total(v, date6));
                data.add(
                        get_ventes_total(v, date5));
                data.add(
                        get_ventes_total(v, date4));
                data.add(
                        get_ventes_total(v, date3));
                data.add(
                        get_ventes_total(v, date2));
                data.add(
                        get_ventes_total(v, date1));
                return data;
            }
            case 3: {
                List<Achat> v = usersR.findById(id).get().getAchats();
                data.add(get_achats_total(v, date7));
                data.add(
                        get_achats_total(v, date6));
                data.add(
                        get_achats_total(v, date5));
                data.add(get_achats_total(v, date4));
                data.add(
                        get_achats_total(v, date3));
                data.add(
                        get_achats_total(v, date2));
                data.add(
                        get_achats_total(v, date1));
                return data;
            }
            default: {
                return new ArrayList<>();
            }
        }
    }

    double get_ventes_total(List<ventes> v, String date) {
        double res = 0d;
        for (int i = 0; i < v.size(); i++) {
            if (v.get(i).getDate().toString().equals(date.toString())) {
                res += v.get(i).getEspece();
            }
        }
        return res;
    }

    // ventes de la semaine
    @GetMapping(path = "/vente_de_la_semaine", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    List<?> vente_semaine() {
        return usersR.ventes_de_la_semaine();
    }

    @GetMapping(path = "/all_ventes/{id}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    List<?> all_ventes(@PathVariable(name = "id") Long id) {
        List<?> v = new ArrayList<>();
        v = usersR.all_ventes(id);
        return v;

    }

    @GetMapping(path = "ventes/{id}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<List<ventes>> find_ventes(@PathVariable(name = "id") String id) {
        return new ResponseEntity<List<ventes>>(usersR.findById(id).get().getVentes(), HttpStatus.OK);
    }

    @PostMapping(path = "/add_tache", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<Tache> add_tache(@RequestHeader(name = "Authorization") String token, @RequestBody Tache t) {

        try {
            token = tokenValide(token);
            if (token != null) {
                System.out.println("\n\ntaches: \n" + t);

                Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
                Tache tache = tacheR.save(t);
                u.ajouter_tache(tache);
                usersR.save(u);
                return new ResponseEntity<>(tache, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/deleteTache/{id}")
    ResponseEntity<?> deleteTache(@PathVariable String id, @RequestHeader("Authorization") String token) {

        try {
            token = tokenValide(token);
            if (token != null) {
                System.out.println("Identifiant:" + id + "\n\n");
                tacheR.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(path = "/updateTache/{id}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<Tache> modifierTache(@RequestBody Tache tache, @PathVariable String id,
            @RequestHeader("Authorization") String token) throws Exception {

        try {
            token = tokenValide(token);
            if (token != null) {
                return new ResponseEntity<Tache>(tacheR.save(tache), HttpStatus.CREATED);

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/find_days/{date}")
    ResponseEntity<List<Tache>> find_days(@RequestHeader("Authorization") String token, @PathVariable String date) {

        try {
            token = tokenValide(token);
            if (token != null) {
                List<Tache> taches = new ArrayList<>();

                usersR.findByEmail(jwtUtils.extractUsername(token)).getTaches().forEach(e -> {
                    if (e.getDate_().equals(date)) {
                        taches.add(e);
                    }
                });
                return new ResponseEntity<>(taches, HttpStatus.OK);

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/ventes_semaine/{id}/{date}/{jour_semaine}/{choice}")
    public List<Double> vente2(@PathVariable(name = "id") String id, @PathVariable(name = "date") String param,
            @PathVariable(name = "jour_semaine") int jj, @PathVariable int choice) {
        String[] date = param.split(",");

        List<ventes> v = usersR.findById(id).get().getVentes();
        List<Achat> a = usersR.findById(id).get().getAchats();
        List<Double> data = new ArrayList<>();
        int i = 0;
        int j = jj;

        switch (choice) {
            case 1: {
                while (j >= 1) {
                    System.out.println("ligne:" + i);
                    try {
                        data.add(get_ventes_total(v, date[i]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;
                    j--;
                }
                break;
            }
            case 2: {

                while (j >= 1) {
                    System.out.println("ligne:" + i);
                    try {
                        data.add(get_achats_total(a, date[i]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;
                    j--;
                }
                break;

            }
        }

        while (jj < 7) {
            data.add(0d);
            jj++;
        }

        return data;

    }

    double get_achats_total(List<Achat> v, String date) {
        double res = 0d;
        for (int i = 0; i < v.size(); i++) {
            if (v.get(i).getDate().toString().equals(date.toString())) {
                res += v.get(i).getEspece();
            }
        }
        return res;
    }

    @PostMapping(path = "/ajouterCommande/{id2}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<Commande> addCommande(@RequestBody Commande c, @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "id2") String id2) {

        try {
            token = tokenValide(token);
            if (token != null) {
                Users utilisateur = usersR.findByEmail(jwtUtils.extractUsername(token));

                for (var commande : utilisateur.getCommande()) {
                    if (commande.getRef().toLowerCase().equals(c.getRef().toLowerCase())) {
                        return new ResponseEntity<>(HttpStatus.CONFLICT);
                    }
                }

                Client client = clientR.findById(id2).get();

                Commande newCommande = commandeRepo.save(c);
                utilisateur.addCommande(newCommande);
                client.ajouterCommande(newCommande);
                clientR.save(client);
                usersR.save(utilisateur);
                return new ResponseEntity<Commande>(newCommande, HttpStatus.CREATED);

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/allCommande", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<List<Commande>> getCommandeByUser(@RequestHeader("Authorization") String token) {

        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
                List<Commande> commandes = new ArrayList<>();

                commandes = u.getCommande();
                return new ResponseEntity<List<Commande>>(commandes, HttpStatus.OK);

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/terminer/{id}")
    ResponseEntity terminerCommande(@PathVariable(name = "id") String id,
            @RequestHeader("Authorization") String token) {

        try {
            token = tokenValide(token);
            if (token != null) {
                Commande c = commandeRepo.findById(id).get();
                c.setTerminer(!c.terminer);
                commandeRepo.save(c);
                return new ResponseEntity<>(HttpStatus.OK);

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getVenteCommande/{name}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<List<ventes>> getventeBycommande(@RequestHeader("Authorization") String token,
            @PathVariable("name") String name) {

        try {
            token = tokenValide(token);
            if (token != null) {
                List<ventes> v = usersR.findByEmail(jwtUtils.extractUsername(token)).getVentes();

                List<ventes> v_ = new ArrayList<>();
                v.forEach((e) -> {
                    if (e.getNumeroCommande() != null && e.getNumeroCommande().equals(name)) {
                        v_.add(e);
                    }
                });
                return new ResponseEntity<List<ventes>>(v_, HttpStatus.OK);

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/updateLogo")
    ResponseEntity updateLogo(@RequestBody String path, @RequestHeader("Authorization") String token) {

        try {
            token = tokenValide(token);
            if (token != null) {
                System.out.println(path);
                Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
                u.setImage_url(path);
                u.getInfo().setLogo(path);
                usersR.save(u);
                return new ResponseEntity<>(HttpStatus.CREATED);

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getVentesAnnee/{year}")
    ResponseEntity<List<Double>> getVentes(@RequestHeader("Authorization") String token, @PathVariable String date) {

        try {
            token = tokenValide(token);
            if (token != null) {
                List<Double> annee = new ArrayList<Double>();
                List<ventes> ventes = usersR.findByEmail(jwtUtils.extractUsername(token)).getVentes();
                for (int i = 1; i <= 12; i++) {
                    Double montant = 0d;
                    for (int j = 0; j < ventes.size(); j++) {
                        if (ventes.get(i).getDate().equals(date)) {
                            montant += ventes.get(i).getEspece();
                        }
                    }
                    annee.add(0, montant);
                }
                return new ResponseEntity<>(annee, HttpStatus.OK);

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "get_vente_by_id/{id}/{choice}")
    ResponseEntity<?> getSingleVente(@PathVariable String id, @PathVariable(name = "choice") int choice,
            @RequestHeader("Authorization") String token)
            throws Exception {

        try {
            token = tokenValide(token);
            if (token != null) {
                switch (choice) {
                    case 1: {
                        return new ResponseEntity<ventes>(venteRepo.findById(id).get(), HttpStatus.OK);
                    }
                    case 2: {
                        return new ResponseEntity<Achat>(achatR.findById(id).get(), HttpStatus.OK);
                    }
                    default:
                        return new ResponseEntity<ventes>(venteRepo.findById(id).get(), HttpStatus.OK);
                }

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "update_ventes/{id}/{montant}/{date}/{choice}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    ResponseEntity<?> updateVente(@PathVariable(name = "id") String id,
            @PathVariable(name = "montant") double montant, @PathVariable(name = "date") String date,
            @PathVariable int choice, @RequestHeader("Authorization") String token) {

        try {
            token = tokenValide(token);
            if (token != null) {
                switch (choice) {
                    case 1: {
                        String val = montant + "," + date;
                        ventes ventes = venteRepo.findById(id).get();
                        ventes.setEspece(ventes.getEspece() + montant);
                        ventes.addVentes(val);
                        return new ResponseEntity<ventes>(venteRepo.save(ventes), HttpStatus.CREATED);
                    }
                    case 2: {
                        String val = montant + "," + date;
                        Achat achat = achatR.findById(id).get();
                        achat.setEspece(achat.getEspece() + montant);
                        achat.addAchat(val);
                        return new ResponseEntity<Achat>(achatR.save(achat), HttpStatus.CREATED);
                    }
                    default: {
                        return null;
                    }
                }

            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
