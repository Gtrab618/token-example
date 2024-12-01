package com.test.token.Security;

import com.test.token.Security.filter.JwtAuthenticationFilter;
import com.test.token.Security.filter.JwtAutorizationFilter;
import com.test.token.Security.jwt.JwtUtils;
import com.test.token.service.UserDetailsServiceImlp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    UserDetailsServiceImlp userDetailsService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    JwtAutorizationFilter autorizationFilter;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationManager authenticationManager) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter= new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authHttp -> {
                    authHttp.requestMatchers("/hello").permitAll(); //pueden accder todos endponit publico
                    authHttp.anyRequest().authenticated();
                })
                .sessionManagement(session ->{
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(autorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //encargado de la autenticacion de los usuarios
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity,PasswordEncoder passwordEncoder) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();


    }

}
