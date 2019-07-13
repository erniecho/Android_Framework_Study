package com.ernshu.www.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;

import static android.text.format.DateFormat.format;
import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private  static final int REQUEST_CONTACT = 1;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

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

        /* In CrimeFragment.onCreateView(), get a reference to the SEND CRIME REPORT button and
        * set a listener on it. Within the listener's implementation, create an implicit intent
        * and pass it into startActivity(Intent). */
        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                /* createChooser does shareing to other devices. */
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });
        /* Now you are going to create another implicit intent that enables users to choose a suspect
        * from their contacts. This implicit intent will have an action and a location where the relevant
        * data can be found. The action will be Intent.ACTION_PICK. The data for contacts is at
        * ContactsContract.Contacts.CONTENT_URI. In short, you are asking Android to help pick an item
        * in the contacts database. */
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
         mSuspectButton.setText(mCrime .getSuspect());
        }
        /*
        * PackageManager knows about all the components installed on your Android device, including
        * all of its activities. By calling resolveActivity(Intent, int), you ask it to find an activity
        * that matches the Intent you gave it. The MATCH_DEFAULT_ONLY flag restricts this search to activities
        * with the CATEGORY_DEFAULT flag, just like startActivity(Intent) does. */
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }
        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);

        return v;
    }
    /*override onActivityResult() to retrieve the extra, set the date on the Crime,
    and refresh the text of the date button*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        /* Getting the data from the contact list
        * you will receive an intent via onActivtyResult(). This intent includes a data URI. The URI
        * is a locator that points at the single contact the user picked. retrieve a contact's name
        * from the contacts application in your onActivityResult() implementation in CrimeFragment.
        * */
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            /* Specify which fields you want your query to return
            * value for*/
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            /* Perform your query - the contactUri is like a "where"
            * clause here */
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null ,null );
            try {
                /* Double-check that you actually got results */
                if (c.getCount() == 0) {
                    return;
                }
                /* Pull out the first column of the first row of data
                * that is your suspect's name */
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }
}
