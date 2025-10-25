package com.shristi.taskify.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {
    private Long projectId;
    private String projectTitle;
    private String projectDescription;
    private boolean isActive;
    private Long userId;
    private String userName;
}
