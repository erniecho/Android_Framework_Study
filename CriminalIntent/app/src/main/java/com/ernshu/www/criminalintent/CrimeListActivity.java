package com.ernshu.www.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
    return new CrimeListFragment();
    }
    /* FrameLayout has a fragment_container layout ID, so the code in
    * SingleFragmentActivity.onCreate() can work as before. When the activity is created, the fragment
    * that is returned in createFragment() will appear in the lefthand pane.*/
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_twopane;
    }
}
