package com.svalero.workhub.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.workhub.domain.WorkPlace;

import java.util.List;

@Dao
public interface WorkPlaceDAO {

    @Query("SELECT * FROM workplace")
    List<WorkPlace> getAll();

    @Query("SELECT * FROM workplace WHERE id = :id")
    WorkPlace getWorkPlaceById(long id);

    @Query("SELECT * FROM workplace WHERE city = :city")
    List<WorkPlace> getWorkplaceByCity(String city);

    @Insert
    void insert(WorkPlace workPlace);

    @Delete
    void delete(WorkPlace workPlace);

    @Update
    void update(WorkPlace workPlace);
}
