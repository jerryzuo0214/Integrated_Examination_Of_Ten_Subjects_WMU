package com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.activity.ExamActivity;
import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.activity.HelpActivity;
import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.activity.SettingActivity;
import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.fragment.QuestionFragment;
import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.fragment.SearchFragment;
import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.hepler.ToolHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private TextView tv1, tv2, tv3, tv4;
    private Fragment fragment;
    private final int QUE=1,COLLECT=2,WRONG=3;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("HelpActivity", MODE_PRIVATE);
        // 判断是不是首次登录
        if (preferences.getBoolean("firstStart", true)) {
            editor = preferences.edit();
            // 将登录标志位设置为false，下次登录时不在显示引导页
            editor.putBoolean("firstStart", false);
            editor.commit();
            //跳转到引导页
            Intent intent = new Intent();
            intent.setClass(this, HelpActivity.class);
            startActivity(intent);

        }
        //如果不是首次登录 启动mainactivity加载项

        initView();
        tv1 = findViewById(R.id.tv_search);
        tv1.setOnClickListener(this);
        tv2 = findViewById(R.id.tv_quest);
        tv2.setOnClickListener(this);
        tv3 = findViewById(R.id.tv_collect);
        tv3.setOnClickListener(this);
        tv4 = findViewById(R.id.tv_wrong);
        tv4.setOnClickListener(this);
        setDefaultFragment();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_help://帮助界面
                Intent intent2=new Intent(this, HelpActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_exam://考试记录
                Intent intent=new Intent(this, ExamActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings://设置
                Intent intent1=new Intent(this, SettingActivity.class);
                startActivity(intent1);
                break;
            case R.id.clear_all://清除已做
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("清空练习记录（全清）?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClearAll();
                    }
                });
                builder.show();
                break;
            case R.id.clear_collect://清空收藏夹
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("清空收藏夹?");
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         ClearCollect();
                    }
                });
                builder1.show();
                break;
            case R.id.clear_wrong://清空错题本
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("清空错题本?");
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClearWrong();
                    }
                });
                builder2.show();
                break;
            case R.id.clear_exam://清空考试记录
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setTitle("清空测试记录?");
                builder3.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         ClearExam();
                    }
                });
                builder3.show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void ClearAll(){
        ToolHelper.excuteDB(this,"update dist set dist_d='' ");
    }
    private void ClearCollect(){
        ToolHelper.excuteDB(this,"DELETE FROM collection ");
    }
    private void ClearWrong(){
        ToolHelper.excuteDB(this,"DELETE FROM wrong ");
    }
    private void ClearExam(){
        ToolHelper.excuteDB(this,"DELETE FROM exam ");
    }


//设置默认fragment
    private void setDefaultFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SearchFragment searchFragment = new SearchFragment();
        transaction.replace(R.id.content, searchFragment);
        transaction.commit();
        tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv2.setTextColor(getResources().getColor(R.color.gray));
        tv3.setTextColor(getResources().getColor(R.color.gray));
        tv4.setTextColor(getResources().getColor(R.color.gray));
        setTitle("查询");
    }
//点击下栏切换界面
    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        fragment = getFragmentManager().findFragmentById(R.id.content);
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.tv_search://查询
                SearchFragment searchFragment = new SearchFragment();
                transaction.hide(fragment).replace(R.id.content, searchFragment);
                tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv2.setTextColor(getResources().getColor(R.color.gray));
                tv3.setTextColor(getResources().getColor(R.color.gray));
                tv4.setTextColor(getResources().getColor(R.color.gray));
                setTitle("查询");
                break;
            case R.id.tv_quest://题库
                QuestionFragment questionFragment = new QuestionFragment(QUE);
                transaction.hide(fragment).replace(R.id.content, questionFragment);
                tv1.setTextColor(getResources().getColor(R.color.gray));
                tv2.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv3.setTextColor(getResources().getColor(R.color.gray));
                tv4.setTextColor(getResources().getColor(R.color.gray));
                setTitle("题库");
                break;
            case R.id.tv_collect://收藏
                QuestionFragment questionFragment1 = new QuestionFragment(COLLECT);
                transaction.hide(fragment).replace(R.id.content, questionFragment1);
                tv1.setTextColor(getResources().getColor(R.color.gray));
                tv2.setTextColor(getResources().getColor(R.color.gray));
                tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv4.setTextColor(getResources().getColor(R.color.gray));
                setTitle("收藏");
                break;
            case R.id.tv_wrong://错题
                QuestionFragment questionFragment2 = new QuestionFragment(WRONG);
                transaction.hide(fragment).replace(R.id.content, questionFragment2);
                tv1.setTextColor(getResources().getColor(R.color.gray));
                tv2.setTextColor(getResources().getColor(R.color.gray));
                tv3.setTextColor(getResources().getColor(R.color.gray));
                tv4.setTextColor(getResources().getColor(R.color.colorPrimary));
                setTitle("错题本");
                break;
        }
        transaction.commit();
    }
}
