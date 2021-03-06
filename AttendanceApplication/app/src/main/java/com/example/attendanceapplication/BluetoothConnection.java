package com.example.attendanceapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class BluetoothConnection extends AppCompatActivity {

    private String deviceName = null;
    private String deviceAddress;
    public static CreateConnectThread createConnectThread;
    public static Handler handler;
    public static BluetoothSocket mSocket;
    public static ConnectedThread connectedThread;
    public static BluetoothAdapter bluetoothAdapter;
    public static Context context;
    public static Intent intent;

    private static final int CONNECTING_STATUS = 1;
    private static final int MESSAGE_READ = 2;
    private BroadcastReceiver BTReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection);

        Button btnConnect = (Button) findViewById(R.id.btnConnect);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        TextView textViewInfo = (TextView) findViewById(R.id.textViewInfo);

        deviceName = getIntent().getStringExtra("deviceName");
        if(deviceName != null)
        {
            deviceAddress = getIntent().getStringExtra("deviceAddress");
            Toast.makeText(BluetoothConnection.this,"Connecting to "+deviceName+" ...",Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.VISIBLE);
            btnConnect.setEnabled(false);

            //t???o lu???ng th???c thi m???i ????? k???t n???i v???i thi???t b??? v???a ch???n
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//tr??? v??? phi??n b???n Adapter
            createConnectThread = new CreateConnectThread(bluetoothAdapter,deviceAddress);
            createConnectThread.start();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(BTReceiver, filter);

        String action = intent.getAction();
        handler = new Handler(Looper.getMainLooper())
        {
            @Override
            public void handleMessage(Message msg)
            {
                switch (msg.what) {
                    case CONNECTING_STATUS:
                        switch (msg.arg1)
                        {
                            case 1:
                                if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                                    intent = new Intent(BluetoothConnection.this,MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(BluetoothConnection.this,"Connected to ",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    btnConnect.setEnabled(true);
                                    finish();
                                }
                                break;
                            case -1:
                                if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                                    intent = new Intent(BluetoothConnection.this,MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "BT Disconnected", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    btnConnect.setEnabled(true);
                                }
                                break;
                        }
                        break;
                    case MESSAGE_READ:
                        String deviceMsg = msg.obj.toString();
                        textViewInfo.setText("UID : " + deviceMsg);
                        break;
                }
            }
        };
        //ch???n thi???t b??? Bluetooth
        btnConnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BluetoothConnection.this,SelectActivity.class);
                startActivity(intent);
            }
        });

    }

//    The BroadcastReceiver that listens for bluetooth broadcasts
//    private final BroadcastReceiver BTReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
//                intent = new Intent(BluetoothConnection.this,MainActivity.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "BT Connected", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//
//            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
//                intent = new Intent(BluetoothConnection.this,MainActivity.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "BT Disconnected", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    };
    //lu???ng k???t n???i Bluetooth
    public static class CreateConnectThread extends Thread
    {
        public CreateConnectThread(BluetoothAdapter bluetoothAdapter,String address)
        {
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            UUID uuid = bluetoothDevice.getUuids()[0].getUuid();

            try
            {
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            }
            catch (Exception e)
            {
                Log.e(TAG, "H??m t???o Socket kh??ng th??nh c??ng!");
            }
            mSocket = tmp;
        }

        public void run()
        {
            //h???y ti???n tr??nh n???u k???t n???i ch???m
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try
            {
                mSocket.connect();
                Log.e("Status","Thi???t b??? ???????c k???t n???i!");
                handler.obtainMessage(CONNECTING_STATUS,1,-1).sendToTarget();

            }
            catch (IOException connectException)
            {
                //kh??ng th??? k???t n???i, ????ng ??? c???m v?? k???t th??c
                try
                {
                    mSocket.close();
                    Log.e("Status","kh??ng th??? k???t n???i v???i thi???t b???!");
                    handler.obtainMessage(CONNECTING_STATUS,-1,-1).sendToTarget();
                }
                catch (IOException closeException)
                {
                    Log.e(TAG,"kh??ng th??? ng???t ???????c ??? c???m m??y kh??ch",closeException);
                }
                return;
            }
            //k???t n???i th??nh c??ng, th???c hi???n trong m???i lu???ng ri??ng bi???t
            connectedThread = new ConnectedThread(mSocket);
            connectedThread.run();
        }
        //ng???t ??? c???m m??y kh??ch v?? k???t th??c lu???ng, k???t n???i chia ra th??nh c??c lu???ng ri??ng bi???t
        public void cancel()
        {
            try{
                mSocket.close();
            }
            catch (Exception e){
                Log.e(TAG,"kh??ng th??? ng???t ??? c???m m??y kh??ch",e);
            }
        }
    }

    //h???y k???t n???i n???u nh???n back
    @Override
    public void onBackPressed() {
        if (createConnectThread != null)
        {
            createConnectThread.cancel();
        }
        finish();
    }
}