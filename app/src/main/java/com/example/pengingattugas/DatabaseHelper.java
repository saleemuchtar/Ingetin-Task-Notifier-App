package com.example.pengingattugas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tugasku.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TUGAS = "tugas";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAMA_TUGAS = "nama_tugas";
    public static final String COLUMN_MATA_KULIAH = "mata_kuliah";
    public static final String COLUMN_DEADLINE = "deadline";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TUGAS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAMA_TUGAS + " TEXT NOT NULL, " +
                    COLUMN_MATA_KULIAH + " TEXT, " +
                    COLUMN_DEADLINE + " INTEGER NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUGAS);
        onCreate(db);
    }
}