package com.example.attendanceapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Object> deviceList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textAddress;
        LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            textName = view.findViewById(R.id.textViewDeviceName);
            textAddress = view.findViewById(R.id.textViewDeviceAddress);
            linearLayout = view.findViewById(R.id.linearLayoutDeviceInfo);
        }
    }

    public DeviceListAdapter(Context context, List<Object> devistList) {
        this.context = context;
        this.deviceList = devistList;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_info_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        final DeviceInfoModel deviceInfoModel = (DeviceInfoModel) deviceList.get(position);
        itemHolder.textName.setText(deviceInfoModel.getDeviceName());
        itemHolder.textAddress.setText(deviceInfoModel.getDeviceHardwareAddress());

        //khi thiết bị được chọn
        itemHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BluetoothConnection.class);
                intent.putExtra("deviceName",deviceInfoModel.getDeviceName());
                intent.putExtra("deviceAddress",deviceInfoModel.getDeviceHardwareAddress());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount()
    {
        int dataCount = deviceList.size();
        return dataCount;
    }

//    public void onResume(){
//        super.onResume();
//        boolean fAdapterEnabled = BluetoothAdapter.getDefaultAdapter().isEnabled();
//        try {
//            if (!fAdapterEnabled) {
//                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivity(intent);
//            }
//        }catch (Exception e){
//            Log.d("bgx_dbg", "Exception caught while calling isEnabled.");
//            Toast.makeText(this,"Exception caught", Toast.LENGTH_LONG).show();
//        }
//    }
}