package com.sod.quran.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sod.quran.R;
import com.sod.quran.activity.SuratViewActivity;
import com.sod.quran.model.SaveModel;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.CustomVH> {

  private ArrayList<SaveModel> data;
  private Context context;

  public FavoriteAdapter(ArrayList<SaveModel> data, Context context) {
    this.data = data;
    this.context = context;
  }

  @NonNull
  @Override
  public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from( context ).inflate( R.layout.layout_favorite,parent,false );
    return new CustomVH( view );
  }

  @Override
  public void onBindViewHolder(@NonNull CustomVH h, final int position) {
    h.tvAyat.setText( data.get( position ).getAyatName() );
    h.tvSurat.setText( data.get( position ).getSuratName() );

    h.layout.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent inte=new Intent( context, SuratViewActivity.class );
        inte.putExtra( SuratViewActivity.SURAT_POS_EXTRAKEY, data.get( position ).getSuratID());
        inte.putExtra( SuratViewActivity.AYAT_POS_EXTRAKEY, data.get( position ).getAyatID());
        context.startActivity( inte );
      }
    } );
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public class CustomVH extends RecyclerView.ViewHolder {
    TextView tvAyat,tvSurat;
    ImageView btnGo;
    ConstraintLayout layout;
    public CustomVH(@NonNull View itemView) {
      super( itemView );
      layout=itemView.findViewById( R.id.layoutClickFav );
      tvAyat=itemView.findViewById( R.id.tvAyatFav );
      tvSurat=itemView.findViewById( R.id.tvSuratFav );
      btnGo=itemView.findViewById( R.id.btnGoFav );
    }
  }
}
