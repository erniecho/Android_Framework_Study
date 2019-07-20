package com.ernshu.www.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks {
    private static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //when you setContentView with the layout activity_crime_pager.xml
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent()
                        .getSerializableExtra(EXTRA_CRIME_ID);
        //attach the ViewPager to layout.
        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        //Get the crime list.
        mCrimes = CrimeLab.get(this).getCrimes();
        //get the activity instantce of Fragmentmanger.
        FragmentManager fragmentManger = getSupportFragmentManager();
        /*Then you set the adapter to be an unamed instance of FragmentStatePagerAdapter.
        * Creating the FragmentStatePagerAdapter require the FragmentManger. Remember that
        * FragmentStatePagerAdapter is your agent managing the conversation with ViewPager. For your
        * agent to do its job with the fragments that getItem(int) returns, it needs to be able to
        * add them to your activity. That is why it needs your FragmentManager. adding the fragments
        * you return to you activity and helping ViewPager identify the fragments' views so that they
        * can be placed correctly. */
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManger) {
            @Override
            public Fragment getItem(int position) {
                /*The getItem(int) method it fetches the crime instance
                for the given position in the data set.*/
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                //the number of items in the array list.
                return mCrimes.size();
            }
        });
        /* The ViewPager shows the first item in its pagerAdapter. You can have it show the crime
        * that was selected by setting the ViewPager's
        * current item to the index of the selected crime.
        * There is another PagerAdapter type that you can use called FragmentPagerAdapter.
        * FragmentPagerAdapter is used exactly like FragmentStatePagerAdapter. It only differs in how it
        * unloads you fragments when they are no longer needed.
        * */
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
