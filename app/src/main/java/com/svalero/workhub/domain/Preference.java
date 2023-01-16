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
@Entity(indices = {@Index(value = {"id"}, unique = true)})
public class Preference {
    @PrimaryKey
    private long id;
    private String username;
    private String password;
    private boolean rememberMe;
    private boolean autoPlaceMarker;
    private boolean mapDetailCenterMe;
}
