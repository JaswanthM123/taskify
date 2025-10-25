package com.shristi.taskify.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    private String taskTitle;
    private String taskDescription;
    private LocalDate taskDeadline;
    private Long assignedUserId;
}
