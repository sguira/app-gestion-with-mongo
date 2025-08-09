// package com.application.mongo.app_e_feray.controller.souscription;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.application.mongo.app_e_feray.entities.Users;

// import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/api/souscription")
// @RequiredArgsConstructor
// public class SouscriptionController {

//     @GetMapping(path = "/updateSuscription/{dateDebut}/{dateFin}/{prix}/{number}/{duree}")
//     ResponseEntity<Users> updatSuscription(@RequestHeader("Authorization") String token,
//             @PathVariable(name = "prix") double montant,
//             @PathVariable(name = "dateDebut") String dateAbonnement,
//             @PathVariable(name = "dateFin") String finAbonnement,
//             @PathVariable(name = "number") String numero,
//             @PathVariable(name = "duree") int duree
//     ) {
//         try {

//         } catch (Exception e) {
//             System.out.println("Erreur abonnement.");
//             e.printStackTrace();
//             return new ResponseEntity<>(HttpStatus.CONFLICT);
//         }

//         try {
//             token = tokenValide(token);
//             if (token != null) {
//                 Users u = usersR.findByEmail(jwtUtils.extractUsername(token));
//                 u.setMontantAbonnement(montant);
//                 u.setSuscription(true);
//                 u.setDateAbonnement(dateAbonnement);
//                 u.setFinAbonnement(finAbonnement);
//                 DetailAbonnement abonnement = new DetailAbonnement();
//                 abonnement.setDateAbonnement(dateAbonnement);
//                 abonnement.setFinAbonnement(finAbonnement);
//                 abonnement.setNumero(numero);
//                 abonnement.setPrix(montant);
//                 abonnement.setDuree(duree);
//                 // abonnement.setLabel(numero);
//                 abonnement = detailAbonnementR.save(abonnement);

//                 u.addAbonnement(abonnement);
//                 System.out.println("Abonnement");
//                 return new ResponseEntity<>(usersR.save(u), HttpStatus.CREATED);

//             }
//             return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

//         } catch (Exception e) {
//             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

// }

