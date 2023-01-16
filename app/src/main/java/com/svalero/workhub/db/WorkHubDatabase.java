package com.svalero.workhub.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.svalero.workhub.db.dao.PreferenceDAO;
import com.svalero.workhub.db.dao.ReserveDAO;
import com.svalero.workhub.db.dao.SpaceDAO;
import com.svalero.workhub.db.dao.UserDAO;
import com.svalero.workhub.db.dao.WorkPlaceDAO;
import com.svalero.workhub.domain.Preference;
import com.svalero.workhub.domain.Reserve;
import com.svalero.workhub.domain.Space;
import com.svalero.workhub.domain.User;
import com.svalero.workhub.domain.WorkPlace;
import com.svalero.workhub.util.DateConverter;

@Database(entities = {User.class, WorkPlace.class, Space.class, Reserve.class, Preference.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class WorkHubDatabase extends RoomDatabase {
    public abstract UserDAO getUserDAO();
    public abstract WorkPlaceDAO getWorkPlaceDAO();
    public abstract SpaceDAO getSpaceDAO();
    public abstract ReserveDAO getReserveDAO();
    public abstract PreferenceDAO getPreferenceDAO();
}
