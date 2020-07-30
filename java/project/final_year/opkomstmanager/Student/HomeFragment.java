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

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;
import project.final_year.opkomstmanager.MainActivity;
import project.final_year.opkomstmanager.R;
import project.final_year.opkomstmanager.adapter.CustomAdapter;
import project.final_year.opkomstmanager.adapter.RecyclerViewAdapter1;
import project.final_year.opkomstmanager.model.DaoStudent;
import project.final_year.opkomstmanager.model.Model;
import project.final_year.opkomstmanager.model.Show_stu_subjects;

public class HomeFragment extends Fragment {

    TextView nameV, rollV, branchV, emailV, contactV, yearV, lateralV, enrlV;
    public static DaoStudent daoStudent;
    CircleImageView profileImageHome;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        Student_Home.flag = 1;
        nameV = (TextView) view.findViewById(R.id.nameV);
        rollV = (TextView) view.findViewById(R.id.rollV);
        branchV = (TextView) view.findViewById(R.id.branchV);
        emailV = (TextView) view.findViewById(R.id.emailV);
        contactV = (TextView) view.findViewById(R.id.contactV);
        yearV = (TextView) view.findViewById(R.id.yearV);
        lateralV = (TextView) view.findViewById(R.id.lateralV);
        enrlV = (TextView) view.findViewById(R.id.enrlV);
        profileImageHome = view.findViewById(R.id.profileImageHome);

        nameV.setText(daoStudent.getName());
        rollV.setText(daoStudent.getRoll());
        branchV.setText(daoStudent.getDepartment());
        emailV.setText(daoStudent.getEmail());
        contactV.setText(daoStudent.getContact());
        yearV.setText(daoStudent.getYear() + " YEAR, " + daoStudent.getSemester() + "th Semester");
        lateralV.setText(daoStudent.getLateral());
        enrlV.setText(daoStudent.getEnrollmentNo());
        if(!daoStudent.getDpurl().equals("")){
            Glide.with(getActivity()).load(daoStudent.getDpurl()).into(profileImageHome);
        }
    }
}

