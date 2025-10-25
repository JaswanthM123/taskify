package com.shristi.taskify.service.impl;

import com.shristi.taskify.dto.ProjectRequest;
import com.shristi.taskify.dto.ProjectResponse;
import com.shristi.taskify.exception.AdminNotFoundException;
import com.shristi.taskify.exception.UserNotFoundException;
import com.shristi.taskify.model.Project;
import com.shristi.taskify.model.User;
import com.shristi.taskify.repository.IAuthRepository;
import com.shristi.taskify.repository.IProjectRepository;
import com.shristi.taskify.repository.IUserRepository;
import com.shristi.taskify.service.declaration.IProjectService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ProjectServiceImpl implements IProjectService {
    private final IUserRepository userRepository;
    private final IProjectRepository projectRepository;

    public ProjectServiceImpl(IUserRepository userRepository, IProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectResponse createProject(ProjectRequest projectRequest, String adminName) {
        User admin = userRepository.findByUserName(adminName)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        Project project = Project.builder()
                .projectTitle(projectRequest.getProjectTitle())
                .projectDescription(projectRequest.getProjectDescription())
                .user(admin)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .build();
        return mapToDto(projectRepository.save(project));
    }

    @Override
    public List<ProjectResponse> getProjectsForUser(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole().name().equals("ROLE_ADMIN")) {
            return projectRepository.findAll()
                    .stream()
                    .filter(Project::isActive)
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        }

        // For now, normal users see all active projects (until assignment feature)
        return projectRepository.findAll()
                .stream()
                .filter(Project::isActive)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponse getProjectById(Long projectId, String userName) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new UserNotFoundException("Project not found"));

        return mapToDto(project);
    }

    @Override
    public ProjectResponse updateProject(Long projectId, ProjectRequest projectRequest, String adminName) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new UserNotFoundException("Project not found"));

        project.setProjectTitle(projectRequest.getProjectTitle());
        project.setProjectDescription(projectRequest.getProjectDescription());
        project.setUpdatedAt(LocalDateTime.now());


        return mapToDto(projectRepository.save(project));
    }

    @Override
    public void deleteProject(Long projectId, String adminName, boolean softDelete) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new UserNotFoundException("Project not found"));

        if (softDelete) {
            project.setActive(false);
            project.setUpdatedAt(LocalDateTime.now());
            projectRepository.save(project);
        } else {
            projectRepository.delete(project);
        }
    }
    private ProjectResponse mapToDto(Project project) {
        return ProjectResponse.builder()
                .projectId(project.getProjectId())
                .projectTitle(project.getProjectTitle())
                .projectDescription(project.getProjectDescription())
                .isActive(project.isActive())
                .userId(project.getUser().getUserId())
                .userName(project.getUser().getUserName())
                .build();
    }
}
