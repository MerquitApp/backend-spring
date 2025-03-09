package com.mercapp.backendspring.task;


import com.mercapp.backendspring.task.dtos.UpdateTaskDTO;
import com.mercapp.backendspring.task.models.Task;
import com.mercapp.backendspring.user.models.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/{projectId}/{taskId}")
    public ResponseEntity<Task> getById(@PathVariable("taskId") Long taskId, @PathVariable("projectId") Long projectId) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = this.taskService.getById(taskId, projectId, user.getId());

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{projectId}/{taskId}")
    public ResponseEntity<Task> update(@PathVariable("taskId") Long taskId, @PathVariable("projectId") Long projectId, @RequestBody UpdateTaskDTO updateTaskDTO) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = this.taskService.updateById(taskId, projectId, updateTaskDTO, user.getId());

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{projectId}/{taskId}")
    public ResponseEntity<Task> deleteById(@PathVariable("taskId") Long taskId, @PathVariable("projectId") Long projectId) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = this.taskService.deleteById(taskId, projectId, user.getId());

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }
}
