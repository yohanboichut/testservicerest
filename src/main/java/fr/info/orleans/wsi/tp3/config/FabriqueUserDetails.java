package fr.info.orleans.wsi.tp3.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface FabriqueUserDetails {

    UserDetails creer(String name, String password, String[] roles);
}
