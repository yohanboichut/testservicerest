package fr.info.orleans.wsi.tp3.config;

import fr.info.orleans.wsi.tp3.modele.FacadeUtilisateurs;
import fr.info.orleans.wsi.tp3.modele.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CostumizedUserDetailsService implements UserDetailsService {
    @Autowired
    private FacadeUtilisateurs facadeUtilisateurs;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @Autowired
    FabriqueUserDetails fabriqueUserDetails;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = facadeUtilisateurs.getUtilisateurByLogin(username);
        if (utilisateur==null) {
            throw  new UsernameNotFoundException("User "+username+" not found");
        }
        String[] roles =  SecurityConfig.getRoles(username);

        return (UserDetails) fabriqueUserDetails.creer(utilisateur.getLogin(),
                utilisateur.getMotDePasse(), roles);
    }

}
