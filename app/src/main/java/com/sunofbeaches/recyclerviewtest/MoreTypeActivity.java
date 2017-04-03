package com.sunofbeaches.recyclerviewtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sunofbeaches.recyclerviewtest.adapters.MoreTypeAdapter;
import com.sunofbeaches.recyclerviewtest.beans.Datas;
import com.sunofbeaches.recyclerviewtest.beans.MoreTypeBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by TrillGates on 17/4/4.
 * God bless my code!
 */
public class MoreTypeActivity extends AppCompatActivity {

    private static final String TAG = "MoreTypeActivity";
    private RecyclerView mRecyclerView;
    private List<MoreTypeBean> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_type_activity);

        //find the view here
        mRecyclerView = (RecyclerView) this.findViewById(R.id.more_type_list);

        //准备数据

        mData = new ArrayList<>();

        initDatas();

        //创建和设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //创建适配器
        MoreTypeAdapter adapter = new MoreTypeAdapter(mData);

        //设置适配器
        mRecyclerView.setAdapter(adapter);


    }

    private void initDatas() {
        Random random = new Random();

        for (int i = 0; i < Datas.icons.length; i++) {
            MoreTypeBean data = new MoreTypeBean();
            data.pic = Datas.icons[i];
            data.type = random.nextInt(3);
            Log.d(TAG, "type == " + data.type);
            mData.add(data);
        }
    }
}
