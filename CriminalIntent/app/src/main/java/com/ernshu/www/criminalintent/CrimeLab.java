package com.ernshu.www.criminalintent;

/*
singleton class that allows only one instance of itself to be created.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ernshu.www.criminalintent.datebase.CrimeBaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        /* Checks to see if sCrimeLab is empty. If it doesn't have anything
        create a new object CrimeLab with context object into it.
         */
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        /*
        * attach a datebase to mDatabase object. */
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addCrime(Crime c) {
        /*To respond to the user pressing the New Crime action item,
        you need a way to add a new Crime to your list of crimes. In CrimeLab.java,
        add a method to do this.*/
        //mCrimes.add(c);
    }

    public List<Crime> getCrimes() {
        return new ArrayList<>();
    }

    public Crime getCrime(UUID id) {
        /*
        method to return a UUID of a object.
         */

        return null;
    }
}
