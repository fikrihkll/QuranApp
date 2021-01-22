package com.sod.quran.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sod.quran.R;
import com.sod.quran.adapter.ViewPagerSjAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuratJuzFragment extends Fragment {

    ViewPager vp;
    TextView tabSurat,tabJuz;

    public SuratJuzFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate( R.layout.fragment_surat_juz, container, false );

        vp=v.findViewById( R.id.vpSJ );
        vp.setAdapter( new ViewPagerSjAdapter( getChildFragmentManager(),ViewPager.OVER_SCROLL_ALWAYS ) );
        tabSurat=v.findViewById( R.id.tabSurat );
        tabJuz=v.findViewById( R.id.tabJuz );

        listener();

        return v;
    }

    private void listener(){
        tabSurat.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vp.setCurrentItem( 0,true );

            }
        } );

        tabJuz.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vp.setCurrentItem( 1,true );
            }
        } );

        vp.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==0){
                    tabSurat.setTextColor( getResources().getColor( android.R.color.white ) );
                    tabJuz.setTextColor( getResources().getColor( R.color.colorPrimary ) );
                    tabSurat.setBackground( getContext().getResources().getDrawable( R.drawable.bg_tab_left_selected, getContext().getTheme()) );
                    tabJuz.setBackground( getContext().getResources().getDrawable( R.drawable.bg_tab_right, getContext().getTheme()) );
                }else{
                    tabSurat.setTextColor( getResources().getColor( R.color.colorPrimary ) );
                    tabJuz.setTextColor( getResources().getColor( android.R.color.white) );
                    tabSurat.setBackground( getContext().getResources().getDrawable( R.drawable.bg_tab_left, getContext().getTheme()) );
                    tabJuz.setBackground( getContext().getResources().getDrawable( R.drawable.bg_tab_right_selected, getContext().getTheme()) );
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        } );
    }

}
