package com.mercapp.backendspring.auth;

import com.mercapp.backendspring.auth.dtos.LoginDTO;
import com.mercapp.backendspring.common.services.JWTService;
import com.mercapp.backendspring.user.UserDetailsService;
import com.mercapp.backendspring.user.dtos.CreateUserDTO;
import com.mercapp.backendspring.user.models.UserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetails getUserByJwt(String token) {
        boolean isValid = this.jwtService.isValid(token);

        if (!isValid) {
            return null;
        }

        Long userId = this.jwtService.getJwtId(token);
        return this.userDetailsService.getById(userId);
    }

    public UserDetails login(LoginDTO loginDTO) {
        UserDetails user = this.userDetailsService.getByUsername(loginDTO.getUsername());

        if (user == null) {
            return null;
        }

        boolean arePasswordsEqual = this.arePasswordsEqual(loginDTO.getPassword(), user.getPassword());

        if (!arePasswordsEqual) {
            return null;
        }

        return user;
    }

    public void appendTokenToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(AuthController.COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
        cookie.setAttribute("SameSite", "None");
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public UserDetails register(CreateUserDTO createUserDTO) {
        UserDetails user = this.userDetailsService.create(createUserDTO);
        return user;
    }

    private boolean arePasswordsEqual(String password1, String password2) {
        return this.passwordEncoder.matches(password1, password2);
    }
}
