package com.svalero.workhub.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpaceDTO {
    private String name;
    private String description;
    private String services;
    private long workplace;
}
