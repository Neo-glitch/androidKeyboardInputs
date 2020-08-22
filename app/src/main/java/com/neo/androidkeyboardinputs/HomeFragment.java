package com.neo.androidkeyboardinputs;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.neo.androidkeyboardinputs.models.User;
import com.neo.androidkeyboardinputs.util.PreferenceKeys;
import com.neo.androidkeyboardinputs.util.Users;

import java.util.ArrayList;


/**
 * Apps HomeScreen fragment
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "HomeFragment";

    //constants
    private static final int NUM_COLUMNS = 2;                       // def num_columns in the recyclerView

    //widgets
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //vars
    private ArrayList<User> mMatches = new ArrayList<>();               // list of users
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private MainRecyclerViewAdapter mRecyclerViewAdapter;
    private String mSelectedInterest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d(TAG, "onCreateView: started.");
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        findMatches();

        return view;
    }

    /**
     * retrieves a list of users from Users class
     */
    private void findMatches() {
        getSavedPreferences();
        Users users = new Users();
        if(mMatches != null){
            mMatches.clear();
        }
        for(User user: users.USERS){
            if(mSelectedInterest.equals(getString(R.string.interested_in_anyone))){
                mMatches.add(user);
            }
            else if(user.getGender().equals(mSelectedInterest) || user.getGender().equals(getString(R.string.gender_none))){
                Log.d(TAG, "findMatches: found a match: " + user.getName());
                mMatches.add(user);
            }
        }
        if(mRecyclerViewAdapter == null){
            initRecyclerView();
        }
    }

    private void getSavedPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSelectedInterest = preferences.getString(PreferenceKeys.INTERESTED_IN, getString(R.string.interested_in_anyone));
        Log.d(TAG, "getSavedPreferences: got selected interest: " + mSelectedInterest);
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerViewAdapter = new MainRecyclerViewAdapter(getActivity(), mMatches);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    /**
     * method scrolls RV when back button is pressed to top
     */
    public void scrollToTop(){
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called.");
    }

    @Override
    public void onRefresh() {
        findMatches();
        onItemsLoadComplete();
    }


    /**
     * notifies adapter that data set might have changed and tells refresh listener to stop refreshing
     */
    void onItemsLoadComplete() {
        mRecyclerViewAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}

















