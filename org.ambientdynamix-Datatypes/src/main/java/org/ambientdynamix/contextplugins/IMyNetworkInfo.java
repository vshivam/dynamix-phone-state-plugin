package org.ambientdynamix.contextplugins;

import android.net.NetworkInfo;
import org.ambientdynamix.api.application.IContextInfo;

/**
 * Created by shivam on 11/5/14.
 */
public interface IMyNetworkInfo extends IContextInfo {

    public NetworkInfo getActiveNetworkInfo();
}
