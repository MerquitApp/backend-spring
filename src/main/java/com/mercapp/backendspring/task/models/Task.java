package com.mercapp.backendspring.task.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercapp.backendspring.projectColumn.models.ProjectColumn;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Integer priority;

    @JsonIgnore
    @ManyToOne
    private ProjectColumn project_column;
}
