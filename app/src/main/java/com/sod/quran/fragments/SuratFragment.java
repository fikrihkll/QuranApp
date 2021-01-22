package com.sod.quran.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.sod.quran.activity.MainActivity;
import com.sod.quran.R;
import com.sod.quran.activity.SuratViewActivity;
import com.sod.quran.adapter.SuratAdapter;
import com.sod.quran.model.SuratData;
import com.sod.quran.viewmodel.ViewModelSurat;

import java.util.ArrayList;


public class SuratFragment extends Fragment {

    private MainActivity ma;
    private ViewModelSurat vm;
    private ArrayList<String> list;
    private SuratAdapter adapter;
    private RecyclerView rc;
    private AutoCompleteTextView etSearch;

    public SuratFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate( R.layout.fragment_surat, container, false );

        ma=(MainActivity) getActivity();

        vm = ViewModelProviders.of(this).get(ViewModelSurat.class);
        vm.getSurat().observe( this, getSurat);

        list=new ArrayList<>(  );
        etSearch=view.findViewById( R.id.etSearch );

        rc=view.findViewById( R.id.rcSurat );
        rc.setLayoutManager( new LinearLayoutManager( getContext() ) );

        vm.setListSurat();

        ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, SuratData.surat );
        etSearch.setAdapter(adapterSearch);

        listener();
        return view;
    }

    private void listener(){
        etSearch.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition( position );
                int pos = -1;

                for (int j = 0; j < SuratData.surat.length; j++) {
                    if (SuratData.surat[j].equals( selection )) {
                        pos = j;
                        break;
                    }
                }

                Intent in=new Intent( getContext(), SuratViewActivity.class );
                in.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,pos );
                getContext().startActivity( in );
            }
        } );
    }

    private Observer<ArrayList<String>> getSurat=new Observer<ArrayList<String>>() {
        @Override
        public void onChanged(ArrayList<String> strings) {
            list=strings;
            adapter=new SuratAdapter( getContext(),list);
            rc.setAdapter( adapter );
        }
    };

}
