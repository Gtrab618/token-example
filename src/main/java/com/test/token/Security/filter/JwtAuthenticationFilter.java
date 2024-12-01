package com.test.token.Security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.token.Security.jwt.JwtUtils;
import com.test.token.model.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils){
        this.jwtUtils= jwtUtils;
    }

    //recogo las credenciales de la web
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserEntity userEntity= null;
        String username;
        String password;

        try{
            userEntity= new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);

            username= userEntity.getUsername();
            password = userEntity.getPassword();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    //valido esas credenciales
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

       //generar token

        User user=(User) authResult.getPrincipal();
        // Obtener el rol del usuario
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Como solo tienes un rol, puedes obtener el primer elemento de la colección
        String role = authorities.iterator().next().getAuthority();

        String token = jwtUtils.generateAccesToken(user.getUsername(),role);
        response.addHeader("Authorization",token);
        Map<String,Object> httpResponse= new HashMap<>();
        httpResponse.put("token",token);
        httpResponse.put("Message","Autentication Correct");
        httpResponse.put("Username",user.getUsername());
        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
