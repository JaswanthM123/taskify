package com.shristi.taskify.service.impl;

import com.shristi.taskify.dto.TaskRequest;
import com.shristi.taskify.dto.TaskResponse;
import com.shristi.taskify.exception.*;
import com.shristi.taskify.model.*;
import com.shristi.taskify.repository.IProjectRepository;
import com.shristi.taskify.repository.ITaskRepository;
import com.shristi.taskify.repository.IUserRepository;
import com.shristi.taskify.service.declaration.ITaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements ITaskService {

    private final ITaskRepository taskRepository;
    private final IProjectRepository projectRepository;
    private final IUserRepository userRepository;

    public TaskServiceImpl(ITaskRepository taskRepository, IProjectRepository projectRepository, IUserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskResponse createTask(Long projectId, TaskRequest taskRequest, String adminName) {
        User admin = userRepository.findByUserName(adminName)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        if (!admin.getRole().name().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Only admins can create tasks");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = Task.builder()
                .taskTitle(taskRequest.getTaskTitle())
                .taskDescription(taskRequest.getTaskDescription())
                .taskDeadline(taskRequest.getTaskDeadline())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .updatedAt(LocalDateTime.now())
                .taskStatus(TaskStatus.TO_DO)
                .project(project)
                .build();
        if (taskRequest.getAssignedUserId()!= null) {
            User assignedUser = userRepository.findById(taskRequest.getAssignedUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedUser(assignedUser);
        }

        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }
    @Override
    public TaskResponse assignTaskToUser(Long taskId, Long userId, String adminName) {
        User admin = userRepository.findByUserName(adminName)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Only admin can assign tasks");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        task.setAssignedUser(user);
        return mapToResponse(taskRepository.save(task));
    }

    private TaskResponse mapToResponse(Task savedTask) {
        return TaskResponse.builder()
                .taskId(savedTask.getTaskId())
                .taskTitle(savedTask.getTaskTitle())
                .taskDescription(savedTask.getTaskDescription())
                .taskDeadline(savedTask.getTaskDeadline())
                .taskStatus(savedTask.getTaskStatus())
                .projectId(savedTask.getProject().getProjectId())
                .userId(savedTask.getAssignedUser().getUserId())
                .build();
    }

    @Override
    public TaskResponse updateTask(Long taskId, TaskStatus taskStatus, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!user.getRole().name().equals("ADMIN")) {
            if (task.getAssignedUser() == null || !task.getAssignedUser().getUserId().equals(user.getUserId())) {
                throw new UnauthorizedException("You are not authorized to update this task");
            }
        }
        task.setTaskStatus(taskStatus);
        task.setUpdatedAt(LocalDateTime.now());
        Task updated = taskRepository.save(task);

        return mapToResponse(updated);
    }

    @Override
    public List<TaskResponse> getTasksByProject(Long projectId, TaskStatus taskStatus) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        List<Task> tasks = (taskStatus != null)
                ? taskRepository.findByProjectAndTaskStatus(project, taskStatus)
                : taskRepository.findByProject(project);

        return tasks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
//        return null;
    }

    @Override
    public void deleteTask(Long taskId, boolean softDelete, String adminName) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (softDelete) {
            task.setActive(false);
            task.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(task);
        } else {
            taskRepository.delete(task);
        }
    }
}
