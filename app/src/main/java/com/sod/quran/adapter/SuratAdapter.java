package com.sod.quran.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sod.quran.R;
import com.sod.quran.activity.SuratViewActivity;

import java.util.ArrayList;

public class SuratAdapter extends RecyclerView.Adapter<SuratAdapter.CustomViewHolder> {

  private Context context;
  private ArrayList<String> data;

  public SuratAdapter(Context context, ArrayList<String> data) {
    this.context = context;
    this.data = data;

  }

  @NonNull
  @Override
  public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from( context ).inflate( R.layout.layout_surat ,parent,false);
    return new CustomViewHolder( view );
  }

  @Override
  public void onBindViewHolder(@NonNull final CustomViewHolder h, final int i) {
    h.tvSuratID.setText( Integer.toString( (i+1) ) );
    h.tvSuratName.setText( data.get( i ) );

    h.layout.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent in=new Intent( context, SuratViewActivity.class );
        in.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY,i );
        context.startActivity( in );
      }
    } );
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public class CustomViewHolder extends RecyclerView.ViewHolder {
    TextView tvSuratID,tvSuratName;
    ConstraintLayout layout;

    public CustomViewHolder(@NonNull View itemView) {
      super( itemView );
      tvSuratID=itemView.findViewById( R.id.tvNumberls );
      tvSuratName=itemView.findViewById( R.id.tvSuratNamels );
      layout=itemView.findViewById( R.id.layoutSurat );
    }
  }
}
