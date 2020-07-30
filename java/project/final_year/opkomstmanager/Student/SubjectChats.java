package project.final_year.opkomstmanager.Student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import project.final_year.opkomstmanager.R;
import project.final_year.opkomstmanager.adapter.Subject_chat_adapter;

public class SubjectChats extends Fragment {
    public static ArrayList<String> subjects, subjects_codes;
    TextView noSubjects;
    RecyclerView listSubjects;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject_chats, container, false);
        listSubjects = (RecyclerView)view.findViewById(R.id.listSubjects);
        noSubjects = view.findViewById(R.id.noSubjects);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Student_Home.flag=0;
        if(subjects.size()==0){
            listSubjects.setVisibility(View.GONE);
            noSubjects.setVisibility(View.VISIBLE);
        }
        else {
            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        Log.e("this", "called");
        listSubjects.setVisibility(View.VISIBLE);
        noSubjects.setVisibility(View.GONE);
        Subject_chat_adapter subject_chat_adapter = new Subject_chat_adapter(subjects, subjects_codes);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        listSubjects.setLayoutManager(layoutManager);
        listSubjects.setAdapter(subject_chat_adapter);
        subject_chat_adapter.notifyDataSetChanged();
    }

}