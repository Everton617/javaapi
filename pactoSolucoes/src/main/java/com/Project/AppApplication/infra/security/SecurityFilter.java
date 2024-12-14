package com.Project.AppApplication.infra.security;


import com.Project.AppApplication.domain.LoginUser;
import com.Project.AppApplication.repository.candidato.CandidatoRepository;
import com.Project.AppApplication.repository.company.CompanyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Autowired
    CandidatoRepository candidatoRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        var login = tokenService.validateToken(token);

        if (login != null) {
            String userType = this.tokenService.getUserTypeFromToken(token);
            LoginUser user;

            if (userType.equalsIgnoreCase("candidato")) {
                user = candidatoRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("Candidato Not Found"));
            } else if (userType.equalsIgnoreCase("company")) {
                user = companyRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("Company Not Found"));
            } else {
                throw new RuntimeException("Invalid User Type in Token");
            }


            var authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }


}

