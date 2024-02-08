package com.application.mongo.app_e_feray.controller.controller_user;

import java.util.List;

import com.application.mongo.app_e_feray.entities.Categorie;
import com.application.mongo.app_e_feray.entities.Users;

public class verifier {

    static boolean addUsers(List<Users> list, String email) {

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getEmail().equals(email)) {
                    return true;

                }
            }
        }
        return false;
    }

    // static Users searchUserByEmail(List<Users> list, String email) {
    // for (int i = 0; i < list.size(); i++) {
    // if (list.get(i).equals(email)) {
    // return (Users) list.get(i);
    // }
    // }
    // return null;
    // }

    static int search_category(List<Categorie> list, String name) {

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName() != null) {
                    if (list.get(i).getName().equals(name)) {
                        return 1;
                    }
                }

            }
        }
        return 0;
    }

}
