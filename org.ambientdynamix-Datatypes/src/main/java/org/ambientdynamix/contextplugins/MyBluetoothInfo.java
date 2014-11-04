package org.ambientdynamix.contextplugins;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shivam on 10/31/14.
 */
public class MyBluetoothInfo implements IMyBluetoothInfo {

    public static String CONTEXT_TYPE = "org.ambientdynamix.contextplugins.phonestate.bluetooth";
    private String name;
    private String address;
    private int bond_state;

    public static Parcelable.Creator<MyBluetoothInfo> CREATOR = new Parcelable.Creator<MyBluetoothInfo>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had
         * previously been written by Parcelable.writeToParcel().
         */
        public MyBluetoothInfo createFromParcel(Parcel in) {
            return new MyBluetoothInfo(in);
        }

        /**
         * Create a new array of the Parcelable class.
         */
        public MyBluetoothInfo[] newArray(int size) {
            return new MyBluetoothInfo[size];
        }
    };

    public MyBluetoothInfo(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        name = device.getName();
        address = device.getAddress();
        bond_state = device.getBondState();
    }

    public MyBluetoothInfo(String name, String address, int bond_state) {
        this.name = name;
        this.address = address;
        this.bond_state = bond_state;
    }

    @Override
    public String getContextType() {
        return CONTEXT_TYPE;
    }

    @Override
    public String getImplementingClassname() {
        return this.getClass().getName();
    }

    @Override
    public Set<String> getStringRepresentationFormats() {
        Set<String> formats = new HashSet<String>();
        formats.add("text/plain");
        return formats;
    }

    @Override
    public String getStringRepresentation(String format) {
        if (format.equalsIgnoreCase("text/plain"))
            return "Name : " + name + "\n" + "Address : " + address + "\n" + "Bond state : " + bond_state;
        else {
            //Format not supported, so return empty string
            return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getBondState() {
        return bond_state;
    }

    /**
     * Used by Parcelable when sending (serializing) data over IPC.
     */
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(address);
        out.writeInt(bond_state);
    }

    /**
     * Used by the Parcelable.Creator when reconstructing (deserializing) data sent over IPC.
     */
    private MyBluetoothInfo(final Parcel in) {
        name = in.readString();
        address = in.readString();
        bond_state = in.readInt();
    }

}
