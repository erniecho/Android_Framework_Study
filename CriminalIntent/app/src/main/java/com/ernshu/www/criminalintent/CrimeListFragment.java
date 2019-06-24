package com.ernshu.www.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    RecyclerView.Adapter would use this code.
     */
    private class CrimeHolder extends RecyclerView.ViewHolder {
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
        }
    }
    /*
    Creating a Adapter that uses the CrimeHolder class.
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

        }

        @Override
        public int getItemCount() {
            //get a count number of mCrime array by call the method .size()
            return mCrimes.size();
        }
    }
}
