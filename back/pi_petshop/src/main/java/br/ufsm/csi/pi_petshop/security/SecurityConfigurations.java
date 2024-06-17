package br.ufsm.csi.pi_petshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/pet/create").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.GET, "/pet").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/pet/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.PUT, "/pet/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/pet/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.GET, "/cliente/{id}").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/cliente/todos").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/cliente").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.PUT, "/cliente").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.PUT, "/cliente/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/cliente/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.POST, "/cliente/create").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.GET, "/cliente/me").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.PUT, "/cliente/me").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.GET, "/funcionario").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/funcionario/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/funcionario/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/funcionario/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/funcionario/create").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user").permitAll()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
