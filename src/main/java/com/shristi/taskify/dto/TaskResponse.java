package com.shristi.taskify.dto;

import com.shristi.taskify.model.TaskStatus;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    private Long taskId;
    private String taskTitle;
    private String taskDescription;
    private LocalDate taskDeadline;
    private TaskStatus taskStatus;
    private Long projectId;
    private Long userId;
}
