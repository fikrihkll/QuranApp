package com.sod.quran.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sod.quran.R;
import com.sod.quran.activity.MainActivity;
import com.sod.quran.activity.SuratViewActivity;
import com.sod.quran.broadcastreceiver.AlertReceiverDaily;
import com.sod.quran.db.DataDB;
import com.sod.quran.db.QuranDbHelper;
import com.sod.quran.model.QuranModel;
import com.sod.quran.model.SaveModel;
import com.sod.quran.model.SuratData;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class SuratViewAdapter extends RecyclerView.Adapter<SuratViewAdapter.CustomVH> {

    private DataDB db;
    private Context context;
    private ArrayList<QuranModel> quran;
    private ArrayList<SaveModel> saveData;
    private ArrayList<String> latin;
    private ArrayList<Integer> checked;
    private ArrayList<QuranModel> trans;
    private float textSize;
    private String font;
    private Boolean isTransVisible;
    private Boolean isTranslitVisible;
    private Boolean isEnabled=true;
    private SuratViewActivity act;

    public SuratViewAdapter(Context context, ArrayList<QuranModel> quran, ArrayList<String> latin, ArrayList<QuranModel> trans, float textSize,String font,Boolean isTransVisible,Boolean isTranslitVisible) {
        this.context = context;
        this.quran = quran;
        this.latin = latin;
        this.trans = trans;
        this.textSize = textSize;
        this.font= font;
        this.isTranslitVisible=isTranslitVisible;
        this.isTransVisible=isTransVisible;
        db=new DataDB(context);
        act=(SuratViewActivity)context;
        loadFav();
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from( context ).inflate( R.layout.layout_surat_view,parent,false );
        return new CustomVH( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomVH h, final int i) {
        setSizeText( h );
        h.tvNumb.setTextSize( 30 );
        h.tvNumb.setText( quran.get( i ).getVerseId().toString() );
        h.tvAyat.setText( quran.get( i ).getAyatText() );
        //if(!isTranslitVisible)
        //   h.tvLatin.setVisibility( View.GONE );
       // h.tvLatin.setText( latin.get( i ) );
        if(!isTransVisible)
            h.tvTrans.setVisibility( View.GONE );
        h.tvTrans.setText( trans.get( i ).getAyatText() );

        h.btnMark.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveModel model=new SaveModel();
                model.setSuratID( act.pos );
                model.setAyatID( i );
                model.setSuratName( SuratData.surat[act.pos] );
                model.setAyatName( quran.get( i ).getVerseId().toString() );

                db.addLastRead( model );
                LocalBroadcastManager.getInstance( context ).sendBroadcast( new Intent( "RELOAD_DATA" ) );
                Toast.makeText(context ,"Marked "+SuratData.surat[act.pos]+" Ayat "+quran.get( i ).getVerseId().toString(),Toast.LENGTH_LONG ).show();
                startReminderDaily();
            }
        } );

        h.btnFav.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEnabled){
                    PopupMenu popup = new PopupMenu(context, h.btnFav);
                    if(checked.get( i )==-1){
                        popup.inflate( R.menu.fav_menu );
                    }else{
                        popup.inflate( R.menu.unfav_menu );
                    }
                    popup.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            switch (menuItem.getItemId()){
                                case R.id.mFav:
                                    if(checked.get( i )==-1){
                                        SaveModel model=new SaveModel();
                                        model.setSuratID( act.pos );
                                        model.setAyatID( i );
                                        model.setSuratName( SuratData.surat[act.pos] );
                                        model.setAyatName(  Integer.toString( quran.get( i ).getVerseId() ) );

                                        db.addFav( model );
                                        saveData.add( model );
                                        checked.set( i,saveData.size()-1 );
                                    }else{
                                        new DataDB( context ).deleteFav( saveData.get( checked.get( i ) ));

                                        saveData.remove( saveData.get( checked.get( i ) ) );
                                        checked.set( i,-1 );
                                    }
                                    LocalBroadcastManager.getInstance( context ).sendBroadcast( new Intent( "RELOAD_DATA" ) );
                                    break;
                            }
                            return false;
                        }
                    } );
                    popup.show();
                }
            }
        } );
    }

    private void loadFav(){
        saveData=new ArrayList<>(  );
        saveData=db.getFav();
        checked=new ArrayList<>(  );
        for(Integer i=0;i<quran.size();i++){
            checked.add( -1 );
        }

        for(Integer i=0;i<quran.size();i++){
            for(Integer p=0;p<saveData.size();p++){
                if(saveData.get( p ).getSuratID()==act.pos && saveData.get( p ).getAyatID() == i){
                    checked.set( i,p );
                }
            }
        }
    }

    private void startReminderDaily() {
        //START NOTIF
        Intent i = new Intent( context, AlertReceiverDaily.class );
        PendingIntent pi = PendingIntent.getBroadcast( context, 1, i, PendingIntent.FLAG_UPDATE_CURRENT );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService( ALARM_SERVICE );

        Calendar triggerhCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        triggerhCal.set( Calendar.HOUR_OF_DAY, 19 );
        triggerhCal.set( Calendar.MINUTE, 0 );
        triggerhCal.set( Calendar.SECOND, 0 );

        long intendedTime = triggerhCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        if (intendedTime >= currentTime) {
            alarmManager.setRepeating( AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pi );
        } else {
            triggerhCal.add( Calendar.DAY_OF_MONTH, 1 );
            intendedTime = triggerhCal.getTimeInMillis();

            alarmManager.setRepeating( AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pi );
        }
        //START NOTIF
    }

    private void setSizeText(CustomVH h){
        h.tvAyat.setTextSize( textSize );
        h.tvTrans.setTextSize( textSize-22 );
        h.tvNumb.setTextSize( textSize-22 );
        h.tvLatin.setTextSize( textSize-22 );
    }

    private void setFont(CustomVH h,String font){
        if(font.equals( "elmessiri" )) {
            Typeface myFont = Typeface.createFromAsset( context.getAssets(), "fonts/" + font + ".otf" );
            h.tvAyat.setTypeface( myFont );
        }else{
            Typeface myFont = Typeface.createFromAsset( context.getAssets(), "fonts/" + font + ".ttf" );
            h.tvAyat.setTypeface( myFont );
        }
    }

    @Override
    public int getItemCount() {
        return quran.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {
        TextView tvNumb,tvAyat,tvLatin,tvTrans;
        ImageView btnFav,btnMark;
        public CustomVH(@NonNull View itemView) {
            super( itemView );
            btnFav=itemView.findViewById( R.id.btnLoveSV );
            btnMark=itemView.findViewById( R.id.btnMarkSV );
            tvNumb=itemView.findViewById( R.id.tvAyatNumbsv );
            tvAyat=itemView.findViewById(R.id.tvAyatsv);
            tvLatin=itemView.findViewById(R.id.tvLatinsv);
            tvTrans=itemView.findViewById(R.id.tvTranssv);
        }
    }
}
