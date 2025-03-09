package com.mercapp.backendspring.projectColumn.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateProjectColumnDTO {
    private String name;
    private String description;
    private Integer priority;
}
