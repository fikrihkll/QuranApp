package com.sod.quran.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.sod.quran.R;
import com.sod.quran.adapter.SuratViewAdapter;
import com.sod.quran.db.QuranDbHelper;
import com.sod.quran.fragments.ListenFragment;
import com.sod.quran.model.QuranModel;
import com.sod.quran.model.SuratData;
import com.sod.quran.viewmodel.ViewModelQuran;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.ArrayList;

public class SuratViewActivity extends AppCompatActivity {

	public static String SURAT_POS_EXTRAKEY="SURAT_POS_KEY";
	public static String AYAT_POS_EXTRAKEY="AYAT_POST_KEY";
	public static String LISTEN_KEY="LISTEN_KEY";
	public static String AYAT_TOTAL_LISTEN_KEY="TOTALAYATLISTEN_KEY";

	public static String SHARED_PREF_NAME="SettingPref";
	public static String SETTING_LANG_KEY="SettingLang";
	public static String SETTING_T_SIZE_KEY="SettingSize";
	public static String SETTING_T_FONT_KEY="SettingFont";

	//DialogSearch
	private ViewModelQuran vmSearch;
	private ArrayAdapter<String> suratSearchAdapter;
	private ArrayAdapter<String> ayatSearchAdapter;
	private ArrayList<QuranModel> searchQuran;
	private Button btnGo;
	private Dialog dlSearch;
	private Boolean goToSearch=false;
	private Integer ayatSearchSelected=0;

	//DialogSetting
	Dialog dlSetting;


	private ViewModelQuran vm;
	private ArrayList<QuranModel> quran;
	private ArrayList<QuranModel> trans;
	private ArrayList<String> latin=new ArrayList<>(  );
	public Integer pos=0;
	private float tSize=40;
	private String language;
	private String font="lateef";
	private Boolean transV=true;
	private Boolean translitV=false;
	private Integer lastScroll=0;

