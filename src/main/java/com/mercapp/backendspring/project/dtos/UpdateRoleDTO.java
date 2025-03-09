package com.mercapp.backendspring.project.dtos;

import com.mercapp.backendspring.projectUser.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateRoleDTO {
    public Long userId;
    public Roles role;
}
