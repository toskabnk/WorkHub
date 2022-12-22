package com.svalero.workhub.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkPlace {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;
    private String city;
    private String address;
    private String schedule;
}
