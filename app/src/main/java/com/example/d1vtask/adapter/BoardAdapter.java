package com.example.d1vtask.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d1vtask.LoginActivity;
import com.example.d1vtask.MainActivity;
import com.example.d1vtask.R;
import com.example.d1vtask.RegisterActivity;
import com.example.d1vtask.inteface.OnItemClickListener;
import com.example.d1vtask.model.Board;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BoardAdapter extends FirebaseRecyclerAdapter<Board, BoardAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     *
     *
     */

    private OnItemClickListener mListener;
    public BoardAdapter(@NonNull FirebaseRecyclerOptions<Board> options, OnItemClickListener listener) {
        super(options);
        this.mListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull BoardAdapter.myViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i, @NonNull Board board) {
        myViewHolder.tv_board.setText(board.getBoard());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(board, i);
//                Toast.makeText(myViewHolder.tv_board.getContext(), "go to board activity", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @NonNull
    @Override
    public BoardAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false);

        return new BoardAdapter.myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView tv_board;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_board = itemView.findViewById(R.id.tv_board);

        }
    }
}
