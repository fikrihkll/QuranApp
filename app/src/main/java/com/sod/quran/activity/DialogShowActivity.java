package com.sod.quran.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;

import com.sod.quran.R;
import com.sod.quran.fragments.ListenFragment;

import static com.sod.quran.activity.SuratViewActivity.AYAT_TOTAL_LISTEN_KEY;
import static com.sod.quran.activity.SuratViewActivity.LISTEN_KEY;

public class DialogShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_dialog_show );


    }
}
