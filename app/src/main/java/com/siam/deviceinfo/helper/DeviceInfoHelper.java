package com.siam.deviceinfo.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.DisplayMetrics;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Locale;

public final class DeviceInfoHelper {
    private DeviceInfoHelper() {}
    public static LinkedHashMap<String,String> getInfo(Activity a) {
        LinkedHashMap<String,String> m=new LinkedHashMap<>();
        m.put("ব্র্যান্ড",Build.MANUFACTURER); m.put("মডেল",Build.MODEL); m.put("Android",Build.VERSION.RELEASE);
        m.put("API Level",String.valueOf(Build.VERSION.SDK_INT)); m.put("ডিভাইস",Build.DEVICE); m.put("বোর্ড",Build.BOARD);
        ActivityManager.MemoryInfo mi=new ActivityManager.MemoryInfo();
        ((ActivityManager)a.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(mi);
        m.put("RAM মোট",format(mi.totalMem)); m.put("RAM খালি",format(mi.availMem));
        File p=android.os.Environment.getDataDirectory(); android.os.StatFs st=new android.os.StatFs(p.getPath());
        m.put("স্টোরেজ মোট",format(st.getTotalBytes())); m.put("স্টোরেজ খালি",format(st.getAvailableBytes()));
        DisplayMetrics d=new DisplayMetrics(); a.getWindowManager().getDefaultDisplay().getRealMetrics(d);
        m.put("ডিসপ্লে",d.widthPixels+" × "+d.heightPixels+" px"); m.put("Density",String.format(Locale.US,"%.2f",d.density));
        StringBuilder abis=new StringBuilder(); for(String abi:Build.SUPPORTED_ABIS){if(abis.length()>0)abis.append(", ");abis.append(abi);}
        m.put("CPU",abis.length()>0?abis.toString():"অনুপলব্ধ");
        SensorManager sm=(SensorManager)a.getSystemService(Context.SENSOR_SERVICE); m.put("সেন্সর",sm.getSensorList(Sensor.TYPE_ALL).size()+"টি");
        return m;
    }
    public static String format(long n) {
        if(n<1024)return n+" B";
        int i=(int)(Math.log(n)/Math.log(1024));
        return String.format(Locale.getDefault(),"%.1f %s",n/Math.pow(1024,i),new String[]{"B","KB","MB","GB","TB"}[Math.min(i,4)]);
    }
}
