package com.project.rishabhsingh.dWarden.DonorSearchingSystem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.project.rishabhsingh.dWarden.R;

public class BloodSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_search);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DonorSearchFragment donorSearchFragment = new DonorSearchFragment(this);
        DonorListFragment donorListFragment = new DonorListFragment(this);
        fragmentTransaction.add(R.id.fragment_donorSearch,donorSearchFragment);
        fragmentTransaction.add(R.id.fragment_donorList,donorListFragment);
        fragmentTransaction.commit();
    }
}
