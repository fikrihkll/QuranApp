package com.sod.quran.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class AudioPPBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LocalBroadcastManager.getInstance( context ).sendBroadcast( new Intent( "PLAY/PAUSE" ) );
    }
}
