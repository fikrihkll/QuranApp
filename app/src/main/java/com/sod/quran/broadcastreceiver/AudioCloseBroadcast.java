package com.sod.quran.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.sod.quran.services.AudioService;

public class AudioCloseBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in=new Intent( "PLAY/PAUSE_IC" );
        in.putExtra( "PP","PAUSE" );
        LocalBroadcastManager.getInstance(context ).sendBroadcast( in );
        Intent in2=new Intent( context, AudioService.class );
        context.stopService( in2 );
    }
}
