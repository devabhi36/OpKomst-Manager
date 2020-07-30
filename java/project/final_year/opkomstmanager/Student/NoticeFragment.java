package project.final_year.opkomstmanager.Student;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import project.final_year.opkomstmanager.R;
import project.final_year.opkomstmanager.adapter.NoticeAdapter;
import project.final_year.opkomstmanager.model.Notice;

import static android.app.Activity.RESULT_OK;

public class NoticeFragment extends Fragment {

    public static String deptName;
    RecyclerView noticeList;
    SwipeRefreshLayout refreshLayout;
    ProgressDialog progressDialog;
    ArrayList<Notice> notices = new ArrayList<>();
    NoticeAdapter noticeAdapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notices").child(deptName);
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        noticeList = view.findViewById(R.id.noticeList);
        noticeList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        noticeList.setLayoutManager(linearLayoutManager);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getSupportFragmentManager().popBackStack("HomeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Student_Home.flag=0;

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red), getResources().getColor(R.color.blue),
                getResources().getColor(R.color.green), getResources().getColor(R.color.yellow));

        getData();

        refreshLayout.setOnRefreshListener(() ->{
            getData();
            new Handler().postDelayed(() -> refreshLayout.setRefreshing(false), 2000);
        });

    }

    private void getData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    notices.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Notice notice = snapshot.getValue(Notice.class);
                        notices.add(notice);

                        noticeAdapter = new NoticeAdapter(notices);
                        noticeList.setAdapter(noticeAdapter);
                        noticeAdapter.notifyDataSetChanged();
                    }
                    progressDialog.dismiss();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "No Notice Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}