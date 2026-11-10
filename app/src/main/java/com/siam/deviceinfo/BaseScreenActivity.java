package com.siam.deviceinfo;
import android.app.*;import android.os.*;import android.graphics.Color;import android.graphics.drawable.GradientDrawable;import android.view.*;import android.widget.*;
public abstract class BaseScreenActivity extends Activity {
 protected LinearLayout content; protected int dp(float x){return (int)(x*getResources().getDisplayMetrics().density+.5f);} protected abstract int layout(); protected abstract String title();
 @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(layout());getWindow().setStatusBarColor(Color.WHITE);getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);content=findViewById(R.id.content);TextView back=txt("‹    "+title(),22,0xff1a1a1a,true);back.setPadding(0,dp(4),0,dp(16));back.setOnClickListener(v->finish());content.addView(back);}
 protected TextView txt(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(null,1);return t;}
 protected void heading(String s){TextView t=txt(s,18,0xff1a1a1a,true);t.setPadding(0,dp(15),0,dp(6));content.addView(t);}
 protected void line(String k,String v){LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER_VERTICAL);row.setPadding(dp(12),dp(13),dp(12),dp(13));TextView a=txt(k,14,0xff777777,false),b=txt(v==null?"অনুপলব্ধ":v,14,0xff222222,true);row.addView(a,new LinearLayout.LayoutParams(0,-2,1));row.addView(b);content.addView(row);View d=new View(this);d.setBackgroundColor(0xfff1f1f1);content.addView(d,new LinearLayout.LayoutParams(-1,dp(1)));}
 protected Button action(String s,Runnable r){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextColor(0xff333333);GradientDrawable d=new GradientDrawable();d.setColor(Color.WHITE);d.setCornerRadius(dp(18));d.setStroke(dp(1),0xfff1f1f1);b.setBackground(d);b.setOnClickListener(v->{if(r!=null)r.run();});content.addView(b,new LinearLayout.LayoutParams(-1,dp(54)));return b;}
 protected void note(String s){TextView t=txt(s,13,0xff777777,false);t.setPadding(dp(4),dp(8),dp(4),dp(12));content.addView(t);}
}
