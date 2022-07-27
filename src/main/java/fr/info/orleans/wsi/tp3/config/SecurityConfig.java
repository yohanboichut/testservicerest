package fr.info.orleans.wsi.tp3.config;

import fr.info.orleans.wsi.tp3.modele.FacadeUtilisateurs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    BCryptPasswordEncoder passwordEncoder;



    public static String[] getRoles(String email){
        String s = (email.split("@"))[1];
        switch (s){
            case "etu.univ-orleans.fr":{
                return new String[]{"ETUDIANT"};
            }
            case "univ-orleans.fr" : {
                return new String[]{"PROFESSEUR"};
            }
            default: {
                return new String[0];
            }
        }

    }


    @Bean
    @Override
    protected UserDetailsService userDetailsService() {

        return new CostumizedUserDetailsService();
    }




    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/question").hasRole("ETUDIANT")
                .antMatchers(HttpMethod.PATCH,"/api/question").hasRole("PROFESSEUR")
                .antMatchers(HttpMethod.POST,"/api/utilisateur").permitAll()
                .antMatchers("/api/utilisateur/**").hasRole("ETUDIANT")
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}

