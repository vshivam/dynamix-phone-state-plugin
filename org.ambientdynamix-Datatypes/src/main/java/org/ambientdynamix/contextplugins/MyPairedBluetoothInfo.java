package org.ambientdynamix.contextplugins;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by shivam on 11/4/14.
 */


public class MyPairedBluetoothInfo implements IMyPairedBluetoothInfo {

    public static String CONTEXT_TYPE = "org.ambientdynamix.contextplugins.phonestate.paired";
    ArrayList<MyBluetoothInfo> pairedDevices;

    public static Parcelable.Creator<MyPairedBluetoothInfo> CREATOR = new Parcelable.Creator<MyPairedBluetoothInfo>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had
         * previously been written by Parcelable.writeToParcel().
         */
        public MyPairedBluetoothInfo createFromParcel(Parcel in) {
            return new MyPairedBluetoothInfo(in);
        }

        /**
         * Create a new array of the Parcelable class.
         */
        public MyPairedBluetoothInfo[] newArray(int size) {
            return new MyPairedBluetoothInfo[size];
        }
    };

    public MyPairedBluetoothInfo(List<MyBluetoothInfo> pairedDevicesList) {
        pairedDevices = new ArrayList<MyBluetoothInfo>(pairedDevicesList);
    }

    @Override
    public ArrayList<MyBluetoothInfo> getPairedDevices() {
        return pairedDevices;
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
        if (format.equalsIgnoreCase("text/plain")) {
            String stringRep = "";
            for (MyBluetoothInfo device : pairedDevices) {
                stringRep = device.getName() + "\n" + stringRep;
            }
            return stringRep;
        } else {
            // Unsupported Format. Reply with empty string.
            return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeList(pairedDevices);
    }


    private MyPairedBluetoothInfo(final Parcel in) {
        pairedDevices = in.readArrayList(getClass().getClassLoader());
    }

}
