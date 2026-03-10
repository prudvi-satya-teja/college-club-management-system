package com.project.aclub.controller;

import com.project.aclub.dto.user.CreateUserRequest;
import com.project.aclub.dto.user.UpdateUserRequest;
import com.project.aclub.dto.user.UserResponse;
import com.project.aclub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody CreateUserRequest userRequest) {
        UserResponse userResponse = userService.registerUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam(name = "name", required = false) String name,
                                                          @RequestParam(name = "rollNumber", required = false)
                                                          String rollNumber,
                                                          @RequestParam(name = "phoneNumber", required = false)
                                                          String phoneNumber,
                                                          @RequestParam(name = "email", required = false) String email,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "user_id") String sortBy,
                                                          @RequestParam(defaultValue = "asc") String sortDir
    ) {
        List<UserResponse> users = userService.searchUsers(name, rollNumber, phoneNumber, email, page, size, sortBy
                , sortDir);
        return ResponseEntity.ok().body(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable Long id,
                                                       @Valid @RequestBody UpdateUserRequest userRequest) {
        UserResponse user = userService.updateUserById(id, userRequest);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}



