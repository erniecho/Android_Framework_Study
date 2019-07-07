package com.ernshu.www.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        /*When the hosting activity needs instance of that fragment, you have it call
        * the newInstance(...) method rather than calling the constructor directly. The activity can
        * pass in any required parameters to newInstance(...) that the fragment needs to create its
        * arguments. Attaching arguments to a fragment. */
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieving the UUID from the fragment arguments.
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    /* save and update when set to pause. */
    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        //passing layout ID resource ID, create a view parent, configure the widget, connect inflate to view.
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        //Wiring widget in fragment.
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        //fetch a update to onCreateView(...) to display the crime's title.
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Button connection listener.
        mDateButton = (Button)v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*add a constant for the DatePickerFragment's tag. Then, in onCreateView(...)
                * remove the code that disables the date button and set a View.OnClickerListener
                * that shows a DatePicker Fragment when the date button is pressed. DatePickerFragment
                 * needs to initialize the DatePicker using the information held in the date.
                 * However, initializing the DatePicker requires integers for the month, day, and year.
                 * Date is more of a timestamp and cannot provide integers like this directly. */
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                /*method accepts the fragment that will be the target and a request code just
                like the one you send in startActivityForResult(...) You can retrieve them by
                calling getTargetFragment() and getTargetRequestCode() on the fragment that has set the target. */
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        
        //Connect the checkbox with listener.
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        //fetch a update to onCreateView(...) to display the solved status.
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });


        return v;
    }
    /*override onActivityResult() to retrieve the extra, set the date on the Crime,
    and refresh the text of the date button*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }
}
