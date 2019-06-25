package com.ernshu.www.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    /*You can tell CrimeFragment which Crime to display by passing the crime ID as an Intent
    * extra when CrimeActivity is started binds the data from the list to the form of the xml layout
    * you call putExtra(...) and pass in a string key and the value the key maps to (the crimeId).
    * In this case, you are calling putExtra(string, serializable) because UUID is a
    * Serializable object.*/
    public static final String EXTRA_CRIME_ID = "com.ernshu.www.criminalintent.crime_id";
    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    /*
    uses a re-useable fragment from SingleFragmentActivity.
    you also have to edit the androidmanifest.xml and remove the launcher and main code outside.
     */
    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }

}
