package com.ernshu.www.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Fragment Manger is created on pg 148
        If AppCompatActivity is used  then use getSupportFragmentManager()
        Using Activity to describe the public class use getFragmentManger()
        */
        FragmentManager fm = getSupportFragmentManager();
        //uses the the layout in fragment container in activity_main.xml
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new CrimeFragment();
            //beginTransaction method that returns a an instance. include add and commit operation.
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }
}
