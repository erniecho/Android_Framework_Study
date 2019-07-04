package com.ernshu.www.criminalintent;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "date";
    private DatePicker mDatePicker;
    public static final String EXTRA_DATE =
            "com.ernshu.www.criminalintent.date";
/* To get data into your DatePickerFragment, you are going to stash the date in DatePickerFragment's
* arguments bundle, where the DatePickerFragment can access it.
* Creating and setting fragment arguments is typically done in a newInstance(Date) method that replaces the
* fragment constructor. In DatePickerFragment.java, add a newInstance(Date) method. */
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*To get the integers you need, you must create a Calander object and use the date
        * configure the Calander. Then you can retrive the requred information from the Calander. */
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        /*single View object - a DatePicker that you will inflate and pass into setView(...)*/
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        /*In onCreateDialog(Bundle), get the Date from the arguments and use it and a Calendar
        * to initialize the DatePicker. */
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        /* In this implemetation, you use the AlertDialog.Builder class, which provides a
        * fluent interface for constructing an AlterDialog instance. First, you pass a
        * Context into the AlertDialog.Builder constructor, which returns an instance of
        * AlertDialog.Builder.*/
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        /* setPostiveButton() with an implementation od DialogInterface.OnClickListener
                        * that retrieves the selected date and calls sendResult() */
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, month, day).getTime();
                                sendResult(Activity.RESULT_OK, date);
                            }
                        })
                .create();
    }
    /* sendResult method. When user presses the postive button in the dialog,
    you want to retreve the date from the DatePicker and send the result back to CrimeFragment.
    In on CreateDialog, replace the null parameter of setPostiveButton with an implementation
    of DialogInterface.OnClickerListener that retrieves the selected date and calls sendResult*/
    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(),resultCode, intent);
    }
}
