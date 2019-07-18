package com.ernshu.www.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
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

import com.ernshu.www.criminalintent.CrimeListFragment.Callbacks;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private  static final int REQUEST_CONTACT = 1;
    private  static final int REQUEST_PHOTO = 2;

    private Crime mCrime;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;

    /*
    * Required interface for hosting activities. */
    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

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
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieving the UUID from the fragment arguments.
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }

    /* save and update when set to pause. */
    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
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
        /* Calling FileProvider.getUriForFile() translates your local filepath into a Uri the camera app
         * can see. To actually write to it, though, you need to grant the camera app permission. To do this,
         * you grant the Intent.FLAG_GRANT_WRITE_URI_PERMISSION flag to every activity your cameraImage
         * intent can resolve to. That grants them all a write permission specifically for this one Uri.
         * Adding the android:grantUriPermissions attribute in your provider declaration was necessary
         * to open this bit of functionality. Later, you will revoke this permission
         * to close up that gap in your armor again. */
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.ernshu.www.criminalintent.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
        updatePhotoView();

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
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.ernshu.www.criminalintent.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
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

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
