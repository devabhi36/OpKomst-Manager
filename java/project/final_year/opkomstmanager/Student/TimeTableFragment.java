package project.final_year.opkomstmanager.Student;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import project.final_year.opkomstmanager.R;
import project.final_year.opkomstmanager.adapter.TimeTableAdapter;
import project.final_year.opkomstmanager.model.TimeSubjects;

public class TimeTableFragment extends Fragment {

    public static String deptName, sem, uRoll;
    int showHide = 0;
    Button monday, tuesday, wednesday, thursday, friday, saturday;
    RecyclerView mondayList, tuesdayList, wednesdayList, thursdayList, fridayList, saturdayList;
    ArrayList<TimeSubjects> timeSubjectsMonday, timeSubjectsTuesday, timeSubjectsWednesday, timeSubjectsThursday, timeSubjectsFriday, timeSubjectsSaturday;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Time Table").child(deptName).child("Semester "+sem);
    ProgressDialog progressDialog;
    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Alarm ONOFF").child(deptName).child("Semester "+sem).child(uRoll);

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        monday = view.findViewById(R.id.monday);
        tuesday = view.findViewById(R.id.tuesday);
        wednesday = view.findViewById(R.id.wednesday);
        thursday = view.findViewById(R.id.thursday);
        friday = view.findViewById(R.id.friday);
        saturday = view.findViewById(R.id.saturday);

        LinearLayoutManager linearLayoutManagerMon = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManagerTue = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManagerWed = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManagerThu = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManagerFri = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManagerSat = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mondayList    = view.findViewById(R.id.mondayList);
        tuesdayList   = view.findViewById(R.id.tuesdayList);
        wednesdayList = view.findViewById(R.id.wednesdayList);
        thursdayList  = view.findViewById(R.id.thursdayList);
        fridayList    = view.findViewById(R.id.fridayList);
        saturdayList  = view.findViewById(R.id.saturdayList);

        mondayList.setLayoutManager(linearLayoutManagerMon);
        tuesdayList.setLayoutManager(linearLayoutManagerTue);
        wednesdayList.setLayoutManager(linearLayoutManagerWed);
        thursdayList.setLayoutManager(linearLayoutManagerThu);
        fridayList.setLayoutManager(linearLayoutManagerFri);
        saturdayList.setLayoutManager(linearLayoutManagerSat);

        timeSubjectsMonday = new ArrayList<>();
        timeSubjectsTuesday = new ArrayList<>();
        timeSubjectsWednesday = new ArrayList<>();
        timeSubjectsThursday = new ArrayList<>();
        timeSubjectsFriday = new ArrayList<>();
        timeSubjectsSaturday = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Student_Home.flag=0;

        databaseReference.child("Monday").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        TimeSubjects timeSubjects = new TimeSubjects(snapshot.getKey(), snapshot.getValue(String.class));
                        timeSubjectsMonday.add(timeSubjects);

                        TimeTableAdapter timeTableAdapter = new TimeTableAdapter(timeSubjectsMonday, sem, 2, databaseReference1);
                        mondayList.setAdapter(timeTableAdapter);
                        timeTableAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        databaseReference.child("Tuesday").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        TimeSubjects timeSubjects = new TimeSubjects(snapshot.getKey(), snapshot.getValue(String.class));
                        timeSubjectsTuesday.add(timeSubjects);

                        TimeTableAdapter timeTableAdapter = new TimeTableAdapter(timeSubjectsTuesday, sem, 3, databaseReference1);
                        tuesdayList.setAdapter(timeTableAdapter);
                        timeTableAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        databaseReference.child("Wednesday").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        TimeSubjects timeSubjects = new TimeSubjects(snapshot.getKey(), snapshot.getValue(String.class));
                        timeSubjectsWednesday.add(timeSubjects);

                        TimeTableAdapter timeTableAdapter = new TimeTableAdapter(timeSubjectsWednesday, sem, 4, databaseReference1);
                        wednesdayList.setAdapter(timeTableAdapter);
                        timeTableAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        databaseReference.child("Thursday").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        TimeSubjects timeSubjects = new TimeSubjects(snapshot.getKey(), snapshot.getValue(String.class));
                        timeSubjectsThursday.add(timeSubjects);

