package project.final_year.opkomstmanager;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmService extends Service {

    Ringtone r;
    IBinder binder = new StopAlarm();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sub_name = intent.getExtras().getString("SubjectName");
        if(!sub_name.equals(null))
            YourTask("of "+sub_name+" ");
        else
            YourTask("");
        return Service.START_STICKY;
    }

    private void YourTask(String sub_name){
        Uri notificationSound = Uri.parse("android.resource://"+this.getPackageName()+"/"+R.raw.ringtone);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "subjectalarm")
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Class Alarm")
                .setContentText("You have a class " +sub_name+ "scheduled in 10 minutes!")
                .setSound(notificationSound)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        notificationManagerCompat.notify(251, builder.build());

        r = RingtoneManager.getRingtone(this, notificationSound);
        r.play();
    }

    public class StopAlarm extends Binder{
        public void stop(){
            r.stop();
        }
    }
}
