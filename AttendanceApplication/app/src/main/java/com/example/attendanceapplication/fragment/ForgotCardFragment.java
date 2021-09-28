package com.example.attendanceapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendanceapplication.R;

public class ForgotCardFragment extends Fragment {

    public ForgotCardFragment() {
        // Required empty public constructor
    }

    public static ForgotCardFragment newInstance() {
        ForgotCardFragment fragment = new ForgotCardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_card, container, false);
    }
}