package com.example.d1vtask.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    String board;
    ArrayList<Task> mArrayList;

    Board() {

    }

    public Board(String board, ArrayList<Task> mArrayList) {
        this.board = board;
        this.mArrayList = mArrayList;
    }

    public ArrayList<Task> getmArrayList() {
        return mArrayList;
    }

    public void setmArrayList(ArrayList<Task> mArrayList) {
        this.mArrayList = mArrayList;
    }


    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }
}
