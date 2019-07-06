package com.ernshu.www.criminalintent.datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ernshu.www.criminalintent.datebase.CrimeDbSchema.CrimeTable;

/*
* A SQLiteOpenHelper is a class designed to get rid of the grunt work of opening a SQLiteDatabase.
* Use it inside of CrimeLab to create your crime database. */
public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    /*Android provides the SQLiteOpenHelper class to handle all of this for you.
    Create a class called CrimeBaseHelper in your database package. */
    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    /*if it does not, create it and create tables and initial data it needs. */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /* creates a new litesql base on the CrimeTable */
        db.execSQL("create table " + CrimeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CrimeTable.Cols.UUID + ", " +
                CrimeTable.Cols.TITLE + ", " +
                CrimeTable.Cols.DATE + ", " +
                CrimeTable.Cols.SOLVED  +
                ")"
        );
    }
    /*If it is old version, upgrade it to a newer version. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
