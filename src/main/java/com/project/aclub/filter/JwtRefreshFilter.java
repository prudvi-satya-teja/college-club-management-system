package com.project.aclub.filter;

import com.project.aclub.token.JwtAuthenticationToken;
import com.project.aclub.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtRefreshFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public JwtRefreshFilter(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!request.getServletPath().startsWith("/refresh-token")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = extractTokenFromCookie(request);

        if (refreshToken == null) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(refreshToken);
        Authentication authResult = authenticationManager.authenticate(jwtAuthenticationToken);

        if (authResult.isAuthenticated()) {
            String newRefreshToken = jwtUtil.generateToken(authResult.getName(), 15);
            response.setHeader("Authorization", "Bearer " + newRefreshToken);
        }
    }

    public String extractTokenFromCookie(HttpServletRequest request) {
        String refreshToken = null;
        for (Cookie cookie : request.getCookies()) {
            System.out.println("cookie name: " + cookie.getName());
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }
        return refreshToken;
    }
}
