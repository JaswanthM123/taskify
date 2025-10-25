package com.shristi.taskify.repository;

import com.shristi.taskify.model.Task;
import com.shristi.taskify.model.Project;
import com.shristi.taskify.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
    List<Task> findByProjectAndTaskStatus(Project project, TaskStatus taskStatus);
}
