package project.final_year.opkomstmanager.Student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import project.final_year.opkomstmanager.R;

public class AssignmentActivity extends AppCompatActivity {
    public static String sub_name, assignmentNo, faculty_code, roll_no;
    String assignmentUrl, uploadedAssignmentUrl, window;
    ImageView back;
    Button view_given_assignment, submit_assignment, view_submitted_assignment, unSubmit_assignment;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Assignment").child(sub_name);
    Dialog openDocs;
    int PICK_FILE_REQUEST = 342;
    ProgressDialog progressDialog;
    private Uri filePath;
    StorageTask uploadTask;
    int fileType = 0;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("assignments").child(sub_name);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        Window window1 = this.getWindow();
        window1.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window1.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        TextView chat_name_title = toolbar.findViewById(R.id.chat_name_title);
        chat_name_title.setText(assignmentNo.toUpperCase());
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Uploading");

        back = toolbar.findViewById(R.id.back);
        back.setOnClickListener(v -> AssignmentActivity.super.onBackPressed());

        view_given_assignment = findViewById(R.id.view_given_assignment);
        submit_assignment = findViewById(R.id.submit_assignment);
        view_submitted_assignment = findViewById(R.id.view_submitted_assignment);
        unSubmit_assignment = findViewById(R.id.unSubmit_assignment);

