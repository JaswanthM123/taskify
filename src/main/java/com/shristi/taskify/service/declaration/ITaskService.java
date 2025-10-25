package com.shristi.taskify.service.declaration;

import com.shristi.taskify.dto.TaskRequest;
import com.shristi.taskify.dto.TaskResponse;
import com.shristi.taskify.model.TaskStatus;

import java.util.List;

public interface ITaskService {
    TaskResponse createTask(Long projectId, TaskRequest taskRequest, String adminName);
    TaskResponse assignTaskToUser(Long taskId, Long userId, String adminName);
    TaskResponse updateTask(Long taskId, TaskStatus taskStatus, String userName);
    List<TaskResponse> getTasksByProject(Long projectId, TaskStatus taskStatus);
    void deleteTask(Long taskId, boolean softDelete, String adminName);
}
