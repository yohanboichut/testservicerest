package fr.info.orleans.wsi.tp3.modele;

public interface FacadeUtilisateurs {
    /**
     * Permet de récupérer l'identifiant Integer à partir du login (email)
     *
     * @param login
     * @return
     * @throws UtilisateurInexistantException
     */
    int getUtilisateurIntId(String login) throws UtilisateurInexistantException;

    /**
     * Permet de récupérer un Utilisateur à partir de son login
     *
     * @param login
     * @return
     */

    Utilisateur getUtilisateurByLogin(String login);

    /**
     * Permet d'inscrire un nouvel utilisateur à la plate-forme
     *
     * @param login
     * @param mdp
     * @return son identifiant Integer
     * @throws LoginDejaUtiliseException
     */
    int inscrireUtilisateur(String login, String mdp) throws LoginDejaUtiliseException;

    /**
     * Permet de vérifier si le mot de passe est correct (useless
     * dans la version finale)
     *
     * @param login
     * @param motDePasse
     * @return
     */
    boolean verifierMotDePasse(String login, String motDePasse);
}