                        TimeTableAdapter timeTableAdapter = new TimeTableAdapter(timeSubjectsThursday, sem, 5, databaseReference1);
                        thursdayList.setAdapter(timeTableAdapter);
                        timeTableAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        databaseReference.child("Friday").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        TimeSubjects timeSubjects = new TimeSubjects(snapshot.getKey(), snapshot.getValue(String.class));
                        timeSubjectsFriday.add(timeSubjects);

                        TimeTableAdapter timeTableAdapter = new TimeTableAdapter(timeSubjectsFriday, sem, 6, databaseReference1);
                        fridayList.setAdapter(timeTableAdapter);
                        timeTableAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        databaseReference.child("Saturday").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        TimeSubjects timeSubjects = new TimeSubjects(snapshot.getKey(), snapshot.getValue(String.class));
                        timeSubjectsSaturday.add(timeSubjects);

                        TimeTableAdapter timeTableAdapter = new TimeTableAdapter(timeSubjectsSaturday, sem, 7, databaseReference1);
                        saturdayList.setAdapter(timeTableAdapter);
                        timeTableAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        new Handler().postDelayed(() -> progressDialog.dismiss(),2500);

        monday.setOnClickListener(v -> {
            if(timeSubjectsMonday.size()==0)
                Toast.makeText(getContext(), "No Class Scheduled!", Toast.LENGTH_SHORT).show();
            else {
                if(showHide==0){
                    mondayList.setVisibility(View.VISIBLE);
                    showHide=1;
                }
                else if(showHide==1){
                    mondayList.setVisibility(View.GONE);
                    showHide=0;
                }
            }
        });
        tuesday.setOnClickListener(v -> {
            if(timeSubjectsTuesday.size()==0)
                Toast.makeText(getContext(), "No Class Scheduled!", Toast.LENGTH_SHORT).show();
            else {
                if(showHide==0){
                    tuesdayList.setVisibility(View.VISIBLE);
                    showHide=1;
                }
                else if(showHide==1){
                    tuesdayList.setVisibility(View.GONE);
                    showHide=0;
                }
            }
        });
        wednesday.setOnClickListener(v -> {
            if(timeSubjectsWednesday.size()==0)
                Toast.makeText(getContext(), "No Class Scheduled!", Toast.LENGTH_SHORT).show();
            else {
                if(showHide==0){
                    wednesdayList.setVisibility(View.VISIBLE);
                    showHide=1;
                }
                else if(showHide==1){
                    wednesdayList.setVisibility(View.GONE);
                    showHide=0;
                }
            }
        });
        thursday.setOnClickListener(v -> {
            if(timeSubjectsThursday.size()==0)
                Toast.makeText(getContext(), "No Class Scheduled!", Toast.LENGTH_SHORT).show();
            else {
                if(showHide==0){
                    thursdayList.setVisibility(View.VISIBLE);
                    showHide=1;
                }
                else if(showHide==1){
                    thursdayList.setVisibility(View.GONE);
                    showHide=0;
                }
            }
        });
        friday.setOnClickListener(v -> {
            if(timeSubjectsFriday.size()==0)
                Toast.makeText(getContext(), "No Class Scheduled!", Toast.LENGTH_SHORT).show();
            else {
                if(showHide==0){
                    fridayList.setVisibility(View.VISIBLE);
                    showHide=1;
                }
                else if(showHide==1){
                    fridayList.setVisibility(View.GONE);
                    showHide=0;
                }
            }
        });
        saturday.setOnClickListener(v -> {
            if(timeSubjectsSaturday.size()==0)
                Toast.makeText(getContext(), "No Class Scheduled!", Toast.LENGTH_SHORT).show();
            else {
                if(showHide==0){
                    saturdayList.setVisibility(View.VISIBLE);
                    showHide=1;
                }
                else if(showHide==1){
                    saturdayList.setVisibility(View.GONE);
                    showHide=0;
                }
            }
        });
    }
}