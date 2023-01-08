package com.svalero.workhub.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.workhub.domain.Reserve;

import java.util.List;

@Dao
public interface ReserveDAO {
    @Query("SELECT * FROM reserve")
    List<Reserve> getAll();

    @Query("SELECT * FROM reserve WHERE id = :id")
    Reserve getReserveById(long id);

    @Query("SELECT * FROM reserve WHERE id_user = :idUser")
    List<Reserve> getReserveByUser(long idUser);

    @Insert
    void insert(Reserve reserve);

    @Delete
    void delete(Reserve reserve);

    @Update
    void update(Reserve reserve);
}
