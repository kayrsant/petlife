package br.ufsm.csi.pi_petshop.security;

import java.io.IOException;

import br.ufsm.csi.pi_petshop.services.AuthorizationService;
import br.ufsm.csi.pi_petshop.user.repositories.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;


@Component
public class SecurityFilter extends OncePerRequestFilter{
    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorizationService authorizationService;

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String tokenJWT = recuperarToken(request);
        if (tokenJWT != null) {
            String subject = this.tokenService.getSubject(tokenJWT);
            UserDetails userDetails = this.authorizationService.loadUserByUsername(subject);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                logger.info("UserDetails não encontrado para o subject: " + subject);
            }
        }
        filterChain.doFilter(request, response);
    }


    public String recuperarToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "").trim();
    }
}