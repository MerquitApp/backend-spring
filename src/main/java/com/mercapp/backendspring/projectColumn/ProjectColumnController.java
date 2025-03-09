package com.mercapp.backendspring.projectColumn;

import com.mercapp.backendspring.projectColumn.dtos.CreateProjectColumnDTO;
import com.mercapp.backendspring.projectColumn.dtos.UpdateProjectColumnDTO;
import com.mercapp.backendspring.projectColumn.models.ProjectColumn;
import com.mercapp.backendspring.task.dtos.CreateTaskDTO;
import com.mercapp.backendspring.task.models.Task;
import com.mercapp.backendspring.user.models.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/project-column")
public class ProjectColumnController {
    @Autowired
    private ProjectColumnService projectColumnService;

    @GetMapping("/{projectId}/{columnId}")
    public ResponseEntity<ProjectColumn> getById(@PathVariable("columnId") Long columnId, @PathVariable("projectId") Long projectId) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectColumn projectColumn = this.projectColumnService.getById(projectId, columnId, user.getId());

        if (projectColumn == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(projectColumn);
    }

    @GetMapping("/{projectId}/{columnId}/tasks")
    public ResponseEntity<List<Task>> getTasksById(@PathVariable("columnId") Long columnId, @PathVariable("projectId") Long projectId) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> tasks = this.projectColumnService.getProjectColumnTasks(projectId, columnId, user.getId());

        if (tasks == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{projectId}/{columnId}/tasks")
    public ResponseEntity<ProjectColumn> addTaskToColumn(@PathVariable("columnId") Long columnId, @PathVariable("projectId") Long projectId, @RequestBody CreateTaskDTO createTaskDTO) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectColumn projectColumn = this.projectColumnService.addTaskToColumn(projectId, columnId, user.getId(), createTaskDTO);

        if (projectColumn == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(projectColumn);
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<ProjectColumn> create(@RequestBody CreateProjectColumnDTO createProjectColumnDTO, @PathVariable("projectId") Long projectId) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectColumn projectColumn = this.projectColumnService.create(createProjectColumnDTO, projectId, user.getId());

        if (projectColumn == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(projectColumn);
    }

    @PatchMapping("/{projectId}/{columnId}")
    public ResponseEntity<ProjectColumn> update(@RequestBody UpdateProjectColumnDTO updateProjectColumnDTO, @PathVariable("columnId") Long columnId, @PathVariable("projectId") Long projectId) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectColumn projectColumn = this.projectColumnService.update(projectId, columnId, user.getId(), updateProjectColumnDTO);

        if (projectColumn == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(projectColumn);
    }

    @DeleteMapping("/{projectId}/{columnId}")
    public ResponseEntity<ProjectColumn> deleteById(@PathVariable("columnId") Long columnId, @PathVariable("projectId") Long projectId) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectColumn projectColumn = this.projectColumnService.delete(projectId, columnId, user.getId());

        if (projectColumn == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(projectColumn);
    }
}
