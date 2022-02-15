package com.example.fa_pawankumar_c0835049_android;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private Context context;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "map_db";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        // create categories table
        db.execSQL(AddPlaceModel.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + AddPlaceModel.TABLE_NAME);

        // Create tables again
        onCreate(db);

    }



    public long insert(String name,String lat,String lng,String completed) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(AddPlaceModel.COLUMN_NAME, name);
        values.put(AddPlaceModel.COLUMN_LAT, lat);
        values.put(AddPlaceModel.COLUMN_LNG, lng);
        values.put(AddPlaceModel.COLUMN_COMPLETED, completed);

        // insert row
        long id = db.insert(AddPlaceModel.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    @SuppressLint("Range")
    public List<AddPlaceModel> getAll() {
        List<AddPlaceModel> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + AddPlaceModel.TABLE_NAME + " ORDER BY " +
                AddPlaceModel.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AddPlaceModel note = new AddPlaceModel();
                note.setId(cursor.getInt(cursor.getColumnIndex(AddPlaceModel.COLUMN_ID)));
                note.setName(cursor.getString(cursor.getColumnIndex(AddPlaceModel.COLUMN_NAME)));
                note.setLat(cursor.getString(cursor.getColumnIndex(AddPlaceModel.COLUMN_LAT)));
                note.setLng(cursor.getString(cursor.getColumnIndex(AddPlaceModel.COLUMN_LNG)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(AddPlaceModel.COLUMN_TIMESTAMP)));
                note.setCompleted(cursor.getString(cursor.getColumnIndex(AddPlaceModel.COLUMN_COMPLETED)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public void delete(AddPlaceModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AddPlaceModel.TABLE_NAME, AddPlaceModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public int update(AddPlaceModel note,String name,String lat,String lng,String completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AddPlaceModel.COLUMN_NAME, name);
        values.put(AddPlaceModel.COLUMN_LAT, lat);
        values.put(AddPlaceModel.COLUMN_LNG, lng);
        values.put(AddPlaceModel.COLUMN_COMPLETED, completed);
        // updating row
        return db.update(AddPlaceModel.TABLE_NAME, values, AddPlaceModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }



}
