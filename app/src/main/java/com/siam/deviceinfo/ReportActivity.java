package com.siam.deviceinfo;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import com.siam.deviceinfo.helper.BatteryHelper;
import com.siam.deviceinfo.helper.DeviceInfoHelper;
import java.io.OutputStream;
import java.util.Map;

public class ReportActivity extends BaseScreenActivity {
    private static final int CREATE_PDF = 140;
    protected int layout() { return R.layout.activity_report; }
    protected String title() { return "রিপোর্ট"; }
    public void onCreate(Bundle b) {
        super.onCreate(b);
        heading("ফোন রিপোর্ট");
        note("রিপোর্ট ডিভাইসেই তৈরি হয়। PDF সংরক্ষণ করলে Android-এর file picker ব্যবহার হবে।");
        action("PDF তৈরি ও সংরক্ষণ করুন", this::choosePdfLocation);
        action("রিপোর্ট Copy করুন", () -> {
            android.content.ClipboardManager c = (android.content.ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            c.setPrimaryClip(ClipData.newPlainText("Device report", summary())); note("রিপোর্ট কপি হয়েছে।");
        });
        action("টেক্সট Share করুন", () -> {
            Intent i = new Intent(Intent.ACTION_SEND); i.setType("text/plain"); i.putExtra(Intent.EXTRA_TEXT, summary());
            startActivity(Intent.createChooser(i, "রিপোর্ট শেয়ার করুন"));
        });
    }
    private String summary() {
        StringBuilder s = new StringBuilder("ফোনের ডাক্তার — ডিভাইস রিপোর্ট\n");
        for (Map.Entry<String,String> e : DeviceInfoHelper.getInfo(this).entrySet()) s.append(e.getKey()).append(": ").append(e.getValue()).append('\n');
        for (Map.Entry<String,String> e : BatteryHelper.read(this).entrySet()) s.append(e.getKey()).append(": ").append(e.getValue()).append('\n');
        return s.toString();
    }
    private void choosePdfLocation() {
        Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT); i.addCategory(Intent.CATEGORY_OPENABLE); i.setType("application/pdf"); i.putExtra(Intent.EXTRA_TITLE, "device-report.pdf"); startActivityForResult(i, CREATE_PDF);
    }
    @Override protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (request != CREATE_PDF || result != RESULT_OK || data == null || data.getData() == null) return;
        Uri uri = data.getData();
        PdfDocument pdf = new PdfDocument();
        PdfDocument.Page page = pdf.startPage(new PdfDocument.PageInfo.Builder(595,842,1).create());
        Paint paint = new Paint(); paint.setTextSize(15); int y=50;
        for (String row : summary().split("\n")) { page.getCanvas().drawText(row,40,y,paint); y+=25; if(y>800) break; }
        pdf.finishPage(page);
        try (OutputStream out = getContentResolver().openOutputStream(uri)) {
            if (out == null) throw new java.io.IOException("ফাইল লেখা যায়নি");
            pdf.writeTo(out); note("PDF সংরক্ষণ হয়েছে।");
        } catch (Exception e) { note("PDF সংরক্ষণ করা যায়নি: " + e.getMessage()); }
        finally { pdf.close(); }
    }
}
