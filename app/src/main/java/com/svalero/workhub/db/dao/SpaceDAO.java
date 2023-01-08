package com.svalero.workhub.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.workhub.domain.Space;

import java.util.List;

@Dao
public interface SpaceDAO {

    @Query("SELECT * FROM space")
    List<Space> getAll();

    @Query("SELECT * FROM space WHERE id = :id")
    Space getSpaceById(long id);

    @Query("SELECT * FROM space WHERE workplace = :idWorkplace")
    List<Space> getSpacesByWorklace(long idWorkplace);

    @Insert
    void insert(Space space);

    @Delete
    void delete(Space space);

    @Update
    void update(Space space);
}
