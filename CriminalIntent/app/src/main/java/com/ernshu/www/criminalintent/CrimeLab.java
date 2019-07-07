package com.ernshu.www.criminalintent;

/*
singleton class that allows only one instance of itself to be created.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ernshu.www.criminalintent.datebase.CrimeBaseHelper;
import com.ernshu.www.criminalintent.datebase.CrimeDbSchema.CrimeTable;

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
        add a method to do this. add row to the datebase. Fill out addCrime(Crime) */
        ContentValues values = getContentValues(c);
        /* The insert(String, String, ContentValues) method has two important arguments and one that
        * is rarely used. The first argument is the table you want to insert into -here, CrimeTable.NAME.
        * The last argument is the data you want to put in. */
        mDatabase.insert(CrimeTable.NAME, null, values);
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
    /* The update(String, ContentValues, String, String[]) method starts off similarly to insert()
    * you pass in the table name you want to update and the ContentValues you want to assign to each
    * row you update. However, the last bit is different, beacause now you have to specify which rows get
    * updated. You do that by building a where clause (the third argument) and then specifying values
    * for the arguments in the where clause (the final String[] array). */
    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    /* Use query() in a convenience method to call this on your CrimeTable.
    * */

    private Cursor queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return cursor;
    }


    /* You will bey creating ContentValues instances from Crimes a few times in CrimeLab. Add
    * a private method to take care of shuttling a Crime into a ContentValues. */
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }

}
