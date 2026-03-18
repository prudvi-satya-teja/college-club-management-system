package com.project.aclub.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aclub.dto.auth.LoginRequest;
import com.project.aclub.entity.User;
import com.project.aclub.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final int ACCESS_TOKEN_TIME = 50;
    public static final int REFRESH_TOKEN_TIME = 150;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!request.getServletPath().equals("/generate-token")) {
            filterChain.doFilter(request, response);
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword());
        Authentication authResult = authenticationManager.authenticate(authentication);
        System.out.println("auth result " + authResult.toString());
        if (authResult.isAuthenticated()) {
            String username = loginRequest.getUsername().toLowerCase();
            User user = (User) authResult.getPrincipal();
            String accessToken = jwtUtil.generateToken(username, user.getUserId(), user.getRole(), ACCESS_TOKEN_TIME);
            response.addHeader("Authorization", "Bearer " + accessToken);

            String refreshToken = jwtUtil.generateToken(username, user.getUserId(), REFRESH_TOKEN_TIME);
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setPath("/refresh-token");
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(false);
            response.addCookie(refreshTokenCookie);
        }
    }
}
