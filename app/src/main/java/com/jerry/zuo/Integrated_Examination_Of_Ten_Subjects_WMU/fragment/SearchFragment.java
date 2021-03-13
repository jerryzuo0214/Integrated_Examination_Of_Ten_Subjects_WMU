package com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.R;
import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.activity.HelpActivity;
import com.jerry.zuo.Integrated_Examination_Of_Ten_Subjects_WMU.activity.SearchActivity;

/**
 * Created by Administrator on 2021/1/27.
 */

public class SearchFragment extends Fragment {
    private View rootView;
    private TextView sv;
    private ImageView help,help2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_search,container,false);
        this.rootView=rootView;
        help=rootView.findViewById(R.id.helppage);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://jerryzuo0214.github.io/Integrated_Examination_Of_Ten_Subjects_WMU/");    //设置跳转的网站
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        help2=rootView.findViewById(R.id.helppage2);
        help2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);
            }
        });
        sv=rootView.findViewById(R.id.tv_sv);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
