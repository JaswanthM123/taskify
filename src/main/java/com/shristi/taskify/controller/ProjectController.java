package com.shristi.taskify.controller;


import com.shristi.taskify.dto.ProjectRequest;
import com.shristi.taskify.dto.ProjectResponse;
import com.shristi.taskify.service.declaration.IProjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final IProjectService projectService;

    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    // ✅ Only ADMIN can create project
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProjectResponse createProject(@RequestBody ProjectRequest projectRequest, Principal principal) {
        System.out.println("hii");
        System.out.println(principal.getName());
        return projectService.createProject(projectRequest, principal.getName());

    }

    // ✅ Both ADMIN and USER can view projects (different scopes)
    @GetMapping
    public List<ProjectResponse> getProjects(Principal principal) {
        return projectService.getProjectsForUser(principal.getName());
    }

    // ✅ Both can view project details
    @GetMapping("/{projectId}")
    public ProjectResponse getProjectById(@PathVariable Long projectId, Principal principal) {
        return projectService.getProjectById(projectId, principal.getName());
    }

    // ✅ Only ADMIN can update project
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{projectId}")
    public ProjectResponse updateProject(@PathVariable Long projectId,
                                            @RequestBody ProjectRequest projectRequest,
                                            Principal principal) {
        return projectService.updateProject(projectId,projectRequest, principal.getName());
    }

    // ✅ Only ADMIN can delete project
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{projectId}")
    public String deleteProject(@PathVariable Long projectId,
                                @RequestParam(defaultValue = "true") boolean softDelete,
                                Principal principal) {
        projectService.deleteProject(projectId, principal.getName(), softDelete);
        return softDelete ? "Project deactivated successfully" : "Project deleted permanently";
    }
}

