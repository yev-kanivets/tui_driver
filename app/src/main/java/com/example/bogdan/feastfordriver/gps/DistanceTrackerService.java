package com.example.bogdan.feastfordriver.gps;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.example.bogdan.feastfordriver.BuildConfig;
import com.example.bogdan.feastfordriver.R;
import com.example.bogdan.feastfordriver.activity.delivery.DeliveryActivity;
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

    private static final int UPDATE_PERIOD = 100000; // 10 seconds
    private static final int NOTIFICATION_ID = 18515;

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
        showNotification();
        sendCurrentState();
    }

    private void stopTracking() {
        manager.stopTracking();
        stopTimer();
        hideNotification();
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

    public void hideNotification() {
        stopForeground(true);
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

    public void showNotification() {
        Notification.Builder notificationBuilder = new Notification.Builder(this);
        notificationBuilder.setContentTitle(getString(R.string.online));
        notificationBuilder.setContentText(getString(R.string.location_is_monitored));
        notificationBuilder.setSmallIcon(R.drawable.ic_check);
        notificationBuilder.setOngoing(true);
        notificationBuilder.setOnlyAlertOnce(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "FdChannel";
            notificationBuilder.setChannelId(CHANNEL_ID);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    getString(R.string.channel_id),
                    NotificationManager.IMPORTANCE_HIGH);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        Intent intentOpenPlayer = new Intent(this, DeliveryActivity.class);
        PendingIntent pendingIntentOpenPlayer = PendingIntent.getActivity(this, 0,
                intentOpenPlayer, 0);
        notificationBuilder.setContentIntent(pendingIntentOpenPlayer);

        Notification notification = notificationBuilder.getNotification();

        startForeground(NOTIFICATION_ID, notification);
    }

}
