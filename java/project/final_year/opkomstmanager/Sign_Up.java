package project.final_year.opkomstmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.final_year.opkomstmanager.model.DaoStudent;

public class Sign_Up extends AppCompatActivity {
    Spinner role, dept;
    EditText name, email, roll, contact, password, cpassword, year;
    ImageView show, hide;
    Button f_sign_up;
    String name1="", email1="", roll1="", contact1="", password1="", cpassword1="", designation="", department="", year1="";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        role = (Spinner)findViewById(R.id.role);
        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        dept = (Spinner)findViewById(R.id.dept);
        roll = (EditText)findViewById(R.id.roll_no);
        year = (EditText)findViewById(R.id.currentYear);
        contact = (EditText)findViewById(R.id.contact_no);
        password = (EditText)findViewById(R.id.password);
        cpassword = (EditText)findViewById(R.id.cpassword);
        show = (ImageView)findViewById(R.id.show);
        hide = (ImageView)findViewById(R.id.hide);
        f_sign_up = (Button)findViewById(R.id.f_sign_up);

        String[] roles = {"Student"};
        ArrayAdapter<CharSequence> langAdapter1 = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_text, roles);
        langAdapter1.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        role.setAdapter(langAdapter1);

        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    roll.setText("");
                    contact.setText("");
                    designation = "Student";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Sign_Up.this, "Nothing selected", Toast.LENGTH_SHORT).show();
            }
        });

        String[] departments = {"Chemical","Civil","Computer Science", "Information Technology", "Electrical", "Electronics & Communication", "Electronics & Instrumentation", "Mechanical"};
        ArrayAdapter<CharSequence> langAdapter2 = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_text, departments);
        langAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        dept.setAdapter(langAdapter2);

        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    department = "Chemical";
                }
                if(position==1){
                    department = "Civil";
                }
                if(position==2){
                    department = "Computer Science";
                }
                if(position==3){
                    department = "Information Technology";
                }
                if(position==4){
                    department = "Electrical";
                }
                if(position==5){
                    department = "Electronics & Communication";
                }
                if(position==6){
                    department = "Electronics & Instrumentation";
                }
                if(position==7){
                    department = "Mechanical";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Sign_Up.this, "Nothing selected", Toast.LENGTH_SHORT).show();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password1 = password.getText().toString();
                cpassword1 = cpassword.getText().toString();
                if(password1.equals("") && cpassword1.equals(""))
                    Toast.makeText(Sign_Up.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                else{
                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                cpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                password.setSelection(password.length());
                cpassword.setSelection(cpassword.length());
                show.setVisibility(View.INVISIBLE);
                hide.setVisibility(View.VISIBLE);
                }
            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    cpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.length());
                    cpassword.setSelection(cpassword.length());
                    hide.setVisibility(View.INVISIBLE);
                    show.setVisibility(View.VISIBLE);
            }
        });

        f_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name1 = name.getText().toString();
                email1 = email.getText().toString();
                contact1 = contact.getText().toString();
                roll1 = roll.getText().toString();
                year1 = year.getText().toString();
                password1 = password.getText().toString();
                cpassword1 = cpassword.getText().toString();

                if(designation.equals("Student")){
                    final DaoStudent daoStudent = new DaoStudent(name1, email1, department, contact1, roll1, year1, password1, "", "", "", "", "");
                    databaseReference = firebaseDatabase.getReference().child("Students").child(roll1);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                databaseReference.setValue(daoStudent).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Sign_Up.this, "Successfully Registered!\nLogin with your credentials!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Sign_Up.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });
                            }
                            else
                                Toast.makeText(Sign_Up.this, "User Exists.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Sign_Up.this, "Server Error! Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(designation.equals("Faculty")){
                    contact1 = contact.getText().toString();
                    databaseReference = firebaseDatabase.getReference().child("Faculty").child(department).child(contact1);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                            }
                            else
                                Toast.makeText(Sign_Up.this, "User Exists.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Sign_Up.this, "Server Error! Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(designation.equals("Faculty (HOD)")){
                    contact1 = contact.getText().toString();

                    databaseReference = firebaseDatabase.getReference().child("Faculty(HOD)").child(department).child(contact1);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                            }
                            else
                                Toast.makeText(Sign_Up.this, "User Exists.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Sign_Up.this, "Server Error! Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
