package com.shristi.taskify.repository;

import com.shristi.taskify.model.Project;
import com.shristi.taskify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IProjectRepository extends JpaRepository<Project,Long> {
    List<Project> findByUser(User user);
}
