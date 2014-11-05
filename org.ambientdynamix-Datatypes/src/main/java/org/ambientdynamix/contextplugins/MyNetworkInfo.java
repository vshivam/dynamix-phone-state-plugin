package org.ambientdynamix.contextplugins;

import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shivam on 11/5/14.
 */
public class MyNetworkInfo implements IMyNetworkInfo {

    public static String CONTEXT_TYPE = "org.ambientdynamix.contextplugins.phonestate.network";
    NetworkInfo networkInfo;

    public static Parcelable.Creator<MyNetworkInfo> CREATOR = new Parcelable.Creator<MyNetworkInfo>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had
         * previously been written by Parcelable.writeToParcel().
         */
        public MyNetworkInfo createFromParcel(Parcel in) {
            return new MyNetworkInfo(in);
        }

        /**
         * Create a new array of the Parcelable class.
         */
        public MyNetworkInfo[] newArray(int size) {
            return new MyNetworkInfo[size];
        }
    };

    public MyNetworkInfo(NetworkInfo networkInfo) {
        this.networkInfo = networkInfo;
    }

    private MyNetworkInfo(final Parcel in) {
        networkInfo = in.readParcelable(getClass().getClassLoader());
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        return networkInfo;
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
        if (format.equals("text/plain")) {
            return networkInfo.getSubtype() + " : " + networkInfo.getSubtype() + "\n Connected Status : " + networkInfo.isConnected();
        } else {
            return "unsupported format. use text/plain";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        networkInfo.writeToParcel(parcel, i);
    }
}
