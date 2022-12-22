package com.svalero.workhub.domain;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(foreignKeys = {
        @ForeignKey(entity = WorkPlace.class, parentColumns = "id", childColumns = "workplace", onDelete = CASCADE)
})
public class Space {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;
    private String services;
    private long workplace;
}
