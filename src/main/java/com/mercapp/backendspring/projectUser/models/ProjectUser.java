package com.mercapp.backendspring.projectUser.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercapp.backendspring.project.models.Project;
import com.mercapp.backendspring.projectUser.enums.Roles;
import com.mercapp.backendspring.user.models.UserDetails;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProjectUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Roles role = Roles.MEMBER;

    private String name;

    @JsonIgnore
    @ManyToOne
    private Project project;

    @ManyToOne
    private UserDetails user;
}
