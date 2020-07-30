package project.final_year.opkomstmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Scanner;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String sub_name = intent.getExtras().getString("SubjectName");
        Intent i = new Intent(context, AlarmService.class);
        if(!sub_name.equals(null))
        i.putExtra("SubjectName", sub_name);
        context.startService(i);
    }
}
