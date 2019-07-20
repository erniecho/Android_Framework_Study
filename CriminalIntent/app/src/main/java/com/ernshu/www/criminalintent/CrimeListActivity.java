package com.ernshu.www.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    @Override
    protected Fragment createFragment(){
    return new CrimeListFragment();
    }
    /* FrameLayout has a fragment_container layout ID, so the code in
    * SingleFragmentActivity.onCreate() can work as before. When the activity is created, the fragment
    * that is returned in createFragment() will appear in the lefthand pane.*/
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }


    /* If the layout does have a detail_fragment_container, then you are going to create a fragment
    * transaction that removes the existing fragment from the detail_fragment_container and
    * adds the fragment that you want to see. */
    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this , crime.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment)
                getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
