package com.application.mongo.app_e_feray.controller.controller_client;

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
import org.springframework.web.bind.annotation.RestController;

import com.application.mongo.app_e_feray.entities.Client;
import com.application.mongo.app_e_feray.entities.Contact;
import com.application.mongo.app_e_feray.entities.Fournisseur;
import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.entities.ventes;
import com.application.mongo.app_e_feray.repository.ClientRepo;
import com.application.mongo.app_e_feray.repository.ContactRepo;
import com.application.mongo.app_e_feray.repository.UserRepositori;

@RestController
@CrossOrigin
@RequestMapping(path = "/backend/clients")
public class ControllerClient {
    @Autowired(required = true)
    private ClientRepo clientR;

    @Autowired(required = true)
    private UserRepositori usersR;

    @Autowired(required = true)
    private ContactRepo contactRepo;

    // ajouter un nouveau client
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

    @GetMapping(path = "/get_client/{id}")
    ResponseEntity<List<ventes>> get_clients_ventes(@PathVariable(name = "id") String id) {

        return new ResponseEntity<List<ventes>>((List<ventes>) clientR.findById(id).get().getVentes(), HttpStatus.OK);
    }

    @GetMapping(path = "/ventes_for_clients/{id}/{choice}")
    List<ventes> getVentes(@PathVariable String id, @PathVariable(name = "choice") Long choice) {
        return clientR.findById(id).get().getVentes();
    }

    @GetMapping(path = "/search_client_by_name/{id}/{name}")
    List<Client> recherche_client(@PathVariable(name = "id") String id, @PathVariable(name = "name") String name) {
        return clientR.search_by_name(id, name);
    }

    @GetMapping(path = "/get_fournisseur/{id}")
    ResponseEntity<List<Fournisseur>> fournisseurs(@PathVariable String id) {
        return new ResponseEntity<List<Fournisseur>>((List<Fournisseur>) usersR.findById(id).get().getFournisseurs(),
                HttpStatus.OK);
    }

    // Fonction pour modifier un client
    @PutMapping(path = "/update_client/{id}")
    ResponseEntity<Client> update_client(@PathVariable(name = "id") String id, @RequestBody Client c) {
        Users u = usersR.findById(id).get();
        Client client = clientR.findById(c.getId()).get();
        List<Client> clients = u.getClients();
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId().toString().equals(c.getId().toString())) {
                client.setEmail(c.getEmail());
                client.setName(c.getName());
                client.setNumber(c.getNumber());
                u.getClients().set(i, client);
                break;
            }
        }
        usersR.save(u);
        return new ResponseEntity<Client>(clientR.save(client), HttpStatus.OK);
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

    // get client by id
    @GetMapping(path = "/get_client_by_id/{id}")
    ResponseEntity<Client> client(@PathVariable String id) {
        return new ResponseEntity<Client>(clientR.findById(id).get(), HttpStatus.OK);
    }

    @DeleteMapping("/delete_client/{id}/{idF}")
    ResponseEntity<?> deleteClient(@PathVariable(name = "id") String id, @PathVariable(name = "idF") String id2) {

        clientR.deleteById(id2);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/get_clients/{id}")
    ResponseEntity<List<Client>> clients(@PathVariable String id) {

        return new ResponseEntity<List<Client>>((List<Client>) usersR.findById(id).get().getClients(), HttpStatus.OK);

    }

}
