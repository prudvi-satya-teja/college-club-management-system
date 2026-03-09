package com.project.aclub.dto.user;

import com.project.aclub.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String rollNumber;

    public User toEntity() {
        User user = new User();
        user.setEmail(this.getEmail());
        user.setRollNumber(this.getRollNumber());
        user.setName(this.getName());
        user.setPassword(this.getPassword());
        user.setPhoneNumber(this.getPhoneNumber());
        return user;
    }
}
