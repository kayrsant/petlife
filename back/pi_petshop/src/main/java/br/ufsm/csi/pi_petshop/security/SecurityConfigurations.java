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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Autenticação e Registro
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // Pet
                        .requestMatchers(HttpMethod.GET, "/pet").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pet/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.POST, "/pet/create").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.PUT, "/pet/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/pet/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")

                        // Cliente
                        .requestMatchers(HttpMethod.GET, "/cliente").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.GET, "/cliente/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.GET, "/cliente/todos").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers(HttpMethod.POST, "/cliente/create").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.PUT, "/cliente/me").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.PUT, "/cliente/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/cliente/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")

                        // Agendamento
                        .requestMatchers(HttpMethod.GET, "/agendamento").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.GET, "/agendamento/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.POST, "/agendamento/create").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.PUT, "/agendamento/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/agendamento/{id}").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")

                        // Serviço
                        .requestMatchers(HttpMethod.GET, "/servico").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.POST, "/servico/create").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers(HttpMethod.PUT, "/servico/{id}").hasAnyRole("ADMIN", "FUNCIONARIO")

                        // Funcionário
                        .requestMatchers(HttpMethod.GET, "/funcionario/me").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.GET, "/funcionario").permitAll()
                        .requestMatchers(HttpMethod.GET, "/funcionario/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/funcionario/create").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/funcionario/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/funcionario/{id}").permitAll()

                        // User
                        .requestMatchers(HttpMethod.GET, "/user").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/user").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.GET, "/user/me").hasAnyRole("ADMIN", "FUNCIONARIO", "USER")
                        .requestMatchers(HttpMethod.GET, "/user/user-role").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/user/{id}/role").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers(HttpMethod.POST, "/user/role").hasAnyRole("ADMIN", "FUNCIONARIO")

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
