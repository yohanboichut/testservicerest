package fr.info.orleans.wsi.tp3.modele;

import java.util.Collection;

public interface FacadeApplication {
    /**
     * Permet à un idUtilisateur de poser une question
     *
     * @param idUtilisateur
     * @param question
     * @return l'identifiant string aléatoire de la question créée
     */
    String ajouterUneQuestion(int idUtilisateur, String question);

    /**
     * Permet à un utilisateur de répondre à une question
     *
     * @param idQuestion
     * @param reponse
     * @throws QuestionInexistanteException
     */
    void repondreAUneQuestion(String idQuestion, String reponse) throws QuestionInexistanteException;

    /**
     * Permet de récupérer toutes les questions en attente de réponse
     *
     * @return
     */

    Collection<Question> getQuestionsSansReponses();

    /**
     * Permet à un utilisateur de récupérer toutes les questions qu'il a posées pour lesquelles
     * quelqu'un a répondu
     *
     * @param idUtilisateur
     * @return
     * @throws UtilisateurInexistantException
     */

    Collection<Question> getQuestionsAvecReponsesByUser(int idUtilisateur) throws UtilisateurInexistantException;

    /**
     * Permet à un utilisateur de récupérer toutes les questions qu'il a posées pour lesquelles
     * personne n'a répondu
     *
     * @param idUtilisateur
     * @return
     * @throws UtilisateurInexistantException
     */

    Collection<Question> getQuestionsSansReponsesByUser(int idUtilisateur) throws UtilisateurInexistantException;

    /**
     * Permet de récupérer toutes les questions posées par un utilisateur
     *
     * @param idUtilisateur
     * @return
     * @throws UtilisateurInexistantException
     */


    Collection<Question> getToutesLesQuestionsByUser(int idUtilisateur) throws UtilisateurInexistantException;

    /**
     * Permet de récupérer l'ensemble des questions posées
     *
     * @return
     */
    Collection<Question> getToutesLesQuestions();

    /**
     * Permet de récupérer une question à partir du moment où
     * cette personne appartient à l'utilisateur qui l'a posée
     *
     * @param idUtilisateur
     * @param idQuestion
     * @return
     * @throws QuestionInexistanteException
     * @throws AccessIllegalAUneQuestionException
     * @throws UtilisateurInexistantException
     */

    Question getQuestionByIdPourUnUtilisateur(int idUtilisateur, String idQuestion) throws QuestionInexistanteException, AccessIllegalAUneQuestionException, UtilisateurInexistantException;
}
