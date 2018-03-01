package com.baichang.android.printer.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

/**
 * Created by iscod. Time:2016/12/29-16:25.
 */

public class BluetoothUtil {

    private Context mContext;
    private BluetoothUtlListener mListener;

    public BluetoothUtil(Context context, boolean isRegister) {
        mContext = context;
        //是否注册广播。
        if (isRegister) {
            register();
        }
    }

    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mContext.registerReceiver(mFindBlueToothReceiver, filter);
    }

    public void unregister() {
        if (mContext != null) {
            mContext.unregisterReceiver(mFindBlueToothReceiver);
        }
    }

    public void setListener(BluetoothUtlListener listener) {
        mListener = listener;
    }

    public void openBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(mContext, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                //蓝牙未打开
                if (mListener != null) {
                    mListener.noOpen();
                }
            } else {
                //已经打开
                if (mListener != null) {
                    mListener.opened();
                }
            }
        }
    }

    /**
     * 获取蓝牙新设备
     */
    private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (resultListener != null) {
                        resultListener.find(device.getName(), device.getAddress());
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (resultListener != null) {
                    resultListener.finished();
                }
            }
        }
    };

    public interface BluetoothUtlListener {

        void opened();

        void noOpen();
    }

    private BluetoothResultListener resultListener;

    public void setBluetoothResultListener(BluetoothResultListener listener) {
        resultListener = listener;
    }

    public interface BluetoothResultListener {

        void find(String deviceName, String deviceAddress);

        void finished();
    }
}
