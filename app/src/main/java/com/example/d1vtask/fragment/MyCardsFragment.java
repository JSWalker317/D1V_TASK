package com.example.d1vtask.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d1vtask.R;
import com.example.d1vtask.adapter.TaskAdapter;
import com.example.d1vtask.model.Task;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyCardsFragment extends Fragment {
    private RecyclerView rv_myCards;
    private TaskAdapter mTaskAdapter;
    private SearchView sv_search;
    private Button btn_add_card;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mycards_fragment, container, false);
        rv_myCards = view.findViewById(R.id.rv_MyCards);
        sv_search = view.findViewById(R.id.sv_search);
        btn_add_card = view.findViewById(R.id.btn_add_card);

        rv_myCards.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerOptions<Task> options =
                new FirebaseRecyclerOptions.Builder<Task>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Task"), Task.class)
                        .build();

        mTaskAdapter = new TaskAdapter(options);
        rv_myCards.setAdapter(mTaskAdapter);


        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });

        btn_add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard();
            }
        });

        return view;
    }

    private void addCard() {
        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(getActivity());
        dialogPlusBuilder.setContentHolder(new ViewHolder(R.layout.input_file));
        dialogPlusBuilder.setExpanded(true,1000);
        final DialogPlus dialogPlus = dialogPlusBuilder
                .create();

        View view = dialogPlus.getHolderView();
        EditText addTask = view.findViewById(R.id.addtask);
        EditText addDes = view.findViewById(R.id.addDiscription);
        Button btn_save = view.findViewById(R.id.btn_save_add);
        Button btn_cancel = view.findViewById(R.id.btn_cancel_add);
        dialogPlus.show();


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = addTask.getText().toString().trim();
                if (TextUtils.isEmpty(task)) {
                    addTask.setError("Task Required");
                    return;
                } else {
                    Map<String ,Object> map = new HashMap<>();
                    map.put("taskname", addTask.getText().toString());
                    map.put("description", addDes.getText().toString());
                    map.put("date", DateFormat.getDateInstance().format(new Date()));

                    FirebaseDatabase.getInstance().getReference().child("Task").push()
                            .setValue(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Data add successfully", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Data add failed", Toast.LENGTH_SHORT).show();

                                }
                            });
                    dialogPlus.dismiss();
                }
            }
        });
//        addTast.setText("");
//        addDes.setText("");

    }

    private void txtSearch(String str){
        FirebaseRecyclerOptions<Task> options =
                new FirebaseRecyclerOptions.Builder<Task>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Task").orderByChild("taskname").startAt(str).endAt(str+"~"), Task.class)
                        .build();
        mTaskAdapter = new TaskAdapter(options);
        mTaskAdapter.startListening();
        rv_myCards.setAdapter(mTaskAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mTaskAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mTaskAdapter.stopListening();

    }
}