package fr.info.orleans.wsi.tp3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;

@Configuration
public class MyBeans {
    @Bean
    public FabriqueUserDetails fabriqueUserDetails(){
        return (name, password, roles) -> User.builder().username(name).password(password)
                .roles(roles).build();
    }
}
