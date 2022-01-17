package com.example.d1vtask.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d1vtask.CardActivity;
import com.example.d1vtask.R;
import com.example.d1vtask.adapter.BoardAdapter;
import com.example.d1vtask.adapter.TaskAdapter;
import com.example.d1vtask.inteface.OnItemClickListener;
import com.example.d1vtask.model.Board;
import com.example.d1vtask.model.Task;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class SearchFragment extends Fragment {
    private RecyclerView rv_total_Cards;
    private RecyclerView rv_total_Boards;

    private TaskAdapter mTaskAdapter;
    private BoardAdapter mBoardAdapter;
    private SearchView sv_search_total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        rv_total_Cards = view.findViewById(R.id.rv_total_card);
        rv_total_Boards = view.findViewById(R.id.rv_total_board);

        sv_search_total = view.findViewById(R.id.sv_search_total);

        rv_total_Cards.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_total_Boards.setLayoutManager(new LinearLayoutManager(getActivity()));

//
        sv_search_total.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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


        return view;
    }

    private void txtSearch(String str) {
        FirebaseRecyclerOptions<Task> options =
                new FirebaseRecyclerOptions.Builder<Task>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("boards").child("task").orderByChild("taskname").startAt(str).endAt(str + "~"), Task.class)
                        .build();
        mTaskAdapter = new TaskAdapter(options);
        mTaskAdapter.startListening();
        rv_total_Cards.setAdapter(mTaskAdapter);

        FirebaseRecyclerOptions<Board> options1 =
                new FirebaseRecyclerOptions.Builder<Board>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("boards").orderByChild("board").startAt(str).endAt(str+"~"), Board.class)
                        .build();
        mBoardAdapter = new BoardAdapter(options1, new OnItemClickListener() {
            @Override
            public void onItemClick(Board board, int position) {

            }
        });
        mBoardAdapter.startListening();
        rv_total_Boards.setAdapter(mBoardAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseRecyclerOptions<Task> options =
                new FirebaseRecyclerOptions.Builder<Task>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("boards").child("task").orderByChild("taskname").startAt("*").endAt("*" + "~"), Task.class)
                        .build();

        mTaskAdapter = new TaskAdapter(options);
        rv_total_Cards.setAdapter(mTaskAdapter);
        //
        FirebaseRecyclerOptions<Board> options1 =
                new FirebaseRecyclerOptions.Builder<Board>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("boards").orderByChild("board").startAt("*").endAt("*" + "~"), Board.class)
                        .build();
        mBoardAdapter = new BoardAdapter(options1, new OnItemClickListener() {
            @Override
            public void onItemClick(Board board, int position) {

            }
        });
        rv_total_Boards.setAdapter(mBoardAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
//        mTaskAdapter.stopListening();


    }

}