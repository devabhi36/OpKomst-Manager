package project.final_year.opkomstmanager.Student;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.final_year.opkomstmanager.MainActivity;
import project.final_year.opkomstmanager.R;
import project.final_year.opkomstmanager.adapter.CustomAdapter;
import project.final_year.opkomstmanager.adapter.GetSubjectsAdapter;
import project.final_year.opkomstmanager.adapter.RecyclerViewAdapter1;
import project.final_year.opkomstmanager.model.DaoStudent;
import project.final_year.opkomstmanager.model.Model;
import project.final_year.opkomstmanager.model.Show_stu_subjects;

public class YourSubjects extends Fragment {

    TextView noSubjects;
    RecyclerView subjectList, selectedSubjects;
    Button submitSubjects;
    ArrayList<String> subject_list = new ArrayList<>();
    ArrayList<String> noOfAssignments = new ArrayList<>();
    ArrayList<Show_stu_subjects> selected_subjects = new ArrayList<>();
    ProgressDialog progressDialog;

    public static String roll_no, departmentSem;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_subjects, container, false);

        noSubjects = view.findViewById(R.id.noSubjects);
        subjectList = view.findViewById(R.id.subjectList);
        selectedSubjects = view.findViewById(R.id.selectedSubjects);
        submitSubjects = view.findViewById(R.id.submitSubjects);

        LinearLayoutManager linearLayoutManagerSubjectList = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        subjectList.setLayoutManager(linearLayoutManagerSubjectList);
        LinearLayoutManager linearLayoutManagerSelectedSubjects = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        selectedSubjects.setLayoutManager(linearLayoutManagerSelectedSubjects);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Student_Home.flag=0;

        databaseReference.child("StudentEnrollments").child(roll_no).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    subjectList.setVisibility(View.GONE);
                    selectedSubjects.setVisibility(View.VISIBLE);
                    submitSubjects.setVisibility(View.GONE);
                    fetchSavedSubjects();
                }
                else {
                    subjectList.setVisibility(View.VISIBLE);
                    selectedSubjects.setVisibility(View.GONE);
                    submitSubjects.setVisibility(View.VISIBLE);
                    fetchSubjectsToSelect();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submitSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Subjects Saved", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                subjectList.setVisibility(View.GONE);
                selectedSubjects.setVisibility(View.VISIBLE);
                submitSubjects.setVisibility(View.GONE);
                fetchSavedSubjects();
            }
        });
    }

    private void fetchSubjectsToSelect() {
        Query query = databaseReference.child("Subjects").orderByChild("departmentsem").equalTo(departmentSem);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    noSubjects.setVisibility(View.GONE);
                    subject_list.clear();
                    noOfAssignments.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        DaoSubject daoSubject = snapshot.getValue(DaoSubject.class);
                        subject_list.add(daoSubject.getCode()+" "+daoSubject.getSubjectname());
                        noOfAssignments.add(daoSubject.getNoofassignments());

                        GetSubjectsAdapter getSubjectsAdapter = new GetSubjectsAdapter(subject_list, roll_no, noOfAssignments);
                        subjectList.setAdapter(getSubjectsAdapter);
                        getSubjectsAdapter.notifyDataSetChanged();
                    }
                    progressDialog.dismiss();
                }
                else {
                    progressDialog.dismiss();
                    noSubjects.setVisibility(View.VISIBLE);
                    submitSubjects.setClickable(false);
                    submitSubjects.setFocusable(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchSavedSubjects() {
        databaseReference.child("StudentEnrollments").child(roll_no).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    noSubjects.setVisibility(View.GONE);
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String subjectName = snapshot.getKey();
                        String classAttended = snapshot.child("classattended").getValue(String.class);
                        double attendance = snapshot.child("attendance").getValue(double.class);
                        ArrayList<Map<String, String>> assignmentList = new ArrayList<>();
                        databaseReference.child("Subjects").child(subjectName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    DaoSubject daoSubject = dataSnapshot.getValue(DaoSubject.class);
                                    int nAssignment = Integer.parseInt(daoSubject.getNoofassignments());
                                    for(int i=1; i<=nAssignment; i++){
                                        //int j=i;
                                        Map<String, String> hashMap = new HashMap<>();
                                        hashMap.put("title", "Assignment"+i);
                                        hashMap.put("value", snapshot.child("assignment"+i).getValue(String.class));
                                        //Log.e("Assignment"+i, snapshot.child("Assignment"+i).getValue(String.class));
                                        assignmentList.add(hashMap);
                                    }
                                    selected_subjects.add(new Show_stu_subjects(subjectName, "Faculty: " + daoSubject.getFaculty(), "Classes Held: " + daoSubject.getNoofclasses(), "Classes Attended: " + classAttended, "Attendance: " + Double.toString(attendance)+"%",
                                            "Assignments Given: " +daoSubject.getNoofassignments(), assignmentList, daoSubject.getFuniqueid()));

                                    Log.e(subjectName, assignmentList.size()+"");

                                    initRecyclerView();
                                }
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        /*new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initRecyclerView();
                            }
                        },3000);*/
                    }
                }
                else {
                    progressDialog.dismiss();
                    noSubjects.setVisibility(View.VISIBLE);
                    submitSubjects.setClickable(false);
                    submitSubjects.setFocusable(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRecyclerView() {
        RecyclerViewAdapter1 recyclerViewAdapter1 = new RecyclerViewAdapter1(selected_subjects);
        selectedSubjects.setAdapter(recyclerViewAdapter1);
        recyclerViewAdapter1.notifyDataSetChanged();
    }
}