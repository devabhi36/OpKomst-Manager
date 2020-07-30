package project.final_year.opkomstmanager.Student;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import project.final_year.opkomstmanager.R;

public class ResultFragment extends Fragment {

    public static String uRoll;
    Button semester, classTest;
    ListView listClassTests;
    int flag1 = 0, count=0;
    ArrayList<String> ctList = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Class Test Students").child(uRoll);
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        semester = view.findViewById(R.id.semester);
        classTest = view.findViewById(R.id.classTest);
        listClassTests = view.findViewById(R.id.listClassTests);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Student_Home.flag=0;

        semester.setOnClickListener(v -> {
            SemResult.uRoll = uRoll;
            startActivity(new Intent(getActivity().getApplicationContext(), SemResult.class));
        });

        getData();

        classTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag1==0)
                    Toast.makeText(getContext(), "NO CLASS TEST FOUND!", Toast.LENGTH_SHORT).show();
                else if(flag1==1){
                    if(count==0){
                        listClassTests.setVisibility(View.VISIBLE);
                        count=1;
                    }
                    else if(count==1){
                        listClassTests.setVisibility(View.GONE);
                        count=0;
                    }
                }
            }
        });
    }

    private void getData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    flag1 = 1;
                    ctList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ctList.add(snapshot.getKey());
                    }
                }
                else {
                    flag1=0;
                    listClassTests.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initListView();
            }
        }, 1000);
    }

    private void initListView() {
        arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, ctList);
        listClassTests.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        listClassTests.setOnItemClickListener((parent, view, position, id) -> ItemClick(position));
    }

    private void ItemClick(int position) {
        ClassTest.ct_no = ctList.get(position);
        ClassTest.uRoll = uRoll;
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(((ViewGroup)getView().getParent()).getId(), new ClassTest()).
                addToBackStack("ResultFragment").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }
}