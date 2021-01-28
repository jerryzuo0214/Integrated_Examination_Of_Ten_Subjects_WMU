package com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.R;
import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.fragment.QuestionFragment;
import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.hepler.MyTag;
import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.hepler.ToolHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PracticeActivity extends AppCompatActivity implements View.OnClickListener {
    private int tab,progress_total=0;
    private String table,content,from, value_dist, field_dist;
    private TextView tvTitle;
    private Cursor cursor, progress_dist;
    private boolean isCollect=false;
    private int num,progress_num;
    private int index=0,index1;
    public static List<String> anList;
    private String source;
    private String qid, type, que, A, B, C, D,E, answer, detail;
    private ImageView imgPre, imgNext;
    private AdapterViewFlipper vf;
    private BaseAdapter adapter;
    private ProgressBar pb;
    private View root;
    private TextView tvQue, tvDetail, tvAns, tvYou;
    private CheckBox cb1, cb2, cb3, cb4,cb5;
    private ImageView imgCollect,imgCard, imgTrash;
    private SharedPreferences sp;
    private Button bt2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        //ActionBar工具栏设置
        Toolbar toolbar = findViewById(R.id.toolbar_que);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp=getPreferences(Context.MODE_PRIVATE);
        Intent intent=getIntent();
        tab=intent.getIntExtra("tab",1);
        initTable();
        initView();
    }

    public void queChecked(View view) {
        isAnswerTrue(index);
    }
    private void initTable() {
        switch (tab) {
            case MyTag.QUE://题库
                table = "que";
                content="题库";
                from="que";
                break;
            case MyTag.COLLECT://收藏
                table = "collection ,que where collection.qid=que._id ";
                content="收藏";
                from="collect";
                break;
            case MyTag.WRONG://错题
                table = "wrong,que where wrong.qid=que._id ";
                content="错题";
                from="wrong";
                break;
        }
        getProgress(from);
    }

    private void getProgress(String t) {
        if(t=="que"){
        index1=sp.getInt(t+"_"+QuestionFragment.value+"_"+"index",0);}
        else{
            index1=sp.getInt(t+"index",0);}
    }

    private void saveProgess(String t) {
        SharedPreferences.Editor editor=sp.edit();
        if(t=="que"){
            editor.putInt(t+"_"+QuestionFragment.value+"_"+"index",index);}
        else{
            editor.putInt(t+"index",index);}
        editor.commit();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        //初始化清除历史记录按钮
        imgTrash =findViewById(R.id.img_trash);
        imgTrash.setOnClickListener(this);

        //初始化收藏按钮
        imgCollect =findViewById(R.id.img_collect);
        imgCollect.setOnClickListener(this);
        //初始化答题卡按钮
        imgCard=findViewById(R.id.img_card);
        imgCard.setOnClickListener(this);

        findViewById(R.id.mytime).setVisibility(View.GONE);
        findViewById(R.id.tv_num).setVisibility(View.GONE);
        if(content=="收藏"){
            findViewById(R.id.img_trash).setVisibility(View.GONE);
        }
        if(content=="错题"){
            findViewById(R.id.img_trash).setVisibility(View.GONE);
        }

        //获取题目集关键字
        String field = QuestionFragment.field;
        String value = QuestionFragment.value;
        source = value;
        value_dist=value;
        field_dist=field;
        //设置标题
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(source);
        //获取SQLite数据库中题库数据
        if(tab==MyTag.QUE)
            cursor = ToolHelper.loadDB(this,
                    "select que.* from "+table+" where " + field + "='" + value + "' order by _id");
        else
            cursor = ToolHelper.loadDB(this,
                    "select que.* from "+table+" and " + field + "='" + value + "' order by _id");
        num = cursor.getCount();
        //答案List初始化,if数据库中dist表为空，则创建新的anlist，最后结束时把anlist赋值到dist，若dist列不为空则把dist加载到anlist中,最后把dist表更新为最新的list
        progress_dist = ToolHelper.loadDB(this,
                    "select * from dist where " + field + "='" + value + "' order by _id");
        progress_num = progress_dist.getCount();
        if (progress_num==0){
                                    anList = new ArrayList<>();
                                    for (int i = 0; i < num; i++) { anList.add("");}
                                }
        else{
                anList = new ArrayList<>();
                for(progress_dist.moveToFirst();!progress_dist.isAfterLast();progress_dist.moveToNext())
                {
                    String progress_item = progress_dist.getString(progress_dist.getColumnIndex("dist_d"));
                    if(progress_item==null){anList.add("");}
                    else if(progress_item.contains("A")){anList.add("A");}
                    else if(progress_item.contains("B")){anList.add("B");}
                    else if(progress_item.contains("C")){anList.add("C");}
                    else if(progress_item.contains("D")){anList.add("D");}
                    else if(progress_item.contains("E")){anList.add("E");}
                    else {anList.add("");}
                    progress_total++;
                }
                if(progress_total!=num) {
                    for(int j = progress_total; j<num ;j++) {
                        anList.add(""); }
                    }
            }

        //设置进度条
        pb = findViewById(R.id.pb);
        pb.setMax(num-1);
        pb.setProgress(0);
        //前后按钮
        imgPre = findViewById(R.id.img_pre);
        imgNext = findViewById(R.id.img_next);
        imgPre.setOnClickListener(this);
        imgNext.setOnClickListener(this);

        //设置ViewFlipper
        vf=findViewById(R.id.vf);
        adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return num;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                index=position;
                createView(position);
                return root;
            }
        };
        vf.setAdapter(adapter);
        moveToItem(index1);

    }
    //答题卡设置
    private void createView(final int pos) {
        root = LayoutInflater.from(PracticeActivity.this).inflate(R.layout.queitem, null);
        tvQue = root.findViewById(R.id.tv_que1);
        cb1 = root.findViewById(R.id.cb_choice1);
        cb2 = root.findViewById(R.id.cb_choice2);
        cb3 = root.findViewById(R.id.cb_choice3);
        cb4 = root.findViewById(R.id.cb_choice4);
        cb5 = root.findViewById(R.id.cb_choice5);
        tvAns = root.findViewById(R.id.tv_answer1);
        tvDetail = root.findViewById(R.id.tv_detail1);
        tvYou= root.findViewById(R.id.tv_you);
       //提交
        bt2= root.findViewById(R.id.bt_record4);
        final int pos_d = pos + 1;
        final int[] wmi = {0};
        bt2.setText("提交");
        if(anList.get(index).contains("A")||anList.get(index).contains("B")||anList.get(index).contains("C")||anList.get(index).contains("D")||anList.get(index).contains("E")){
            wmi[0] =0;
            if (pos_d == num) {bt2.setText("最后一题");}
            else {
                bt2.setText("下一题");
                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vf.showNext();
                    }
                });
                }
             }//之前做过的就不再重复做了
        else {
            bt2.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    isAnswerTrue(index);
                    if (cb1.isChecked() || cb2.isChecked() || cb3.isChecked() || cb4.isChecked() || cb5.isChecked()) {
                        wmi[0] = 1;
                    }//选择了选项才能下一题
                    if (wmi[0] == 1) {
                        bt2.setText("下一题");
                        bt2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                vf.showNext();
                            }
                        });
                    }
                    if (index == num-1) {
                        bt2.setText("最后一题");
                        bt2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isAnswerTrue(index);
                            }
                        });
                    }
                }
            });
        }
        //获取数据
        cursor.moveToPosition(pos);
        type = cursor.getString(cursor.getColumnIndex("type"));
        que = cursor.getString(cursor.getColumnIndex("que"));
        A = "A."+cursor.getString(cursor.getColumnIndex("choiceA"));
        B =  "B."+cursor.getString(cursor.getColumnIndex("choiceB"));
        C =  "C."+cursor.getString(cursor.getColumnIndex("choiceC"));
        D =  "D."+cursor.getString(cursor.getColumnIndex("choiceD"));
        E =  "E."+cursor.getString(cursor.getColumnIndex("choiceE"));
        answer = cursor.getString(cursor.getColumnIndex("answer"));
        detail = cursor.getString(cursor.getColumnIndex("detail"));
        qid = cursor.getString(cursor.getColumnIndex("_id"));
        //加载内容
        tvQue.setText((pos + 1) + ".(" + type + ")" + que);
        cb1.setText(A);
        cb2.setText(B);
        cb3.setText(C);
        cb4.setText(D);
        cb5.setText(E);
        cb1.setButtonDrawable(R.drawable.cb);
        cb2.setButtonDrawable(R.drawable.cb);
        cb3.setButtonDrawable(R.drawable.cb);
        cb4.setButtonDrawable(R.drawable.cb);
        cb5.setButtonDrawable(R.drawable.cb);
        cb1.setEnabled(true);
        cb2.setEnabled(true);
        cb3.setEnabled(true);
        cb4.setEnabled(true);
        cb5.setEnabled(true);
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        cb4.setChecked(false);
        cb5.setChecked(false);
        //初始化复选框
        cb1.setOnClickListener(this);
        cb2.setOnClickListener(this);
        cb3.setOnClickListener(this);
        cb4.setOnClickListener(this);
        cb5.setOnClickListener(this);
        tvAns.setText("【正确答案】" + answer);
        tvDetail.setText("【解析】" + detail);
        if (anList.get(pos).equals("")) {
            tvAns.setVisibility(View.GONE);
            tvDetail.setVisibility(View.GONE);
        } else {
            //已答题设置为不可操作
            disableChecked(pos);
        }

        //设置当前进度
        pb.setProgress(pos);
        //设置是否被收藏
        if(queCollect()){
            isCollect=true;
            imgCollect.setImageResource(R.drawable.star_on);
        }else {
            isCollect=false;
            imgCollect.setImageResource(R.drawable.star1);
        }
        //滑动切换
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float startX = v.getWidth()/2, endX = v.getWidth()/2, min = 100;
                   switch (event.getAction()) {
                       case MotionEvent.ACTION_DOWN:
                           startX = event.getX();
                       case MotionEvent.ACTION_UP:
                           endX = event.getX();
                           break;
                   }
                    if (startX - endX > min) {
                        vf.showPrevious();
                    } else if (endX - startX > min) {
                        vf.showNext();
                    }
                return true;
            }
        });
    }

    //判断选择答案对错
    private void isAnswerTrue(int pos) {
        if (cb1.isChecked() || cb2.isChecked() || cb3.isChecked() || cb4.isChecked() || cb5.isChecked()) {
            //获取答案
            StringBuffer sb = new StringBuffer();
            if (cb1.isChecked()) sb.append("A");
            if (cb2.isChecked()) sb.append("B");
            if (cb3.isChecked()) sb.append("C");
            if (cb4.isChecked()) sb.append("D");
            if (cb5.isChecked()) sb.append("E");

            String you = sb.toString();
            //保存答案
            anList.set(pos, you);
            ToolHelper.excuteDB(this,"update dist set dist_d='" + you + "' where _id ='" + qid + "' ");
            //判断对错
            if (you.equals(answer)) {
                moveCorrect();
                disableChecked(pos);
            } else {
                //错误则保存错题，显示答案
                saveWrong(sb.toString());
                disableChecked(pos);
            }
        }else {
            Toast.makeText(PracticeActivity.this, "请选择答案", Toast.LENGTH_SHORT).show();
        }
    }
    //移除正确题目
    @SuppressLint("SetTextI18n")
    private void moveCorrect() {
        //vf.showNext();
        int c=ToolHelper.loadDB(this,"select _id from wrong where qid="+qid).getCount();
        if(c>0)
            ToolHelper.excuteDB(this, "delete from wrong where qid=" +qid);
    }

    //已做题不可再做
    private void disableChecked(int pos) {
        tvAns.setVisibility(View.VISIBLE);
        tvDetail.setVisibility(View.VISIBLE);
        tvYou.setText("【你的答案】" + anList.get(pos));

        if (answer.contains("A")) cb1.setButtonDrawable(R.drawable.cb_right);
        if (answer.contains("B")) cb2.setButtonDrawable(R.drawable.cb_right);
        if (answer.contains("C")) cb3.setButtonDrawable(R.drawable.cb_right);
        if (answer.contains("D")) cb4.setButtonDrawable(R.drawable.cb_right);
        if (answer.contains("E")) cb5.setButtonDrawable(R.drawable.cb_right);

    }

    //保存错题
    private void saveWrong(String ans) {
        int c=ToolHelper.loadDB(this,"select _id from wrong where qid="+qid).getCount();
        if(c==0) {
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String mydate = ft.format(date);
            ToolHelper.excuteDB(this,
                    "insert into wrong (_id,qid,answer,anTime) values (" + String.valueOf(Math.random() * 10000) + "," + qid + ",'" + ans + "','" + mydate + "')");
        }
    }

    //判断当前题目是否被收藏
    private boolean queCollect() {
        int c=ToolHelper.loadDB(this,"select _id from collection where qid="+qid).getCount();
        if(c>0) return true;
        else return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.que, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.que_ok://提交答案
                    isAnswerTrue(index);
                break;

            case android.R.id.home://返回
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("是否取消练习？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveProgess(from);
                        PracticeActivity.this.finish();
                    }
                });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_next:
                vf.showNext();
                break;
            case R.id.img_pre:
                vf.showPrevious();
                break;
            case R.id.img_collect://收藏
                if(!isCollect){
                    imgCollect.setImageResource(R.drawable.star_on);
                    ToolHelper.excuteDB(this,"insert into collection (_id,qid) values ("+String.valueOf(Math.random()*10000)+","+qid+")");
                    Toast.makeText(this,"成功收藏",Toast.LENGTH_SHORT).show();
                    isCollect=true;
                }else {
                    imgCollect.setImageResource(R.drawable.star1);
                    ToolHelper.excuteDB(this,"delete from collection where qid="+qid);
                    Toast.makeText(this,"取消收藏",Toast.LENGTH_SHORT).show();
                    isCollect=false;
                }
                break;
            case R.id.img_card:
                Intent intent=new Intent(this,CardActivity.class);
                intent.putExtra("num",num);
                intent.putExtra("from",2);
                startActivityForResult(intent,MyTag.CARD);
                break;
            case R.id.img_trash: //清除历史记录
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("清空历史记录并退出练习?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveToTrash();
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putInt(from+"index",0);
                        editor.commit();
                        PracticeActivity.this.finish();
                    }
                });
                builder.show();
                break;
            case R.id.cb_choice1:
                if(cb1.isChecked()&&type.contains("单") ){
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    cb4.setChecked(false);
                    cb5.setChecked(false);
                }
                break;
            case R.id.cb_choice2:
                if(cb2.isChecked() &&type.contains("单")){
                    cb1.setChecked(false);
                    cb3.setChecked(false);
                    cb4.setChecked(false);
                    cb5.setChecked(false);
                }
                break;
            case R.id.cb_choice3:
                if(cb3.isChecked()&&type.contains("单") ){
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    cb4.setChecked(false);
                    cb5.setChecked(false);
                }
                break;
            case R.id.cb_choice4:
                if(cb4.isChecked() &&type.contains("单")){
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    cb5.setChecked(false);
                }
                break;
            case R.id.cb_choice5:
                if(cb5.isChecked()&&type.contains("单") ){
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    cb4.setChecked(false);
                }
                break;
        }
    }

    //清空历史记录
    private void moveToTrash(){
        ToolHelper.excuteDB(this,"update dist set dist_d='' where " + field_dist + "='" + value_dist + "' ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==MyTag.CARD&&resultCode==MyTag.CARD){
            int select=data.getIntExtra("card",0);
            moveToItem(select);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void moveToItem(int t) {
        if (t != index) {
            if(t>index) {
                int d= t-index;
                for (int i = 0; i < d; i++)
                    vf.showNext();
            }else if(t<index){
                int p=index-t;
                for (int i = 0; i < p; i++)
                    vf.showPrevious();
            }
        }
    }
}