	private SuratViewAdapter adapter;
	private RecyclerView rc;
	private LinearLayoutManager lm;
	private ProgressBar pgb;
	private TextView tvTitle;
	private ImageView btnSearch;
	private ImageView btnSetting;
	private ImageView btnListen;
	private ImageView btnInfo;
	private ImageView btnBack;
	private DialogFragment dlListen;
	private AppBarLayout topBar;
	private BottomAppBar botBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_surat_view );

		pgb=findViewById( R.id.pgbSV );
		topBar=findViewById( R.id.appBarLayout );
		botBar=findViewById( R.id.bottom_nav );
		botBar.setHideOnScroll( true );
		rc=findViewById( R.id.rcSuratView );
		lm=new LinearLayoutManager( this );
		rc.setLayoutManager( lm );
		btnSearch=findViewById( R.id.btnSearchSV );
		btnSetting=findViewById( R.id.btnSettingSV );
		btnListen=findViewById( R.id.btnListenSV );
		btnInfo=findViewById( R.id.btnInfoSV );
		btnBack=findViewById( R.id.btnBackSV );
		tvTitle=findViewById( R.id.tvTitleSV );

		lastScroll=getIntent().getIntExtra( AYAT_POS_EXTRAKEY,0 );
		pos=getIntent().getIntExtra(SURAT_POS_EXTRAKEY ,1 );
		SharedPreferences sp=getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
		language=sp.getString( SETTING_LANG_KEY,"en_AhAli" );
		tSize=sp.getFloat( SETTING_T_SIZE_KEY,40 );
		font=sp.getString( SETTING_T_FONT_KEY,"lateef");
		tvTitle.setText( SuratData.surat[pos] );

		vm= ViewModelProviders.of( this).get( ViewModelQuran.class );
		vm.getQuran().observe( this,getQuran );
		vm.getTrans().observe( this,getTrans );

		listener();
		new doBackground().execute(  );
	}

	private void listener(){

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			rc.setOnScrollChangeListener( new View.OnScrollChangeListener() {
				@Override
				public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

				}
			} );
		}

		btnListen.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dlListen=new ListenFragment();
				Bundle bd=new Bundle(  );
				bd.putInt(LISTEN_KEY,pos);
				bd.putInt(AYAT_TOTAL_LISTEN_KEY,quran.size());
				dlListen.setArguments( bd );

				dlListen.show( getSupportFragmentManager(),ListenFragment.class.getSimpleName() );
			}
		} );

		btnBack.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SuratViewActivity.this.finish();
			}
		} );

		btnSetting.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				settingDialog();
			}
		} );

		btnSearch.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchDialog();
			}
		} );
	}

	private Observer<ArrayList<QuranModel>> getQuran=new Observer<ArrayList<QuranModel>>() {
		@Override
		public void onChanged(ArrayList<QuranModel> quranModels) {
			quran=quranModels;
			if(quran!=null && trans!=null){
				if(goToSearch){
					setAdapter();
					rc.post( new Runnable() {
						@Override
						public void run() {
							rc.smoothScrollToPosition( ayatSearchSelected);
						}
					} );
					goToSearch=false;
				}else
					setAdapter();
			}
		}
	};

	private Observer<ArrayList<QuranModel>> getTrans=new Observer<ArrayList<QuranModel>>() {
		@Override
		public void onChanged(ArrayList<QuranModel> quranModels) {
			trans=quranModels;
			if(quran!=null && trans!=null){
				if(goToSearch){
					setAdapter();
					rc.post( new Runnable() {
						@Override
						public void run() {
							rc.smoothScrollToPosition( ayatSearchSelected);
						}
					} );
					goToSearch=false;
				}else
					setAdapter();
			}
		}
	};

	private void setAdapter(){
		adapter=new SuratViewAdapter( SuratViewActivity.this,quran,latin,trans,tSize,font,transV,translitV );
		rc.setAdapter( adapter );
		rc.scrollToPosition( lastScroll );
	}

	private void getAllData(){
		vm.setListQuran(pos);
		vm.setListTrans(language,pos);
	}

	//Search----
	private void searchDialog(){
		dlSearch=new Dialog( this );
		dlSearch.getWindow().setBackgroundDrawableResource( R.color.transparent );
		dlSearch.setContentView( R.layout.dialog_search );

		final Spinner surat=dlSearch.findViewById( R.id.spSuratDS );
		suratSearchAdapter=new ArrayAdapter<>( this,android.R.layout.simple_list_item_1, SuratData.surat );
		suratSearchAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		surat.setAdapter( suratSearchAdapter );
		surat.setSelection( pos );

		final Spinner ayat=dlSearch.findViewById( R.id.spAyatDS );
		ayatSearchAdapter=new ArrayAdapter<>( this,android.R.layout.simple_list_item_1 );
		ayatSearchAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		ayat.setAdapter( ayatSearchAdapter );


		surat.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position==pos){
					ayatSearchAdapter.clear();
					for(Integer i=0;i<quran.size();i++){
						ayatSearchAdapter.add( quran.get( i ).getVerseId().toString() );
					}
				}else
					getSearchData( position );
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		} );

		ayat.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ayatSearchSelected=position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		} );

		ImageView btnClose=dlSearch.findViewById( R.id.btnCloseDS );
		btnClose.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dlSearch.hide();
			}
		} );

		btnGo=dlSearch.findViewById( R.id.btnGoDS );
		btnGo.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(pos==surat.getSelectedItemPosition()){
					rc.post( new Runnable() {
						@Override
						public void run() {
							rc.smoothScrollToPosition( ayat.getSelectedItemPosition());
						}
					} );
					dlSearch.dismiss();
				}else{
					pos=surat.getSelectedItemPosition();
					tvTitle.setText( SuratData.surat[pos] );
					goToSearch=true;
					quran=null;
					trans=null;
					new doBackground().execute(  );
				}
			}
		} );

		dlSearch.show();
	}
	private void getSearchData(Integer posSearch){
		new SearchProcess( posSearch ).execute(  );
	}
	public void setSearchListQuran(Integer pos) {
		ArrayList<QuranModel> data;
		data = new QuranDbHelper( getApplication() ).getListDataWhereCriteria( "SuratID", pos + 1 );

		searchQuran=data;
	}
	//Search----

	//Setting Dialog
	private void settingDialog(){
		lastScroll=lm.findFirstVisibleItemPosition();
		dlSetting=new Dialog( this );
		dlSetting.getWindow().setBackgroundDrawableResource( R.color.transparent );
		dlSetting.setContentView( R.layout.dialog_setting );

		final TextView tvSuratSample,tvTransSample;
		final Spinner spFont,spSize,spLang;
		final ArrayAdapter<String> fontAdapter,sizeAdapter,langAdapter;
		Switch swTrans,swTransli;
		ImageView btnBack;

		tvSuratSample=dlSetting.findViewById( R.id.tvAyatSampleST );
		tvTransSample=dlSetting.findViewById( R.id.tvTransSampleST );
		spFont=dlSetting.findViewById( R.id.spFontTypeST );
		spSize=dlSetting.findViewById( R.id.spTextSizeST );
		spLang=dlSetting.findViewById( R.id.spLangST );
		swTrans=dlSetting.findViewById( R.id.swTransST );
		swTransli=dlSetting.findViewById( R.id.swTranslitST );
		btnBack=dlSetting.findViewById( R.id.btnBackSettdl );

		swTrans.setChecked( transV );

		fontAdapter=new ArrayAdapter<>( this,android.R.layout.simple_list_item_1 ,SuratData.fontName);
		fontAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		spFont.setAdapter( fontAdapter );

		sizeAdapter=new ArrayAdapter<>( this,android.R.layout.simple_list_item_1,SuratData.size);
		sizeAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		spSize.setAdapter( sizeAdapter );

		langAdapter=new ArrayAdapter<>( this,android.R.layout.simple_list_item_1,SuratData.lang);
		langAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		spLang.setAdapter( langAdapter );

		//Listener
		//Check Font
		if(font.equals( "elmessiri" )){
			spFont.setSelection( 1 );
		}else if(font.equals( "scheherazade" )){
			spFont.setSelection( 2 );
		}

		//Check Size
		if(tSize==42){
			spSize.setSelection( 1 );
		}else if(tSize==44){
			spSize.setSelection( 2 );
		}else if(tSize==46){
			spSize.setSelection( 3 );
		}

		//Check Lang
		if(language.equals( "en_YusAli" )){
			spLang.setSelection( 1 );
		}else if(language.equals( "IDN" )){
			spLang.setSelection( 2 );
		}else if(language.equals( "Malay" )){
			spLang.setSelection( 3 );
		}

		spLang.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position==0 && !language.equals( "en_AhAli" )){
					rc.setVisibility( View.GONE );
					language="en_AhAli";
					SharedPreferences sp=getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
					SharedPreferences.Editor ed=sp.edit();
					ed.putString( SETTING_LANG_KEY, "en_AhAli");
					ed.apply();
					goToSearch=false;
					trans=null;
					new doBackground().execute(  );
				}if(position==1 && !language.equals( "en_YusAli" )){
					rc.setVisibility( View.GONE );
					language="en_YusAli";
					SharedPreferences sp=getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
					SharedPreferences.Editor ed=sp.edit();
					ed.putString( SETTING_LANG_KEY, "en_YusAli");
					ed.apply();
					goToSearch=false;
					trans=null;
					new doBackground().execute(  );
				}if(position==2 && !language.equals( "IDN" )){
					rc.setVisibility( View.GONE );
					language="IDN";
					SharedPreferences sp=getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
					SharedPreferences.Editor ed=sp.edit();
					ed.putString( SETTING_LANG_KEY, "IDN");
					ed.apply();
					goToSearch=false;
					trans=null;
					new doBackground().execute(  );
				}if(position==3 && !language.equals( "Malay" )){
					rc.setVisibility( View.GONE );
					language="Malay";
					SharedPreferences sp=getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
					SharedPreferences.Editor ed=sp.edit();
					ed.putString( SETTING_LANG_KEY, "Malay");
					ed.apply();
					goToSearch=false;
					trans=null;
					new doBackground().execute(  );
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		} );

		spFont.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SharedPreferences sp=getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
				font=fontAdapter.getItem( position );
				SharedPreferences.Editor ed=sp.edit();
				ed.putString( SETTING_T_FONT_KEY,fontAdapter.getItem( position ) );
				ed.apply();

				if(font.equals( "elmessiri" )) {
					Typeface myFont = Typeface.createFromAsset( getAssets(), "fonts/" + font + ".otf" );
					tvSuratSample.setTypeface( myFont );
				}else{
					Typeface myFont = Typeface.createFromAsset( getAssets(), "fonts/" + font + ".ttf" );
					tvSuratSample.setTypeface( myFont );
				}

				setAdapter();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		} );

		spSize.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SharedPreferences sp=getSharedPreferences( SHARED_PREF_NAME,MODE_PRIVATE );
				if(position==0){
					if(tSize==40){

					}else{
						tSize=40;
					}
				}else if(position==1){
					if(tSize==42){

					}else{
						tSize=42;
					}
				}else if(position==2){
					if(tSize==44){

					}else{
						tSize=44;
					}
				}else if(position==3){
					if(tSize==46){

					}else{
						tSize=46;
					}
				}
				SharedPreferences.Editor ed=sp.edit();
				ed.putFloat( SETTING_T_SIZE_KEY,tSize );
				ed.apply();

				tvTransSample.setTextSize( tSize-22 );
				tvSuratSample.setTextSize( tSize);

				setAdapter();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		} );

		swTrans.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					transV=true;
					setAdapter();
				}else{
					transV=false;
					setAdapter();
				}
			}
		} );

		btnBack.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dlSetting.dismiss();
			}
		} );

		dlSetting.show();
	}
	//Setting Dialog

	private class doBackground extends AsyncTask<Void,Integer,Void>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pgb.setVisibility( View.VISIBLE );
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute( aVoid );
			rc.setVisibility( View.VISIBLE );
			pgb.setVisibility( View.GONE );
			if(dlSearch!=null && dlSearch.isShowing())
				dlSearch.dismiss();
		}

		@Override
		protected Void doInBackground(Void... voids) {
			getAllData();
			return null;
		}
	}

	private class SearchProcess extends AsyncTask<Void,Integer,Void>{

		Integer posSearch;

		public SearchProcess(Integer posSearch) {
			this.posSearch = posSearch;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(dlSearch.isShowing())
				btnGo.setEnabled( false );
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute( aVoid );
			if(dlSearch.isShowing()) {
				btnGo.setEnabled( true );
				ayatSearchAdapter.clear();
				if(searchQuran!=null){
					for(Integer i=0;i<searchQuran.size();i++){
						ayatSearchAdapter.add( searchQuran.get( i ).getVerseId().toString() );
					}
					ayatSearchAdapter.notifyDataSetChanged();
				}
			}
		}

		@Override
		protected Void doInBackground(Void... voids) {
			setSearchListQuran( posSearch );
			return null;
		}
	}
}
