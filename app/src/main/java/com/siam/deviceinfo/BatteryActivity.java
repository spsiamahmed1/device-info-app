package com.siam.deviceinfo;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.siam.deviceinfo.helper.BatteryHelper;
import java.util.Map;

public class BatteryActivity extends BaseScreenActivity {
    private boolean registered=false;
    private final BroadcastReceiver receiver=new com.siam.deviceinfo.receiver.BatteryReceiver();
    protected int layout(){return R.layout.activity_battery;}
    protected String title(){return "ব্যাটারি ডাক্তার";}
    public void onCreate(Bundle b){
        super.onCreate(b); heading("ব্যাটারির বর্তমান অবস্থা");
        for(Map.Entry<String,String> e:BatteryHelper.read(this).entrySet())line(e.getKey(),e.getValue());
        heading("ব্যাটারি ব্যবহার");
        note("প্রতি অ্যাপের battery usage Android-এর system screen থেকে দেখুন; অন্য অ্যাপের private drain history সরাসরি পড়া যায় না।");
        action("Battery usage settings খুলুন",()->{try{startActivity(new Intent("android.intent.action.POWER_USAGE_SUMMARY"));}catch(Exception e){note("System battery settings পাওয়া যায়নি।");}});
        note("৪০°C বা বেশি হলে এই স্ক্রিন খোলা থাকা অবস্থায় সতর্কতা দেখানো হবে। স্থায়ী background সতর্কতার জন্য foreground service চালু রাখতে হয়।");
        if(Build.VERSION.SDK_INT>=33&&checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED){action("তাপমাত্রা সতর্কতার notification অনুমতি দিন",()->requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},21));}
    }
    @Override protected void onResume(){super.onResume();if(!registered){IntentFilter f=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);if(Build.VERSION.SDK_INT>=33)registerReceiver(receiver,f,Context.RECEIVER_NOT_EXPORTED);else registerReceiver(receiver,f);registered=true;}}
    @Override protected void onPause(){if(registered){try{unregisterReceiver(receiver);}catch(Exception ignored){}registered=false;}super.onPause();}
}
