package com.sod.quran.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sod.quran.model.SuratData;

import java.util.ArrayList;

public class ViewModelSurat extends AndroidViewModel {
    public ViewModelSurat(@NonNull Application application) {
        super( application );
    }

    public MutableLiveData<ArrayList<String>> listSurat=new MutableLiveData<>();

    public LiveData<ArrayList<String>> getSurat(){
        return listSurat;
    }

    public void setListSurat(){
        ArrayList<String> list=new ArrayList<>(  );
        for(Integer i=0;i<SuratData.surat.length;i++){
            list.add( SuratData.surat[i] );
        }
        listSurat.postValue( list );
    }
}
