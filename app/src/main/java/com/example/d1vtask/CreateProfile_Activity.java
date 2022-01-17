package com.example.d1vtask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.d1vtask.fragment.AccountFragment;
import com.example.d1vtask.model.All_user_member;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class CreateProfile_Activity extends AppCompatActivity {
    private EditText edtName, edtProfession, edtEmail, edtPhone;
    private Button btn_saveProfile;
    private ImageView img_Profile;
    Uri imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    private static final int PICK_IMAGE = 1;
    All_user_member  member;
    String currentUserId;

    private ProgressDialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        edtName = findViewById(R.id.edt_name_pro);
        edtEmail = findViewById(R.id.edt_email_pro);
        edtProfession = findViewById(R.id.edt_profession_pro);
        edtPhone = findViewById(R.id.edt_phone_pro);
        btn_saveProfile = findViewById(R.id.btn_saveProfile);
        img_Profile = findViewById(R.id.img_profile);
        loader = new ProgressDialog(this);


        member = new All_user_member();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();

        documentReference =db.collection("user").document(currentUserId);
        storageReference = FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference = database.getReference("All Users");

        btn_saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
        img_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if(requestCode == PICK_IMAGE || resultCode == RESULT_OK||
                    data != null || data.getData() != null){
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(img_Profile);
            }
        }catch (Exception e) {
            Toast.makeText(this, "Error"+e, Toast.LENGTH_SHORT).show();

        }


    }
    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void uploadData() {
        String name = edtName.getText().toString();
        String prof = edtProfession.getText().toString();
        String email = edtEmail.getText().toString();
        String phone = edtPhone.getText().toString();

        if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(prof)
                ||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(phone) || imageUri != null){


            loader.setMessage("Update in progress");
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+ getFileExt(imageUri));
            uploadTask = reference.putFile(imageUri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();

                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {

                @Override
                public void onComplete(@NonNull Task<Uri> task) {


                    if(task.isSuccessful()){
                        Uri dowloadUri = task.getResult();
                        Map<String, String> profile = new HashMap<>();
                        profile.put("name",name);
                        profile.put("prof",prof);
                        profile.put("url",dowloadUri.toString());
                        profile.put("email",email);
                        profile.put("phone", phone);
                        profile.put("uid",currentUserId);
                        profile.put("privacy", "Public");

                        member.setName(name);
                        member.setProf(prof);
                        member.setUid(currentUserId);
                        member.setPhone(phone);
                        member.setUrl(dowloadUri.toString());

                        databaseReference.child(currentUserId).setValue(member);
                        documentReference.set(profile)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(CreateProfile_Activity.this, "Profile Created", Toast.LENGTH_SHORT).show();

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(new Intent(CreateProfile_Activity.this, MainActivity.class));
                                            }
                                        }, 2000);
                                    }
                                });



                    }

                }
            });

        }else {
            loader.dismiss();
            Toast.makeText(this, "Please fill all Fields", Toast.LENGTH_SHORT).show();
        }


    }
}