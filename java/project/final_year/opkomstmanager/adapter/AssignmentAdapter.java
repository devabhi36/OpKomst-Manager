package project.final_year.opkomstmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import project.final_year.opkomstmanager.Student.AssignmentActivity;
import project.final_year.opkomstmanager.R;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.subjectsVH> {
    ArrayList<Map<String, String>> assignments;
    String sub_name, faculty_code;
    Context context;

    public AssignmentAdapter(ArrayList<Map<String, String>> assignments, String sub_name, String faculty_code){
        this.assignments = assignments;
        this.sub_name = sub_name;
        this.faculty_code = faculty_code;
    }

    @NonNull
    @Override
    public subjectsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_list, parent, false);
        context = parent.getContext();
        return new subjectsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentAdapter.subjectsVH holder, int position) {
        Map<String, String> data = assignments.get(position);
        holder.assignmentHeading.setText(data.get("title"));
        holder.assignmentValue.setText(data.get("value"));
        holder.assignmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssignmentActivity.sub_name = sub_name;
                AssignmentActivity.assignmentNo = data.get("title");
                AssignmentActivity.faculty_code = faculty_code;
                context.startActivity(new Intent(context, AssignmentActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }
    class subjectsVH extends RecyclerView.ViewHolder{

        RelativeLayout assignmentLayout;
        TextView assignmentHeading, assignmentValue;
        public subjectsVH(@NonNull View itemView) {
            super(itemView);
            assignmentLayout = itemView.findViewById(R.id.assignmentLayout);
            assignmentHeading = itemView.findViewById(R.id.assignmentHeading);
            assignmentValue = itemView.findViewById(R.id.assignmentValue);
        }
    }
}
