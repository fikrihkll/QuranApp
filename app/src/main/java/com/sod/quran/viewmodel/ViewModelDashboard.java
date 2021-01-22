package com.sod.quran.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sod.quran.db.DataDB;
import com.sod.quran.model.SaveModel;

import java.util.ArrayList;

public class ViewModelDashboard extends AndroidViewModel {
    public ViewModelDashboard(@NonNull Application application) {
        super( application );
    }
    public MutableLiveData<ArrayList<SaveModel>>listLR=new MutableLiveData<>();
    public MutableLiveData<ArrayList<SaveModel>>listFav=new MutableLiveData<>();

    public LiveData<ArrayList<SaveModel>> getLastRead(){
        return listLR;
    }
    public LiveData<ArrayList<SaveModel>> getFav(){
        return listFav;
    }

    public void setListFav(){
        listFav.postValue( new DataDB( getApplication() ).getFav() );
    }
    public void setLastRead(){
        listLR.postValue( new DataDB( getApplication() ).getLastRead() );
    }
}
