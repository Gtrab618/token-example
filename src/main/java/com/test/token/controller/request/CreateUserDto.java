package com.test.token.controller.request;

import com.test.token.model.RoleEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {
        @Email
        @NotBlank
        private String email;
        @NotBlank
        private String username;
        @NotBlank
        private String password;
        private RoleEntity role;

}
