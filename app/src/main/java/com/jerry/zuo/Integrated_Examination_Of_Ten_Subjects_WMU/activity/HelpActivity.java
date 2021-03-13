package com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.activity;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.R;
import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.hepler.ToolHelper;

import java.io.File;

public class HelpActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        //ActionBar工具栏设置
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = (WebView) findViewById(R.id.webview);
        final Cursor cursor = ToolHelper.loadDB(this, "");
        webView.loadUrl("file:///data/data/com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU/help1.jpg");
        WebSettings settings = webView.getSettings();// 设置可任意缩放
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
