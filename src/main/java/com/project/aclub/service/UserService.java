package com.project.aclub.service;

import com.project.aclub.dto.user.CreateUserRequest;
import com.project.aclub.dto.user.UpdateUserRequest;
import com.project.aclub.dto.user.UserResponse;
import com.project.aclub.entity.User;
import com.project.aclub.exception.ConflictException;
import com.project.aclub.exception.ResourceNotFoundException;
import com.project.aclub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        email = email.toLowerCase();

        String finalEmail = email;
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found with email :" + finalEmail));
    }

    public UserResponse registerUser(CreateUserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail().toLowerCase())) {
            throw new ConflictException("User already exist with this email");
        }

        User user = userRequest.toEntity();
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return UserResponse.toResponse(user);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with this id : " + id));

        return UserResponse.toResponse(user);
    }

    public List<UserResponse> searchUsers(String name, String rollNumber, String phoneNumber,
                                          String email, int page, int size, String sortBy, String sortDir) {
        Sort sort;
        if (sortDir.equals("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        List<User> users = userRepository.searchUsers(name, rollNumber, phoneNumber, email, pageable);

        return users.stream()
                .map(user -> UserResponse.toResponse(user))
                .toList();
    }

    public UserResponse updateUserById(Long id, UpdateUserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with this id : " + id));

        user.setName(userRequest.getName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        User updatedUser = userRepository.save(user);

        return UserResponse.toResponse(updatedUser);
    }

    public void deleteUserById(Long id) {
        userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with this id : " + id));
        userRepository.deleteById(id);
    }
}