        DatabaseReference reference = databaseReference.child(faculty_code).child(assignmentNo.toLowerCase());
        reference.child("url").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    assignmentUrl = dataSnapshot.getValue(String.class);
                    Log.e("SUBJECT", sub_name);
                    Log.e("FACULTY CODE", faculty_code);
                    Log.e("ASSIGNMENT", assignmentNo.toLowerCase());
                    Log.e("URL", assignmentUrl);
                }
                else{
                    assignmentUrl = "";
                    submit_assignment.setFocusable(false);
                    submit_assignment.setClickable(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        view_given_assignment.setOnClickListener(view ->{
            if(!assignmentUrl.equals("")){
                final String fileExtention = MimeTypeMap.getFileExtensionFromUrl(assignmentUrl);
                File file = new File("/storage/emulated/0/Android/data/project.final_year.opkomstmanager/files/Download/Assignments/"+sub_name+"/"+assignmentNo + "." + fileExtention);
                fileType = 0;
                showAssignment(assignmentUrl, file, fileType, fileExtention);
            }
            else
                Toast.makeText(this, "NO ASSIGNMENTS GIVEN", Toast.LENGTH_SHORT).show();
        });

        DatabaseReference reference1 = databaseReference.child(assignmentNo.toLowerCase()).child(roll_no);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    uploadedAssignmentUrl = dataSnapshot.getValue(String.class);
                    if(!uploadedAssignmentUrl.equals("")){
                        submit_assignment.setClickable(false);
                        submit_assignment.setFocusable(false);
                        view_submitted_assignment.setVisibility(View.VISIBLE);
                        unSubmit_assignment.setVisibility(View.VISIBLE);
                    }
                    else {
                        submit_assignment.setClickable(true);
                        submit_assignment.setFocusable(true);
                        view_submitted_assignment.setVisibility(View.GONE);
                        unSubmit_assignment.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("window").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                window = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submit_assignment.setOnClickListener(view ->{
            if(window.equals("Open")) {
                Intent pickFile = new Intent(Intent.ACTION_GET_CONTENT);
                pickFile.setType("file/*");
                startActivityForResult(pickFile, PICK_FILE_REQUEST);
                Toast.makeText(getApplicationContext(), "Open Documents", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Faculty is not accepting the assignment!", Toast.LENGTH_SHORT).show();
        });

        view_submitted_assignment.setOnClickListener(view ->{
            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    uploadedAssignmentUrl = dataSnapshot.getValue(String.class);
                    Log.e("URL", uploadedAssignmentUrl);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final String fileExtention = MimeTypeMap.getFileExtensionFromUrl(uploadedAssignmentUrl);
            File file = new File("/storage/emulated/0/Android/data/project.final_year.opkomstmanager/files/Download/Assignments/"+sub_name+"/"+assignmentNo+"/"+ roll_no + "." + fileExtention);
            fileType = 1;
            showAssignment(uploadedAssignmentUrl, file, fileType, fileExtention);
        });

        unSubmit_assignment.setOnClickListener(view ->{
            deleteAssignment(uploadedAssignmentUrl, reference1);
        });
    }

    private void showAssignment(String fileUrl, File file, final int type, String fileExt) {
        openDocs = new Dialog(this, android.R.style.Theme_Light);
        openDocs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDocs.setContentView(R.layout.web_view);
        openDocs.show();
        WebView webView = openDocs.findViewById(R.id.openDocs);
        final ProgressDialog progDailog = ProgressDialog.show(this, "Loading..", "Please wait.. ", true);
        progDailog.setCancelable(true);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();
            }
        });

        if (file.exists()) {
            openDocs.dismiss();
            progDailog.dismiss();
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExt);
            if (fileExt.equalsIgnoreCase("") || mimetype == null) {
                intent.setDataAndType(Uri.fromFile(file), "text/*");
            } else {
                intent.setDataAndType(Uri.fromFile(file), mimetype);
            }
            startActivity(Intent.createChooser(intent, "Choose an Application:"));
        } else {
            try {
                webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(fileUrl, "ISO-8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        ImageView back = openDocs.findViewById(R.id.back);
        back.setOnClickListener(v -> openDocs.dismiss());
        TextView sender_name = openDocs.findViewById(R.id.sender_name);
        sender_name.setText(assignmentNo.toUpperCase());
        TextView image_date = openDocs.findViewById(R.id.image_date);
        image_date.setVisibility(View.GONE);
        TextView image_time = openDocs.findViewById(R.id.image_time);
        image_time.setVisibility(View.GONE);
        ImageView download = openDocs.findViewById(R.id.download);
        download.setOnClickListener(v -> {
                        /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(chat.getMessage()));
                        context.startActivity(intent);*/
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(assignmentUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            if(type==0)
                request.setDestinationInExternalFilesDir(getApplicationContext(), "Download/Assignments/"+sub_name+"/", assignmentNo + "." + fileExt);
            else if(type==1)
                request.setDestinationInExternalFilesDir(getApplicationContext(), "Download/Assignments/"+sub_name+"/"+assignmentNo+"/", roll_no + "." + fileExt);
            downloadManager.enqueue(request);
            //Toast.makeText(context, "Downloading File", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            progressDialog.show();
            filePath = data.getData();
            String fileExt = String.valueOf(filePath).substring(String.valueOf(filePath).lastIndexOf("."));
            Log.e("Extention", fileExt);
            if(fileExt.equalsIgnoreCase(".docx")||fileExt.equalsIgnoreCase(".doc")||fileExt.equalsIgnoreCase(".docm")||
                    fileExt.equalsIgnoreCase(".wps")||fileExt.equalsIgnoreCase(".txt")){
                final StorageReference reference = storageReference.child("/Assignment/"+assignmentNo+roll_no+fileExt);
                uploadTask = reference.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext().getApplicationContext(), "File Saved", Toast.LENGTH_SHORT).show();
                            getDownloadUrl(uploadTask, reference/*, type, key*/);
                        }
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploading " + ((int) progress) + "%...");
                    }
                });
            }
            else {
                progressDialog.dismiss();
                Toast.makeText(this, "Only .docx and .txt files are allowed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getDownloadUrl(StorageTask uploadTask, StorageReference reference) {
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    uploadedAssignmentUrl = downloadUri.toString();

                    DatabaseReference databaseReference1 = databaseReference.child(assignmentNo.toLowerCase()).child(roll_no);
                    databaseReference1.setValue(downloadUri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseDatabase.getInstance().getReference().child("StudentEnrollments").child(roll_no).child(sub_name)
                                    .child(assignmentNo.toLowerCase()).setValue("Submitted");
                            Toast.makeText(getApplicationContext(), "Assignment Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                            submit_assignment.setClickable(false);
                            submit_assignment.setFocusable(false);
                            view_submitted_assignment.setVisibility(View.VISIBLE);
                            unSubmit_assignment.setVisibility(View.VISIBLE);
                        }
                    });
                } else if(task.isCanceled()){
                    Toast.makeText(getApplicationContext(), "Assignment Uploading Failed!", Toast.LENGTH_SHORT).show();
                }
                else{

                }
            }
        });
    }

    private void deleteAssignment(String uploadedAssignmentUrl, DatabaseReference reference) {
        ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setCancelable(false);
        progressDialog1.setTitle("Deleting..");
        progressDialog1.show();
        StorageReference assignmentref = FirebaseStorage.getInstance().getReferenceFromUrl(uploadedAssignmentUrl);
        assignmentref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase.getInstance().getReference().child("StudentEnrollments").child(roll_no).child(sub_name)
                                .child(assignmentNo.toLowerCase()).setValue("Pending");
                        submit_assignment.setFocusable(true);
                        submit_assignment.setClickable(true);
                        view_submitted_assignment.setVisibility(View.GONE);
                        unSubmit_assignment.setVisibility(View.GONE);
                        progressDialog1.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog1.dismiss();
            }
        });
    }
}
