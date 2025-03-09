package com.mercapp.backendspring.user;

import com.mercapp.backendspring.projectUser.ProjectUserService;
import com.mercapp.backendspring.user.dtos.CreateOauthUserDTO;
import com.mercapp.backendspring.user.dtos.CreateUserDTO;
import com.mercapp.backendspring.user.dtos.UpdateUserDTO;
import com.mercapp.backendspring.user.models.UserDetails;
import com.mercapp.backendspring.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private ProjectUserService projectUserService;

    public UserDetails create(CreateUserDTO createUserDTO) {
        String encodedPassword = this.passwordEncoder.encode((createUserDTO.getPassword()));

        UserDetails user = UserDetails.builder()
                .username(createUserDTO.getUsername().toLowerCase())
                .password(encodedPassword)
                .build();

        return this.userRepository.save(user);
    }

    @CacheEvict(value = "user", key = "#id")
    public UserDetails update(Long id, UpdateUserDTO updateUserDTO) {
        UserDetails user = this.userRepository.findById(id).orElse(null);

        if (user == null) {
            return null;
        }

        if (updateUserDTO.getUsername() != null) {
            user.setUsername(updateUserDTO.getUsername());
        }

        return this.userRepository.save(user);
    }

    @Cacheable(value = "user-by-github-id", key = "#githubId")
    public UserDetails findByGithubId(String githubId) {
        return this.userRepository.findByGithubId(githubId);
    }

    @CacheEvict(value = "user", key = "#id")
    public void delete(Long id) {
        // Delete First all project users
        this.projectUserService.deleteAllProjectUsersByUserId(id);

        this.userRepository.deleteById(id);
    }

    public UserDetails findOrCreateOauthUser(CreateOauthUserDTO createOauthUserDTO) {
        UserDetails user = this.userRepository.findByGithubId(createOauthUserDTO.getGithubId());

        if (user == null) {
            return this.createOauth(createOauthUserDTO);
        }

        return user;
    }

    public UserDetails createOauth(CreateOauthUserDTO createOauthUserDTO) {
        UserDetails user = UserDetails.builder()
                .username(createOauthUserDTO.getUsername())
                .githubId(createOauthUserDTO.getGithubId())
                .build();

        return this.userRepository.save(user);
    }

    @Cacheable(value = "user-by-username", key = "#username")
    public UserDetails getByUsername(String username) {
        return this.userRepository.findUserByUsername(username);
    }

    @Cacheable(value = "user", key = "#id")
    public UserDetails getById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }
}
