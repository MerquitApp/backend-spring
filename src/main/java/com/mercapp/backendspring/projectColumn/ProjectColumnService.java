package com.mercapp.backendspring.projectColumn;

import com.mercapp.backendspring.project.ProjectService;
import com.mercapp.backendspring.project.models.Project;
import com.mercapp.backendspring.projectColumn.dtos.CreateProjectColumnDTO;
import com.mercapp.backendspring.projectColumn.dtos.UpdateProjectColumnDTO;
import com.mercapp.backendspring.projectColumn.models.ProjectColumn;
import com.mercapp.backendspring.projectColumn.repositories.ProjectColumnRepository;
import com.mercapp.backendspring.projectUser.ProjectUserService;
import com.mercapp.backendspring.projectUser.enums.Roles;
import com.mercapp.backendspring.task.dtos.CreateTaskDTO;
import com.mercapp.backendspring.task.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectColumnService {
    @Autowired
    private ProjectColumnRepository projectColumnRepository;
    @Autowired
    private ProjectService projectService;

    @CacheEvict(value = "project-column", key = "#projectId + #result.id")
    public ProjectColumn create(CreateProjectColumnDTO createProjectColumnDTO, Long projectId, Long userId) {
        Project project = this.projectService.getById(projectId);
        Roles role = this.projectService.getProjectUserRole(projectId, userId);

        if (ProjectUserService.WRITE_ROLES.contains(role)) {
            ProjectColumn projectColumn = ProjectColumn.builder()
                    .name(createProjectColumnDTO.getName())
                    .project(project)
                    .description(createProjectColumnDTO.getDescription())
                    .priority(createProjectColumnDTO.getPriority())
                    .tasks(new ArrayList<>())
                    .build();

            return this.projectColumnRepository.save(projectColumn);
        }

        return null;
    }

    @CacheEvict(value = "project-column", key = "#projectId + #columnId")
    public ProjectColumn update(Long projectId, Long columnId, Long userId, UpdateProjectColumnDTO updateProjectColumnDTO) {
        Roles role = this.projectService.getProjectUserRole(projectId, userId);

        if (ProjectUserService.ADMIN_ROLES.contains(role)) {
            ProjectColumn projectColumn = this.projectColumnRepository.findById(columnId).orElse(null);

            if (updateProjectColumnDTO.getName() != null) {
                projectColumn.setName(updateProjectColumnDTO.getName());
            }

            if (updateProjectColumnDTO.getDescription() != null) {
                projectColumn.setDescription(updateProjectColumnDTO.getDescription());
            }

            if (updateProjectColumnDTO.getPriority() != null) {
                projectColumn.setPriority(updateProjectColumnDTO.getPriority());
            }

            return this.projectColumnRepository.save(projectColumn);
        }

        return null;
    }

    @Cacheable(value = "project-column-tasks", key = "#projectId + #columnId")
    public List<Task> getProjectColumnTasks(Long projectId, Long columnId, Long userId) {
        Roles role = this.projectService.getProjectUserRole(projectId, userId);
        ProjectColumn projectColumn = this.projectColumnRepository.findById(columnId).orElse(null);

        if (role != null && projectColumn != null) {
            return projectColumn.getTasks();
        }

        return null;
    }

    @CacheEvict(value = "project-column", key = "#projectId + #columnId")
    public ProjectColumn delete(Long projectId, Long columnId, Long userId) {
        Roles role = this.projectService.getProjectUserRole(projectId, userId);

        if (ProjectUserService.ADMIN_ROLES.contains(role)) {
            ProjectColumn projectColumn = this.projectColumnRepository.findById(columnId).orElse(null);
            this.projectColumnRepository.deleteById(columnId);
            return projectColumn;
        }

        return null;
    }

    @Cacheable(value = "project-column", key = "#projectId + #columnId")
    public ProjectColumn getById(Long projectId, Long columnId, Long userId) {
        Roles role = this.projectService.getProjectUserRole(projectId, userId);

        if (role != null) {
            return this.projectColumnRepository.findById(columnId).orElse(null);
        }

        return null;
    }

    @CacheEvict(value = "project-column", key = "#projectId + #columnId")
    public ProjectColumn addTaskToColumn(Long projectId, Long columnId, Long userId, CreateTaskDTO createTaskDTO) {
        Roles role = this.projectService.getProjectUserRole(projectId, userId);

        if(ProjectUserService.WRITE_ROLES.contains(role)) {
            ProjectColumn projectColumn = this.getById(projectId, columnId, userId);
            Task task = Task.builder()
                    .title(createTaskDTO.getTitle())
                    .description(createTaskDTO.getDescription())
                    .priority(createTaskDTO.getPriority())
                    .project_column(projectColumn)
                    .build();
            projectColumn.getTasks().add(task);
            return this.projectColumnRepository.save(projectColumn);
        }

        return null;
    }
}
