package com.mercapp.backendspring.project;

import com.mercapp.backendspring.project.dtos.CreateProjectDTO;
import com.mercapp.backendspring.project.dtos.UpdateProjectDTO;
import com.mercapp.backendspring.project.dtos.UpdateRoleDTO;
import com.mercapp.backendspring.project.models.Project;
import com.mercapp.backendspring.projectUser.models.ProjectUser;
import com.mercapp.backendspring.user.models.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping()
    public ResponseEntity<Iterable<Project>> getAll() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Project> projects = this.projectService.getAll(user.getId());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(@RequestParam("id") Long id) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project project = this.projectService.getById(id, user.getId());

        if (project == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(project);
    }

    @PostMapping()
    public ResponseEntity<Project> create(@RequestBody CreateProjectDTO createProjectDTO) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project project = this.projectService.create(createProjectDTO, user);
        return ResponseEntity.ok(project);
    }

    @PatchMapping("/share/{id}")
    public ResponseEntity<Project> share(@PathVariable("id") Long id) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project project = this.projectService.shareProject(id, user.getId());

        if (project == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(project);
    }

    @PatchMapping("/unshare/{id}")
    public ResponseEntity<Project> unshare(@PathVariable("id") Long id) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project project = this.projectService.unshareProject(id, user.getId());

        if (project == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(project);
    }

    @PatchMapping("/leave/{id}")
    public ResponseEntity leave(@PathVariable("id") Long id) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.projectService.leaveProject(id, user.getId());
        return ResponseEntity.ok("Proyecto Abandonado");
    }

    @PatchMapping("/update-role/{id}/{projectUserId}")
    public ResponseEntity<ProjectUser> updateRole(@RequestBody UpdateRoleDTO updateRoleDTO, @PathVariable("id") Long id, @PathVariable("projectUserId") Long projectUserId) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        updateRoleDTO.setUserId(projectUserId);
        ProjectUser projectUser = this.projectService.updateProjectUserRole(id, user.getId(), updateRoleDTO);

        if (projectUser == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(projectUser);
    }

    @PatchMapping("/kick/{id}/{projectUserId}")
    public ResponseEntity<ProjectUser> kick(@PathVariable("id") Long id, @PathVariable("projectUserId") Long projectUserId) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectUser projectUser = this.projectService.kickProjectUser(id, user.getId(), projectUserId);

        if (projectUser == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(projectUser);
    }

    @PostMapping("/join/{code}")
    public ResponseEntity<Project> join(@PathVariable("code") String code) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project project = this.projectService.joinProject(code, user.getId());

        if (project == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(project);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable("id") Long id, @RequestBody UpdateProjectDTO updateProjectDTO) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project project = this.projectService.updateProject(id, user.getId(), updateProjectDTO);

        if (project == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteById(@PathVariable("id") Long id) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project project = this.projectService.deleteProject(id, user.getId());

        if (project == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(project);
    }
}
