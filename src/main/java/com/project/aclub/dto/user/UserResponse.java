package com.project.aclub.dto.user;

import com.project.aclub.entity.User;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String rollNumber;
    private String phoneNumber;

    public static UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getUserId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRollNumber(user.getRollNumber());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        return userResponse;
    }
}
