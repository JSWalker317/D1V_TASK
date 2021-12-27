package com.example.d1vtask.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.d1vtask.LoginActivity;
import com.example.d1vtask.R;
import com.example.d1vtask.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {
    private TextView tv_logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.account_fragment, container, false);
        tv_logout = view.findViewById(R.id.tv_logout);
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
}