package com.mercapp.backendspring.projectColumn.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercapp.backendspring.project.models.Project;
import com.mercapp.backendspring.task.models.Task;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProjectColumn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer priority;

    @OneToMany(mappedBy = "project_column", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @JsonIgnore
    @ManyToOne
    private Project project;
}
