package com.mercapp.backendspring.task.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateTaskDTO {
    private String title;
    private String description;
    private Integer priority;
}
