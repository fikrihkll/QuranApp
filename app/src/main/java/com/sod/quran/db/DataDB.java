package com.sod.quran.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sod.quran.model.SaveModel;

import java.util.ArrayList;

public class DataDB extends SQLiteOpenHelper {
    protected static final String DATABASE_NAME = "dataSodQuran";
    protected static final String TABLE_NAME = "LastRead";
    protected static final String TABLE_FAV="FavTable";
    private static final int DATABASE_VERSION = 1;

    public DataDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = " CREATE TABLE IF NOT EXISTS 'LastRead' (" +
                " 'ID' INTEGER PRIMARY KEY," +
                " 'SuratID' INTEGER NOT NULL," +
                " 'AyatID' INTEGER NOT NULL," +
                " 'SuratName' INTEGER NOT NULL," +
                " 'AyatName' TEXT" +
                "); ";
        db.execSQL(sql);

        String sqlFav = " CREATE TABLE IF NOT EXISTS 'FavTable' (" +
                " 'ID' INTEGER PRIMARY KEY," +
                " 'SuratID' INTEGER NOT NULL," +
                " 'AyatID' INTEGER NOT NULL," +
                " 'SuratName' INTEGER NOT NULL," +
                " 'AyatName' TEXT," +
                " 'MixedName' TEXT" +
                "); ";
        db.execSQL(sqlFav);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + "";
        db.execSQL(sql);
        onCreate(db);

        String sqlFav = "DROP TABLE IF EXISTS " + TABLE_FAV + "";
        db.execSQL(sqlFav);
        onCreate(db);
    }

    public void addLastRead(SaveModel save) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("SuratID", save.getSuratID());
        values.put("AyatID", save.getAyatID());
        values.put("SuratName", save.getSuratName());
        values.put("AyatName", save.getAyatName());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<SaveModel> getLastRead() {
        ArrayList<SaveModel> saveList = new ArrayList<SaveModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SaveModel save = new SaveModel();
                save.setID( Integer.parseInt(cursor.getString(0)) );
                save.setSuratID(Integer.parseInt(cursor.getString(1)));
                save.setAyatID(Integer.parseInt(cursor.getString(2)));
                save.setSuratName(cursor.getString(3));
                save.setAyatName(cursor.getString(4));
                saveList.add(save);
            } while (cursor.moveToNext());
        }

        return saveList;
    }

   /* public void deleteLastRead(SaveModel saveModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID" + " = ?",
                new String[] { String.valueOf(saveModel.getID()) });
        db.close();
    }*/

    public void addFav(SaveModel save) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("SuratID", save.getSuratID());
        values.put("AyatID", save.getAyatID());
        values.put("SuratName", save.getSuratName());
        values.put("AyatName", save.getAyatName());
        values.put("MixedName", save.getSuratName()+save.getAyatName());

        db.insert(TABLE_FAV, null, values);
        db.close();
    }

   /* public int getId(SaveModel save){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery(
                "SELECT ID  FROM FavTable WHERE MixedName= '"+save.getSuratName()+save.getAyatName()+"'" , null);
        if(mCursor.getCount()>0)
            return mCursor.getInt(0);
        else
            return 0;
    }*/

    public void deleteFav(SaveModel saveModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAV, "MixedName" + " = ?",
                new String[] { String.valueOf(saveModel.getSuratName()+saveModel.getAyatName()) });
        db.close();
    }

    public ArrayList<SaveModel> getFav() {
        ArrayList<SaveModel> saveList = new ArrayList<SaveModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAV;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SaveModel save = new SaveModel();
                save.setID(Integer.parseInt(cursor.getString(0)));
                save.setSuratID(Integer.parseInt(cursor.getString(1)));
                save.setAyatID(Integer.parseInt(cursor.getString(2)));
                save.setSuratName(cursor.getString(3));
                save.setAyatName(cursor.getString(4));
                saveList.add(save);
            } while (cursor.moveToNext());
        }

        return saveList;
    }
}
