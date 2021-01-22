package com.sod.quran.fragments;


import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sod.quran.R;
import com.sod.quran.activity.SuratViewActivity;
import com.sod.quran.broadcastreceiver.AudioPPBroadcast;
import com.sod.quran.db.QuranDbHelper;
import com.sod.quran.model.QuranModel;
import com.sod.quran.model.SuratData;
import com.sod.quran.services.AudioService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListenFragment extends DialogFragment {

    private int pos=0;
    private int totalAyat=0;

    private SeekBar seekBar;
    private Boolean isSeekBarTouched=false;
    private Boolean first=true;
    private String[] surahName;
    private String[][] ayatName;
    private String[] audioUrl;
    private Spinner spAyat;
    private ArrayAdapter<String> spAdapter;
    private TextView tvTitle,btnNewPlay;
    private ImageView btnPlay,btnClose;

    public ListenFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate( R.layout.fragment_listen, container, false );

        pos=getArguments().getInt( SuratViewActivity.LISTEN_KEY);
        totalAyat=getArguments().getInt( SuratViewActivity.AYAT_TOTAL_LISTEN_KEY);

        btnNewPlay=view.findViewById( R.id.btnNewPlay );
        btnClose=view.findViewById( R.id.btnBackLN );
        btnPlay=view.findViewById( R.id.btnPlayLN );
        seekBar=view.findViewById( R.id.pgbLN );
        spAyat=view.findViewById( R.id.spAyatLN );
        tvTitle=view.findViewById( R.id.tvTitleLN );

        getData();
        listener();
        LocalBroadcastManager.getInstance( getContext() ).registerReceiver( bcPlayingNoew, new IntentFilter( "PLAYINGNOW" ) );
        LocalBroadcastManager.getInstance( getContext() ).registerReceiver( bcButtonIcon, new IntentFilter( "PP_IC_PLAY" ) );
        LocalBroadcastManager.getInstance( getContext() ).registerReceiver( bcButtonIconPause, new IntentFilter( "PP_IC_PAUSE" ) );
        LocalBroadcastManager.getInstance( getContext() ).registerReceiver( durationRec, new IntentFilter( AudioService.BC_DURATION ) );
        return view;
    }

    private void getData(){
        ArrayList<QuranModel> data=new ArrayList<>(  );
        try {
            data=new QuranDbHelper( getContext() ).getAudioUrl( "SuratID",pos+1 );

            audioUrl=new String[totalAyat];
            for(Integer i=0;i<totalAyat;i++){
                audioUrl[i]=data.get( i ).getAyatText();
            }

            if(getDialog()!=null){
                spAdapter=new ArrayAdapter<>( getContext(),android.R.layout.simple_list_item_1 );
                spAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                spAyat.setAdapter( spAdapter );

                spAdapter.add( "-" );
                for(Integer i=0;i<totalAyat;i++){
                    spAdapter.add( Integer.toString( i+1 ) );
                }


            }

            SharedPreferences sharedPref = getContext().getSharedPreferences( "AudServ", MODE_PRIVATE );
            int recentSurat=sharedPref.getInt( AudioService.RECENT_SURAT_KEY,1 );
            if(recentSurat!=pos){
                Intent in2=new Intent( getContext(), AudioService.class );
                getContext().stopService( in2 );
            }else{
                spAyat.setSelection( sharedPref.getInt( AudioService.RECENT_AYAT_KEY,0 )+1 );
                tvTitle.setText( SuratData.surat[sharedPref.getInt( AudioService.RECENT_SURAT_KEY,1 )] );
            }

            tvTitle.setText( SuratData.surat[pos] );
        }catch (Exception e){
            tvTitle.setText( "Audio Url is Being Prepared, Please Wait For a Few Minutes(Re-Open this Menu)" );
        }
    }

    private void listener(){
        btnNewPlay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spAdapter!=null && spAdapter.getCount()!=0 && spAyat.getSelectedItemPosition()!=0){
                    Intent in=new Intent( getContext(),AudioService.class );
                    in.putExtra( AudioService.SURAT_POS_EXTRA, pos );
                    in.putExtra( AudioService.AYAT_POS_EXTRA, spAyat.getSelectedItemPosition()-1 );
                    in.putExtra( AudioService.FILE_PATH_EXTRA,audioUrl[spAyat.getSelectedItemPosition()-1] );
                    in.putExtra( AudioService.TOTAL_AYAT_EXTRA,totalAyat );
                    getContext().startService( in );

                    setPlay();
                }
            }
        } );

        btnClose.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        } );

        btnPlay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spAdapter!=null && spAdapter.getCount()!=0 && spAyat.getSelectedItemPosition()!=0){
                    if(!isMyServiceRunning()){
                        Intent in=new Intent( getContext(),AudioService.class );
                        in.putExtra( AudioService.SURAT_POS_EXTRA, pos );
                        in.putExtra( AudioService.AYAT_POS_EXTRA, spAyat.getSelectedItemPosition()-1 );
                        in.putExtra( AudioService.FILE_PATH_EXTRA,audioUrl[spAyat.getSelectedItemPosition()-1] );
                        in.putExtra( AudioService.TOTAL_AYAT_EXTRA,totalAyat );
                        getContext().startService( in );

                        setPlay();
                    }else{
                        getContext().sendBroadcast( new Intent( getContext(), AudioPPBroadcast.class ) );
                        //LocalBroadcastManager.getInstance( getContext() ).sendBroadcast( new Intent( "PLAY/PAUSE" ) );
                    }
                }
            }
        } );

        seekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //TODO Send Broadcast to Service, Seek the Player

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarTouched=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekBarTouched=false;
                Intent in=new Intent( "SEEKTO" );
                in.putExtra( "SEEK_EXTRA",seekBar.getProgress() );
                LocalBroadcastManager.getInstance( getContext() ).sendBroadcast( in );
            }
        } );

        spAyat.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
    }

    public void setPlay(){
        btnPlay.setImageURI( getUriToDrawable( getContext(),R.drawable.ic_pause_black_24dp ) );
    }

    public void setPause(){
        btnPlay.setImageURI( getUriToDrawable( getContext(),R.drawable.ic_play_arrow_black_24dp) );
    }

    private BroadcastReceiver bcPlayingNoew=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            spAyat.setSelection( intent.getIntExtra( AudioService.RECENT_AYAT_KEY,0 )+1 );
        }
    };

    private BroadcastReceiver bcButtonIcon=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setPlay();

        }
    };
    private BroadcastReceiver bcButtonIconPause=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setPause();
        }
    };

    private BroadcastReceiver durationRec=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!isSeekBarTouched){
                seekBar.setProgress( intent.getIntExtra( AudioService.RECENT_DURATION,1 ) );
            }

            seekBar.setMax( intent.getIntExtra( AudioService.RECENT_TOTAL_DURATION,100  ));
            if(intent.getBooleanExtra( "STATE",false )){
                setPlay();
            }else{

            }
        }
    };

    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse( ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Dialog d = (Dialog) dialog;
                d.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog );
            }
        });
        return d;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance( getContext() ).unregisterReceiver( durationRec );
        LocalBroadcastManager.getInstance( getContext() ).unregisterReceiver( bcButtonIcon );
        LocalBroadcastManager.getInstance( getContext() ).unregisterReceiver( bcButtonIconPause );
        LocalBroadcastManager.getInstance( getContext() ).unregisterReceiver( bcPlayingNoew );
    }

    private Boolean isMyServiceRunning() {
        final ActivityManager activityManager = (ActivityManager) getContext().getSystemService( ACTIVITY_SERVICE );
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices( Integer.MAX_VALUE );

        for (int i = 0; i < services.size(); i++) {
            if ("com.sod.quran".equals( services.get( i ).service.getPackageName() )) {

                if ("com.sod.quran.services.AudioService".equals( services.get( i ).service.getClassName() )) {
                    return true;
                }
            }
        }
        return false;
    }

}
