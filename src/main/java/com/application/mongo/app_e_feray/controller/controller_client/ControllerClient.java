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
import org.springframework.web.bind.annotation.RequestHeader;
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
import com.application.mongo.app_e_feray.services.JWTUtils;

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

    // ajouter un nouveau client
    @PostMapping(path = "/add_client")
    ResponseEntity<Client> ajouter_client(@RequestBody Client c, @RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
                List<Client> clients = new ArrayList<Client>();
                clients = u.getClients();
                Client cc = clientR.save(c);
                u.ajouter_client(cc);

                usersR.save(u);
                return new ResponseEntity<Client>(cc, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/add_contact")
    ResponseEntity<Contact> ajouterContact(@RequestBody Contact c, @RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
                u.ajouter_contact(c);
                usersR.save(u);
                return new ResponseEntity<Contact>(contactRepo.save(c), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/get_client/{id}")
    ResponseEntity<List<ventes>> get_clients_ventes(@PathVariable(name = "id") String id,
            @RequestHeader("Authorization") String token) {

        try {
            token = tokenValide(token);
            if (token != null) {
                return new ResponseEntity<List<ventes>>((List<ventes>) clientR.findById(id).get().getVentes(),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/ventes_for_clients/{id}/{choice}")
    ResponseEntity<List<ventes>> getVentes(@PathVariable String id, @PathVariable(name = "choice") Long choice,
            @RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                return new ResponseEntity<List<ventes>>(clientR.findById(id).get().getVentes(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @GetMapping(path = "/search_client_by_name/{id}/{name}")
    // List<Client> recherche_client(@PathVariable(name = "id") String id,
    // @PathVariable(name = "name") String name) {
    // return clientR.search_by_name(id, name);
    // }

    @GetMapping(path = "/get_fournisseur/{id}")
    ResponseEntity<List<Fournisseur>> fournisseurs(@RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                return new ResponseEntity<List<Fournisseur>>(
                        (List<Fournisseur>) usersR.findByEmail(token).getFournisseurs(),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Fonction pour modifier un client
    @PutMapping(path = "/update_client")
    ResponseEntity<Client> update_client(@RequestHeader("Authorization") String token, @RequestBody Client c) {
        try {
            token = tokenValide(token);
            if (token != null) {
                Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
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
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    ResponseEntity<Client> client(@PathVariable String id, @RequestHeader("Authorization") String token) {
        try {
            token = tokenValide(token);
            if (token != null) {
                return new ResponseEntity<Client>(clientR.findById(id).get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/delete_client/{idF}")
    ResponseEntity<?> deleteClient(@RequestHeader("Authorization") String token,
            @PathVariable(name = "idF") String id2) {

        try {
            token = tokenValide(token);
            if (token != null) {
                clientR.deleteById(id2);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/get_clients")
    ResponseEntity<List<Client>> clients(@RequestHeader("Authorization") String token) {

        try {
            token = tokenValide(token);
            if (token != null) {
                return new ResponseEntity<List<Client>>(
                        (List<Client>) usersR.findByEmail(jwtUtils.extractUsername(token)).getClients(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
