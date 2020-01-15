package pl.rozekm.saucemanager.frontend.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        String title = (String) intent.getExtras().get("title");

        Intent alarmIntent = new Intent(context, ScreenActivity.class);
        alarmIntent.putExtra("title", title);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
        setResultCode(Activity.RESULT_OK);
    }
}

