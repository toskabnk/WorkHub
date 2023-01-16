package com.svalero.workhub.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.workhub.domain.Preference;

@Dao
public interface PreferenceDAO {
    @Query("SELECT * FROM preference WHERE id=0")
    Preference getPreference();

    @Insert
    void insert(Preference preferences);

    @Update
    void update(Preference preferences);
}
