package org.ambientdynamix.contextplugins;

import org.ambientdynamix.api.application.IContextInfo;

import java.util.List;

/**
 * Created by shivam on 11/3/14.
 */
public interface IMyWiFiInfo extends IContextInfo {

    public List<ScanResultCustom> getResults();
}
