package com.example.bogdan.feastfordriver.gps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.example.bogdan.feastfordriver.BuildConfig;
import com.example.bogdan.feastfordriver.util.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Custom {@link Service} to track current location and distance traveled.
 * Created on 3/29/17.
 *
 * @author Evgenii Kanivets
 */

public class DistanceTrackerService extends Service {

    public static final String ACTION_START_TRACKING = BuildConfig.APPLICATION_ID + ".action_start_tracking";
    public static final String ACTION_STOP_TRACKING = BuildConfig.APPLICATION_ID + ".action_stop_tracking";
    public static final String ACTION_BROADCAST_CURRENT_STATE = BuildConfig.APPLICATION_ID + ".action_broadcast_current_state";

    public static final String EXTRA_DISTANCE_TRACKER_STATE = BuildConfig.APPLICATION_ID + ".extra_distance_tracker_state";

    private static final int UPDATE_PERIOD = 10000; // 10 seconds

    private DistanceTrackerManager manager;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        manager = new DistanceTrackerManager(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            //noinspection ConstantConditions
            return super.onStartCommand(intent, flags, startId);
        }

        switch (intent.getAction()) {
            case ACTION_START_TRACKING:
                startTracking();
                break;

            case ACTION_STOP_TRACKING:
                stopTracking();
                break;

            case ACTION_BROADCAST_CURRENT_STATE:
                sendCurrentState();
                break;

            default:
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startTracking() {
        manager.startTracking();
        startTimer();
        sendCurrentState();
    }

    private void stopTracking() {
        manager.stopTracking();
        stopTimer();
        sendCurrentState();
    }

    private void sendCurrentState() {
        double latitude = manager.getDistanceTrackerState().getLatitude();
        double longitude = manager.getDistanceTrackerState().getLongitude();
        sendLocationToServer(latitude, longitude);
    }

    private void sendLocationToServer(double lat, double lon) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Const.INSTANCE.getDRIVERS_REF().document(firebaseUser.getUid()).update("gps", lat + ", " + lon);
        }
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendCurrentState();
            }
        }, 0, UPDATE_PERIOD);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
