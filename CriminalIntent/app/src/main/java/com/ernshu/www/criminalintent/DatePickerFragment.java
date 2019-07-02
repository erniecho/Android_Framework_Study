package com.ernshu.www.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        /* In this implemetation, you use the AlertDialog.Builder class, which provides a
        * fluent interface for constructing an AlterDialog instance. First, you pass a
        * Context into the AlertDialog.Builder constructor, which returns an instance of
        * AlertDialog.Builder.*/
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
