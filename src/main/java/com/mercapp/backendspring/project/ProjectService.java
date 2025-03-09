package com.mercapp.backendspring.project;

import com.mercapp.backendspring.project.dtos.CreateProjectDTO;
import com.mercapp.backendspring.project.dtos.UpdateProjectDTO;
import com.mercapp.backendspring.project.dtos.UpdateRoleDTO;
import com.mercapp.backendspring.project.models.Project;
import com.mercapp.backendspring.project.repositories.ProjectRepository;
import com.mercapp.backendspring.projectUser.ProjectUserService;
import com.mercapp.backendspring.projectUser.dtos.AddProjectUserToProjectDTO;
import com.mercapp.backendspring.projectUser.enums.Roles;
import com.mercapp.backendspring.projectUser.models.ProjectUser;
import com.mercapp.backendspring.user.models.UserDetails;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    @Lazy
    private ProjectUserService projectUserService;

    private final Integer SHARE_CODE_LENGTH = 6;

    public Project create(CreateProjectDTO createProjectDTO, UserDetails user) {
        Project project = Project.builder()
                .name(createProjectDTO.getName())
                .description(createProjectDTO.getDescription())
                .project_users(new ArrayList<>())
                .build();

        project.getProject_users().add(ProjectUser.builder()
                .user(user)
                .project(project)
                .role(Roles.CREATOR)
                .build());

        return this.projectRepository.save(project);
    }

    @Cacheable(value = "projects", key = "#userId")
    public List<Project> getAll(Long userId) {
        return this.projectRepository.findByUserId(userId);
    }

    @Cacheable(value = "project", key = "#id")
    public Project getById(Long id) {
        return this.projectRepository.findById(id).orElse(null);
    }

    @Cacheable(value = "project", key = "#id + #userId")
    public Project getById(Long id, Long userId) {
        boolean isProjectUser = this.isProjectUser(id, userId);

        if (!isProjectUser) {
            return null;
        }

        return this.projectRepository.findById(id).orElse(null);
    }

    public boolean isProjectUser(Long projectId, Long userId) {
        return this.projectRepository.findById(projectId).orElse(null).getProject_users().stream()
                .anyMatch(projectUser -> projectUser.getUser().getId().equals(userId));
    }

    @Cacheable(value = "project-user-role", key = "#projectId + #userId")
    public Roles getProjectUserRole(Long projectId, Long userId) {
        Project project = this.projectRepository.findById(projectId).orElse(null);

        if (project == null) {
            return null;
        }

        return project.getProject_users().stream()
                .filter(projectUser -> projectUser.getUser().getId().equals(userId))
                .findFirst()
                .orElse(null)
                .getRole();
    }

    public Project shareProject(Long id, Long userId) {
        Roles role = this.getProjectUserRole(id, userId);

        if (ProjectUserService.WRITE_ROLES.contains(role)) {
            Project project = this.getById(id);
            String shareCode = this.generateShareCode();
            project.setShareCode(shareCode);
            this.projectRepository.save(project);
            return project;
        }

        return null;
    }

    public Project unshareProject(Long id, Long userId) {
        Roles role = this.getProjectUserRole(id, userId);

        if (ProjectUserService.WRITE_ROLES.contains(role)) {
            Project project = this.getById(id);
            project.setShareCode(null);
            return this.projectRepository.save(project);
        }

        return null;
    }

    public void leaveProject(Long id, Long userId) {
        ProjectUser projectUser = this.projectUserService.getProjectUserByProjectIdAndUserId(id, userId);

        if (projectUser != null) {
            this.projectUserService.deleteProjectUser(projectUser.getId());
        }
    }

    public ProjectUser kickProjectUser(Long id, Long userId, Long projectUserId) {
        ProjectUser projectUser = this.projectUserService.getProjectUserByProjectIdAndUserId(id, userId);

        if (ProjectUserService.ADMIN_ROLES.contains(projectUser.getRole())) {
            return this.projectUserService.deleteProjectUser(projectUser.getId());
        }

        return null;
    }

    @CacheEvict(value = "project", key = "#id")
    public Project deleteProject(Long id, Long userId) {
        Roles role = this.getProjectUserRole(id, userId);

        if (role == Roles.CREATOR) {
            Project project = this.getById(id);
            this.projectRepository.deleteById(id);
            return project;
        }

        return null;
    }

    public Project getByShareCode(String shareCode) {
        return this.projectRepository.findByShareCode(shareCode);
    }

    @CacheEvict(value = "project", key = "#id")
    public Project updateProject(Long id, Long userId, UpdateProjectDTO updateProjectDTO) {
        Roles role = this.getProjectUserRole(id, userId);

        if (ProjectUserService.WRITE_ROLES.contains(role)) {
            Project project = this.getById(id);

            if (updateProjectDTO.getName() != null) {
                project.setName(updateProjectDTO.getName());
            }

            if (updateProjectDTO.getDescription() != null) {
                project.setDescription(updateProjectDTO.getDescription());
            }

            return this.projectRepository.save(project);
        }

        return null;
    }

    public Project joinProject(String code, Long userId) {
        Project project = this.getByShareCode(code);

        if (project != null) {
            AddProjectUserToProjectDTO addProjectUserToProjectDTO = AddProjectUserToProjectDTO.builder()
                    .projectId(project.getId())
                    .userId(userId)
                    .role(Roles.MEMBER)
                    .build();
            this.projectUserService.addProjectUserToProject(addProjectUserToProjectDTO);
            return project;
        }

        return null;
    }

    public ProjectUser updateProjectUserRole(Long id, Long userId, UpdateRoleDTO updateRoleDTO) {
        ProjectUser projectUser = this.projectUserService.getProjectUserByProjectIdAndUserId(id, userId);

        if (ProjectUserService.ADMIN_ROLES.contains(projectUser.getRole())) {
            projectUser.setRole(updateRoleDTO.getRole());
            return this.projectUserService.updateProjectUserRole(projectUser.getId(), projectUser.getRole());
        }

        return null;
    }

    private String generateShareCode() {
        return RandomStringUtils.randomAlphanumeric(SHARE_CODE_LENGTH);
    }
}
