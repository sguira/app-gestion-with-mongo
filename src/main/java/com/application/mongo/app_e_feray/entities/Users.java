package com.application.mongo.app_e_feray.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

@Document()
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Users {

    @Id
    private String id;

    private String name;

    private String last_name;
    private String number;

    private String email;
    private String confirmCode = null;
    private boolean confirmed = false;

    class Abonnement_ {

    }

    @Exclude
    private String password;
    private String image_url;
    private InfoEntreprise info;

    private boolean suscription = false;
    String dateAbonnement;
    double montantAbonnement;
    String code;
    String finAbonnement;
    String numeroCompte;
    private String recuperation = "";

    private List<String> articlesFrequent = new ArrayList<>();

    String lang = "fr";

    @DBRef
    private List<Categorie> categories = new ArrayList<>();

    @DBRef
    private List<Client> clients = new ArrayList<>();

    @DBRef
    private Collection<Contact> contacts = new ArrayList<>();

    @DBRef
    private List<Fournisseur> fournisseurs = new ArrayList<>();

    @DBRef
    private List<Achat> achats = new ArrayList<>();

    // @JsonProperty(access = Access.WRITE_ONLY)
    @DBRef
    private List<ventes> ventes = new ArrayList<>();

    @DBRef
    private List<Tache> taches = new ArrayList();

    @DBRef
    private List<Commande> commande = new ArrayList<>();

    @DBRef
    private List<DetailAbonnement> abonnements = new ArrayList<>();

    @DBRef
    private List<Devis> devis = new ArrayList<>();

    public void ajouterCategorie(Categorie cat) {
        this.categories.add(cat);
    }

    public void ajouterDevis(Devis devis) {
        this.devis.add(devis);
    }

    public void ajouter_client(Client u) {
        this.clients.add(u);
    }

    public void ajouter_fournisseur(Fournisseur f) {
        this.fournisseurs.add(f);
    }

    public void ajouter_ventes(ventes v) {
        this.ventes.add(0, v);
    }

    public void ajouter_achats(Achat a) {
        this.achats.add(a);
    }

    public void ajouter_tache(Tache t) {
        this.taches.add(t);
    }

    public void addCommande(Commande t) {
        this.commande.add(t);
    }

    public void ajouter_contact(Contact c) {
        this.contacts.add(c);
    }

    public void addAbonnement(DetailAbonnement abonnement) {
        this.abonnements.add(abonnement);
    }

    public String formatMail() {
        return "Cher client nous vous souhaitons la bienvenue sur notre l'application " +
                "Vous venez de créer un compte sur l'application E-Guira avec les informations suivantes:" + "\n Nom:"
                + name + "\tEmail:" + email + "\tNom Entreprise:" + info.name + "\tNuméro:" + info.telephone
                + "\tAdresse:" + info.addresse
                + "Vous pouvez à present vous connecter grâce à votre nom d'utilisateur" + email
                + " et votre mot de passe:**********";
    }

    public void ajouterArticleFrequent(String id) {
        this.articlesFrequent.add(id);
    }

}
