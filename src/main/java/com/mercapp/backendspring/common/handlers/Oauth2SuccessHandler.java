package com.mercapp.backendspring.common.handlers;

import com.mercapp.backendspring.auth.AuthService;
import com.mercapp.backendspring.common.services.JWTService;
import com.mercapp.backendspring.user.UserDetailsService;
import com.mercapp.backendspring.user.models.UserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String githubId = Integer.toString(oauthUser.getAttribute("id"));

            UserDetails user = this.userDetailsService.findByGithubId(githubId);
            String token = this.jwtService.generateToken(user.getId());

            this.authService.appendTokenToCookie(response, token);

            response.sendRedirect(this.frontendUrl);
        }
    }
}
