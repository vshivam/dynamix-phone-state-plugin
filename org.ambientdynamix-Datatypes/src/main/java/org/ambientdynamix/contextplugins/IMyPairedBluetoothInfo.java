package org.ambientdynamix.contextplugins;

import org.ambientdynamix.api.application.IContextInfo;

import java.util.ArrayList;

/**
 * Created by shivam on 11/4/14.
 */
public interface IMyPairedBluetoothInfo extends IContextInfo{

    public ArrayList<MyBluetoothInfo> getPairedDevices();
}
