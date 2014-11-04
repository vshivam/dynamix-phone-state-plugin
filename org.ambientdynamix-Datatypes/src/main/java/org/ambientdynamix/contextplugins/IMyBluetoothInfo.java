package org.ambientdynamix.contextplugins;

import org.ambientdynamix.api.application.IContextInfo;

/**
 * Created by shivam on 10/31/14.
 */
public interface IMyBluetoothInfo extends IContextInfo {


    /**
     * Returns the hardware address of this BluetoothDevice.
     *
     * @return hardware address
     */
    public String getAddress();

    /**
     * Get the friendly Bluetooth name of the remote device.
     *
     * @return friendly name
     */
    public String getName();


    /**
     * Get the bond state of the remote device.
     *
     * Possible values for the bond state are: BOND_NONE, BOND_BONDING, BOND_BONDED.
     */
    public int getBondState();

}
