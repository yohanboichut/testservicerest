package fr.info.orleans.wsi.tp3;


import fr.info.orleans.wsi.tp3.config.Paths;
import fr.info.orleans.wsi.tp3.modele.FacadeApplication;
import fr.info.orleans.wsi.tp3.modele.FacadeUtilisateurs;
import fr.info.orleans.wsi.tp3.modele.LoginDejaUtiliseException;
import fr.info.orleans.wsi.tp3.modele.Utilisateur;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestWebServiceRest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FacadeUtilisateurs facadeUtilisateurs;

    @MockBean
    private FacadeApplication facadeApplication;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;


    @Test
    public void testCreerUtilisateurOk() throws Exception {

        String email = "yohan.boichut@univ-orleans.fr";
        String motDePasse ="monMotDePasse";
        String motDePasseEncode = "babar";
        int id = 1;
        given(passwordEncoder.encode(motDePasse)).willReturn(motDePasseEncode);
        given(facadeUtilisateurs.inscrireUtilisateur(email,motDePasseEncode)).willReturn(id);

        mvc.perform(post(Paths.HOTE+Paths.VERSION+Paths.UTILISATEUR)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("pseudo="+email+"&password="+motDePasse))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }


    @Test
    public void testCreerUtilisateuKO() throws Exception {

        String email = "yohan.boichut@univ-orleans.fr";
        String motDePasse ="monMotDePasse";
        String motDePasseEncode = "babar";

        given(passwordEncoder.encode(motDePasse)).willReturn(motDePasseEncode);
        given(facadeUtilisateurs.inscrireUtilisateur(email,motDePasseEncode)).willThrow(new LoginDejaUtiliseException());

        mvc.perform(post(Paths.HOTE+Paths.VERSION+Paths.UTILISATEUR)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("pseudo="+email+"&password="+motDePasse))
                .andExpect(status().isConflict());
    }



    @Test
    public void testCreateKO() throws Exception {

        String email = "yohan.boichut@univ-orleans.fr";
        String motDePasse ="monMotDePasse";
        String motDePasseEncode = "babar";
        String question = "Quelle est la couleur du cheval blanc ?";
        Utilisateur utilisateur = BDDMockito.mock(Utilisateur.class);
        given(utilisateur.getLogin()).willReturn(email);
        given(utilisateur.getMotDePasse()).willReturn(motDePasseEncode);
        given(passwordEncoder.matches(motDePasse,motDePasseEncode))
                .willReturn(true);

        given(facadeUtilisateurs.getUtilisateurByLogin(email))
                .willReturn(utilisateur);


        mvc.perform(
                post(Paths.HOTE+Paths.VERSION+
                        Paths.QUESTION)
                        .with(httpBasic(email,motDePasse))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("question="+question)
        ).andExpect(status().isUnauthorized());




    }


    @Test
    public void testCreateOK() throws Exception {

        String email = "yohan.boichut@etu.univ-orleans.fr";
        String motDePasse ="monMotDePasse";
        String motDePasseEncode = "babar";
        String question = "Quelle est la couleur du cheval blanc ?";

        int id = 2;
        String idQuestion = "idQuestion";
        Utilisateur utilisateur = BDDMockito.mock(Utilisateur.class);
        given(utilisateur.getLogin()).willReturn(email);
        given(utilisateur.getMotDePasse()).willReturn(motDePasseEncode);
        given(passwordEncoder.matches(motDePasse,motDePasseEncode))
                .willReturn(true);

        given(facadeUtilisateurs.getUtilisateurByLogin(email))
                .willReturn(utilisateur);

        given(facadeUtilisateurs.getUtilisateurIntId(email)).willReturn(id);
        given(facadeApplication.ajouterUneQuestion(id,question))
                .willReturn(idQuestion);



        mvc.perform(
                post(Paths.HOTE+Paths.VERSION+
                        Paths.QUESTION)
                        .with(httpBasic(email,motDePasse))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("question="+question)
        ).andExpect(status().isCreated()).andExpect(header().exists("Location"));
    }


}
