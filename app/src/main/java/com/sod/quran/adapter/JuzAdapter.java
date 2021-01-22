package com.sod.quran.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sod.quran.R;
import com.sod.quran.activity.SuratViewActivity;

import java.util.ArrayList;

public class JuzAdapter extends RecyclerView.Adapter<JuzAdapter.CustomVH> {

    private Context context;
    private ArrayList<String> juz;
    private ArrayList<String> detail;

    public JuzAdapter(Context context, ArrayList<String> juz, ArrayList<String> detail) {
        this.context = context;
        this.juz = juz;
        this.detail = detail;
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from( context ).inflate( R.layout.layout_juz,parent,false );
        return new CustomVH( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomVH h, final int i) {
        h.tvName.setText( juz.get( i ) );
        h.tvDetail.setText( detail.get( i ) );

        h.layout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct(i );
            }
        } );

        h.tvName.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct( i );
            }
        } );

        h.tvDetail.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAct( i );
            }
        } );
    }

    private void startAct( Integer i){
        if(i==0){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,0);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 0);
            context.startActivity( inte );
        }else if(i==1){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,1);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 141);
            context.startActivity( inte );
        }else if(i==2){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,1);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 252);
            context.startActivity( inte );
        }else if(i==3){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,2);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 92);
            context.startActivity( inte );
        }else if(i==4){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,3);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 23);
            context.startActivity( inte );
        }else if(i==5){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,3);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 147);
            context.startActivity( inte );
        }else if(i==6){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,4);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 81);
            context.startActivity( inte );
        }else if(i==7){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,5);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 110);
            context.startActivity( inte );
        }else if(i==8){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,6);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 87);
            context.startActivity( inte );
        }else if(i==9){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,7);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 40);
            context.startActivity( inte );
        }else if(i==10){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,8);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 92);
            context.startActivity( inte );
        }else if(i==11){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,10);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 5);
            context.startActivity( inte );
        }else if(i==12){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,11);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 52);
            context.startActivity( inte );
        }else if(i==13){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,14);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 0);
            context.startActivity( inte );
        }else if(i==14){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,16);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 0);
            context.startActivity( inte );
        }else if(i==15){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,17);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 74);
            context.startActivity( inte );
        }else if(i==16){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,20);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 0);
            context.startActivity( inte );
        }else if(i==17){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,22);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 0);
            context.startActivity( inte );
        }else if(i==18){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,24);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 20);
            context.startActivity( inte );
        }else if(i==19){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,26);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 55);
            context.startActivity( inte );
        }else if(i==20){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,28);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 45);
            context.startActivity( inte );
        }else if(i==21){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,32);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 30);
            context.startActivity( inte );
        }else if(i==22){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,35);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 27);
            context.startActivity( inte );
        }else if(i==23){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,38);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 31);
            context.startActivity( inte );
        }else if(i==24){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,40);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 46);
            context.startActivity( inte );
        }else if(i==25){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,45);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 0);
            context.startActivity( inte );
        }else if(i==26){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,50);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 30);
            context.startActivity( inte );
        }else if(i==27){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,57);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 0);
            context.startActivity( inte );
        }else if(i==28){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,66);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 0);
            context.startActivity( inte );
        }else if(i==29){
            Intent inte=new Intent( context, SuratViewActivity.class );
            inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,77);
            inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, 0);
            context.startActivity( inte );
        }
    }

    @Override
    public int getItemCount() {
        return juz.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {
        TextView tvName,tvDetail;
        ConstraintLayout layout;
        public CustomVH(@NonNull View itemView) {
            super( itemView );
            tvName=itemView.findViewById( R.id.tvJuzNameLJ );
            tvDetail=itemView.findViewById( R.id.tvDetailLJ );
            layout=itemView.findViewById( R.id.layoutLJ );
        }
    }
}
