package steelUnicorn.laplacity.android;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import steelUnicorn.laplacity.R;
import steelUnicorn.laplacity.utils.NotificationHandler;

public class AndroidNotificationHandler implements NotificationHandler {
    private int restoreTime;
    private final Context context;

    public AndroidNotificationHandler(Context context) {
        this.context = context;
    }

    @Override
    public void setFoodAlarm() {
        Log.i("Notification", "set food alarm was called for notifications" +
                "\nRestore time is: " + restoreTime +
                "\nSystem time is: " + System.currentTimeMillis());

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("show_notification");
        broadcastIntent.setClass(context, FoodNotificationReceiver.class);
        broadcastIntent.putExtra("title", context.getString(R.string.food_notification_title));
        broadcastIntent.putExtra("text", context.getString(R.string.food_notification_text));
        context.sendBroadcast(broadcastIntent);
    }

    @Override
    public void setRestoreTime(int time) {
        restoreTime = time;
    }
}
