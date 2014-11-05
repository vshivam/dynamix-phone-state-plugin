/*
 * Copyright (C) The Ambient Dynamix Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ambientdynamix.contextplugins;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import org.ambientdynamix.api.contextplugin.ContextListenerInformation;
import org.ambientdynamix.api.contextplugin.ContextPluginRuntime;
import org.ambientdynamix.api.contextplugin.ContextPluginSettings;
import org.ambientdynamix.api.contextplugin.PowerScheme;
import org.ambientdynamix.api.contextplugin.security.PrivacyRiskLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Example auto-reactive plug-in that detects the device's battery level.
 *
 * @author Shivam
 */
public class AmbientDynamixRuntime extends ContextPluginRuntime {
    private static final int VALID_CONTEXT_DURATION = 60000;
    // Static logging TAG
    private final String TAG = this.getClass().getSimpleName();
    // Our secure context
    private Context context;
    ArrayList<UUID> bluetoothListeners;
    ArrayList<UUID> wifiListeners;

    WifiManager mWifiManager;
    private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Sending context event for bluetooth device to " + bluetoothListeners.size() + "listeners");
            for (UUID listenerId : bluetoothListeners) {
                sendContextEvent(listenerId, new MyBluetoothInfo(intent), PrivacyRiskLevel.LOW, VALID_CONTEXT_DURATION);
            }
        }
    };


    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mWifiManager != null) {
                Log.i(TAG, "Sending context event for wifi scan to " + wifiListeners.size() + "listeners");
                if (intent.getAction() == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                    List<ScanResult> mScanResults = mWifiManager.getScanResults();
                    ArrayList<ScanResultCustom> mScanResultsCustom = new ArrayList<ScanResultCustom>();

                    for (ScanResult scanResult : mScanResults) {
                        ScanResultCustom scanResultCustom = new ScanResultCustom();
                        scanResultCustom.setBSSID(scanResult.BSSID);
                        scanResultCustom.setCapabilities(scanResult.capabilities);
                        scanResultCustom.setFrequency(scanResult.frequency);
                        scanResultCustom.setLevel(scanResult.level);
                        scanResultCustom.setSSID(scanResult.SSID);
                        mScanResultsCustom.add(scanResultCustom);
                    }

                    for (UUID listenerId : wifiListeners) {
                        sendContextEvent(listenerId, new MyWiFiInfo(mScanResultsCustom), PrivacyRiskLevel.LOW, VALID_CONTEXT_DURATION);
                    }

                }
            }

        }
    };

    /**
     * Called once when the ContextPluginRuntime is first initialized. The implementing subclass should acquire the
     * resources necessary to run. If initialization is unsuccessful, the plug-ins should throw an exception and release
     * any acquired resources.
     */
    @Override
    public void init(PowerScheme powerScheme, ContextPluginSettings settings) throws Exception {
        // Set the power scheme
        this.setPowerScheme(powerScheme);
        // Store our secure context
        this.context = this.getSecuredContext();
    }

    /**
     * Called by the Dynamix Context Manager to start (or prepare to start) context sensing or acting operations.
     */
    @Override
    public void start() {
        Log.i(TAG, "Starting " + getClass().getName());
        bluetoothListeners = new ArrayList<UUID>();
        wifiListeners = new ArrayList<UUID>();
    }

    /**
     * Called by the Dynamix Context Manager to stop context sensing or acting operations; however, any acquired
     * resources should be maintained, since start may be called again.
     */
    @Override
    public void stop() {
        // Unregister battery level changed notifications
        try {
            context.unregisterReceiver(bluetoothReceiver);
            context.unregisterReceiver(wifiReceiver);
        } catch (IllegalArgumentException e) {
//            do nothing, receiver is not registered
        }
        Log.d(TAG, "Stopped!");
    }

    /**
     * Stops the runtime (if necessary) and then releases all acquired resources in preparation for garbage collection.
     * Once this method has been called, it may not be re-started and will be reclaimed by garbage collection sometime
     * in the indefinite future.
     */
    @Override
    public void destroy() {
        this.stop();
        context = null;
        Log.d(TAG, "Destroyed!");
    }

    @Override
    public void handleContextRequest(UUID requestId, String contextType) {
        Log.i(TAG, "New context request >> " + contextType);
        if (contextType.equals(MyBluetoothInfo.CONTEXT_TYPE)) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Log.e(TAG, "Device does not support Bluetooth");
            } else {
                if (!mBluetoothAdapter.isEnabled()) {
                    Log.e(TAG, "Bluetooth is disabled");
                } else {
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    context.registerReceiver(bluetoothReceiver, filter);
                    mBluetoothAdapter.startDiscovery();
                }
            }
            sendContextRequestSuccess(requestId);
        } else if (contextType.equals(MyWiFiInfo.CONTEXT_TYPE)) {
            mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            if (mWifiManager.isWifiEnabled()) {
                context.registerReceiver(wifiReceiver, filter);
                mWifiManager.startScan();
            } else {
                Log.e(TAG, "WiFi is disabled");
            }
            sendContextRequestSuccess(requestId);
        } else if (contextType.equals(MyPairedBluetoothInfo.CONTEXT_TYPE)) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Log.e(TAG, "Device does not support Bluetooth");
            } else {
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                List<MyBluetoothInfo> pairedDevicesList = new ArrayList<MyBluetoothInfo>();
                for (BluetoothDevice device : pairedDevices) {
                    pairedDevicesList.add(new MyBluetoothInfo(device.getName(), device.getAddress(), device.getBondState()));
                }
                sendContextEvent(requestId, new MyPairedBluetoothInfo(pairedDevicesList), PrivacyRiskLevel.LOW);
            }
        }
        /*
        else if (contextType.equals(MyNetworkInfo.CONTEXT_TYPE)) {
            Log.e(TAG, "New context request received >> " + MyNetworkInfo.CONTEXT_TYPE);
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            MyNetworkInfo info = new MyNetworkInfo(mConnectivityManager.getActiveNetworkInfo());
            Log.i(TAG, info.getStringRepresentation("text/plain"));
            sendContextEvent(requestId, info, PrivacyRiskLevel.LOW);
        }
        */
    }

    @Override
    public void handleConfiguredContextRequest(UUID requestId, String contextType, Bundle config) {

        // Warn that we don't handle configured requests
        Log.w(TAG, "handleConfiguredContextRequest called, but we don't support configuration!");
        // Drop the config and default to handleContextRequest
        handleContextRequest(requestId, contextType);
    }

    @Override
    public void updateSettings(ContextPluginSettings settings) {

    }

    @Override
    public void setPowerScheme(PowerScheme scheme) {

    }

    @Override
    public boolean addContextlistener(ContextListenerInformation listenerInfo) {
        if (listenerInfo.getContextType().equals(MyBluetoothInfo.CONTEXT_TYPE)) {
            bluetoothListeners.add(listenerInfo.getListenerId());
        } else if (listenerInfo.getContextType().equals(MyWiFiInfo.CONTEXT_TYPE)) {
            wifiListeners.add(listenerInfo.getListenerId());
        }
        return true;
    }

}