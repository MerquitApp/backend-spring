package com.mercapp.backendspring.auth;

import com.mercapp.backendspring.auth.dtos.LoginDTO;
import com.mercapp.backendspring.common.services.JWTService;
import com.mercapp.backendspring.user.dtos.CreateUserDTO;
import com.mercapp.backendspring.user.models.UserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public static final String COOKIE_NAME = "auth";
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDetails> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        UserDetails user = this.authService.login(loginDTO);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        String token = this.jwtService.generateToken(user.getId());

        this.authService.appendTokenToCookie(response, token);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody CreateUserDTO createUserDTO) {
        this.authService.register(createUserDTO);
        return ResponseEntity.ok("");
    }

    @GetMapping("/login/github")
    public String loginWithGithub(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/oauth2/authorization/github"); // Redirect to the URL that Spring Security expects
        return null;
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }
}
