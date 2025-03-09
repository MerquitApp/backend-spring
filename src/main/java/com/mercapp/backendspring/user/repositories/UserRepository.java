package com.mercapp.backendspring.user.repositories;

import com.mercapp.backendspring.user.models.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDetails, Long>  {
    UserDetails findByGithubId(String githubId);
    UserDetails findUserByUsername(String username);
}
