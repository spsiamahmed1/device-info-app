package com.siam.deviceinfo;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import com.siam.deviceinfo.helper.BatteryHelper;
import java.util.Calendar;
import java.util.Map;

public class MainActivity extends Activity {
    private LinearLayout root;
    private Typeface font;
    private int dp(float x) { return (int)(x * getResources().getDisplayMetrics().density + .5f); }
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        try { font = Typeface.createFromAsset(getAssets(), "fonts/HindSiliguri.ttf"); } catch (Exception ignored) { font = Typeface.DEFAULT; }
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().setNavigationBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        root = findViewById(R.id.main_content);
        build();
        nav();
    }
    private TextView text(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color); t.setTypeface(font, bold ? Typeface.BOLD : Typeface.NORMAL); return t;
    }
    private GradientDrawable card() { GradientDrawable d = new GradientDrawable(); d.setColor(Color.WHITE); d.setCornerRadius(dp(23)); d.setStroke(dp(1), 0xfff1f1f1); return d; }
    private void space(int h) { View v = new View(this); root.addView(v, new LinearLayout.LayoutParams(1, dp(h))); }
    private String bn(int n) { String a = String.valueOf(n); StringBuilder b = new StringBuilder(); for (char c : a.toCharArray()) b.append((char)('০' + c - '0')); return b.toString(); }
    private void calendarStrip() {
        String[] days = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        Calendar today = Calendar.getInstance(); Calendar start = (Calendar) today.clone(); start.set(Calendar.DAY_OF_MONTH, 1); start.add(Calendar.DAY_OF_MONTH, -((start.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY)));
        LinearLayout labels = new LinearLayout(this), dates = new LinearLayout(this); labels.setGravity(Gravity.CENTER); dates.setGravity(Gravity.CENTER);
        for (int i = 0; i < 7; i++) {
            Calendar d = (Calendar) start.clone(); d.add(Calendar.DAY_OF_MONTH, i);
            TextView w = text(days[i], 12, 0xff8a8a8a, false); w.setGravity(Gravity.CENTER);
            TextView day = text(bn(d.get(Calendar.DAY_OF_MONTH)), 14, 0xff1a1a1a, true); day.setGravity(Gravity.CENTER);
            if (d.get(Calendar.YEAR) == today.get(Calendar.YEAR) && d.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                GradientDrawable active = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffff8a00, 0xffff6b00}); active.setShape(GradientDrawable.OVAL); day.setBackground(active); day.setTextColor(Color.WHITE);
            }
            LinearLayout col = new LinearLayout(this); col.setOrientation(LinearLayout.VERTICAL); col.setGravity(Gravity.CENTER);
            col.addView(w, new LinearLayout.LayoutParams(-1, dp(24))); col.addView(day, new LinearLayout.LayoutParams(dp(34), dp(34)));
            labels.addView(new View(this), new LinearLayout.LayoutParams(0, 1, 1)); dates.addView(col, new LinearLayout.LayoutParams(0, dp(54), 1));
        }
        root.addView(labels, new LinearLayout.LayoutParams(-1, dp(24))); root.addView(dates); space(12);
    }
    private void build() {
        root.removeAllViews();
        LinearLayout top = new LinearLayout(this); top.setGravity(Gravity.CENTER_VERTICAL);
        top.addView(text("আমার ফোন", 24, 0xff1a1a1a, true), new LinearLayout.LayoutParams(0, dp(48), 1));
        Button profile = button("SIAM  ›"); profile.setOnClickListener(v -> open(ProfileActivity.class)); top.addView(profile);
        root.addView(top);
        EditText search = new EditText(this); search.setSingleLine(); search.setHint("ফোনের তথ্য খুঁজুন"); search.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0); search.setBackground(card());
        root.addView(search, new LinearLayout.LayoutParams(-1, dp(48))); space(12);
        calendarStrip();
        LinearLayout summary = new LinearLayout(this); summary.setGravity(Gravity.CENTER_VERTICAL); summary.setPadding(dp(16), dp(16), dp(16), dp(16)); summary.setBackground(card());
        summary.addView(new BatteryRingView(this), new LinearLayout.LayoutParams(dp(108), dp(108)));
        LinearLayout info = new LinearLayout(this); info.setOrientation(LinearLayout.VERTICAL); info.setPadding(dp(18), 0, 0, 0);
        Map<String, String> battery = BatteryHelper.read(this);
        info.addView(text("ব্যাটারি অবস্থা", 18, 0xff1a1a1a, true));
        info.addView(text("তাপমাত্রা: " + battery.get("তাপমাত্রা"), 14, 0xff8a8a8a, false));
        info.addView(text("স্বাস্থ্য: " + battery.get("স্বাস্থ্য"), 14, 0xff8a8a8a, false)); summary.addView(info); root.addView(summary); space(16);
        root.addView(text("আপনার ফোন", 19, 0xff1a1a1a, true)); space(8);
        String[][] items = {{"ডিভাইস তথ্য", "ব্র্যান্ড, RAM, স্টোরেজ, ডিসপ্লে", "DeviceActivity"}, {"ফোন টেস্ট", "স্ক্রিন, সেন্সর, স্পিকার", "TestActivity"}, {"ব্যাটারি ডাক্তার", "চার্জ ও তাপমাত্রা", "BatteryActivity"}, {"ক্লিনার ও স্টোরেজ", "স্টোরেজ বিশ্লেষণ", "CleanerActivity"}, {"নেটওয়ার্ক", "Wi-Fi তথ্য", "NetworkActivity"}, {"নিরাপত্তা ও অনুমতি", "অ্যাপ অনুমতি দেখুন", "PermissionActivity"}};
        for (String[] item : items) {
            Button a = button(item[0] + "   ›\n" + item[1]); a.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT); a.setPadding(dp(16), dp(8), dp(10), dp(8)); a.setTag(item[0] + " " + item[1]);
            Class<?> c = item[2].equals("DeviceActivity") ? DeviceActivity.class : item[2].equals("TestActivity") ? TestActivity.class : item[2].equals("BatteryActivity") ? BatteryActivity.class : item[2].equals("CleanerActivity") ? CleanerActivity.class : item[2].equals("NetworkActivity") ? NetworkActivity.class : PermissionActivity.class;
            a.setOnClickListener(v -> open(c)); root.addView(a, new LinearLayout.LayoutParams(-1, dp(70))); space(8);
        }
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int st, int before, int count) { String q = s.toString().trim().toLowerCase(); for (int i=0;i<root.getChildCount();i++) { View v=root.getChildAt(i); if(v instanceof Button && v.getTag()!=null) v.setVisibility(((String)v.getTag()).toLowerCase().contains(q)?View.VISIBLE:View.GONE); } }
            public void afterTextChanged(Editable e) {}
        });
    }
    private Button button(String s) { Button b = new Button(this); b.setText(s); b.setTextColor(0xff333333); b.setAllCaps(false); b.setBackground(card()); b.setTypeface(font); return b; }
    private void open(Class<?> c) { startActivity(new Intent(this, c)); }
    private void nav() {
        LinearLayout n = findViewById(R.id.bottom_nav); String[] labels = {"হোম", "ডিভাইস", "+", "চার্ট", "প্রোফাইল"};
        for (int i=0;i<labels.length;i++) { final int x=i; Button b = new Button(this); b.setText(labels[i]); b.setTextSize(i==2?22:11); b.setTypeface(font); b.setAllCaps(false);
            if(i==2){GradientDrawable d=new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[]{0xffff8a00,0xffff6b00});d.setCornerRadius(dp(28));b.setBackground(d);b.setTextColor(Color.WHITE);} else { b.setBackgroundColor(Color.TRANSPARENT); b.setTextColor(0xff777777); }
            n.addView(b,new LinearLayout.LayoutParams(0,dp(i==2?54:60),1)); b.setOnClickListener(v->{if(x==1)open(DeviceActivity.class);if(x==2)open(TestActivity.class);if(x==3)open(ReportActivity.class);if(x==4)open(ProfileActivity.class);});
        }
    }
    public static class BatteryRingView extends View {
        private final Paint p = new Paint(3); private int level;
        public BatteryRingView(Context c) { super(c); Map<String,String> m=BatteryHelper.read(c); try{level=Integer.parseInt(m.get("ব্যাটারি").replace("%",""));}catch(Exception ignored){} }
        @Override protected void onDraw(Canvas c) { super.onDraw(c); float w=getWidth(),cx=w/2,cy=getHeight()/2,r=w*.39f; p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(w*.09f);p.setColor(0xffffead5);c.drawCircle(cx,cy,r,p);p.setShader(new LinearGradient(0,0,w,w,0xffff8a00,0xffff6b00,Shader.TileMode.CLAMP));c.drawArc(cx-r,cy-r,cx+r,cy+r,-90,360*level/100f,false,p);p.setShader(null);p.setStyle(Paint.Style.FILL);p.setColor(0xff1a1a1a);p.setTextAlign(Paint.Align.CENTER);p.setTextSize(w*.21f);c.drawText(level+"%",cx,cy+w*.07f,p); }
    }
}
