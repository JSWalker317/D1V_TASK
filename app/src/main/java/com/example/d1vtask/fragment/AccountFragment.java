package com.example.d1vtask.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.d1vtask.CreateProfile_Activity;
import com.example.d1vtask.LoginActivity;
import com.example.d1vtask.R;
import com.example.d1vtask.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment implements View.OnClickListener{
    private TextView tv_logout;
    ImageView img_profile_frag;
    TextView tv_name_frag, tv_email_frag, tv_prof_frag, tv_phone_frag;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.account_fragment, container, false);
        tv_logout = view.findViewById(R.id.tv_logout);
        img_profile_frag = view.findViewById(R.id.img_profile_fragment);
        tv_name_frag = view.findViewById(R.id.tv_name_fragment);
        tv_email_frag = view.findViewById(R.id.tv_email_fragment);
        tv_prof_frag = view.findViewById(R.id.tv_prof_fragment);
        tv_phone_frag = view.findViewById(R.id.tv_phone_fragment);


        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });

        return view;
    }

    private void alertDialog() {
        AlertDialog.Builder buider = new AlertDialog.Builder(getActivity());
        buider.setTitle("Đăng xuất");
        buider.setMessage("Bạn có muốn đăng xuất không?");
        buider.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        buider.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = buider.create();
        alert.show();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView textView = getActivity().findViewById(R.id.tv_help);
        textView.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = user.getUid();
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("user").document(currentId);
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            String nameResult = task.getResult().getString("name");
                            String profResult = task.getResult().getString("prof");
                            String emailResult = task.getResult().getString("email");
                            String phoneResult = task.getResult().getString("phone");
                            String url = task.getResult().getString("url");

                            Picasso.get().load(url).into(img_profile_frag);
                            tv_name_frag.setText(nameResult);
                            tv_email_frag.setText(emailResult);
                            tv_prof_frag.setText(profResult);
                            tv_phone_frag.setText(phoneResult);



                        }else {
                            Intent intent = new Intent(getActivity(),CreateProfile_Activity.class);
                            startActivity(intent);

                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_help:
            Intent intent = new Intent(getActivity(), CreateProfile_Activity.class);
            startActivity(intent);
        }
    }
}