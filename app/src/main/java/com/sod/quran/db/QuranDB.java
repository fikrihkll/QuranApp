package com.sod.quran.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuranDB extends SQLiteOpenHelper {
    protected static final String DATABASE_NAME = "myalquran";
    protected static final String TABLE_NAME = "Quran";
    protected static final String TABLE_ID_TRANS = "MalayTranslation";
    protected static final String TABLE_ID_TRANS_EN_AHMED_ALI = "EnAhmedAli";
    protected static final String TABLE_ID_TRANS_EN_YUSUF_ALI = "EnYusufAli";
    protected static final String TABLE_ID_TRANS_IDN = "IndonesiaTranslation";
    protected static final String TABLE_AUDIO_URL = "AudioUrl";
    private static final int DATABASE_VERSION = 1;

    public QuranDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = " CREATE TABLE IF NOT EXISTS 'Quran' (" +
                " 'ID' INTEGER PRIMARY KEY," +
                " 'DatabaseID' SMALLINT NOT NULL," +
                " 'SuratID' INTEGER NOT NULL," +
                " 'VerseID' INTEGER NOT NULL," +
                " 'AyatText' TEXT" +
                "); ";
        db.execSQL(sql);

        String transMalay = " CREATE TABLE IF NOT EXISTS 'MalayTranslation' (" +
                " 'ID' INTEGER PRIMARY KEY," +
                " 'DatabaseID' SMALLINT NOT NULL," +
                " 'SuratID' INTEGER NOT NULL," +
                " 'VerseID' INTEGER NOT NULL," +
                " 'AyatText' TEXT" +
                "); ";
        db.execSQL(transMalay);

        String transEnAhmedAli = " CREATE TABLE IF NOT EXISTS 'EnAhmedAli' (" +
                " 'ID' INTEGER PRIMARY KEY," +
                " 'DatabaseID' SMALLINT NOT NULL," +
                " 'SuratID' INTEGER NOT NULL," +
                " 'VerseID' INTEGER NOT NULL," +
                " 'AyatText' TEXT" +
                "); ";
        db.execSQL(transEnAhmedAli);

        String transEnYusufAli = " CREATE TABLE IF NOT EXISTS 'EnYusufAli' (" +
                " 'ID' INTEGER PRIMARY KEY," +
                " 'DatabaseID' SMALLINT NOT NULL," +
                " 'SuratID' INTEGER NOT NULL," +
                " 'VerseID' INTEGER NOT NULL," +
                " 'AyatText' TEXT" +
                "); ";
        db.execSQL(transEnYusufAli);

        String transIndo = " CREATE TABLE IF NOT EXISTS 'IndonesiaTranslation' (" +
                " 'ID' INTEGER PRIMARY KEY," +
                " 'DatabaseID' SMALLINT NOT NULL," +
                " 'SuratID' INTEGER NOT NULL," +
                " 'VerseID' INTEGER NOT NULL," +
                " 'AyatText' TEXT" +
                "); ";
        db.execSQL(transIndo);

        String urlTable = " CREATE TABLE IF NOT EXISTS 'AudioUrl' (" +
                " 'ID' INTEGER PRIMARY KEY," +
                " 'SuratID' INTEGER NOT NULL," +
                " 'UrlText' TEXT" +
                "); ";
        db.execSQL(urlTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + "";
        db.execSQL(sql);
        onCreate(db);

        String sqlT = "DROP TABLE IF EXISTS " + TABLE_ID_TRANS + "";
        db.execSQL(sqlT);
        onCreate(db);

        String sqlTEAA = "DROP TABLE IF EXISTS " + TABLE_ID_TRANS_EN_AHMED_ALI + "";
        db.execSQL(sqlTEAA);
        onCreate(db);

        String sqlTEYA = "DROP TABLE IF EXISTS " + TABLE_ID_TRANS_EN_YUSUF_ALI + "";
        db.execSQL(sqlTEYA);
        onCreate(db);

        String sqlTIDN = "DROP TABLE IF EXISTS " + TABLE_ID_TRANS_IDN + "";
        db.execSQL(sqlTIDN);
        onCreate(db);

        String sqlAud = "DROP TABLE IF EXISTS " + TABLE_AUDIO_URL + "";
        db.execSQL(sqlAud);
        onCreate(db);
    }
}
