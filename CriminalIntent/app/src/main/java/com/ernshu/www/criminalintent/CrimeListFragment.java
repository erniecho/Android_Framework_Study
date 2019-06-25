package com.ernshu.www.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment extends Fragment {
    //declare recycleview object.
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
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
        //updateUI call method that sets up CrimeListFragment's
        updateUI();
        //pass back out a view object when this method is called.
       return view;
    }
    /*
    method updateUI creates adapter and set it on RecycleView.
     */
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
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
        onClick method created with a Toast Message.
        */
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),
                    mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
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
    }
}
