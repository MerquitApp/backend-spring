package com.mercapp.backendspring.project.repositories;

import com.mercapp.backendspring.project.models.Project;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByShareCode(String shareCode);

    @Query("SELECT p FROM Project AS p JOIN ProjectUser AS pu ON pu.project.id = p.id WHERE pu.user.id = :userId")
    List<Project> findByUserId(@Param("userId") Long userId);
}
