package com.sod.quran.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sod.quran.db.QuranDbHelper;
import com.sod.quran.model.QuranModel;

import java.util.ArrayList;

public class ViewModelQuran extends AndroidViewModel{

    private MutableLiveData<ArrayList<QuranModel>> quranData=new MutableLiveData<>();
    private MutableLiveData<ArrayList<QuranModel>> transData=new MutableLiveData<>();

    public ViewModelQuran(@NonNull Application application) {
        super( application );
    }

    public LiveData<ArrayList<QuranModel>> getQuran(){
        return quranData;
    }

    public LiveData<ArrayList<QuranModel>> getTrans(){
        return transData;
    }

    public void setListQuran(Integer pos){
        quranData.postValue( new QuranDbHelper( this.getApplication() ).getListDataWhereCriteria( "SuratID",pos+1 ) );
    }

    public void setListTrans(String lang,Integer pos){
        if(lang.equals( "en_AhAli" )){
            transData.postValue( new QuranDbHelper( this.getApplication() ).getListDataEnAhAli( "SuratID",pos+1 ) );
        }if(lang.equals( "en_YusAli" )){
            transData.postValue( new QuranDbHelper( this.getApplication() ).getListDataEnYusAli( "SuratID",pos+1 ) );
        }if(lang.equals( "IDN" )){
            transData.postValue( new QuranDbHelper( this.getApplication() ).getListDataIndo( "SuratID",pos+1 ) );
        }if(lang.equals( "Malay" )){
            transData.postValue( new QuranDbHelper( this.getApplication() ).getListDataMalay( "SuratID",pos+1 ) );
        }
    }
}