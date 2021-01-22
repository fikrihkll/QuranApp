package com.sod.quran.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.sod.quran.R;
import com.sod.quran.activity.MainActivity;
import com.sod.quran.db.DataDB;
import com.sod.quran.model.SaveModel;

import java.util.ArrayList;

public class AlertReceiverDaily extends BroadcastReceiver {

    String CHANNEL_ID="QuranReminder";
    String CHANNEL_NAME="AlQuran";
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<SaveModel> data=new DataDB(context).getLastRead();
        String head="Quran Last Read";
        notif( context,head,data.get( data.size()-1 ).getSuratName()+", Ayat "+data.get( data.size()-1 ).getAyatName() );
    }
    private void notif(@NonNull Context mContext, String head, String body) {
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService( Context.NOTIFICATION_SERVICE );

        Intent resultIntent = new Intent( mContext, MainActivity.class );
        resultIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        PendingIntent resultPendingIntent = PendingIntent.getActivity( mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder( mContext, CHANNEL_ID ).
                setSmallIcon( R.drawable.ic_menu_book_24px ).
                setLargeIcon( BitmapFactory.
                        decodeResource( mContext.getResources(),R.mipmap.ic_launcher )  ).
                setContentTitle( head ).setContentText( body ).setSubText( "SOD Inc." ).
                setContentIntent( resultPendingIntent ).setAutoCancel( true );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel( CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT );
            mBuilder.setChannelId( CHANNEL_ID );
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel( channel );
            }
        }

        Notification notification = mBuilder.build();
        mNotificationManager.notify( 1, notification );
    }
}
