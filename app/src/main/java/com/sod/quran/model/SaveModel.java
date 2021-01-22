package com.sod.quran.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SaveModel implements Parcelable {
    Integer suratID;
    Integer ayatID;
    String suratName;
    String ayatName;
    Integer ID;

    public Integer getSuratID() {
        return suratID;
    }

    public void setSuratID(Integer suratID) {
        this.suratID = suratID;
    }

    public Integer getAyatID() {
        return ayatID;
    }

    public void setAyatID(Integer ayatID) {
        this.ayatID = ayatID;
    }

    public String getSuratName() {
        return suratName;
    }

    public void setSuratName(String suratName) {
        this.suratName = suratName;
    }

    public String getAyatName() {
        return ayatName;
    }

    public void setAyatName(String ayatName) {
        this.ayatName = ayatName;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue( this.suratID );
        dest.writeValue( this.ayatID );
        dest.writeString( this.suratName );
        dest.writeString( this.ayatName );
        dest.writeValue( this.ID );
    }

    public SaveModel() {
    }

    protected SaveModel(Parcel in) {
        this.suratID = (Integer) in.readValue( Integer.class.getClassLoader() );
        this.ayatID = (Integer) in.readValue( Integer.class.getClassLoader() );
        this.suratName = in.readString();
        this.ayatName = in.readString();
        this.ID = (Integer) in.readValue( Integer.class.getClassLoader() );
    }

    public static final Parcelable.Creator<SaveModel> CREATOR = new Parcelable.Creator<SaveModel>() {
        @Override
        public SaveModel createFromParcel(Parcel source) {
            return new SaveModel( source );
        }

        @Override
        public SaveModel[] newArray(int size) {
            return new SaveModel[size];
        }
    };
}
