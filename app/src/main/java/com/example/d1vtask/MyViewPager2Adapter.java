package com.example.d1vtask;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.d1vtask.fragment.AccountFragment;
import com.example.d1vtask.fragment.BoardsFragment;
import com.example.d1vtask.fragment.MyCardsFragment;
import com.example.d1vtask.fragment.NotificationFragment;
import com.example.d1vtask.fragment.SearchFragment;

public class MyViewPager2Adapter extends FragmentStateAdapter {
    public MyViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BoardsFragment();
            case 1:
                return new MyCardsFragment();
            case 2:
                return new SearchFragment();
            case 3:
                return new NotificationFragment();
            case 4:
                return new AccountFragment();
            default:
                return new BoardsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
