package com.application.mongo.app_e_feray.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.application.mongo.app_e_feray.Email.BodyEmail;
import com.application.mongo.app_e_feray.Email.EmailServiceImp;
import com.application.mongo.app_e_feray.entities.Abonnement;
import com.application.mongo.app_e_feray.entities.Achat;
import com.application.mongo.app_e_feray.entities.Bilan;
import com.application.mongo.app_e_feray.entities.Categorie;
import com.application.mongo.app_e_feray.entities.Client;
import com.application.mongo.app_e_feray.entities.Commande;
import com.application.mongo.app_e_feray.entities.Contact;
import com.application.mongo.app_e_feray.entities.Fournisseur;
import com.application.mongo.app_e_feray.entities.InfoEntreprise;
import com.application.mongo.app_e_feray.entities.Produit;
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

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/backend")
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

    @Autowired(required = true)
    EmailServiceImp emailService = new EmailServiceImp();

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

    @PostMapping(path = "/adduser")
    ResponseEntity<Users> ajouterUtilisateur(@RequestBody Users u) {

        if (!verifier.addUsers(usersR.findAll(), u.getEmail())) {
            BodyEmail email = new BodyEmail();
            email.setRecipient(u.getEmail());
            email.setBody("Création de compte");
            email.setMessage(
                    "Bienvenue sur Notre application\nVotre Compte a bien été crée avec les coordonnées suivante.\nNom:"
                            + u.getName() + "\tEmail:" + u.getEmail() +
                            "\t\u001B31 Nom entreprise:" + u.getInfo().getName() + "\tAddresse:"
                            + u.getInfo().getAddresse());
            String res = emailService.sendSimpleMessage(email);
            System.out.println(res);
            if (res.equals("Mail Sent Successfully...")) {
                return new ResponseEntity<>(usersR.save(u), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<Users>(HttpStatus.BAD_GATEWAY);
            }

        }

        return new ResponseEntity<Users>(HttpStatus.CONFLICT);
    }

    @PostMapping(path = "/ajouter_user")
    ResponseEntity<Users> ajouter_utilisateur2(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "number") String number,
            @RequestParam(name = "image") MultipartFile image,
            @RequestParam(name = "entreprise") String n_en,
            @RequestParam(name = "addresse") String addrese,
            @RequestParam(name = "num") String num) {

        Users users = new Users();
        users.setName(name);
        users.setEmail(email);
        users.setNumber(number);
        users.setPassword(password);
        users.setImage_url(image.getOriginalFilename());
        InfoEntreprise info = new InfoEntreprise(n_en, num, addrese, image.getOriginalFilename());
        System.out.println("info info:" + info.getName());
        users.setInfo(info);
        try {
            File ul = new File("D://GESTION CLIENT APP//images//image_profit//",
                    image.getOriginalFilename());
            ul.createNewFile();
            FileOutputStream fout = new FileOutputStream(ul);
            fout.write(image.getBytes());
            fout.close();
        } catch (Exception e) {
            System.out.print("mon erreru" + e.toString());
        } finally {
            if (!verifier.addUsers(usersR.findAll(), email)) {

                return new ResponseEntity<>(usersR.save(users), HttpStatus.CREATED);
            }

            return new ResponseEntity<Users>(HttpStatus.CONFLICT);
        }

    }

    @GetMapping(path = "users")
    ResponseEntity<List<Users>> alluser() {
        return new ResponseEntity<List<Users>>(usersR.findAll(), HttpStatus.OK);
    }

    @PostMapping(path = "/addcategorie/{id}")
    ResponseEntity<Categorie> ajouterCategorie(@RequestBody Categorie cat, @PathVariable(name = "id") String id) {
        Users u = usersR.findById(id).get();
        System.out.println("Client " + u.getName());
        List<Categorie> cats = (List<Categorie>) (u.getCategories());
        if (cats.size() > 0) {
            for (int i = 0; i < cats.size(); i++) {
                if (cats.get(i).getName() != null) {
                    if (cats.get(i).getName().equals(cat.getName())) {
                        System.out.println(" CONFLICT >>>>>>>>>>>>> ");
                        return new ResponseEntity<>(HttpStatus.CONFLICT);
                    }
                }
            }
        }
        Categorie categorie = catR.save(cat);
        u.ajouterCategorie(categorie);
        usersR.save(u);
        // catR.save(categorie);
        return new ResponseEntity<Categorie>(categorie, HttpStatus.CREATED);

    }

    @GetMapping(path = "/get_user_by_id/{id}")
    ResponseEntity<userRender> get_users(@PathVariable(name = "id") String id) {

        Users u = usersR.findById(id).get();
        userRender u_ = new userRender(u.getId(), u.getName(), u.getEmail(), u.getNumber(), u.getInfo(),
                u.getFinAbonnement(), u.getDateAbonnement());

        return new ResponseEntity<userRender>(u_, HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    String login(@RequestBody Users u) {

        List<Users> result = usersR.findAll();
        for (int i = 0; i < result.size(); i++) {

            if (result.get(i).getEmail().equals(u.getEmail()) &&
                    result.get(i).getPassword().equals(u.getPassword())) {
                return result.get(i).getId();
            } else if (result.get(i).getRecuperation() != null) {
                if (result.get(i).getEmail().equals(u.getEmail()) &&
                        result.get(i).getRecuperation().equals(u.getPassword())) {
                    return "-2";
                } else {
                    return "-1";
                }

            }
        }
        return "-1";
    }

    @PostMapping(path = "/addproduit/{id}")
    ResponseEntity<Produit> ajouterProduit(@RequestBody Produit p,
            @PathVariable(name = "id") String id,
            @RequestPart MultipartFile img) throws IOException {
        Categorie cat = catR.findById(id).get();
        String img_name = img.getOriginalFilename();

        cat.ajouterProduit(p);
        catR.save(cat);
        File f = new File("images_gestion_entreprise/", img_name);
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(img.getBytes());
        fout.close();
        return new ResponseEntity<>(pr.save(p), HttpStatus.CREATED);

    }

    @PostMapping(path = "/ajouter_produit/{id}")
    ResponseEntity<Produit> ajouter_produitf(@RequestBody Produit p, @PathVariable String id) {
        Categorie cat = catR.findById(id).get();
        cat.ajouterProduit(p);
        catR.save(cat);
        return new ResponseEntity<Produit>(pr.save(p), HttpStatus.CREATED);
    }

    @GetMapping(path = "/get_clients/{id}")
    ResponseEntity<List<Client>> clients(@PathVariable String id) {

        return new ResponseEntity<List<Client>>((List<Client>) usersR.findById(id).get().getClients(), HttpStatus.OK);

    }

    @PutMapping(path = "/update_user")
    ResponseEntity<Users> modifierUtilisateur(@RequestBody Users u) {

        Users user = usersR.findById(u.getId()).get();
        user.setName(u.getName());
        user.setEmail(u.getEmail());
        user.setLast_name(u.getLast_name());
        user.setNumber(u.getNumber());
        user.setPassword(u.getPassword());
        return new ResponseEntity<Users>(usersR.save(user), HttpStatus.CREATED);

    }

    @PostMapping(path = "/update_user/{id}")
    ResponseEntity<Users> modifierUser(@RequestBody Users u, @PathVariable String id) {
        Users u_ = usersR.findById(id).get();

        System.out.println("appel..\n");
        u_.setName(u.getName());
        u_.setEmail(u.getEmail());
        u_.setNumber(u.getNumber());
        u_.setInfo(u.getInfo());
        return new ResponseEntity<>(usersR.save(u_), HttpStatus.CREATED);
    }

    @GetMapping(path = "/abonnement")
    ResponseEntity<List<Abonnement>> getAbonnement() {
        return new ResponseEntity<List<Abonnement>>(abonnement.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/delete_aboonement/{id}")
    ResponseEntity<?> deleteAbonnement(@PathVariable String id) {
        abonnement.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/get_user/{id}")
    ResponseEntity<Users> getUser(@PathVariable(name = "id") String id) {
        return new ResponseEntity<Users>(usersR.findById(id).get(), HttpStatus.OK);
    }

    @PostMapping(path = "/save_abonnement")
    ResponseEntity<Abonnement> saveAbonnement(@RequestBody Abonnement abonnement_) {
        return new ResponseEntity<Abonnement>(abonnement.save(abonnement_), HttpStatus.CREATED);
    }

    @PostMapping(path = "/add_client/{id}")
    ResponseEntity<Client> ajouter_client(@RequestBody Client c, @PathVariable(name = "id") String id) {
        Users u = usersR.findById(id).get();
        List<Client> clients = new ArrayList<Client>();
        clients = u.getClients();
        Client cc = clientR.save(c);
        u.ajouter_client(cc);
        // Contact contact = new Contact();
        // contact.setName(c.getName());
        // contact.setNumero(c.getNumber());
        // contactRepo.save(contact);
        // u.ajouter_contact(contact);
        usersR.save(u);
        return new ResponseEntity<Client>(cc, HttpStatus.CREATED);
    }

    @PostMapping(path = "/add_contact/{id}")
    ResponseEntity<Contact> ajouterContact(@RequestBody Contact c, @PathVariable String id) {
        Users u = usersR.findById(id).get();
        u.ajouter_contact(c);
        usersR.save(u);
        return new ResponseEntity<Contact>(contactRepo.save(c), HttpStatus.CREATED);
    }

    @GetMapping(path = "/all_categories/{id}")
    ResponseEntity<List<Categorie>> get_categorie_by_user(@PathVariable(name = "id") String id) {
        return new ResponseEntity<List<Categorie>>((List<Categorie>) usersR.findById(id).get().getCategories(),
                HttpStatus.OK);
    }

    @PutMapping(path = "/add_article/{id}")
    ResponseEntity<Produit> ajouter_article(@RequestBody Produit p, @PathVariable(name = "id") String id) {
        Categorie cat = catR.findById(id).get();
        for (var i = 0; i < cat.getProduits().size(); i++) {
            if (p.getName().toLowerCase().equals(cat.getProduits().get(i).getName().toLowerCase())) {
                Produit prod = cat.getProduits().get(i);
                prod.setQuantite(prod.getQuantite() + p.getQuantite());
                cat.getProduits().set(i, prod);
                catR.save(cat);
                return new ResponseEntity<Produit>(pr.save(prod), HttpStatus.CREATED);
            }
        }
        cat.ajouterProduit(p);
        catR.save(cat);
        return new ResponseEntity<Produit>(pr.save(p), HttpStatus.CREATED);
    }

    @PutMapping(path = "/update_article/{id}/{id_}")
    ResponseEntity<Produit> modifier_article(@RequestBody Produit p, @PathVariable(name = "id") String id,
            @PathVariable(name = "id_") String id_) {

        Categorie cat = catR.findById(id_).get();

        List<Produit> prod = cat.getProduits();
        for (var i = 0; i < prod.size(); i++) {
            if (prod.get(i).getId() == p.getId()) {
                System.out.println("okok\n\n");
                prod.get(i).setName(p.getName());
                prod.get(i).setPrix(p.getPrix());
                prod.get(i).setDescription(p.getDescription());
            }
        }

        catR.save(cat);
        return new ResponseEntity<Produit>(pr.save(p), HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/all_contact/{id}")
    ResponseEntity<List<Contact>> contacts(@PathVariable(name = "id") String id) {
        return new ResponseEntity<List<Contact>>((List<Contact>) usersR.findById(id).get().getContacts(),
                HttpStatus.OK);
    }

    @PostMapping(path = "/add_fournisseur/{id}")
    ResponseEntity<Fournisseur> add_fournisseur(@RequestBody Fournisseur f, @PathVariable String id) {
        Users u = usersR.findById(id).get();
        Fournisseur f_ = fournisseurRepo.save(f);
        u.ajouter_fournisseur(f_);
        usersR.save(u);
        return new ResponseEntity<Fournisseur>(f_, HttpStatus.CREATED);
    }

    @PostMapping(path = "/add_ventes/{id_user}/{id_clients}")
    ResponseEntity<ventes> ajouter_ventes(@RequestBody ventes v, @PathVariable(name = "id_user") String id_1,
            @PathVariable(name = "id_clients") String id_2) {
        Users u = usersR.findById(id_1).get();
        String item = v.getPrix() + "," + v.getDate();
        v.addVentes(item);
        System.out.println("\n\n" + v.getNumeroCommande());
        v.getArticles_().forEach((article) -> {
            List<Produit> produit = pr.findAll();
            for (var i = 0; i < produit.size(); i++) {
                if (article.split(",")[0].equals(produit.get(i).getName())) {
                    Produit p = produit.get(i);
                    p.setQuantite(p.getQuantite() - Integer.parseInt(article.split(",")[1]));
                    pr.save(p);
                }
            }
        });
        if (id_2.equals("-1")) {
            try {

                Commande commande = commandeRepo.getCommandebyName(v.getNumeroCommande());
                String name = clientR.findById(id_2).get().getName();
                v.setNomClient(name);
                if (commande != null) {

                    commandeRepo.save(commande);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ventes v2 = venteRepo.save(v);

            Client c = clientR.findById(id_2).get();
            c.ajouter_ventes(v2);
            u.ajouter_ventes(v2);
            clientR.save(c);
            usersR.save(u);
            return new ResponseEntity<ventes>(v2, HttpStatus.CREATED);
        } else {
            ventes v2 = venteRepo.save(v);
            v.setNomClient("Inconnu");
            u.ajouter_ventes(v2);
            usersR.save(u);
            return new ResponseEntity<ventes>(v2, HttpStatus.CREATED);
        }

    }

    @DeleteMapping(path = "/delete_categorie/{id}")
    int deletecategorie(@PathVariable String id) throws Exception {
        try {
            catR.deleteById(id);
            ;
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @PostMapping(path = "/save_produit/{id_2}")
    ResponseEntity<?> save_article(@PathVariable(name = "id_2") String id,

            @RequestParam(name = "name") String nom,
            @RequestParam(name = "description") String description, @RequestParam(name = "prix") String prix,
            @RequestParam(name = "quantite") String quantite) {
        Produit p = new Produit();
        Produit p_ = pr.save(p);
        // create the product
        p.setDescription(description);
        p.setName(nom);
        p.setPrix(Double.parseDouble(prix));
        p.setQuantite(Integer.parseInt(quantite));

        Categorie cat = catR.findById(id).get();
        cat.ajouterProduit(p_);
        catR.save(cat);
        return new ResponseEntity<>(p_, HttpStatus.CREATED);
    }

    @PostMapping(value = "/ajouter_produit2/{id}")
    ResponseEntity<?> save_produit2(@PathVariable(name = "id") String id, @RequestBody Produit p) {
        Categorie cat = catR.findById(id).get();
        Produit p_ = pr.save(p);
        cat.ajouterProduit(p_);
        catR.save(cat);

        return new ResponseEntity<>(p_, HttpStatus.CREATED);
    }

    @GetMapping(path = "/get_client/{id}")
    ResponseEntity<List<ventes>> get_clients_ventes(@PathVariable(name = "id") String id) {

        return new ResponseEntity<List<ventes>>((List<ventes>) clientR.findById(id).get().getVentes(), HttpStatus.OK);
    }

    @GetMapping(path = "/ventes_for_clients/{id}/{choice}")
    List<ventes> getVentes(@PathVariable String id, @PathVariable(name = "choice") Long choice) {
        return clientR.findById(id).get().getVentes();
    }

    @GetMapping(path = "/get_fournisseur/{id}")
    ResponseEntity<List<Fournisseur>> fournisseurs(@PathVariable String id) {
        return new ResponseEntity<List<Fournisseur>>((List<Fournisseur>) usersR.findById(id).get().getFournisseurs(),
                HttpStatus.OK);
    }

    @GetMapping(path = "/get_bilan/{date1}/{date2}")
    ResponseEntity<Bilan> bilan_ventes(@PathVariable(name = "date2") String date2,
            @PathVariable(name = "date1") String date1) {
        Bilan bilan = new Bilan();

        bilan.setMontant_achat(usersR.get_bilan_achat_montant(date2, date1));
        bilan.setMontant_vente(usersR.get_bilan_ventes_montant(date2, date1));
        bilan.setPaye_achat(usersR.get_bilan_achat_paye(date2, date1));
        bilan.setPaye_vente(usersR.get_bilan_ventes_paye(date2, date1));

        return new ResponseEntity<Bilan>(bilan, HttpStatus.OK);
    }

    @PostMapping(path = "/add_achat/{id_1}/{id_2}")
    ResponseEntity<Achat> ajouter_depense(@PathVariable(name = "id_1") String id,
            @PathVariable(name = "id_2") String id_,
            @RequestBody Achat a) {
        Achat a_ = achatR.save(a);
        if (id_.equals("-1")) {
            Users u = usersR.findById(id).get();
            Fournisseur f = fournisseurRepo.findById(id_).get();

            a.setNomFournisseur(f.getName());
            u.ajouter_achats(a_);
            usersR.save(u);

            f.ajouter_achat(a_);
            fournisseurRepo.save(f);
            return new ResponseEntity<Achat>(a_, HttpStatus.CREATED);
        } else {
            Users u = usersR.findById(id).get();
            a.setNomFournisseur("Inconnu");
            u.ajouter_achats(a_);
            usersR.save(u);
            return new ResponseEntity<Achat>(achatR.save(a), HttpStatus.CREATED);
        }
    }

    // supprimer un produit
    @DeleteMapping(path = "/delete_product/{id}")
    int effacer_produit(@PathVariable String id) {
        try {
            pr.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // delete
    @DeleteMapping(path = "/delete/{id}/{parent_id}/{choice}")
    int delete(@PathVariable(name = "id") String id, @PathVariable(name = "parent_id") String parent,
            @PathVariable(name = "choice") int choice) {
        switch (choice) {
            case 1: {
                Produit p = pr.findById(id).get();
                System.out.println("\n\n id cat:" + parent);
                Categorie cat = catR.findById(parent).get();
                // cat.effacer_produit(p);
                for (var i = 0; i < cat.getProduits().size(); i++) {
                    if (cat.getProduits().get(i).getId() == p.getId()) {
                        System.out.println("produit trouvé \n\n");
                        cat.getProduits().remove(i);
                    }
                }
                catR.save(cat);
                pr.deleteById(id);
                return 1;
            }
            case 2: {
                pr.deleteById(id);
                return 1;
            }
        }
        return -1;
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

    @GetMapping(path = "/search_client_by_name/{id}/{name}")
    List<Client> recherche_client(@PathVariable(name = "id") String id, @PathVariable(name = "name") String name) {
        return clientR.search_by_name(id, name);
    }

    // Fonction pour modifier un client
    @PutMapping(path = "/update_client/{id}")
    ResponseEntity<Client> update_client(@PathVariable(name = "id") String id, @RequestBody Client c) {
        Users u = usersR.findById(id).get();

        List<Client> clients = u.getClients();
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId().toString().equals(c.getId().toString())) {
                clients.get(i).setEmail(c.getEmail());
                clients.get(i).setName(c.getName());
                clients.get(i).setNumber(c.getNumber());
                System.out.println("\n\n" + c.getNumber() + "\tvrai:" + clients.get(i).getNumber());
                break;
            }
        }
        usersR.save(u);
        return new ResponseEntity<Client>(clientR.save(c), HttpStatus.OK);
    }

    // Fonction pour modifier un client
    @PutMapping(path = "/update_fournisseur/{id}")
    ResponseEntity<Fournisseur> update_fournisseur(@PathVariable(name = "id") String id, @RequestBody Fournisseur c) {
        Users u = usersR.findById(id).get();
        System.out.println("Le client recu:>>>");
        System.out.println("id:" + c.getId());
        System.out.println("Name:" + c.getName());
        System.out.println("Email:" + c.getEmail());
        System.out.println("Number:" + c.getNumber());
        System.out.println(c.getId() + " le id >>>>>>>>>>>>>>>\n\n\n");
        List<Fournisseur> f = u.getFournisseurs();
        for (int i = 0; i < f.size(); i++) {
            if (f.get(i).getId() == c.getId()) {
                f.get(i).setEmail(c.getEmail());
                f.get(i).setName(c.getName());
                f.get(i).setNumber(c.getNumber());
                break;
            }
        }
        usersR.save(u);
        return new ResponseEntity<Fournisseur>(fournisseurRepo.save(c), HttpStatus.OK);
    }

    // ventes de la semaine
    @GetMapping("/vente_de_la_semaine")
    List<?> vente_semaine() {
        return usersR.ventes_de_la_semaine();
    }

    @GetMapping(path = "/all_ventes/{id}")
    List<?> all_ventes(@PathVariable(name = "id") Long id) {
        List<?> v = new ArrayList<>();
        v = usersR.all_ventes(id);
        return v;

    }

    @GetMapping(path = "/all_achat/{id}")
    List<?> all_achats(@PathVariable(name = "id") String id) {
        List<Achat> v = usersR.findById(id).get().getAchats();
        return v;

    }

    @GetMapping(path = "ventes/{id}")
    ResponseEntity<List<ventes>> find_ventes(@PathVariable(name = "id") String id) {
        return new ResponseEntity<List<ventes>>(usersR.findById(id).get().getVentes(), HttpStatus.OK);
    }

    @GetMapping(path = "achats/{id}")
    ResponseEntity<List<Achat>> find_achats(@PathVariable(name = "id") String id) {
        return new ResponseEntity<List<Achat>>(usersR.findById(id).get().getAchats(), HttpStatus.OK);
    }

    @PutMapping(path = "/client_modifier/{id}")
    ResponseEntity<Client> modifier(Long id, Client client) {
        // List<Client> clients=usersR.findById(id).get().getClients();
        // clients.forEach((c)->{
        // if(c.getId()==client.getId()){
        // c=client;
        // }
        // });
        // usersR.save(null);

        return new ResponseEntity<>(clientR.save(client), HttpStatus.CREATED);
    }

    @PostMapping(path = "/add_tache/{id}")
    ResponseEntity<?> add_tache(@PathVariable(name = "id") String id, @RequestBody Tache t) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println("\n\ntaches: \n" + t);

        Users u = usersR.findById(id).get();
        u.ajouter_tache(tacheR.save(t));
        usersR.save(u);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/find_days/{id}/{date}")
    List<Tache> find_days(@PathVariable String id, @PathVariable String date) {
        List<Tache> taches = new ArrayList<>();

        usersR.findById(id).get().getTaches().forEach(e -> {
            if (e.getDate_().equals(date)) {
                taches.add(e);
            }
        });
        return taches;
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

    @PostMapping("/ajouterCommande/{id}/{id2}")
    ResponseEntity<Commande> addCommande(@RequestBody Commande c, @PathVariable(name = "id") String id,
            @PathVariable(name = "id2") String id_) {

        Users utilisateur = usersR.findById(id).get();

        for (var commande : utilisateur.getCommande()) {
            if (commande.getRef().toLowerCase().equals(c.getRef().toLowerCase())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        Commande newCommande = commandeRepo.save(c);

        utilisateur.addCommande(newCommande);

        Client client = clientR.findById(id_).get();
        c.setNomC(client.getName());
        client.ajouterCommande(newCommande);
        clientR.save(client);
        usersR.save(utilisateur);
        return new ResponseEntity<Commande>(newCommande, HttpStatus.CREATED);
    }

    @GetMapping("/allCommande/{id}")
    ResponseEntity<List<Commande>> getCommandeByUser(@PathVariable String id) {
        Users u = usersR.findById(id).get();
        List<Commande> commandes = new ArrayList<>();
        // if (u.getClients().size() > 0) {
        // u.getClients().forEach(e -> {
        // e.getCommandes().forEach(c -> {
        // if (c.getRef() != null) {
        // commandes.add(c);
        // }
        // });
        // });
        // }
        commandes = u.getCommande();
        return new ResponseEntity<List<Commande>>(commandes, HttpStatus.OK);
    }

    @GetMapping("/terminer/{id}")
    ResponseEntity terminerCommande(@PathVariable(name = "id") String id) {
        Commande c = commandeRepo.findById(id).get();
        c.setTerminer(!c.terminer);
        commandeRepo.save(c);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getVenteCommande/{id}/{name}")
    ResponseEntity<List<ventes>> getventeBycommande(@PathVariable("id") String id, @PathVariable("name") String name) {
        List<ventes> v = usersR.findById(id).get().getVentes();

        List<ventes> v_ = new ArrayList<>();
        v.forEach((e) -> {
            if (e.getNumeroCommande() != null && e.getNumeroCommande().equals(name)) {
                v_.add(e);
            }
        });
        return new ResponseEntity<List<ventes>>(v_, HttpStatus.OK);
    }

    @PostMapping("/updateLogo/{id}")
    ResponseEntity updateLogo(@RequestBody String path, @PathVariable String id) {
        System.out.println(path);
        Users u = usersR.findById(id).get();
        u.setImage_url(path);
        u.getInfo().setLogo(path);
        usersR.save(u);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/getVentesAnnee/{id}/{year}")
    ResponseEntity<List<Double>> getVentes(@PathVariable String id, @PathVariable String date) {
        List<Double> annee = new ArrayList<Double>();
        List<ventes> ventes = usersR.findById(id).get().getVentes();
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

    @DeleteMapping("/delete_fournisseur/{id}/{idF}")
    ResponseEntity<Long> deleteFournisseur(@PathVariable(name = "id") String id,
            @PathVariable(name = "idF") String id2) {
        // Users u = usersR.findById(id).get();
        fournisseurRepo.deleteById(id2);
        return new ResponseEntity<Long>(HttpStatus.OK);
    }

    @DeleteMapping("/delete_client/{id}/{idF}")
    ResponseEntity<?> deleteClient(@PathVariable(name = "id") String id, @PathVariable(name = "idF") String id2) {

        clientR.deleteById(id2);
        return new ResponseEntity(HttpStatus.OK);
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

    @GetMapping(path = "updateSuscription/{id}/{dateDebut}/{dateFin}/{prix}")
    ResponseEntity<Users> updatSuscription(@PathVariable String id, @PathVariable(name = "prix") double montant,
            @PathVariable(name = "dateDebut") String dateAbonnement,
            @PathVariable(name = "dateFin") String finAbonnement) {
        try {
            Users u = usersR.findById(id).get();
            u.setMontantAbonnement(montant);
            u.setSuscription(true);
            u.setDateAbonnement(dateAbonnement);
            u.setFinAbonnement(finAbonnement);
            return new ResponseEntity<Users>(usersR.save(u), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping(path = "get_vente_by_id/{id}")
    ResponseEntity<ventes> getSingleVente(@PathVariable String id) {
        return new ResponseEntity<ventes>(venteRepo.findById(id).get(), HttpStatus.OK);
    }

    @PostMapping(path = "update_ventes/{id}/{montant}/{date}")
    ResponseEntity<ventes> updateVente(@PathVariable(name = "id") String id,
            @PathVariable(name = "montant") double montant, @PathVariable(name = "date") String date) {
        String val = montant + "," + date;
        ventes ventes = venteRepo.findById(id).get();
        ventes.setEspece(ventes.getEspece() + montant);
        ventes.addVentes(val);
        return new ResponseEntity<ventes>(venteRepo.save(ventes), HttpStatus.CREATED);
    }

    @GetMapping(path = "vente_client/{id}")
    ResponseEntity<List<ventes>> vente_by_client(@PathVariable String id) {
        return new ResponseEntity<List<ventes>>(clientR.findById(id).get().getVentes(), HttpStatus.OK);
    }
}
