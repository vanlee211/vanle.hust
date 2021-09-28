package com.example.attendanceapplication.fragment;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.attendanceapplication.R;
import com.example.attendanceapplication.ConnectedThread;
import com.example.attendanceapplication.fragment.ExcelWriteHandler;
import com.example.attendanceapplication.fragment.addlist.StudentValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SignUpFragment extends Fragment {

    public static Handler handler;
    private static final int MESSAGE_READ = 2;
    ConnectedThread connectedThread;
    ArrayList<StudentValue> studentValues;

    EditText edtMSSV;
    TextView tv

    public SignUpFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectedThread.run();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

}