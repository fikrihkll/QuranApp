package com.sod.quran.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QuranModel implements Parcelable {
    private Integer id, databaseId, suratId, verseId;
    private String ayatText;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Integer databaseId) {
        this.databaseId = databaseId;
    }

    public Integer getSuratId() {
        return suratId;
    }

    public void setSuratId(Integer suratId) {
        this.suratId = suratId;
    }

    public Integer getVerseId() {
        return verseId;
    }

    public void setVerseId(Integer verseId) {
        this.verseId = verseId;
    }

    public String getAyatText() {
        return ayatText;
    }

    public void setAyatText(String ayatText) {
        this.ayatText = ayatText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue( this.id );
        dest.writeValue( this.databaseId );
        dest.writeValue( this.suratId );
        dest.writeValue( this.verseId );
        dest.writeString( this.ayatText );
    }

    public QuranModel() {
    }

    protected QuranModel(Parcel in) {
        this.id = (Integer) in.readValue( Integer.class.getClassLoader() );
        this.databaseId = (Integer) in.readValue( Integer.class.getClassLoader() );
        this.suratId = (Integer) in.readValue( Integer.class.getClassLoader() );
        this.verseId = (Integer) in.readValue( Integer.class.getClassLoader() );
        this.ayatText = in.readString();
    }

    public static final Creator<QuranModel> CREATOR = new Creator<QuranModel>() {
        @Override
        public QuranModel createFromParcel(Parcel source) {
            return new QuranModel( source );
        }

        @Override
        public QuranModel[] newArray(int size) {
            return new QuranModel[size];
        }
    };
}
