package com.siam.deviceinfo.helper;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.util.LinkedHashMap;
import java.util.Map;

public final class NetworkHelper {
    private NetworkHelper() {}
    public static Map<String,String> current(Context c) {
        Map<String,String> m=new LinkedHashMap<>();
        try {
            WifiManager w=(WifiManager)c.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            m.put("Wi-Fi",w.isWifiEnabled()?"চালু":"বন্ধ");
            WifiInfo i=w.getConnectionInfo(); String s=i.getSSID();
            m.put("সংযুক্ত নেটওয়ার্ক",s==null?"অনুপলব্ধ":s.replace("\"",""));
            m.put("Signal",i.getRssi()+" dBm"); m.put("Link speed",i.getLinkSpeed()+" Mbps"); m.put("Frequency",i.getFrequency()+" MHz");
        } catch(Exception e) { m.put("অবস্থা","অনুমতি বা তথ্য পাওয়া যায়নি"); }
        return m;
    }
}
