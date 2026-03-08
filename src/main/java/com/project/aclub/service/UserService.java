package com.project.aclub.service;

import com.project.aclub.entity.User;
import com.project.aclub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        if (userRepository.findByEmail(user.getEmail().toLowerCase()).isPresent()) {
            throw new RuntimeException("User already found with that email");
        }
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        email = email.toLowerCase();
        String finalEmail = email;
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found with email" + finalEmail));
    }
}

