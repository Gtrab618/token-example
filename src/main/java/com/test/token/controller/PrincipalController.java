package com.test.token.controller;

import com.test.token.controller.request.CreateUserDto;
import com.test.token.model.ERole;
import com.test.token.model.RoleEntity;
import com.test.token.model.UserEntity;
import com.test.token.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class PrincipalController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repoUser;

    @GetMapping("/hello")
    public String hello(){
        return "hello world no Secured";
    }

    @GetMapping("/helloSecured")
    public String helloSecured(){
        return "hello world yes Secured";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?>createUser(@Valid @RequestBody CreateUserDto userDto){
        RoleEntity rol = userDto.getRole();

        UserEntity user= UserEntity.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .role(rol).build();

        repoUser.save(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/deleteUser")
    private String deleteUser(@RequestParam String id){
        repoUser.deleteById(Long.parseLong(id));
        return "se a borrado usuario".concat(id);
    }
}
