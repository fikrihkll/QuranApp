package com.sod.quran.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sod.quran.db.QuranDbHelper;
import com.sod.quran.model.QuranModel;
import com.sod.quran.services.AudioService;

import java.util.ArrayList;

public class AudioPrevBroadcast extends BroadcastReceiver {
    ArrayList<QuranModel> data=new ArrayList<>(  );

    int suratPos;
    int ayatPos;
    int totalAyat;
    @Override
    public void onReceive(Context context, Intent intent) {

        suratPos=intent.getIntExtra( AudioService.SURAT_POS_EXTRA,1 );
        ayatPos=intent.getIntExtra( AudioService.AYAT_POS_EXTRA,1 );
        totalAyat=intent.getIntExtra( AudioService.TOTAL_AYAT_EXTRA,1 );

        new backProccess( context ).execute(  );

    }

    private class backProccess extends AsyncTask<Void,Integer,Void> {
        Context context;

        public backProccess(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute( aVoid );
            if(ayatPos!=0 && data.get( ayatPos-1 ).getAyatText()!=null && data.get( ayatPos-1 ).getAyatText()!=null){

                Intent in=new Intent( context,AudioService.class );
                in.putExtra( AudioService.SURAT_POS_EXTRA, suratPos );
                in.putExtra( AudioService.AYAT_POS_EXTRA, ayatPos-1);
                in.putExtra( AudioService.FILE_PATH_EXTRA,data.get(ayatPos-1).getAyatText());
                in.putExtra( AudioService.TOTAL_AYAT_EXTRA,totalAyat );
                context.startService( in );
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            data=new QuranDbHelper( context ).getAudioUrl( "SuratID",suratPos+1 );
            return null;
        }
    }
}
