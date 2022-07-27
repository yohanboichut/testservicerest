package fr.info.orleans.wsi.tp3.controleur;

import fr.info.orleans.wsi.tp3.config.Paths;
import fr.info.orleans.wsi.tp3.modele.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(Paths.VERSION)

public class ControleurServiceQuestionsReponses {

    @Autowired
    private FacadeUtilisateurs facadeUtilisateurs;


    @Autowired
    private FacadeApplication facadeApplication;


    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    /**
     * Permet de créer un utilisateur. Aucun rôle requis étant donné
     * qu'il s'agit d'une inscription
     * @param pseudo : email
     * @param password : mot de passe non encodé
     * @return
     */

    @PostMapping(Paths.UTILISATEUR)
    public ResponseEntity<String> creerUtilisateur(@RequestParam String pseudo, @RequestParam String password){
        try {
            int id = facadeUtilisateurs.inscrireUtilisateur(pseudo,passwordEncoder.encode(password));
            return ResponseEntity.created(URI.create(Paths.HOTE+Paths.VERSION+Paths.UTILISATEUR+"/"+id)).build();
        } catch (LoginDejaUtiliseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(pseudo + " est un pseudo déjà utilisé !");
        }
    }


    /**
     * Permet à un utilisateur possédant le role étudiant de poser une question
     * @param question
     * @param principal
     * @return
     */
    @PostMapping(Paths.QUESTION)
    public ResponseEntity<String> create(
                                         @RequestParam String question,
                                         Authentication principal) {

            if (principal.getAuthorities().
                    contains(new SimpleGrantedAuthority("ROLE_ETUDIANT"))) {
                try {
                int id = facadeUtilisateurs.getUtilisateurIntId(principal.getName());
                String idQuestion = facadeApplication.ajouterUneQuestion(id,
                        question);
                return ResponseEntity
                        .created(
                                URI.create(Paths.HOTE + Paths.QUESTION + "/" + idQuestion))
                        .body(idQuestion);

                } catch(UtilisateurInexistantException e){
                    return ResponseEntity.notFound().build();
                }
            }
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Permet à un utilisateur ayant le role professeur de répondre à une question
     * @param idQuestion
     * @param reponse
     * @return
     */

    @PatchMapping(Paths.QUESTION+"/{idQuestion}")
    public ResponseEntity<String> repondre(@PathVariable String idQuestion,@RequestParam String reponse){
        try {
            this.facadeApplication.repondreAUneQuestion(idQuestion,reponse);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (QuestionInexistanteException e) {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Permet à un utilisateur de récupérer une question qu'il a posé
     * @param idQuestion
     * @param principal
     * @return
     */

    @GetMapping(Paths.QUESTION+"/{idQuestion}")
    public ResponseEntity<Question> getQuestionByUtilisateur(
                                                             @PathVariable String idQuestion,
                                                             Principal principal) {
        try {
            int id = facadeUtilisateurs.getUtilisateurIntId(principal.getName());
            return ResponseEntity.ok(facadeApplication.getQuestionByIdPourUnUtilisateur(id, idQuestion));

        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessIllegalAUneQuestionException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (QuestionInexistanteException e) {
            return ResponseEntity.notFound().build();
        }
        // il n'a pas d'id, juste un texte
    }

    /**
     * Permet à utilisateur de récupérer toutes ses questions ou
     * à un utilisateur ayant le role professeur de récupérer toutes les questions
     * pour un filtre donné
     * @param filtre : si filtre == nontraitees on récupère toutes les questions sans
     *               réponse
     * @param authentication
     * @return
     */
    @GetMapping(Paths.QUESTION)
    public ResponseEntity<Collection<Question>> getQuestion(@RequestParam Optional<String> filtre, Authentication authentication) {

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PROFESSEUR"))) {
            if (filtre.orElseGet(() -> "pasDeFiltre").equals("nontraitees")) {
                return ResponseEntity.ok(facadeApplication.getQuestionsSansReponses());
            } else {
                return ResponseEntity.ok(facadeApplication.getToutesLesQuestions());
            }
        }
        else {
            try {
            int id = facadeUtilisateurs.getUtilisateurIntId(authentication.getName());
            String f = filtre.orElseGet(() -> "pasdefiltre");

                if ("sansreponse".equals(f)) {
                    return ResponseEntity.ok(facadeApplication.getQuestionsSansReponsesByUser(id));
                }
                else {
                    if ("avecreponse".equals(f)){
                        return ResponseEntity.ok(facadeApplication.getQuestionsAvecReponsesByUser(id));
                    }
                    else
                        return ResponseEntity.ok(facadeApplication.getToutesLesQuestionsByUser(id));

                }
            } catch (UtilisateurInexistantException e) {
                return ResponseEntity.notFound().build();
            }


        }

    }

}
