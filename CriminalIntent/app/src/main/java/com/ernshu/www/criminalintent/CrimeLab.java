package com.ernshu.www.criminalintent;

/*
singleton class that allows only one instance of itself to be created.
 */
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

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
        //method that creates a ArrayList for mCrimes.
        mCrimes = new ArrayList<>();
    }

    public void addCrime(Crime c) {
        /*To respond to the user pressing the New Crime action item,
        you need a way to add a new Crime to your list of crimes. In CrimeLab.java,
        add a method to do this.*/
        mCrimes.add(c);
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        /*
        method to return a UUID of a object.
         */
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}
