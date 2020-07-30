package project.final_year.opkomstmanager.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import project.final_year.opkomstmanager.Alarm;
import project.final_year.opkomstmanager.AlarmService;
import project.final_year.opkomstmanager.R;
import project.final_year.opkomstmanager.model.TimeSubjects;

import static android.content.Context.BIND_AUTO_CREATE;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.TimeSubjectsVH> {

    Context context;
    ArrayList<TimeSubjects> timeSubjects;
    String sem;
    int day, pid;
    DatabaseReference databaseReference;
    boolean mBounded;
    AlarmService mServer;

    public TimeTableAdapter(ArrayList<TimeSubjects> timeSubjects, String sem, int day, DatabaseReference databaseReference){
        this.timeSubjects = timeSubjects;
        this.sem = sem;
        this.day = day;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public TimeSubjectsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_layout, parent, false);
        context = parent.getContext();
        return new TimeSubjectsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSubjectsVH holder, int position) {
        TimeSubjects timeSubjects1 = timeSubjects.get(position);
        String time = timeSubjects1.getTime();
        holder.timeofClass.setText(time);
        String subject = timeSubjects1.getSubjects();
        holder.subjectName.setText(subject);

        char timeArray[] = time.toCharArray();
        String hour = timeArray[0]+""+timeArray[1];
        int fhour = Integer.parseInt(hour);
        String minutes = timeArray[3]+""+timeArray[4];
        int fminutes = Integer.parseInt(minutes);
        if(fminutes>=10)
            fminutes = fminutes-10;
        else {
            fhour = fhour-1;
            fminutes = fminutes+50;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, fhour);
        calendar.set(Calendar.MINUTE, fminutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, day);

        String id = sem+day+hour+minutes;
        int fid = Integer.parseInt(id);
        Log.e("ID OF INTENT", fid+"");

        databaseReference.child(fid+"").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String onOff = dataSnapshot.getValue(String.class);
                    if(onOff.equals("ON"))
                        holder.setAlarm.setChecked(true);
                    else
                        holder.setAlarm.setChecked(false);
                }
                else
                    holder.setAlarm.setChecked(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*holder.setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.setAlarm.isChecked()){
                    databaseReference.child(day+"").child(fid+"").setValue("ON");
                    long tenmins = 1000*60*10;
                    set(time);
                }
                else {
                    databaseReference.child(day+"").child(fid+"").setValue("OFF");
                    cancelAlarm(fid);
                }
            }
        });*/

        holder.setAlarm.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if(isChecked){
                databaseReference.child(fid+"").setValue("ON");
                if(calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 7);
                }

                Log.e("TIME IN MILLIS"+subject, calendar.getTimeInMillis()+"");
                setAlarm(fid, calendar.getTimeInMillis(), subject, time);
            }
            else {
                //databaseReference.child(day+"").child(fid+"").setValue("OFF");
                cancelAlarm(fid);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.setAlarm.setChecked(true);
                        Toast.makeText(context, "Alarm Set for upcoming class!", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSubjects.size();
    }

    public class TimeSubjectsVH extends RecyclerView.ViewHolder {

        TextView timeofClass, subjectName;
        Switch setAlarm;
        public TimeSubjectsVH(@NonNull View itemView) {
            super(itemView);
            timeofClass = itemView.findViewById(R.id.timeofClass);
            subjectName = itemView.findViewById(R.id.subjectName);
            setAlarm = itemView.findViewById(R.id.setAlarm);
        }
    }

    private void setAlarm(int fid, long timeInMillis, String sub_name, String time) {
        Intent intent = new Intent(context, Alarm.class);
        intent.putExtra("SubjectName", sub_name);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= 23) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, fid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);


        } else {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, fid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }

        //Toast.makeText(context, "ALARM SET!", Toast.LENGTH_SHORT).show();
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }

    private void cancelAlarm(int fid) {
        Intent intent = new Intent(context, Alarm.class);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, fid, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Intent mIntent = new Intent(context, AlarmService.class);
            Log.e("Called", "service Connection");
            context.bindService(mIntent, serviceConnection, BIND_AUTO_CREATE);
            Toast.makeText(context, "Alarm Cancelled for PID"+ fid, Toast.LENGTH_SHORT).show();
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBounded = true;
            AlarmService.StopAlarm mLocalBinder = (AlarmService.StopAlarm)service;
            mLocalBinder.stop();
            Log.e("INSTANCE OF SERVICE", "Created");

            if(mBounded) {
                context.unbindService(serviceConnection);
                mBounded = false;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            mServer = null;
        }
    };
}
