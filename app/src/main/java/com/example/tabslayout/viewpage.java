package com.example.tabslayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewpage extends FragmentStateAdapter {
    public viewpage(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new HomeFrag();
            case 1:
                return new GraphFrag();
            case 2:
                return new CategoryFrag();
            case 3:
                return new ProfileFrag();
        }


        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
