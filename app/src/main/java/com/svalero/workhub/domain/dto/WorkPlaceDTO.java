package com.svalero.workhub.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkPlaceDTO {
    private String name;
    private String description;
    private String phoneNumber;
    private String city;
    private String address;
    private String schedule;
}
