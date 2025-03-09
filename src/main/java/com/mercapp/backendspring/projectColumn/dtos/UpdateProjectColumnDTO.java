package com.mercapp.backendspring.projectColumn.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateProjectColumnDTO {
    private String name;
    private String description;
    private Integer priority;
}
