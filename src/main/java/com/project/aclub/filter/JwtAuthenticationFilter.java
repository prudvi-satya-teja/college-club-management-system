package com.project.aclub.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aclub.dto.auth.LoginRequest;
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
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getMethod().equals("OPTIONS") ||
                !request.getServletPath().equals("/generate-token");
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

        if (authResult.isAuthenticated()) {
            String username = loginRequest.getUsername().toLowerCase();
            String accessToken = jwtUtil.generateToken(username, 5);
            response.addHeader("Authorization", "Bearer " + accessToken);

            String refreshToken = jwtUtil.generateToken(username, 15);
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setPath("/refresh-token");
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(false);
            response.addCookie(refreshTokenCookie);
        }
    }
}
