package org.ambientdynamix.contextplugins;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shivam on 11/4/14.
 */
public class ScanResultCustom implements Parcelable {

    public static final Parcelable.Creator<ScanResultCustom> CREATOR = new Parcelable.Creator<ScanResultCustom>() {
        public ScanResultCustom createFromParcel(Parcel in) {
            return new ScanResultCustom(in);
        }

        public ScanResultCustom[] newArray(int size) {
            return new ScanResultCustom[size];
        }
    };

    public ScanResultCustom(Parcel in) {
        BSSID = in.readString();
        SSID = in.readString();
        capabilities = in.readString();
        frequency = in.readInt();
        level = in.readInt();
    }

    public ScanResultCustom() {

    }

    /**
     * The address of the access point.
     */
    private String BSSID;
    /**
     * The network name.
     */
    private String SSID;
    /**
     * Describes the authentication, key management, and encryption schemes supported by the access point.
     */
    private String capabilities;
    /**
     * The frequency in MHz of the channel over which the client is communicating with the access point.
     */
    private int frequency;
    /**
     * The detected signal level in dBm, also known as the RSSI.
     */
    private int level;

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "BSSID : " + BSSID + "\n" + "SSID : " + SSID + "\n"
                + "Capabilities : " + capabilities + "\n" + "Frequency : " + frequency + "\n" + "level : " + level;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(BSSID);
        parcel.writeString(SSID);
        parcel.writeString(capabilities);
        parcel.writeInt(frequency);
        parcel.writeInt(level);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
