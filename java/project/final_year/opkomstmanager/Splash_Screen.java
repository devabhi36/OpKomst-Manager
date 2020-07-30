package project.final_year.opkomstmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;
import project.final_year.opkomstmanager.Student.AssignmentActivity;
import project.final_year.opkomstmanager.Student.ChatActivity;
import project.final_year.opkomstmanager.Student.NoticeFragment;
import project.final_year.opkomstmanager.Student.ResultFragment;
import project.final_year.opkomstmanager.Student.Student_Home;
import project.final_year.opkomstmanager.Student.TimeTableFragment;
import project.final_year.opkomstmanager.Student.YourSubjects;
import project.final_year.opkomstmanager.model.DaoStudent;
import project.final_year.opkomstmanager.Student.HomeFragment;
import project.final_year.opkomstmanager.model.Show_stu_subjects;

public class Splash_Screen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 2000;

    Animation topAnim, bottomAnim;
    ImageView logo;
    //TextView app_name;
    GifImageView app_name;
    public ArrayList<Show_stu_subjects> saved_subjects;
    public static final String PREFS_NAME = "LoginPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash__screen);
        //CreateNotificationChannel();
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        logo = findViewById(R.id.logo);
        app_name = findViewById(R.id.app_name);

        logo.setAnimation(topAnim);
        saved_subjects = new ArrayList<>();
        //app_name.setAnimation(bottomAnim);
        final SharedPreferences logged = getSharedPreferences(PREFS_NAME, 0);
        String user = logged.getString("USER", "");
        String dpUrl = logged.getString("DPURL", "");
        String token = logged.getString("StudentToken", "");
        Gson gson = new Gson();
        String json1 = logged.getString("DaoStudent", "");
        DaoStudent object = gson.fromJson(json1, DaoStudent.class);
        MainActivity mainActivity = new MainActivity();
        if(!user.equals("")){
            CreateNotificationChannel();
            mainActivity.saved_Subject(user);
            HomeFragment.daoStudent = object;
            //YourSubjects.daoStudent = object;
            AssignmentActivity.roll_no = user;
            ChatActivity.user = object.getName();
            ChatActivity.userId = user;
            Student_Home.uRoll = user;
            Student_Home.profileImageUrl = dpUrl;
            Student_Home.token = token;
            YourSubjects.roll_no = user;
            YourSubjects.departmentSem = object.getDepartment()+object.getSemester();
            //YourSubjects.saved_subjects = saved_subjects;
            ResultFragment.uRoll = user;
            NoticeFragment.deptName = object.getDepartment();
            TimeTableFragment.deptName = object.getDepartment();
            TimeTableFragment.sem = object.getSemester();
            TimeTableFragment.uRoll = user;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(logged.getString("ROLE", "").equals("Student")){
                    Intent intent = new Intent(Splash_Screen.this, Student_Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                startActivity(new Intent(Splash_Screen.this, MainActivity.class));
                finish();
            }
        }, SPLASH_SCREEN);
    }

    private void CreateNotificationChannel() {
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.ringtone);
        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mChannel = new NotificationChannel("subjectalarm", "ALARM", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.GRAY);
            mChannel.enableLights(true);
            mChannel.setDescription("SUBJECT ALARM");
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.setSound(soundUri, audioAttributes);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel( mChannel );
            }
        }
    }
}
