package com.mercapp.backendspring.auth.config;

import com.mercapp.backendspring.common.filters.JWTAuthorizationFilter;
import com.mercapp.backendspring.common.handlers.Oauth2SuccessHandler;
import com.mercapp.backendspring.common.services.CustomOauth2UserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final List<AntPathRequestMatcher> excludedUrls = List.of(
            new AntPathRequestMatcher("/auth/login/**"),
            new AntPathRequestMatcher("/auth/register/**"),
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/api-docs/**")
    );

    @Autowired
    private CustomOauth2UserDetailsService customOauth2UserService;

    @Autowired
    private Oauth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests.requestMatchers(this.excludedUrls.toArray(AntPathRequestMatcher[]::new)).permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // We must permit OPTIONS requests for CORS
                                .anyRequest().authenticated())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(this.oAuth2SuccessHandler)
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(this.customOauth2UserService)
                        ))
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }))
                .build();
    }

    @Bean
    public JWTAuthorizationFilter jwtAuthorizationFilter() {
        return new JWTAuthorizationFilter(this.excludedUrls);
    }
}
