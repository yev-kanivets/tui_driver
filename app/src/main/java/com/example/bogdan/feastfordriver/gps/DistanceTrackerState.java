package com.example.bogdan.feastfordriver.gps;

import android.os.Parcel;
import android.os.Parcelable;

public class DistanceTrackerState implements Parcelable {

    private boolean tracking;
    private double latitude;
    private double longitude;

    public DistanceTrackerState(boolean tracking, double latitude, double longitude) {
        this.tracking = tracking;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isTracking() {
        return tracking;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    protected DistanceTrackerState(Parcel in) {
        tracking = in.readByte() != 0;
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<DistanceTrackerState> CREATOR = new Creator<DistanceTrackerState>() {
        @Override
        public DistanceTrackerState createFromParcel(Parcel in) {
            return new DistanceTrackerState(in);
        }

        @Override
        public DistanceTrackerState[] newArray(int size) {
            return new DistanceTrackerState[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (tracking ? 1 : 0));
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

}
