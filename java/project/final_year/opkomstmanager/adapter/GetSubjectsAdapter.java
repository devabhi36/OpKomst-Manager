package project.final_year.opkomstmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.final_year.opkomstmanager.R;

public class GetSubjectsAdapter extends RecyclerView.Adapter<GetSubjectsAdapter.SubjectListVH> {

    Context context;
    ArrayList<String> subject_list;
    String roll_no;
    ArrayList<String> noofassignments;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public GetSubjectsAdapter(ArrayList<String> subject_list, String roll_no, ArrayList<String> noofassignments){
        this.subject_list = subject_list;
        this.roll_no = roll_no;
        this.noofassignments = noofassignments;
    }
    @NonNull
    @Override
    public SubjectListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_subjects_layout, parent, false);
        context = parent.getContext();
        return new SubjectListVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectListVH holder, int position) {
        String subject = subject_list.get(position);
        int nAssignment = Integer.parseInt(noofassignments.get(position));
        Map<String, Object> hashmap = new HashMap<>();
        hashmap.put("classattended", "0");
        hashmap.put("attendance", 0.0);
        for(int i=1; i<=nAssignment; i++){
            hashmap.put("assignment"+i, "Pending");
        }

        holder.subjectName.setText(subject);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    databaseReference.child("StudentEnrollments").child(roll_no).child(subject)
                            .setValue(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            databaseReference.child("SubjectEnrollments").child(subject).child(roll_no).setValue("");
                        }
                    });
                }
                else {
                    databaseReference.child("StudentEnrollments").child(roll_no).child(subject).setValue(null);
                    databaseReference.child("SubjectEnrollments").child(subject).child(roll_no).setValue(null);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subject_list.size();
    }

    public class SubjectListVH extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView subjectName;
        public SubjectListVH(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkBox);
            subjectName = itemView.findViewById(R.id.subjectName);
        }
    }
}
