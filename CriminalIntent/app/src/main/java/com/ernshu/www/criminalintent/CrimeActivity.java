package com.ernshu.www.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity {
    /*
    uses a re-useable fragment from SingleFragmentActivity.
    you also have to edit the androidmanifest.xml and remove the launcher and main code outside.
     */
    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }

}
