package com.mercapp.backendspring.projectUser;

import com.mercapp.backendspring.project.ProjectService;
import com.mercapp.backendspring.project.models.Project;
import com.mercapp.backendspring.projectUser.dtos.AddProjectUserToProjectDTO;
import com.mercapp.backendspring.projectUser.enums.Roles;
import com.mercapp.backendspring.projectUser.models.ProjectUser;
import com.mercapp.backendspring.projectUser.repositories.ProjectUserRepository;
import com.mercapp.backendspring.user.models.UserDetails;
import com.mercapp.backendspring.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectUserService {
    public static final List<Roles> WRITE_ROLES = List.of(Roles.CREATOR, Roles.ADMIN, Roles.MEMBER);
    public static final List<Roles> ADMIN_ROLES = List.of(Roles.CREATOR, Roles.ADMIN);

    @Autowired
    private ProjectUserRepository projectUserRepository;
    @Autowired
    @Lazy
    private ProjectService projectService;
    @Autowired
    @Lazy
    private UserRepository userRepository;

    @Cacheable(value = "project-user", key = "#id")
    public ProjectUser getById(Long id) {
        return this.projectUserRepository.findById(id).orElse(null);
    }


    @Cacheable(value = "project-users", key = "#userId")
    public List<ProjectUser> getAllProjectUsersByUserId(Long userId) {
        return this.projectUserRepository.findAllByUserId(userId);
    }

    public void deleteAllProjectUsersByUserId(Long userId) {
        List<ProjectUser> projectUsers = this.projectUserRepository.findAllByUserId(userId);

        if(projectUsers != null) {
            projectUsers.forEach(projectUser -> {
                this.projectUserRepository.deleteById(projectUser.getId());
            });
        }
    }

    @CacheEvict(value = "project-user", key = "#addProjectUserToProjectDTO.projectId + #addProjectUserToProjectDTO.userId")
    public ProjectUser addProjectUserToProject(AddProjectUserToProjectDTO addProjectUserToProjectDTO) {
        Project project = this.projectService.getById(addProjectUserToProjectDTO.getProjectId());
        UserDetails user = this.userRepository.findById(addProjectUserToProjectDTO.getUserId()).orElse(null);

        ProjectUser projectUser = ProjectUser.builder()
                .project(project)
                .user(user)
                .role(addProjectUserToProjectDTO.getRole())
                .build();

        return this.projectUserRepository.save(projectUser);
    }

    @CacheEvict(value = "project-user", key = "#id")
    public ProjectUser updateProjectUserRole(Long id, Roles role) {
        ProjectUser projectUser = this.getById(id);

        if (projectUser == null) {
            return null;
        }

        projectUser.setRole(role);

        return this.projectUserRepository.save(projectUser);
    }

    @Cacheable(value = "project-user-by-project-id-and-user-id", key = "#projectId + #userId")
    public ProjectUser getProjectUserByProjectIdAndUserId(Long projectId, Long userId) {
        return this.projectUserRepository.findByProjectIdAndUserId(projectId, userId);
    }

    @CacheEvict(value = "project-user", key = "#id")
    public ProjectUser deleteProjectUser(Long id) {
        ProjectUser projectUser = this.getById(id);

        if (projectUser == null) {
            return null;
        }

        this.projectUserRepository.deleteById(id);

        return projectUser;
    }
}
