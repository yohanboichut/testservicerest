package fr.info.orleans.wsi.tp3.modele;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FacadeApplicationImpl implements FacadeApplication {
    /**
     * Les utilisateurs ne sont pas stockés ici, on n'utilise que leur identifiant integer
     * On stocke ici toutes les questions posées par chaque utilisateur
     */
    private Map<Integer, Collection<Question>> utilisateursQuestionsMap;

    /**
     * Map de toutes les questions posées
     */
    private Map<String,Question> questionsMap;


    public FacadeApplicationImpl() {
        utilisateursQuestionsMap = new HashMap<Integer, Collection<Question>>();
        questionsMap = new HashMap<String, Question>();

    }

    @Override
    public String ajouterUneQuestion(int idUtilisateur, String question) {
        Question question1 = new Question(idUtilisateur,question);
        questionsMap.put(question1.getIdQuestion(),question1);
        if (utilisateursQuestionsMap.containsKey(idUtilisateur)) {
            this.utilisateursQuestionsMap.get(idUtilisateur).add(question1);
        }
        else {
            Collection<Question> questions = new ArrayList<Question>();
            questions.add(question1);
            this.utilisateursQuestionsMap.put(idUtilisateur,questions);
        }
        return question1.getIdQuestion();
    }
    @Override
    public void repondreAUneQuestion(String idQuestion, String reponse) throws QuestionInexistanteException {
        if (this.questionsMap.containsKey(idQuestion)) {
            this.questionsMap.get(idQuestion).setReponse(reponse);
        }
        else {
            throw new QuestionInexistanteException();
        }
    }

    @Override
    public Collection<Question> getQuestionsSansReponses(){
        return this.questionsMap.values().stream().filter(q ->Objects.isNull(
                q.getReponse()) || q.getReponse().isBlank()).collect(Collectors.<Question>toList());
    }

    @Override
    public Collection<Question> getQuestionsAvecReponsesByUser(int idUtilisateur) throws UtilisateurInexistantException {
        if (this.utilisateursQuestionsMap.containsKey(idUtilisateur)) {
            return this.utilisateursQuestionsMap.get(idUtilisateur)
                    .stream().filter(q -> Objects.nonNull(q.getReponse())
                            && (!q.getReponse().isBlank())).collect(Collectors.toList());
        }
        else {
            throw new UtilisateurInexistantException();
        }
    }


    @Override
    public Collection<Question> getQuestionsSansReponsesByUser(int idUtilisateur) throws UtilisateurInexistantException {
        if (this.utilisateursQuestionsMap.containsKey(idUtilisateur)) {
            return this.utilisateursQuestionsMap.get(idUtilisateur)
                    .stream()
                    .filter(q -> Objects.isNull(q.getReponse()) || q.getReponse().isBlank()).collect(Collectors.toList());
        }
        else {
            throw new UtilisateurInexistantException();

        }
    }


    @Override
    public Collection<Question> getToutesLesQuestionsByUser(int idUtilisateur) throws UtilisateurInexistantException {
        if (this.utilisateursQuestionsMap.containsKey(idUtilisateur)) {
            return this.utilisateursQuestionsMap.get(idUtilisateur);
        }
        else {
            throw new UtilisateurInexistantException();

        }
    }

    @Override
    public Collection<Question> getToutesLesQuestions() {
        return questionsMap.values();
    }


    @Override
    public Question getQuestionByIdPourUnUtilisateur(int idUtilisateur, String idQuestion) throws QuestionInexistanteException, AccessIllegalAUneQuestionException, UtilisateurInexistantException {
        Question q = questionsMap.get(idQuestion);
        if (Objects.isNull(q))
            throw new QuestionInexistanteException();
        Collection<Question> questionsIdUtilisateur = this.utilisateursQuestionsMap.get(idUtilisateur);

        if (Objects.isNull(questionsIdUtilisateur)) {
            throw new UtilisateurInexistantException();
        }


        if (questionsIdUtilisateur.contains(q)) {
            return q;
        }
        else {
            throw new AccessIllegalAUneQuestionException();
        }

    }
}
