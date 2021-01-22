package com.sod.quran.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sod.quran.model.QuranModel;

import java.util.ArrayList;

public class QuranDbHelper extends QuranDB {
    Context context;
    public QuranDbHelper(Context context) {
        super( context );
        this.context=context;
    }

    public ArrayList<QuranModel> getListDataWhereCriteria(String table, Integer value) {
        ArrayList<QuranModel> recordsList = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " where " + table + " = '" + value + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Integer suratId = cursor.getInt(cursor.getColumnIndex("SuratID"));
                Integer verseId = cursor.getInt(cursor.getColumnIndex("VerseID"));
                String ayatText = cursor.getString(cursor.getColumnIndex("AyatText"));

                QuranModel dataObj = new QuranModel();
                dataObj.setSuratId(suratId);
                dataObj.setVerseId(verseId);
                dataObj.setAyatText(ayatText);
                recordsList.add(dataObj);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordsList;
    }

    public ArrayList<QuranModel> getListDataMalay(String table, Integer value) {
        ArrayList<QuranModel> recordsList = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_ID_TRANS + " where " + table + " = '" + value + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Integer suratId = cursor.getInt(cursor.getColumnIndex("SuratID"));
                Integer verseId = cursor.getInt(cursor.getColumnIndex("VerseID"));
                String ayatText = cursor.getString(cursor.getColumnIndex("AyatText"));

                QuranModel dataObj = new QuranModel();
                dataObj.setSuratId(suratId);
                dataObj.setVerseId(verseId);
                dataObj.setAyatText(ayatText);
                recordsList.add(dataObj);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordsList;
    }

    public ArrayList<QuranModel> getListDataIndo(String table, Integer value) {
        ArrayList<QuranModel> recordsList = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_ID_TRANS_IDN + " where " + table + " = '" + value + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Integer suratId = cursor.getInt(cursor.getColumnIndex("SuratID"));
                Integer verseId = cursor.getInt(cursor.getColumnIndex("VerseID"));
                String ayatText = cursor.getString(cursor.getColumnIndex("AyatText"));

                QuranModel dataObj = new QuranModel();
                dataObj.setSuratId(suratId);
                dataObj.setVerseId(verseId);
                dataObj.setAyatText(ayatText);
                recordsList.add(dataObj);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordsList;
    }

    public ArrayList<QuranModel> getListDataEnAhAli(String table, Integer value) {
        ArrayList<QuranModel> recordsList = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_ID_TRANS_EN_AHMED_ALI + " where " + table + " = '" + value + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Integer suratId = cursor.getInt(cursor.getColumnIndex("SuratID"));
                Integer verseId = cursor.getInt(cursor.getColumnIndex("VerseID"));
                String ayatText = cursor.getString(cursor.getColumnIndex("AyatText"));

                QuranModel dataObj = new QuranModel();
                dataObj.setSuratId(suratId);
                dataObj.setVerseId(verseId);
                dataObj.setAyatText(ayatText);
                recordsList.add(dataObj);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordsList;
    }

    public void deleteDB(){
        context.deleteDatabase(DATABASE_NAME);
    }

    public ArrayList<QuranModel> getListDataEnYusAli(String table, Integer value) {
        ArrayList<QuranModel> recordsList = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_ID_TRANS_EN_YUSUF_ALI + " where " + table + " = '" + value + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Integer suratId = cursor.getInt(cursor.getColumnIndex("SuratID"));
                Integer verseId = cursor.getInt(cursor.getColumnIndex("VerseID"));
                String ayatText = cursor.getString(cursor.getColumnIndex("AyatText"));

                QuranModel dataObj = new QuranModel();
                dataObj.setSuratId(suratId);
                dataObj.setVerseId(verseId);
                dataObj.setAyatText(ayatText);
                recordsList.add(dataObj);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordsList;
    }

    public ArrayList<QuranModel> getAudioUrl(String table, Integer value) {
        ArrayList<QuranModel> recordsList = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_AUDIO_URL + " where " + table + " = '" + value + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Integer suratId = cursor.getInt(cursor.getColumnIndex("SuratID"));
                String ayatText = cursor.getString(cursor.getColumnIndex("UrlText"));

                QuranModel dataObj = new QuranModel();
                dataObj.setSuratId(suratId);
                dataObj.setAyatText(ayatText);
                recordsList.add(dataObj);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordsList;
    }

    public void addUrl(QuranModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("SuratID", model.getSuratId());
        values.put("UrlText", model.getAyatText());

        db.insert(TABLE_AUDIO_URL, null, values);
        db.close();
    }
}
