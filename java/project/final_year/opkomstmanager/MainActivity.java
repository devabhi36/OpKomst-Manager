package project.final_year.opkomstmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.final_year.opkomstmanager.Student.AssignmentActivity;
import project.final_year.opkomstmanager.Student.ChatActivity;
import project.final_year.opkomstmanager.Student.NoticeFragment;
import project.final_year.opkomstmanager.Student.ResultFragment;
import project.final_year.opkomstmanager.Student.Student_Home;
import project.final_year.opkomstmanager.Student.SubjectChats;
import project.final_year.opkomstmanager.Student.TimeTableFragment;
import project.final_year.opkomstmanager.Student.YourSubjects;
import project.final_year.opkomstmanager.model.DaoStudent;
import project.final_year.opkomstmanager.Student.DaoSubject;
import project.final_year.opkomstmanager.Student.HomeFragment;
import project.final_year.opkomstmanager.model.Show_stu_subjects;

public class MainActivity extends AppCompatActivity {

    TextView sign_up;
    Button login;
    EditText uname, pass;
    String uname1, pass1, year;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public ArrayList<Show_stu_subjects> saved_subjects;
    public static final String PREFS_NAME = "LoginPrefs";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateNotificationChannel();
        sign_up = (TextView) findViewById(R.id.sign_up);
        uname = (EditText) findViewById(R.id.uname);
        pass = (EditText) findViewById(R.id.epass);
        login = (Button) findViewById(R.id.login);
        saved_subjects = new ArrayList<>();

        /*SharedPreferences logged = getSharedPreferences(PREFS_NAME, 0);
        String user = logged.getString("USER", "");
        String dpUrl = logged.getString("DPURL", "");
        Gson gson = new Gson();
        String json1 = logged.getString("DaoStudent", "");
        DaoStudent object = gson.fromJson(json1, DaoStudent.class);
        if(logged.getString("ROLE", "").equals("Student")){
            saved_Subject(user);
            HomeFragment.daoStudent = object;
            Student_Home.uRoll = user;
            Student_Home.profileImageUrl = dpUrl;
            HomeFragment.saved_subjects = saved_subjects;
            Intent intent = new Intent(MainActivity.this, Student_Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }*/


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Sign_Up.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Signing In...");
                progressDialog.setCancelable(false);
                //progressDialog.show();
                uname1 = uname.getText().toString();
                pass1 = pass.getText().toString();



                DatabaseReference databaseReference2 = databaseReference.child("Students").child(uname1);
                databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            final DaoStudent daoStudent = dataSnapshot.getValue(DaoStudent.class);
                            String value = daoStudent.getPassword();
                            if(pass1.equals(value)){

                                String token = FirebaseInstanceId.getInstance().getToken();
                                String token1 = daoStudent.getToken();
                                            if(!token1.equals(token)){
                                                databaseReference2.child("token").setValue(token).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(MainActivity.this, "Error Occurred, please Sign In again to receive notifications!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        else {
                                            databaseReference2.child("token").setValue(token).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(MainActivity.this, "Error Occurred, please Sign In again to receive notifications!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                saved_Subject(uname1);
                                HomeFragment.daoStudent = daoStudent;
                                //YourSubjects.daoStudent = daoStudent;
                                AssignmentActivity.roll_no = uname1;
                                ChatActivity.user = daoStudent.getName();
                                ChatActivity.userId = uname1;
                                Student_Home.uRoll = uname1;
                                Student_Home.profileImageUrl = daoStudent.getDpurl();
                                Student_Home.token = token;
                                YourSubjects.roll_no = uname1;
                                YourSubjects.departmentSem = daoStudent.getDepartment()+daoStudent.getSemester();
                                //YourSubjects.saved_subjects = saved_subjects;
                                ResultFragment.uRoll = uname1;
                                NoticeFragment.deptName = daoStudent.getDepartment();
                                TimeTableFragment.deptName = daoStudent.getDepartment();
                                TimeTableFragment.sem = daoStudent.getSemester();
                                TimeTableFragment.uRoll = uname1;

                                SharedPreferences login = getSharedPreferences(PREFS_NAME, 0);
                                SharedPreferences.Editor editor = login.edit();
                                editor.putString("ROLE", "Student");
                                editor.putString("USER", uname1);
                                editor.putString("DPURL", daoStudent.getDpurl());
                                editor.putString("StudentToken", token);
                                Gson gson = new Gson();
                                String json1 = gson.toJson(daoStudent);
                                editor.putString("DaoStudent", json1);
                                editor.apply();
                                editor.commit();
                                Log.e("SAVED", "DATA SAVED");

                                //saved_subjects.clear();
                                Intent intent = new Intent(MainActivity.this, Student_Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //progressDialog.dismiss();
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(MainActivity.this, "Password Incorrect!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(MainActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Network Error! Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void saved_Subject(String user){
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<String> subjects_codes = new ArrayList<>();
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("StudentEnrollments").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        final String subject_name = snapshot.getKey();
                        subjects.add(subject_name);
                        databaseReference1.child("Subjects").child(subject_name).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                if (dataSnapshot1.exists()) {
                                    DaoSubject daoSubject = dataSnapshot1.getValue(DaoSubject.class);
                                    subjects_codes.add(daoSubject.getCode());
                                    FirebaseMessaging.getInstance().subscribeToTopic(daoSubject.getCode()).addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            Log.e("Subscribed to", daoSubject.getCode());
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                    SubjectChats.subjects = subjects;
                    SubjectChats.subjects_codes = subjects_codes;
                }
                else {
                    SubjectChats.subjects = subjects;
                    SubjectChats.subjects_codes = subjects_codes;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void CreateNotificationChannel(){
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