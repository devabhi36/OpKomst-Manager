package project.final_year.opkomstmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import project.final_year.opkomstmanager.Student.ChatActivity;
import project.final_year.opkomstmanager.R;

public class Subject_chat_adapter extends RecyclerView.Adapter<Subject_chat_adapter.subjectsVH>{
    ArrayList<String> subjectList, subjects_codes;
    Context context;
    int id;

    public Subject_chat_adapter(ArrayList<String> subjectList, ArrayList<String> subjects_codes){
        this.subjectList = subjectList;
        this.subjects_codes = subjects_codes;
    }

    @NonNull
    @Override
    public subjectsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_chat_view, parent, false);
        context = parent.getContext();
        return new subjectsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull subjectsVH holder, int position) {
        final String sub_name = subjectList.get(position);
        final String sub_code = subjects_codes.get(position);
        Log.e("Sub Name", sub_name);
        holder.subject_name.setText(sub_name);
        holder.subject_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ChatActivity.sub_name = sub_name;
                ChatActivity.sub_code = sub_code;
                context.startActivity(new Intent(activity, ChatActivity.class));
            }
        });
        holder.chatCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ChatActivity.sub_name = sub_name;
                ChatActivity.sub_code = sub_code;
                context.startActivity(new Intent(activity, ChatActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    class subjectsVH extends RecyclerView.ViewHolder{

        TextView subject_name;
        CardView chatCardView;
        public subjectsVH(@NonNull View itemView) {
            super(itemView);
            subject_name = itemView.findViewById(R.id.subject_name);
            chatCardView = itemView.findViewById(R.id.chatCardView);
        }
    }
}
