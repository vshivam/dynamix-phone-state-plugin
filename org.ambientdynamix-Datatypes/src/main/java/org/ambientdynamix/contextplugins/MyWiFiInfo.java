package org.ambientdynamix.contextplugins;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by shivam on 11/3/14.
 */
public class MyWiFiInfo implements IMyWiFiInfo {

    public static String CONTEXT_TYPE = "org.ambientdynamix.contextplugins.phonestate.wifi";
    private static ArrayList<ScanResultCustom> scanResults;

    public static Parcelable.Creator<MyWiFiInfo> CREATOR = new Parcelable.Creator<MyWiFiInfo>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had
         * previously been written by Parcelable.writeToParcel().
         */
        public MyWiFiInfo createFromParcel(Parcel in) {
            return new MyWiFiInfo(in);
        }

        /**
         * Create a new array of the Parcelable class.
         */
        public MyWiFiInfo[] newArray(int size) {
            return new MyWiFiInfo[size];
        }
    };

    public MyWiFiInfo(List<ScanResultCustom> scanResultList) {
        scanResults = new ArrayList<ScanResultCustom>(scanResultList);
    }

    @Override
    public ArrayList<ScanResultCustom> getResults() {
        return scanResults;
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
            for (ScanResultCustom result : scanResults) {
                stringRep = result.getSSID() + "\n" + stringRep;
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
        out.writeList(scanResults);
    }

    private MyWiFiInfo(final Parcel in) {
        scanResults = in.readArrayList(getClass().getClassLoader());
    }
}
