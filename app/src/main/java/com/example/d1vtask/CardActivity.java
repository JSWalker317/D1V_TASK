package com.example.d1vtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

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

public class CardActivity extends AppCompatActivity {
    private RecyclerView rv_myCards;
    private TaskAdapter mTaskAdapter;
    private SearchView sv_search;
    private Button btn_add_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        rv_myCards = findViewById(R.id.rv_MyCards);
        sv_search = findViewById(R.id.sv_search_card);
        btn_add_card = findViewById(R.id.btn_add_card);

        Toolbar toolbar = findViewById(R.id.toolbar_board);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cards");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv_myCards.setLayoutManager(new LinearLayoutManager(CardActivity.this));

        FirebaseRecyclerOptions<Task> options =
                new FirebaseRecyclerOptions.Builder<Task>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("boards").child("task").orderByChild("taskname"), Task.class)
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

    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addCard() {
        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(this);
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

                    FirebaseDatabase.getInstance().getReference().child("boards").child("task").push()
                            .setValue(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(CardActivity.this, "Data add successfully", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CardActivity.this, "Data add failed", Toast.LENGTH_SHORT).show();

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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("boards").child("task").orderByChild("taskname").startAt(str).endAt(str+"~"), Task.class)
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