package project.final_year.opkomstmanager.Student;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import project.final_year.opkomstmanager.R;

public class ClassTest extends Fragment {

    public static String ct_no, uRoll;
    TextView title;
    SwipeRefreshLayout refreshLayout;
    ListView subjectMarksList;
    ArrayList<String> marksList = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Class Test Students").child(uRoll)
                                            .child(ct_no);
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_test, container, false);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        title = view.findViewById(R.id.title);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        subjectMarksList = view.findViewById(R.id.subjectMarksList);
        title.setText(ct_no);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Student_Home.flag=2;

        getData();

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red), getResources().getColor(R.color.blue),
                getResources().getColor(R.color.green), getResources().getColor(R.color.yellow));

        refreshLayout.setOnRefreshListener(() -> {
            getData();
            new Handler().postDelayed(() -> refreshLayout.setRefreshing(false), 1500);
        });
    }

    private void getData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    marksList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        marksList.add(snapshot.getKey()+"\t\t\t\t\t"+snapshot.getValue(String.class));
                    }
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), "No Records Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        new Handler().postDelayed(() -> {
            initListView();
        }, 1500);
    }

    private void initListView() {
        arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, marksList);
        subjectMarksList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
        subjectMarksList.setOnItemClickListener((parent, view, position, id) ->
        {
            Toast.makeText(getContext(), marksList.get(position), Toast.LENGTH_SHORT).show();
        });
    }
}