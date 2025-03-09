package com.mercapp.backendspring.projectUser.repositories;

import com.mercapp.backendspring.projectUser.models.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    ProjectUser findByProjectIdAndUserId(Long projectId, Long userId);
    List<ProjectUser> findAllByUserId(Long userId);
}
