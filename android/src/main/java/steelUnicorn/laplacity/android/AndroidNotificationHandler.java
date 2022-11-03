package steelUnicorn.laplacity.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import steelUnicorn.laplacity.R;
import steelUnicorn.laplacity.core.Laplacity;
import steelUnicorn.laplacity.utils.NotificationHandler;

public class AndroidNotificationHandler implements NotificationHandler {
    int RESTORE_LOWER_BOUND = 30 * 60;
    private int restoreTime;
    private final Context context;

    private final PendingIntent pendingIntent;

    public AndroidNotificationHandler(Context context) {
        this.context = context;

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("show_notification");
        broadcastIntent.setClass(context, FoodNotificationReceiver.class);
        broadcastIntent.putExtra("title", context.getString(R.string.food_notification_title));
        broadcastIntent.putExtra("text", context.getString(R.string.food_notification_text));
        pendingIntent =
                    PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_IMMUTABLE);
    }



    public void cancelAlarms() {
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.i("Notifications", "Alarms canceled");
        }
    }

    @Override
    public void updateRestoreValue() {
        RESTORE_LOWER_BOUND = Laplacity.isDebugEnabled() ? 10 * 60 : 30 * 60;
    }

    @Override
    public void setFoodAlarm() {
        Log.i("Notification", "set food alarm was called for notifications" +
                "\nRestore time is: " + restoreTime +
                "\nSystem time is: " + System.currentTimeMillis());
        if (restoreTime > RESTORE_LOWER_BOUND) {

            long signalTime = SystemClock.elapsedRealtime() + restoreTime * 1000L;
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, signalTime, pendingIntent);
            Log.i("Notifications", "Alarms set");
        }
    }

    @Override
    public void setRestoreTime(int time) {
        restoreTime = time;
    }
}
