package com.ernshu.www.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    /*this creates an abstract version of  the same main activity
    in CrimeActivty there is a method called createFragment. return an instance of the fragment
    that the activity is hosting.
     */
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_fragment);
        /* Fragment Manger is created on pg 148
        If AppCompatActivity is used  then use getSupportFragmentManager()
        Using Activity to describe the public class use getFragmentManger()
        */
        FragmentManager fm = getSupportFragmentManager();
        //uses the the layout in fragment container in activty_fragment.xml
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            //beginTransaction method that returns a an instance. include add and commit operation.
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }
}
