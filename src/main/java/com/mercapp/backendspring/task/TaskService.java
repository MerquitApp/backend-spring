package com.mercapp.backendspring.task;

import com.mercapp.backendspring.project.ProjectService;
import com.mercapp.backendspring.projectUser.ProjectUserService;
import com.mercapp.backendspring.projectUser.enums.Roles;
import com.mercapp.backendspring.task.dtos.UpdateTaskDTO;
import com.mercapp.backendspring.task.models.Task;
import com.mercapp.backendspring.task.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectService projectService;

    @Cacheable(value = "task", key = "#id + #projectId + #userId")
    public Task getById(Long id, Long projectId, Long userId) {
        boolean isProjectUser = this.projectService.isProjectUser(projectId, userId);

        if (!isProjectUser) {
            return null;
        }

        return this.taskRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = "task", key = "#id + #projectId + #userId")
    public Task updateById(Long id, Long projectId, UpdateTaskDTO updateTaskDTO, Long userId) {
        boolean isProjectUser = this.projectService.isProjectUser(projectId, userId);

        if (!isProjectUser) {
            return null;
        }

        Task task = this.taskRepository.findById(id).orElse(null);

        if(updateTaskDTO.getTitle() != null) {
            task.setTitle(updateTaskDTO.getTitle());
        }

        if(updateTaskDTO.getDescription() != null) {
            task.setDescription(updateTaskDTO.getDescription());
        }

        if(updateTaskDTO.getPriority() != null) {
            task.setPriority(updateTaskDTO.getPriority());
        }

        return this.taskRepository.save(task);
    }

    @CacheEvict(value = "task", key = "#id + #projectId + #userId")
    public Task deleteById(Long id, Long projectId, Long userId) {
        Roles role = this.projectService.getProjectUserRole(projectId, userId);

        if(ProjectUserService.WRITE_ROLES.contains(role)) {
            Task task = this.taskRepository.findById(id).orElse(null);
            this.taskRepository.deleteById(id);

            return task;
        }

        return null;
    }
}
