package com.mercapp.backendspring.projectColumn.repositories;

import com.mercapp.backendspring.projectColumn.models.ProjectColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectColumnRepository extends JpaRepository<ProjectColumn, Long> {
}
