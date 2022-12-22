package com.svalero.workhub.domain;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.svalero.workhub.util.DateConverter;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "id_user", onDelete = CASCADE),
        @ForeignKey(entity = Space.class, parentColumns = "id", childColumns = "id_space", onDelete = CASCADE)
})
public class Reserve {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long id_user;
    private long id_space;
    @TypeConverters(DateConverter.class)
    private Date date;
}
