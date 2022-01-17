package com.example.d1vtask.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d1vtask.R;
import com.example.d1vtask.model.Task;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

public class TaskAdapter extends FirebaseRecyclerAdapter<Task, TaskAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TaskAdapter(@NonNull FirebaseRecyclerOptions<Task> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i, @NonNull Task task) {
        myViewHolder.tv_Taskname.setText(task.getTaskname());
        myViewHolder.tv_description.setText(task.getDescription());
        myViewHolder.tv_date.setText(task.getDate());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(myViewHolder.tv_Taskname.getContext());
                dialogPlusBuilder.setContentHolder(new ViewHolder(R.layout.update_data));
                dialogPlusBuilder.setExpanded(true, 1000);
                final DialogPlus dialogPlus = dialogPlusBuilder
                        .create();

                View view = dialogPlus.getHolderView();
                EditText edt_taskname = view.findViewById(R.id.edt_update_taskname);
                EditText edt_description = view.findViewById(R.id.edt_update_description);
                Button btn_Update = view.findViewById(R.id.btn_Update);
                Button btn_delete = view.findViewById(R.id.btn_Delete);

                edt_taskname.setText(task.getTaskname());
                edt_description.setText(task.getDescription());

                dialogPlus.show();
                btn_Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("taskname", edt_taskname.getText().toString());
                        map.put("description", edt_description.getText().toString());
                        map.put("date", DateFormat.getDateInstance().format(new Date()));

                        FirebaseDatabase.getInstance().getReference().child("boards").child("task")
                                .child(getRef(i).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(myViewHolder.tv_Taskname.getContext(), "Data has been updated successfully", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(myViewHolder.tv_Taskname.getContext(), "Data update failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        dialogPlus.dismiss();

                    }
                });
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(myViewHolder.tv_Taskname.getContext());
                        builder.setTitle("Are you sure !");
                        builder.setMessage("Delete data can't be undo");

                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("boards").child("task")
                                        .child(getRef(i).getKey()).removeValue();
                                dialogPlus.dismiss();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(myViewHolder.tv_Taskname.getContext(), "Canceled", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }
                        });
                        builder.show();
                    }
                });
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Taskname, tv_description, tv_date;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Taskname = itemView.findViewById(R.id.tv_task);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_date = itemView.findViewById(R.id.tv_date);

        }
    }
}
