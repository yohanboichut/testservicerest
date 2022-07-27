package fr.info.orleans.wsi.tp3.modele;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FacadeUtilisateursImpl implements FacadeUtilisateurs {
    /**
     * Permet de stocker l'ensemble des utilisateurs inscrits au service
     */



    private Map<String,Utilisateur> utilisateursMap;

    public FacadeUtilisateursImpl() {
        utilisateursMap = new HashMap<>();
    }

    @Override
    public int getUtilisateurIntId(String login) throws UtilisateurInexistantException{
        if (utilisateursMap.containsKey(login))
            return this.utilisateursMap.get(login).getIdUtilisateur();
        else
            throw new UtilisateurInexistantException();
    }

    @Override
    public Utilisateur getUtilisateurByLogin(String login) {
        return utilisateursMap.get(login);
    }


    @Override
    public int inscrireUtilisateur(String login, String mdp) throws LoginDejaUtiliseException {
        if (utilisateursMap.containsKey(login))
            throw new LoginDejaUtiliseException();
        else {
            Utilisateur utilisateur = new Utilisateur(login,mdp);
            utilisateursMap.put(utilisateur.getLogin(),utilisateur);
            return utilisateur.getIdUtilisateur();
        }
    }


    @Override
    public boolean verifierMotDePasse(String login, String motDePasse){
        if (utilisateursMap.containsKey(login)){
            return utilisateursMap.get(login).verifierMotDePasse(motDePasse);
        }
        else
            return false;
    }

}
