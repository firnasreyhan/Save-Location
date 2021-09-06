package com.android.tracking.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.android.tracking.model.LocationModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {LocationModel.class}, version = 1, exportSchema = false)
public abstract class LocationDatabase extends RoomDatabase {
    private static final String DB_NAME = "location_db";
    private static LocationDatabase instance;
    public static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static synchronized LocationDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), LocationDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract LocationDAO locationDAO();
}
