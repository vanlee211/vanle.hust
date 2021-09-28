package com.example.attendanceapplication;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ConnectedThread extends Thread{
    private final BluetoothSocket mSocket;
    private final InputStream mInStream;
    private final OutputStream mOutStream;
    private  boolean createString = false;
    private String readMessage;
    public static Handler handler;

    private static final int MESSAGE_READ = 2;

    public ConnectedThread(BluetoothSocket socket) {
        mSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        //nhận luồng dữ liệu vào ra
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        }
        catch (IOException e) { }

        mInStream = tmpIn;
        mOutStream = tmpOut;
    }

    public void run()
    {
        byte[] buffer = new byte[1024];
        int bytes = 0;//giá trị trả về của hàm read()
        //nhận dữ liệu đầu vào cho đến khi xảy ra ngoại lệ
        while (true)
        {
            try {
                // đọc luồng dữ liệu vào
                buffer[bytes] = (byte) mInStream.read();
                String readByte = new String(buffer, StandardCharsets.UTF_8).substring(0,1);

                //
                switch (readByte){
                    case "<":
                        createString = true;
                        readMessage = "";
                        break;
                    case ">":
                        createString = false;
                        break;
                }

                if (createString)
                {
                    readMessage = readMessage + readByte;
                }
                else {
                    Log.e("UID Length", readMessage.length() + " characters");
                    String readUID = readMessage.substring(1).trim();
                    handler.obtainMessage(MESSAGE_READ,readUID).sendToTarget();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    //gửi dữ liệu đến các thiết bị
    public void write(String input)
    {
        byte[] bytes = input.getBytes(); //chuyển chuỗi kí tự thành bytes
        try {
            mOutStream.write(bytes);
        }
        catch (IOException e) {
            Log.e("Gửi lỗi","không thể gửi tin nhắn!",e);
        }
    }

    //gọi hàm hủy
    public void cancel() {
        try {
            mSocket.close();
        }
        catch (IOException e) { }
    }
}
