package com.sod.quran.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sod.quran.R;
import com.sod.quran.model.SuratData;

import static android.content.Context.MODE_PRIVATE;
import static com.sod.quran.activity.SuratViewActivity.SETTING_LANG_KEY;
import static com.sod.quran.activity.SuratViewActivity.SETTING_T_SIZE_KEY;
import static com.sod.quran.activity.SuratViewActivity.SHARED_PREF_NAME;

public class SettingFragment extends Fragment {

    private float tSize=40;
    private String language;

    public SettingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate( R.layout.dialog_setting,container,false );

        SharedPreferences sp=getContext().getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
        language=sp.getString( SETTING_LANG_KEY,"en_AhAli" );
        tSize=sp.getFloat( SETTING_T_SIZE_KEY,40 );

        final TextView tvSuratSample,tvTransSample;
        final Spinner spFont,spSize,spLang;
        final ArrayAdapter<String> fontAdapter,sizeAdapter,langAdapter;
        Switch swTrans;
        ImageView btnBack;

        tvSuratSample=view.findViewById( R.id.tvAyatSampleST );
        tvTransSample=view.findViewById( R.id.tvTransSampleST );
        spFont=view.findViewById( R.id.spFontTypeST );
        spSize=view.findViewById( R.id.spTextSizeST );
        spLang=view.findViewById( R.id.spLangST );
        swTrans=view.findViewById( R.id.swTransST );
        btnBack=view.findViewById( R.id.btnBackSettdl );
        swTrans.setVisibility( View.GONE );
        btnBack.setVisibility( View.GONE );

        fontAdapter=new ArrayAdapter<>( getContext(),android.R.layout.simple_list_item_1 , SuratData.fontName);
        fontAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spFont.setAdapter( fontAdapter );

        sizeAdapter=new ArrayAdapter<>( getContext(),android.R.layout.simple_list_item_1,SuratData.size);
        sizeAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spSize.setAdapter( sizeAdapter );

        langAdapter=new ArrayAdapter<>( getContext(),android.R.layout.simple_list_item_1,SuratData.lang);
        langAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spLang.setAdapter( langAdapter );

        //Listener

        //Check Size
        if(tSize==42){
            spSize.setSelection( 1 );
        }else if(tSize==44){
            spSize.setSelection( 2 );
        }else if(tSize==46){
            spSize.setSelection( 3 );
        }

        //Check Lang
        if(language.equals( "en_YusAli" )){
            spLang.setSelection( 1 );
        }else if(language.equals( "IDN" )){
            spLang.setSelection( 2 );
        }else if(language.equals( "Malay" )){
            spLang.setSelection( 3 );
        }

        spLang.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0 && !language.equals( "en_AhAli" )){

                    language="en_AhAli";
                    SharedPreferences sp=getContext().getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
                    SharedPreferences.Editor ed=sp.edit();
                    ed.putString( SETTING_LANG_KEY, "en_AhAli");
                    ed.apply();

                }if(position==1 && !language.equals( "en_YusAli" )){
                    language="en_YusAli";
                    SharedPreferences sp=getContext().getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
                    SharedPreferences.Editor ed=sp.edit();
                    ed.putString( SETTING_LANG_KEY, "en_YusAli");
                    ed.apply();

                }if(position==2 && !language.equals( "IDN" )){
                    language="IDN";
                    SharedPreferences sp=getContext().getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
                    SharedPreferences.Editor ed=sp.edit();
                    ed.putString( SETTING_LANG_KEY, "IDN");
                    ed.apply();

                }if(position==3 && !language.equals( "Malay" )){
                    language="Malay";
                    SharedPreferences sp=getContext().getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
                    SharedPreferences.Editor ed=sp.edit();
                    ed.putString( SETTING_LANG_KEY, "Malay");
                    ed.apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        spSize.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sp=getContext().getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
                if(position==0){
                    if(tSize==40){

                    }else{
                        tSize=40;
                    }
                }else if(position==1){
                    if(tSize==42){

                    }else{
                        tSize=42;
                    }
                }else if(position==2){
                    if(tSize==44){

                    }else{
                        tSize=44;
                    }
                }else if(position==3){
                    if(tSize==46){

                    }else{
                        tSize=46;
                    }
                }
                SharedPreferences.Editor ed=sp.edit();
                ed.putFloat( SETTING_T_SIZE_KEY,tSize );
                ed.apply();

                tvTransSample.setTextSize( tSize-22 );
                tvSuratSample.setTextSize( tSize);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        return view;
    }
}
