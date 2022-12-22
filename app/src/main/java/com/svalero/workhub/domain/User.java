package com.svalero.workhub.domain;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(indices = {@Index(value = {"username"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private Boolean admin;
}
