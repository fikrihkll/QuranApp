package com.sod.quran.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sod.quran.fragments.JuzFragment;
import com.sod.quran.fragments.SuratFragment;

public class ViewPagerSjAdapter extends FragmentPagerAdapter {
  public ViewPagerSjAdapter(@NonNull FragmentManager fm, int behavior) {
    super( fm, behavior );
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    SuratFragment sr=new SuratFragment();
    JuzFragment jz=new JuzFragment();

    if(position==1)
      return jz;
    return sr;
  }

  @Override
  public int getCount() {
    return 2;
  }
}
