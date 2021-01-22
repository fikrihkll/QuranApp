package com.sod.quran.services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;

import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import android.widget.RemoteViews;


import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.sod.quran.broadcastreceiver.AudioCloseBroadcast;
import com.sod.quran.broadcastreceiver.AudioNextBroadcast;

import com.sod.quran.R;
import com.sod.quran.activity.MainActivity;
import com.sod.quran.broadcastreceiver.AudioPPBroadcast;
import com.sod.quran.broadcastreceiver.AudioPrevBroadcast;
import com.sod.quran.model.SuratData;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;

import com.google.android.exoplayer2.upstream.DataSource;

import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class AudioService extends Service {
    public static String BC_DURATION="BC_DURATION_KEY";
    public static String RECENT_DURATION="RECENT_DURATION_KEY";
    public static String RECENT_TOTAL_DURATION="RECENT_TOTAL_DURATION_KEY";

    public static String RECENT_SURAT_KEY="RECENT_SURAT_KEY";
    public static String RECENT_AYAT_KEY="RECENT_AYAT_KEY";
    public static String FILE_PATH_EXTRA="FILEPATH_EXTRA_SERVICE";
    public static String SURAT_POS_EXTRA="SURAT_POS_SERVICE";
    public static String AYAT_POS_EXTRA="AYAT_POS_SERVICE";
    public static String TOTAL_AYAT_EXTRA="TOTAL_AYAT_SERVICE";

    private SimpleExoPlayer player;
    private String filePath="";
    private int suratPos=1;
    private int ayatPos=1;
    private int totalAyat=1;
    private Handler handler;
    private Runnable r;
    private Boolean isDestroyed=false;


    public AudioService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException( "Not yet implemented" );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        filePath=intent.getStringExtra( FILE_PATH_EXTRA );
        suratPos=intent.getIntExtra( SURAT_POS_EXTRA,0 );
        ayatPos=intent.getIntExtra( AYAT_POS_EXTRA,0 );
        totalAyat=intent.getIntExtra( TOTAL_AYAT_EXTRA,0 );

        buildNotifPlay();

        handler = new Handler();

        if (player != null) {
            player.release();
        }

        Uri uri = Uri.parse( filePath );
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(),
                Util.getUserAgent(getApplicationContext(), "Safar"));
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        player = ExoPlayerFactory.newSimpleInstance( getApplicationContext() );
        player.prepare(videoSource);
        player.setPlayWhenReady( true );

        player.addListener( new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    Intent in=new Intent( getApplicationContext(), AudioNextBroadcast.class );
                    in.putExtra( SURAT_POS_EXTRA,suratPos );
                    in.putExtra( AYAT_POS_EXTRA,ayatPos );
                    in.putExtra( TOTAL_AYAT_EXTRA,totalAyat );
                    sendBroadcast( in );
                }
            }
        } );

        durationUpdate();
        setData();

        try {
            LocalBroadcastManager.getInstance( getApplicationContext() ).unregisterReceiver( bcSeekTo );
            LocalBroadcastManager.getInstance( getApplicationContext() ).unregisterReceiver( bcPlayPause );
        }catch (Exception e){

        }
        LocalBroadcastManager.getInstance( getApplicationContext() ).registerReceiver( bcSeekTo,new IntentFilter( "SEEKTO" ) );
        LocalBroadcastManager.getInstance( getApplicationContext() ).registerReceiver( bcPlayPause,new IntentFilter( "PLAY/PAUSE" ) );
        return START_NOT_STICKY;
    }
    private void setData(){
        SharedPreferences sharedPref = getSharedPreferences( "AudServ", MODE_PRIVATE );
        SharedPreferences.Editor bd = sharedPref.edit();

        bd.putInt( RECENT_SURAT_KEY, suratPos );
        bd.putInt( RECENT_AYAT_KEY, ayatPos );

        bd.apply();

        Intent intentMA = new Intent( "PLAYINGNOW" );
        intentMA.putExtra( RECENT_AYAT_KEY, ayatPos );
        LocalBroadcastManager.getInstance( getApplicationContext() ).sendBroadcast( intentMA );
    }

    private void buildNotifPlay() {
        Intent notificationIntent = new Intent( this, MainActivity.class );

        //PENDING INTENT FOR NOTIFICATION
        PendingIntent pendingIntent = PendingIntent.getActivity( this, 0, notificationIntent, 0 );

        //INTENT ACTION FOR NOTIFICATION
        Intent broadcastPP = new Intent( this, AudioPPBroadcast.class );
        PendingIntent actionPP = PendingIntent.getBroadcast( this, 0, broadcastPP, PendingIntent.FLAG_UPDATE_CURRENT );

        //INTENT ACTION FOR NEXT
        Intent in=new Intent( getApplicationContext(), AudioNextBroadcast.class );
        in.putExtra( SURAT_POS_EXTRA,suratPos );
        in.putExtra( AYAT_POS_EXTRA,ayatPos );
        in.putExtra( TOTAL_AYAT_EXTRA,totalAyat );

        PendingIntent actionNext = PendingIntent.getBroadcast( this, 1, in, PendingIntent.FLAG_UPDATE_CURRENT );

        //INTENT ACTION FOR Prev
        Intent in2=new Intent( getApplicationContext(), AudioPrevBroadcast.class );
        in2.putExtra( SURAT_POS_EXTRA,suratPos );
        in2.putExtra( AYAT_POS_EXTRA,ayatPos );
        in2.putExtra( TOTAL_AYAT_EXTRA,totalAyat );
        PendingIntent actionPrev = PendingIntent.getBroadcast( this, 1, in2, PendingIntent.FLAG_UPDATE_CURRENT );


        Intent broadcastClose = new Intent( this, AudioCloseBroadcast.class );
        PendingIntent actionClose = PendingIntent.getBroadcast( this, 0, broadcastClose, PendingIntent.FLAG_UPDATE_CURRENT );



        //REMOTE VIEW
        final RemoteViews contentView = new RemoteViews( getPackageName(), R.layout.layout_notif );
        contentView.setTextViewText( R.id.ntSurat, SuratData.surat[suratPos] );
        contentView.setTextViewText( R.id.ntAyat, "Ayat "+Integer.toString( ayatPos+1 ));

        contentView.setImageViewUri( R.id.ntBtnPlay, getUriToResource( getApplicationContext(), R.drawable.ic_pause_black_24dp ) );

        contentView.setOnClickPendingIntent( R.id.ntBtnPlay, actionPP );
        contentView.setOnClickPendingIntent( R.id.ntBtnNext, actionNext );
        contentView.setOnClickPendingIntent( R.id.ntBtnPrev, actionPrev );
        contentView.setOnClickPendingIntent( R.id.ntBtnClose, actionClose );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel( "QuranAudioID", "QuranAudio", NotificationManager.IMPORTANCE_LOW );
            NotificationManager manager = getSystemService( NotificationManager.class );
            manager.createNotificationChannel( serviceChannel );
        }

        final Notification notification = new NotificationCompat.Builder( this, "QuranAudioID" ).
                setSmallIcon( R.mipmap.ic_launcher ).
                setContentIntent( pendingIntent ).
                setCustomContentView( contentView ).
                build();

        startForeground( 1337, notification );
    }
    private void buildNotifPause() {
        Intent notificationIntent = new Intent( this, MainActivity.class );

        //PENDING INTENT FOR NOTIFICATION
        PendingIntent pendingIntent = PendingIntent.getActivity( this, 0, notificationIntent, 0 );

        //INTENT ACTION FOR NOTIFICATION
        Intent broadcastPP = new Intent( this, AudioPPBroadcast.class );
        PendingIntent actionPP = PendingIntent.getBroadcast( this, 0, broadcastPP, PendingIntent.FLAG_UPDATE_CURRENT );

        //INTENT ACTION FOR NEXT
        Intent in=new Intent( getApplicationContext(), AudioNextBroadcast.class );
        in.putExtra( SURAT_POS_EXTRA,suratPos );
        in.putExtra( AYAT_POS_EXTRA,ayatPos );
        in.putExtra( TOTAL_AYAT_EXTRA,totalAyat );

        PendingIntent actionNext = PendingIntent.getBroadcast( this, 1, in, PendingIntent.FLAG_UPDATE_CURRENT );

        //INTENT ACTION FOR Prev
        Intent in2=new Intent( getApplicationContext(), AudioPrevBroadcast.class );
        in2.putExtra( SURAT_POS_EXTRA,suratPos );
        in2.putExtra( AYAT_POS_EXTRA,ayatPos );
        in2.putExtra( TOTAL_AYAT_EXTRA,totalAyat );
        PendingIntent actionPrev = PendingIntent.getBroadcast( this, 1, in2, PendingIntent.FLAG_UPDATE_CURRENT );


        Intent broadcastClose = new Intent( this, AudioCloseBroadcast.class );
        PendingIntent actionClose = PendingIntent.getBroadcast( this, 0, broadcastClose, PendingIntent.FLAG_UPDATE_CURRENT );



        //REMOTE VIEW
        final RemoteViews contentView = new RemoteViews( getPackageName(), R.layout.layout_notif );
        contentView.setTextViewText( R.id.ntSurat, SuratData.surat[suratPos] );
        contentView.setTextViewText( R.id.ntAyat, "Ayat "+Integer.toString( ayatPos+1 ));

        contentView.setImageViewUri( R.id.ntBtnPlay, getUriToResource( getApplicationContext(), R.drawable.ic_play_arrow_black_24dp) );

        contentView.setOnClickPendingIntent( R.id.ntBtnPlay, actionPP );
        contentView.setOnClickPendingIntent( R.id.ntBtnNext, actionNext );
        contentView.setOnClickPendingIntent( R.id.ntBtnPrev, actionPrev );
        contentView.setOnClickPendingIntent( R.id.ntBtnClose, actionClose );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel( "QuranAudioID", "QuranAudio", NotificationManager.IMPORTANCE_LOW );
            NotificationManager manager = getSystemService( NotificationManager.class );
            manager.createNotificationChannel( serviceChannel );
        }

        final Notification notification = new NotificationCompat.Builder( this, "QuranAudioID" ).
                setSmallIcon( R.mipmap.ic_launcher ).
                setContentIntent( pendingIntent ).
                setCustomContentView( contentView ).
                build();

        startForeground( 1337, notification );
    }


    public boolean isPlaying() {
        return player.getPlayWhenReady();
    }

    private void changeSeek(Integer progress) {
        player.seekTo( progress );
    }

    public void durationUpdate() {
        @NonNull Integer dr = (int) player.getCurrentPosition();
        if (dr != null) {
            Intent in=new Intent( BC_DURATION );
            in.putExtra( RECENT_DURATION,dr );
            in.putExtra( RECENT_TOTAL_DURATION,(int)player.getDuration() );
            in.putExtra( "STATE",isPlaying() );
            LocalBroadcastManager.getInstance( getApplicationContext() ).sendBroadcast( in );

            r = new Runnable() {
                @Override
                public void run() {
                    if(!isDestroyed)
                        durationUpdate();
                }
            };
            handler.postDelayed( r, 1000 );
        }

    }

    private void pausePlayer(){
        player.setPlayWhenReady( false );
    }

    private void playPlayer() {
        player.setPlayWhenReady( true );
    }
    private BroadcastReceiver bcPlayPause=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isPlaying()){

                buildNotifPause();
                pausePlayer();
            }else{

                playPlayer();
                buildNotifPlay();
            }
            sendIcon();
        }
    };

    private void sendIcon(){
        if(isPlaying()){
            Intent in=new Intent( "PP_IC_PLAY" );
            LocalBroadcastManager.getInstance( getApplicationContext() ).sendBroadcast( in );
        }else{
            Intent in=new Intent( "PP_IC_PAUSE" );
            LocalBroadcastManager.getInstance( getApplicationContext() ).sendBroadcast( in );
        }
    }

    private BroadcastReceiver bcSeekTo=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            changeSeek( intent.getIntExtra( "SEEK_EXTRA",1 ) );
        }
    };

    public static Uri getUriToResource(@NonNull Context context, @AnyRes int resId) throws Resources.NotFoundException {
        Resources res = context.getResources();

        Uri resUri = Uri.parse( ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + res.getResourcePackageName( resId ) + '/' + res.getResourceTypeName( resId ) + '/' + res.getResourceEntryName( resId ) );
        return resUri;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
        handler.removeCallbacks( r );
        isDestroyed=true;

        LocalBroadcastManager.getInstance( getApplicationContext() ).unregisterReceiver( bcSeekTo );
        LocalBroadcastManager.getInstance( getApplicationContext() ).unregisterReceiver( bcPlayPause );
    }

}
