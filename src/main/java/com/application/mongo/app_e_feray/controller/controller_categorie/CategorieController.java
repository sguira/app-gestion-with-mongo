package com.application.mongo.app_e_feray.controller.controller_categorie;

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

import com.application.mongo.app_e_feray.entities.Categorie;
import com.application.mongo.app_e_feray.entities.Users;
import com.application.mongo.app_e_feray.repository.CategorieRepositori;
import com.application.mongo.app_e_feray.repository.UserRepositori;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/backend/categories/")
public class CategorieController {

    @Autowired(required = true)
    private CategorieRepositori catR;

    @Autowired(required = true)
    private UserRepositori usersR;

    @PostMapping(path = "/addcategorie/{id}")
    ResponseEntity<Categorie> ajouterCategorie(@RequestBody Categorie cat, @PathVariable(name = "id") String id) {
        Users u = usersR.findById(id).get();
        System.out.println("Client " + u.getName());
        List<Categorie> cats = (List<Categorie>) (u.getCategories());
        if (cats.size() > 0) {
            for (int i = 0; i < cats.size(); i++) {
                if (cats.get(i) != null) {
                    if (cats.get(i).getName() != null) {
                        if (cats.get(i).getName().equals(cat.getName())) {
                            System.out.println(" CONFLICT >>>>>>>>>>>>> ");
                            return new ResponseEntity<>(HttpStatus.CONFLICT);
                        }
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

    @DeleteMapping(path = "/delete_categorie/{id}")
    int deletecategorie(@PathVariable String id) throws Exception {
        try {
            catR.deleteById(id);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @PutMapping(value = "/update_categorie/{id}")
    ResponseEntity<Categorie> updateCategorie(@PathVariable(name = "id") String id, @RequestBody Categorie cat_)
            throws Exception {
        try {
            Users u = usersR.findById(id).get();

            Categorie cat = catR.findById(cat_.getId()).get();
            cat.setName(cat_.getName());
            cat.setDescription(cat_.getDescription());

            return new ResponseEntity<Categorie>(catR.save(cat), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("error" + e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(path = "/all_categories/{id}")
    ResponseEntity<List<Categorie>> get_categorie_by_user(@PathVariable(name = "id") String id) {
        return new ResponseEntity<List<Categorie>>((List<Categorie>) usersR.findById(id).get().getCategories(),
                HttpStatus.OK);
    }

}
