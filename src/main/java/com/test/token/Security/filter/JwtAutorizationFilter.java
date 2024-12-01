package com.test.token.Security.filter;

import com.test.token.Security.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAutorizationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,@NotNull HttpServletResponse response,@NotNull FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if(tokenHeader !=null && tokenHeader.startsWith("Bearer ")){
            String token = tokenHeader.substring(7);

            if(jwtUtils.isTokenValid(token)){
                String username= jwtUtils.getUsernameFormToken(token);
                String role= jwtUtils.getRoleFromToken(token);
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                List<GrantedAuthority> authorities = Collections.singletonList(authority);

                UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(username,null,authorities);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);

    }
}
