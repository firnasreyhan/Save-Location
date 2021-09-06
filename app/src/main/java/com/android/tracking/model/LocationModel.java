package com.android.tracking.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LocationModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idLocation")
    public int idLocation;

    @ColumnInfo(name = "latitude")
    public String latitude;

    @ColumnInfo(name = "longitude")
    public String longitude;
}
