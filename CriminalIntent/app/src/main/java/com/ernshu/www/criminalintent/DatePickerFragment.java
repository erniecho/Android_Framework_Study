package com.ernshu.www.criminalintent;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class DatePickerFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*single View object - a DatePicker that you will inflate and pass into setView(...)*/
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);
        /* In this implemetation, you use the AlertDialog.Builder class, which provides a
        * fluent interface for constructing an AlterDialog instance. First, you pass a
        * Context into the AlertDialog.Builder constructor, which returns an instance of
        * AlertDialog.Builder.*/
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
