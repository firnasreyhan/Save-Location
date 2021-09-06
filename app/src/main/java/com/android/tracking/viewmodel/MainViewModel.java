package com.android.tracking.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.tracking.model.LocationModel;
import com.android.tracking.repository.LocationRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LocationRepository locationRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        locationRepository = new LocationRepository(application);
    }

    public LiveData<List<LocationModel>> getLocation() {
        return locationRepository.getLocation();
    }

    public void insertLocation(LocationModel locationModel) {
        locationRepository.insertLocation(locationModel);
    }

    public void deleteImage(LocationModel locationModel) {
        locationRepository.deleteImage(locationModel);
    }
}
