package com.android.tracking.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.android.tracking.model.LocationModel;

import java.util.List;

@Dao
public interface LocationDAO {
    @Query("SELECT * FROM LocationModel")
    LiveData<List<LocationModel>> getLocation();

    @Insert
    void insertLocation(LocationModel imageModel);

    @Delete
    void deleteLocation(LocationModel imageModel);
}
