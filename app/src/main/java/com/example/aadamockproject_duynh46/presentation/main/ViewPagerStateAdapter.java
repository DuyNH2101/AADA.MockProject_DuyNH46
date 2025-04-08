package com.example.aadamockproject_duynh46.presentation.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerStateAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> fragmentArrayList;

    public ViewPagerStateAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> fragmentArrayList) {
        super(fragmentActivity);
        this.fragmentArrayList = fragmentArrayList;
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }
    @Override
    public long getItemId(int position){
        return fragmentArrayList.get(position).hashCode();
    }
    @Override
    public boolean containsItem(long itemId){
        for(Fragment f:fragmentArrayList){
            if(itemId==f.hashCode()){
                return true;
            }
        }
        return false;
    }
}
