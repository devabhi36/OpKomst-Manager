package project.final_year.opkomstmanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import project.final_year.opkomstmanager.R;
import project.final_year.opkomstmanager.model.Show_stu_subjects;

public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.savedSubjectsVH>{

    ArrayList<Show_stu_subjects> subjectList;
    Context context;


    public RecyclerViewAdapter1(ArrayList<Show_stu_subjects> subjectList) {
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public savedSubjectsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_subjects_details, parent, false);
        context = parent.getContext();
        return new savedSubjectsVH(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull savedSubjectsVH holder, int position) {
        Show_stu_subjects show_stu_subjects = subjectList.get(position);
        holder.subject_name.setText(show_stu_subjects.getSubject_name());
        holder.teacher_name.setText(show_stu_subjects.getTeacher_name());
        holder.class_held.setText(show_stu_subjects.getClass_held());
        holder.class_attended.setText(show_stu_subjects.getClass_attended());
        holder.attendance.setText(show_stu_subjects.getAttendance());
        holder.assignments_given.setText(show_stu_subjects.getAssignments_given());

        final ArrayList<Map<String, String>> data5 = show_stu_subjects.getAssignmentList();
        AssignmentAdapter assignmentAdapter = new AssignmentAdapter(data5, show_stu_subjects.getSubject_name(), show_stu_subjects.getFuniqueId());
        LinearLayoutManager layoutManager= new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setStackFromEnd(true);
        holder.assignments.setLayoutManager(layoutManager);
        holder.assignments.setAdapter(assignmentAdapter);

//        boolean isExpanded = subjectList.get(position).isExpanded();
//        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    class savedSubjectsVH extends RecyclerView.ViewHolder{

        TextView subject_name, teacher_name, class_held, class_attended, attendance, assignments_given;
        RecyclerView assignments;
        RelativeLayout expandableLayout;
        LinearLayout linearLayout1;
        public savedSubjectsVH(@NonNull View itemView) {
            super(itemView);

            subject_name = itemView.findViewById(R.id.subject_name);
            teacher_name = itemView.findViewById(R.id.teacherName);
            class_held = itemView.findViewById(R.id.classHeld);
            class_attended = itemView.findViewById(R.id.classAttended);
            attendance = itemView.findViewById(R.id.attendancePercentage);
            assignments_given = itemView.findViewById(R.id.assignments_given);
            assignments = itemView.findViewById(R.id.assignments);
            linearLayout1 = itemView.findViewById(R.id.linerlayout1);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

//            linearLayout1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Show_stu_subjects show_stu_subjects = subjectList.get(getAdapterPosition());
//                    show_stu_subjects.setExpanded(!show_stu_subjects.isExpanded());
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });
        }
    }
}
