package com.siam.deviceinfo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.hardware.SensorManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class TestActivity extends BaseScreenActivity {
    protected int layout() { return R.layout.activity_test; }
    protected String title() { return "ফোন টেস্ট"; }
    public void onCreate(Bundle b) {
        super.onCreate(b);
        heading("স্ক্রিন পরীক্ষা");
        action("লাল / সবুজ / নীল / সাদা / কালো স্ক্রিন টেস্ট", this::colors);
        heading("হার্ডওয়্যার পরীক্ষা");
        action("Vibration টেস্ট", () -> {
            try { Vibrator v=(Vibrator)getSystemService(VIBRATOR_SERVICE); if(Build.VERSION.SDK_INT>=26) v.vibrate(VibrationEffect.createOneShot(350,VibrationEffect.DEFAULT_AMPLITUDE)); else v.vibrate(350); note("কম্পন পরীক্ষা চালানো হয়েছে।"); }
            catch(Exception e) { note("এই ফোনে কম্পন পাওয়া যায়নি।"); }
        });
        action("ক্যামেরা খুলুন", () -> { Intent i=new Intent("android.media.action.IMAGE_CAPTURE"); if(i.resolveActivity(getPackageManager())!=null) startActivity(i); else note("ক্যামেরা অ্যাপ পাওয়া যায়নি।"); });
        action("সেন্সর পরীক্ষা", () -> { SensorManager sm=(SensorManager)getSystemService(SENSOR_SERVICE); line("উপস্থিত সেন্সর", sm.getSensorList(android.hardware.Sensor.TYPE_ALL).size()+"টি"); });
        action("মাইক্রোফোন লেভেল পরীক্ষা", this::startMic);
        action("স্পিকার পরীক্ষা (টোন)", this::tone);
        note("Fingerprint-এর নিরাপদ পরীক্ষা Android BiometricPrompt-এর মাধ্যমে হয়; কোনো আঙুলের ছাপ অ্যাপ সংরক্ষণ করে না।");
    }
    private void colors() {
        String[] names={"লাল","সবুজ","নীল","সাদা","কালো"}; int[] colors={Color.RED,Color.GREEN,Color.BLUE,Color.WHITE,Color.BLACK}; final int[] index={0};
        TextView v=new TextView(this); v.setText("স্ক্রিনে dead pixel দেখুন\nট্যাপ করলে পরের রং"); v.setTextColor(Color.BLACK); v.setGravity(Gravity.CENTER); v.setTextSize(22); v.setBackgroundColor(colors[0]);
        addContentView(v,new android.view.ViewGroup.LayoutParams(-1,-1));
        v.setOnClickListener(w->{index[0]++; if(index[0]>=colors.length){((android.view.ViewGroup)v.getParent()).removeView(v);return;} v.setBackgroundColor(colors[index[0]]); v.setTextColor(index[0]==4?Color.WHITE:Color.BLACK); v.setText(names[index[0]]+"\nট্যাপ করে এগিয়ে যান");});
    }
    private void startMic() {
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},10);return;}
        AudioRecord r=null;
        try { int size=Math.max(AudioRecord.getMinBufferSize(8000,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT),2048); r=new AudioRecord(MediaRecorder.AudioSource.MIC,8000,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT,size); if(r.getState()!=AudioRecord.STATE_INITIALIZED)throw new IllegalStateException(); r.startRecording(); short[] x=new short[512]; int n=r.read(x,0,x.length); if(n<0)throw new IllegalStateException(); int peak=0;for(int i=0;i<n;i++)peak=Math.max(peak,Math.abs((int)x[i]));note("মাইক্রোফোন sample peak: "+peak+" (০ হলে শব্দ ধরা পড়েনি)"); }
        catch(Exception e){note("মাইক্রোফোন পড়া যায়নি।");} finally {if(r!=null){try{if(r.getRecordingState()==AudioRecord.RECORDSTATE_RECORDING)r.stop();}catch(Exception ignored){}r.release();}}
    }
    @Override public void onRequestPermissionsResult(int request,String[] permissions,int[] results){super.onRequestPermissionsResult(request,permissions,results);if(request==10){if(results.length>0&&results[0]==PackageManager.PERMISSION_GRANTED)startMic();else note("অনুমতি দেওয়া হয়নি; মাইক্রোফোন পরীক্ষা চালানো যায়নি।");}}
    private void tone(){try{ToneGenerator t=new ToneGenerator(AudioManager.STREAM_MUSIC,70);t.startTone(ToneGenerator.TONE_PROP_BEEP,350);new Handler().postDelayed(t::release,500);note("ছোট পরীক্ষা-টোন চালানো হয়েছে।");}catch(Exception e){note("স্পিকার টোন চালানো যায়নি।");}}
}
