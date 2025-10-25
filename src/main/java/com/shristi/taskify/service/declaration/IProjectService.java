package com.shristi.taskify.service.declaration;

import com.shristi.taskify.dto.ProjectRequest;
import com.shristi.taskify.dto.ProjectResponse;
import java.util.List;

public interface IProjectService {
    ProjectResponse createProject(ProjectRequest projectRequest, String adminName);
    List<ProjectResponse> getProjectsForUser(String userName);
    ProjectResponse getProjectById(Long projectId, String userName);
    ProjectResponse updateProject(Long projectId, ProjectRequest projectRequest, String adminName);
    void deleteProject(Long projectId, String adminName, boolean softDelete);
}
