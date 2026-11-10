package com.siam.deviceinfo.receiver;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.content.pm.PackageManager;

public class BatteryReceiver extends BroadcastReceiver {
    private boolean alerted=false;
    @Override public void onReceive(Context context,Intent intent){
        if(!Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction()))return;
        int tenths=intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1);
        if(tenths<400){alerted=false;return;}
        if(alerted)return;
        if(Build.VERSION.SDK_INT>=33&&context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED)return;
        NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(nm==null)return;
        String channel="battery_overheat";
        if(Build.VERSION.SDK_INT>=26)nm.createNotificationChannel(new NotificationChannel(channel,"ব্যাটারি তাপমাত্রা",NotificationManager.IMPORTANCE_HIGH));
        Notification.Builder builder=Build.VERSION.SDK_INT>=26?new Notification.Builder(context,channel):new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert).setContentTitle("ফোন গরম হচ্ছে").setContentText("ব্যাটারির তাপমাত্রা "+(tenths/10f)+"°C — চার্জার খুলে ফোন ঠান্ডা করুন।").setAutoCancel(true);
        nm.notify(401,builder.build());alerted=true;
    }
}
