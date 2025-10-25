package com.shristi.taskify.controller;


import com.shristi.taskify.dto.TaskRequest;
import com.shristi.taskify.dto.TaskResponse;
import com.shristi.taskify.model.TaskStatus;
import com.shristi.taskify.service.declaration.ITaskService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final ITaskService taskService;

    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/project/{projectId}")
    public TaskResponse createTask(@PathVariable Long projectId,
                                   @RequestBody TaskRequest dto,
                                   Principal principal) {
        return taskService.createTask(projectId, dto, principal.getName());
    }

    @PutMapping("/{taskId}/status")
    public TaskResponse updateStatus(@PathVariable Long taskId,
                                        @RequestParam TaskStatus taskStatus,
                                        Principal principal) {
        return taskService.updateTask(taskId, taskStatus, principal.getName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/project/{projectId}")
    public List<TaskResponse> getTasks(@PathVariable Long projectId,
                                          @RequestParam(required = false) TaskStatus taskStatus) {
        return taskService.getTasksByProject(projectId, taskStatus);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{taskId}/assign/{userId}")
    public TaskResponse assignTaskToUser(@PathVariable Long taskId,
                                         @PathVariable Long userId,
                                         Principal principal) {
        return taskService.assignTaskToUser(taskId, userId, principal.getName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{taskId}")
    public String deleteTask(@PathVariable Long taskId,
                             @RequestParam(defaultValue = "true") boolean softDelete, Principal principal) {
        taskService.deleteTask(taskId, softDelete, principal.getName());
        return softDelete ? "Project deactivated successfully" : "Project deleted permanently";
    }
}

