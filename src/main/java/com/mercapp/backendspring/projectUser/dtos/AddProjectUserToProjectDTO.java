package com.mercapp.backendspring.projectUser.dtos;

import com.mercapp.backendspring.projectUser.enums.Roles;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddProjectUserToProjectDTO {
    public Long projectId;
    public Long userId;
    public Roles role = Roles.MEMBER;
}
