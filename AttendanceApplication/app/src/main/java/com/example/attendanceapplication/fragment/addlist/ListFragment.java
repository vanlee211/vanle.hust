package com.example.attendanceapplication.fragment.addlist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.attendanceapplication.R;
import com.example.attendanceapplication.databinding.FragmentAddListBinding;

import java.io.File;
import java.util.ArrayList;

public class ListFragment extends Fragment {

    private AddListViewModel addListViewModel;
    private FragmentAddListBinding binding;
    private static final String TAG = "AddListFragment";
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    File file;

    Button btnUpDirectory,btnSDCard;

    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;

    ArrayList<StudentValue> uploadData;
    ListView lvInternalStorage;

    public static AddListFragment newInstance() {
        return new AddListFragment();
    }


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }
}