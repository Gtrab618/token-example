package com.test.token.service;

import com.test.token.model.UserEntity;
import com.test.token.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserDetailsServiceImlp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity= userRepository.findByUsername(username).orElse(null);

        if(userEntity!= null){
            Collection<? extends GrantedAuthority> authorities= Collections.singleton(new SimpleGrantedAuthority("ROLE_".concat(userEntity.getRole().getName().name())));

            return new User(userEntity.getUsername(),userEntity.getPassword(),true,true,true,true,authorities);
        }else{
            throw  new UsernameNotFoundException("Usuario no encontrado");
        }

    }
}
