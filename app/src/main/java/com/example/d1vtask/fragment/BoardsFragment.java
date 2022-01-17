package com.example.d1vtask.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d1vtask.CardActivity;
import com.example.d1vtask.R;
import com.example.d1vtask.adapter.BoardAdapter;
import com.example.d1vtask.inteface.OnItemClickListener;
import com.example.d1vtask.model.Board;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class BoardsFragment extends Fragment {
    private RecyclerView rv_YourBoard;
    private BoardAdapter mBoardAdapter;
    private SearchView sv_search_Board;
//    private Button btn_add_board;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.boards_fragment, container, false);
        setHasOptionsMenu(true);
        rv_YourBoard = view.findViewById(R.id.rv_YourBoard);
        sv_search_Board = view.findViewById(R.id.sv_search_board);
//        btn_add_board = view.findViewById(R.id.bt);

        rv_YourBoard.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerOptions<Board> options =
                new FirebaseRecyclerOptions.Builder<Board>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("boards").orderByChild("board"), Board.class)
                        .build();

        mBoardAdapter = new BoardAdapter(options, new OnItemClickListener() {
            @Override
            public void onItemClick(Board board, int position) {
                startActivity(new Intent(getActivity(), CardActivity.class));
            }
        });
        rv_YourBoard.setAdapter(mBoardAdapter);


        sv_search_Board.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

//        btn_add_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addCard();
//            }
//        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add, menu) ;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) ;
        {
            addBoard();
            Toast.makeText(getActivity(), "Data add", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addBoard() {
        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(getActivity());
        dialogPlusBuilder.setContentHolder(new ViewHolder(R.layout.input_board));
        dialogPlusBuilder.setExpanded(true,1000);
        final DialogPlus dialogPlus = dialogPlusBuilder
                .create();

        View view = dialogPlus.getHolderView();
        EditText addBoard = view.findViewById(R.id.edt_add_board);
        TextView createBoard = view.findViewById(R.id.tv_create_board);

        dialogPlus.show();

        createBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = addBoard.getText().toString().trim();
                if (TextUtils.isEmpty(task)) {
                    addBoard.setError("Task Required");
                    return;
                } else {
                    Map<String ,Object> map = new HashMap<>();
                    map.put("board", addBoard.getText().toString());

                    FirebaseDatabase.getInstance().getReference().child("boards").push()
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
        FirebaseRecyclerOptions<Board> options =
                new FirebaseRecyclerOptions.Builder<Board>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("boards").orderByChild("board").startAt(str).endAt(str+"~"), Board.class)
                        .build();
        mBoardAdapter = new BoardAdapter(options, new OnItemClickListener() {
            @Override
            public void onItemClick(Board board, int position) {
                startActivity(new Intent(getActivity(), CardActivity.class));

            }
        });
        mBoardAdapter.startListening();
        rv_YourBoard.setAdapter(mBoardAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        mBoardAdapter.startListening();
        rv_YourBoard.setAdapter(mBoardAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBoardAdapter.stopListening();

    }
}
