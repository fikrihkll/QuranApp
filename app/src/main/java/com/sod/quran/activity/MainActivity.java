package com.sod.quran.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.sod.quran.R;
import com.sod.quran.db.QuranDB;
import com.sod.quran.db.QuranDbHelper;
import com.sod.quran.fragments.DashboardFragment;
import com.sod.quran.fragments.SettingFragment;
import com.sod.quran.fragments.SuratJuzFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sod.quran.model.QuranModel;
import com.sod.quran.services.AudioService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DashboardFragment.OnFragmentInteractionListener {

    private QuranDB quranDB;
    private QuranDbHelper dbHelper;
    private SQLiteDatabase db;
    private BottomNavigationView bnav;
    private Dialog loadingDl;
    private Toolbar toolbar;
    private Boolean exit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        toolbar=findViewById( R.id.toolbar );
        toolbar.setTitle( "Al-Quran" );
        toolbar.setTitleTextColor( getResources().getColor( R.color.colorPrimary  ));

        bnav=findViewById( R.id.bnav );
        bnav.setOnNavigationItemSelectedListener( bnavListener );
        dbHelper=new QuranDbHelper( this );

        SharedPreferences settings = getSharedPreferences("myalqruan", MODE_PRIVATE);
        if (settings.getBoolean("firsttime", true)) {
            settings.edit().putBoolean("firsttime", false).commit();
            initData();
        }

        //Inflate Dashboard
        DashboardFragment fragment=new DashboardFragment( );
        FragmentManager fm=getSupportFragmentManager();
        if(fm!=null){
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace( R.id.frameContainer,fragment,DashboardFragment.class.getSimpleName() );
            ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );
            ft.commit();
        }

        requestAppPermissions();
    }

    BottomNavigationView.OnNavigationItemSelectedListener bnavListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.mDashboard:
                    DashboardFragment Dfragment=new DashboardFragment();
                    FragmentManager fm=getSupportFragmentManager();
                    if(fm!=null){
                        FragmentTransaction ft=fm.beginTransaction();
                        ft.replace( R.id.frameContainer,Dfragment,DashboardFragment.class.getSimpleName() );
                        ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE );
                        ft.commit();
                    }
                    break;
                case R.id.mSurah:
                    SuratJuzFragment fragment=new SuratJuzFragment( );
                    FragmentManager fm2=getSupportFragmentManager();
                    if(fm2!=null){
                        FragmentTransaction ft=fm2.beginTransaction();
                        ft.replace( R.id.frameContainer,fragment,SuratJuzFragment.class.getSimpleName() );
                        ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE );
                        ft.commit();
                    }
                    break;
                case R.id.mSetting:
                    SettingFragment Sfragment=new SettingFragment( );
                    FragmentManager fm3=getSupportFragmentManager();
                    if(fm3!=null){
                        FragmentTransaction ft=fm3.beginTransaction();
                        ft.replace( R.id.frameContainer,Sfragment, SettingFragment.class.getSimpleName() );
                        ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE );
                        ft.commit();
                    }
                    break;
            }
            return true;
        }
    };

    public void showLoading(){
        loadingDl=new Dialog( this );
        loadingDl.getWindow().setBackgroundDrawableResource(R.color.transparent  );
        loadingDl.setContentView( R.layout.dialog_loading );
        loadingDl.setCancelable( false );
        loadingDl.show();
    }

    public void hideLoading(){
        if(loadingDl!=null)
            loadingDl.hide();
    }

    public void insertFromFile(Context context) {
        try {
            InputStream insertsStream = context.getResources().openRawResource(R.raw.quran);
            BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

            db.beginTransaction();

            while (insertReader.ready()) {
                try {
                    String insertStmt = insertReader.readLine();
                    db.execSQL(insertStmt);
                }catch (Exception e){

                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            insertReader.close();

            //TRANSLATION
            InputStream insertsStream2 = context.getResources().openRawResource(R.raw.malay);
            BufferedReader insertReader2 = new BufferedReader(new InputStreamReader(insertsStream2));

            db.beginTransaction();

            while (insertReader2.ready()) {
                try{
                    String insertStmt = insertReader2.readLine();
                    db.execSQL(insertStmt);
                }catch (Exception e){

                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            insertReader2.close();

            //TRANSLATION- En ahmed_ali
            InputStream insertsStream3 = context.getResources().openRawResource(R.raw.en_ahmed_ali);
            BufferedReader insertReader3 = new BufferedReader(new InputStreamReader(insertsStream3));

            db.beginTransaction();

            while (insertReader3.ready()) {
                try{
                    String insertStmt = insertReader3.readLine();
                    db.execSQL(insertStmt);
                }catch (Exception e){

                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            insertReader3.close();

            InputStream insertsStream4 = context.getResources().openRawResource(R.raw.aud_url);
            BufferedReader insertReader4 = new BufferedReader(new InputStreamReader(insertsStream4));

            db.beginTransaction();

            while (insertReader4.ready()) {
                try{
                    String insertStmt = insertReader4.readLine();
                    db.execSQL(insertStmt);
                }catch (Exception e){

                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            insertReader4.close();

            //TRANSLATION - en Arberry
           /* InputStream insertsStream4 = context.getResources().openRawResource(R.raw.en_arberry);
            BufferedReader insertReader4 = new BufferedReader(new InputStreamReader(insertsStream4));

            db.beginTransaction();

            while (insertReader4.ready()) {
                try{
                    String insertStmt = insertReader4.readLine();
                    db.execSQL(insertStmt);
                }catch (Exception e){

                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            insertReader4.close();*/

            //TRANSLATION - en yusuf ali
            InputStream insertsStream5 = context.getResources().openRawResource(R.raw.en_yusuf_ali);
            BufferedReader insertReader5 = new BufferedReader(new InputStreamReader(insertsStream5));

            db.beginTransaction();

            while (insertReader5.ready()) {
                try{
                    String insertStmt = insertReader5.readLine();
                    db.execSQL(insertStmt);
                }catch (Exception e){

                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            insertReader5.close();

            //TRANSLATION - indo
            InputStream insertsStream6 = context.getResources().openRawResource(R.raw.indonesia);
            BufferedReader insertReader6 = new BufferedReader(new InputStreamReader(insertsStream6));

            db.beginTransaction();

            while (insertReader6.ready()) {
                try{
                    String insertStmt = insertReader6.readLine();
                    db.execSQL(insertStmt);
                }catch (Exception e){

                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            insertReader6.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void initData() {
        quranDB = new QuranDB(this);
        db = quranDB.getWritableDatabase();
        new backgroundProccess().execute();
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.FOREGROUND_SERVICE}, 4326 ); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission( getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission( getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
            return;
        }

        exit = true;
        Toast.makeText( getApplicationContext(),"Press BACK again to Exit",Toast.LENGTH_LONG ).show();

        new Handler().postDelayed( new Runnable() {

            @Override
            public void run() {
                exit=false;
            }
        }, 2000);
    }

    @Override
    public void sendMsg(String str) {
        Toast.makeText( getApplicationContext(),str,Toast.LENGTH_LONG ).show();
    }

    private class backgroundProccess extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            insertFromFile( MainActivity.this );
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            showLoading();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute( aVoid );
            setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            hideLoading();
        }
    }
}
