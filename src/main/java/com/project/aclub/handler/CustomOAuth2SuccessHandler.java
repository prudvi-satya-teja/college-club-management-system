package com.project.aclub.handler;

import com.project.aclub.Enum.Role;
import com.project.aclub.entity.User;
import com.project.aclub.repository.UserRepository;
import com.project.aclub.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.jwt.access-token-expiry}")
    private int accessTokenExpiry;

    @Value("${app.jwt.refresh-token-expiry}")
    private int refreshTokenExpiry;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    @Value("${app.cookie.refresh-path}")
    private String cookieRefreshPath;

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomOAuth2SuccessHandler(UserRepository userRepository,
                                      JwtUtil jwtUtil,
                                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;

            String email = authToken.getPrincipal().getAttribute("email");
            String name = authToken.getPrincipal().getAttribute("name");
            String preferredUsername = authToken.getPrincipal().getAttribute("preferred_username");

            if (email == null) {
                response.sendRedirect(frontendUrl + "/login?error=email_not_found");
                return;
            }

            String rollNumber = preferredUsername != null
                    ? preferredUsername.split("@")[0]
                    : email.split("@")[0];

            User user = userRepository.findByEmail(email.toLowerCase()).orElse(null);
            if (user == null) {
                user = new User();
                user.setEmail(email.toLowerCase());
                user.setName(name != null ? name : email);
                user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                user.setRole(Role.USER);
                user.setRollNumber(rollNumber.toLowerCase());
                user = userRepository.save(user);
            }

            String accessToken = jwtUtil.generateToken(user.getEmail().toLowerCase(), accessTokenExpiry);

            Cookie refreshTokenCookie = new Cookie("refreshToken", jwtUtil.generateToken(user.getEmail(), refreshTokenExpiry));
            refreshTokenCookie.setPath(cookieRefreshPath);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(cookieSecure);
            response.addCookie(refreshTokenCookie);
            response.sendRedirect(frontendUrl + "/callback?token=" + accessToken);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(frontendUrl + "/login?error=oauth_failed");
        }
    }
}
