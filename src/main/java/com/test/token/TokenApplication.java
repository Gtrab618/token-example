package com.test.token;

import com.test.token.model.ERole;
import com.test.token.model.RoleEntity;
import com.test.token.model.UserEntity;
import com.test.token.repository.RoleRepository;
import com.test.token.repository.UserRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class TokenApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokenApplication.class, args);
	}

	//temporal para ingresar ususario al iniciar el servidor
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;
	@Bean
	CommandLineRunner init(){
		return  args -> {
			RoleEntity eRole= new RoleEntity(1L, ERole.ADMIN);
			UserEntity userEntity= UserEntity.builder()
					.email("test@gmail.com")
					.username("admin")
					.password(passwordEncoder.encode("1234"))
					.role(eRole).build();

			RoleEntity eRole2= new RoleEntity(2L, ERole.USER);
			UserEntity userEntity2= UserEntity.builder()
					.email("test@gmail.com")
					.username("user")
					.password(passwordEncoder.encode("1234"))
					.role(eRole2).build();


			RoleEntity eRole3= new RoleEntity(3L, ERole.INVITED);
			UserEntity userEntity3= UserEntity.builder()
					.email("test@gmail.com")
					.username("invited")
					.password(passwordEncoder.encode("1234"))
					.role(eRole3).build();
			roleRepository.save(eRole);
			roleRepository.save(eRole2);
			roleRepository.save(eRole3);
			userRepository.save(userEntity);
			userRepository.save(userEntity2);
			userRepository.save(userEntity3);
		};
	}

}
