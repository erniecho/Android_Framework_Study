package com.ernshu.www.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CrimeListFragment extends Fragment {
    //declare recycleview object.
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    //declare visibility controls
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";


    /* Requrired interfaces for hosting activities
    *  implement a Callbacks interface, you first define call back below.*/

    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }
 /* Activity is a subclass of Context, so onAttach passes a Context as perameter, which is more flexible.
 * OnAttach(context) signature for onAttach.*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    /*The FragmentManger is responsible for calling Fragment.onCreateOptionsMenu(Menu, MenuInflater)
    * when the activity receives its onCreateOptinsMen ()  callback from the OS. You must explicitly
    * tell the FragmentManger that your fragment should receive a call to
    * onCreateOptionmen(). Define CrimeListFragment.onCreate(Bundle) and let the FragmentManger know
    * that CrimeListFragment*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    //create a view object.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //expand the fragment layout to the view.
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        //connect the recycle view inside the layout.
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        //attach a manger to the recycle view.
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*save the mSubtitleVisible instance variable across rotation with the saved
         instance state mechanism.*/
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        //updateUI call method that sets up CrimeListFragment's
        updateUI();
        //pass back out a view object when this method is called.
       return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
    /* onSaveInstanceState prevent subtitle from disapearing when the screen rotates. */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }
    /* mCallbacks variable and override onAttach(Context) and onDetach() to set and unset it.*/
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /*
                method updateUI creates adapter and set it on RecycleView.
                 */
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        /* Modify updateUI() method to call notifyDataSetChanged()
         if the CrimeAdapter is already set up. */
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }
    /*
    This creates a ViewHolder and an Adapter. this helps inflate the list_item-crime.
    RecyclerView.Adapter would use this code. Modify to allow OnClickListener in the view by
    adding implements View.onClickListener.
     */
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //declare binding textviews.
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        //binding data.
        private Crime mCrime;
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            //added Listener
            itemView.setOnClickListener(this);
            //Binding list to Textview.
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }
        public void bind(Crime crime) {
            //set text to the TextView from the data.
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }
        /*
        onClick method created with a Starting an Activity from a fragment.
        */
        @Override
        public void onClick(View view) {
            /*CrimeHolder to use the newIntent method while passing in the crime ID
            * pg207*/
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            /* Fragment fragment = CrimeFragment.newInstance(mCrime.getId());
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.detail_fragment_container, fragment)
                    .commit(); */
            /*
            You call the Fragment.startActivity(intent) method,
            which calls the corresponding Activity method scenes.
             */
            startActivity(intent);
        }
    }
    /*
    Creating a Adapter that uses the CrimeHolder class. Generate code by hover and auto-generate methods.
    hover mouse over the word extend and press Option + Return on the mac keyboard.
    select Implement methods by clicking OK and it will auto generate the code.
     */
    public class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //creates a new ViewHolder
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            //binding data to ViewHolder.
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            //get a count number of mCrime array by call the method .size()
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }
    /*override onCreateOptionsMenu(Menu , MenuInflater) to inflate the menu defined in fragment_crime_list.xml*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        /*menu is created trigger an re-creation of the action items
        * when the user presses on the SHOW SUBTITLE action item. */
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }


    /* When the user presses an action item, your fragment recieves a callback to the method
    * onOptionsItemSelected(MenuItem). This method receives an instance of MenuItem that describe
    * the user's selection. Implement onOptionItemSelected(MenuItem) to respond to selection of the
    * MenuItem by creating a new Crime, adding it to CrimeLab, and then starting an instance of
    * CrimePagerActivity to edit the new Crime. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                /*mSubtitleVisible member variable when showing or hiding
                * the subtitle in the toolbar. */
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
    /* updateSubtitle() first generates the subtitle string using the getString(int resId Object
     * formatArgs) method, which accepts replacement values for the placeholders in the string
      * resource. the activity that is hosting the CrimeListFragment is cast to an AppCompatActivity.
      * Recall that because CriminalIntent uses the AppCompat library, all activities are subclass of
      * AppCompatActivity, which allows you to access the toolbar.*/
    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        /* the subtitle visibilty in the toolbar.*/
        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
}
