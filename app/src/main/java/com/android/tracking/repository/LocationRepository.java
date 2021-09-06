package com.android.tracking.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.android.tracking.database.LocationDAO;
import com.android.tracking.database.LocationDatabase;
import com.android.tracking.model.LocationModel;

import java.util.List;

public class LocationRepository {
    private LocationDAO locationDAO;

    public LocationRepository(Application application) {
        this.locationDAO =  LocationDatabase.getInstance(application).locationDAO();
    }

    public LiveData<List<LocationModel>> getLocation() {
        return locationDAO.getLocation();
    }

    public void insertLocation(final LocationModel locationModel) {
        LocationDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                locationDAO.insertLocation(locationModel);
            }
        });
    }

    public void deleteImage(final LocationModel locationModel) {
        LocationDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                locationDAO.deleteLocation(locationModel);
            }
        });
    }
}
