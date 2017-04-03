package com.sunofbeaches.recyclerviewtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sunofbeaches.recyclerviewtest.adapters.GridViewAdapter;
import com.sunofbeaches.recyclerviewtest.adapters.ListViewAdapter;
import com.sunofbeaches.recyclerviewtest.adapters.RecyclerViewBaseAdapter;
import com.sunofbeaches.recyclerviewtest.adapters.StaggerAdapter;
import com.sunofbeaches.recyclerviewtest.beans.Datas;
import com.sunofbeaches.recyclerviewtest.beans.ItemBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 总结:
 * 1、首先我们要有控件,这个RecyclerView它呢是在V7包下的,所以我们要打开这个 Module settings里的依赖,
 * 添加RecyclerView的依赖,这样子才能在布局文件里使用RecyclerView这个控件。
 * 2、通过findViewById找到控件
 * 3、准备好数据
 * 4、设置布局管理器
 * 5、就是创建适配器
 * 6、设置适配器
 * 7、数据就可以现实出来了
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView mList;
    private List<ItemBean> mData;
    private RecyclerViewBaseAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //找到控件
        mList = (RecyclerView) this.findViewById(R.id.recycler_view);
        mRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.refresh_layout);
        //准备数据
        /**
         * 准备数据,一般来说,我们在现实开发中,我们的数据是从网络中获取的,这里面只是演示。
         * 所以我们只是模拟数据,在现实开发中也是要模拟数据的,比如说,后台没有准备好的时候。
         */
        initData();
        //设置默认的显示样式为ListView的效果
        showList(true, false);

        //处理下啦刷新
        handlerDownPullUpdate();
    }

    private void handlerDownPullUpdate() {

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        mRefreshLayout.setEnabled(true);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //在这里面去执行刷新数据的操作
                /**
                 * 当我们在顶部,下拉的时候 ,这个方法就会被出发
                 * 但是,这个方法是MainThread是主线程,不可以执行耗时操作。
                 * 一般来说,我们去请求数据在开一个线程去获取
                 * //这里面演示的话,我直接添加一条数据
                 */
                //添加数据
                ItemBean data = new ItemBean();
                data.title = "我是新添加的数据...";
                data.icon = R.mipmap.pic_09;
                mData.add(0, data);
                //更新UI
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //这里要做两件事,一件是让刷新停止,另外一件则是要更新列表
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

    }

    private void initListener() {

        mAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //这里处理条目的点击事件,该干嘛就干嘛,跳转的就跳转...
                Toast.makeText(MainActivity.this, "您点击的是第" + position + "个条目", Toast.LENGTH_SHORT).show();
            }
        });

        //这里面去处理上拉加载更多
        if (mAdapter instanceof ListViewAdapter) {
            ((ListViewAdapter) mAdapter).setOnRefreshListener(new ListViewAdapter.OnRefreshListener() {
                @Override
                public void onUpPullRefresh(final ListViewAdapter.LoaderMoreHolder loaderMoreHolder) {
                    //这里面去加载更多的数据,同样,需要在子线程中完成,这里仅作演示

                    //更新UI
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Random random = new Random();

                            if (random.nextInt() % 2 == 0) {
                                ItemBean data = new ItemBean();
                                data.title = "我是新添加的数据...";
                                data.icon = R.mipmap.pic_09;
                                mData.add(data);

                                //这里要做两件事,一件是让刷新停止,另外一件则是要更新列表
                                mAdapter.notifyDataSetChanged();
                                loaderMoreHolder.update(loaderMoreHolder.LOADER_STATE_NORMAL);
                            } else {
                                loaderMoreHolder.update(loaderMoreHolder.LOADER_STATE_RELOAD);
                            }

                        }
                    }, 3000);
                }
            });
        }
    }

    /**
     * 这个方法用于模拟数据
     */
    private void initData() {
        //List<DataBea>---->Adapter------>setAdapter------>显示数据。
        //创建数据集合
        mData = new ArrayList<>();

        //创建模拟数据
        for (int i = 0; i < Datas.icons.length; i++) {
            //创建数据对象
            ItemBean data = new ItemBean();
            data.icon = Datas.icons[i];
            data.title = "我是第 " + i + " 个条目";
            //添加到集合里头
            mData.add(data);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {

            //ListView的部分
            case R.id.list_view_vertical_stander:
                Log.d(TAG, "点击了ListView里头的垂直标准");
                showList(true, false);
                break;
            case R.id.list_view_vertical_reverse:
                Log.d(TAG, "点击了ListView里头的垂直反向");
                showList(true, true);
                break;
            case R.id.list_view_horizontal_stander:
                Log.d(TAG, "点击了ListView里头的水平标准");
                showList(false, false);
                break;
            case R.id.list_view_horizontal_reverse:
                Log.d(TAG, "点击了ListView里头的水平反向");
                showList(false, true);
                break;

            //GridView部分
            case R.id.grid_view_vertical_stander:
                showGrid(true, false);
                break;
            case R.id.grid_view_vertical_reverse:
                showGrid(true, true);
                break;
            case R.id.grid_view_horizontal_stander:
                showGrid(false, false);
                break;
            case R.id.grid_view_horizontal_reverse:
                showGrid(false, true);
                break;

            //瀑布流部分
            case R.id.stagger_view_vertical_stander:
                showStagger(true, false);
                break;
            case R.id.stagger_view_vertical_reverse:
                showStagger(true, true);
                break;
            case R.id.stagger_view_horizontal_stander:
                showStagger(false, false);
                break;
            case R.id.stagger_view_horizontal_reverse:
                showStagger(false, true);
                break;

            // 多种条目类型被点击了
            case R.id.more_type:

                //跳到一个新的Activity里面去实现这个功能
                Intent intent = new Intent(this, MoreTypeActivity.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 这个方法用于实现瀑布流的效果
     */
    private void showStagger(boolean isVertical, boolean isReverse) {
        //准备布局管理器
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, isVertical ? StaggeredGridLayoutManager.VERTICAL : StaggeredGridLayoutManager.HORIZONTAL);
        //设置布局管理器的方向
        layoutManager.setReverseLayout(isReverse);

        //设置布局管理器到RecyclerView里
        mList.setLayoutManager(layoutManager);

        //创建适配器
        mAdapter = new StaggerAdapter(mData);

        //设置适配器
        mList.setAdapter(mAdapter);

        //初始化事件
        initListener();

    }

    /**
     * 这个方法用于实现和GridView一样的效果
     */
    private void showGrid(boolean isVertical, boolean isReverse) {

        //创建布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        //设置水平还是垂直
        layoutManager.setOrientation(isVertical ? GridLayoutManager.VERTICAL : GridLayoutManager.HORIZONTAL);
        //设置标准(正向)还是反向的
        layoutManager.setReverseLayout(isReverse);

        //设置布局管理器
        mList.setLayoutManager(layoutManager);

        //创建适配器
        mAdapter = new GridViewAdapter(mData);

        //设置适配器
        mList.setAdapter(mAdapter);

        //初始化事件
        initListener();
    }

    /**
     * 这个方法用于现实ListView一样的效果
     */
    private void showList(boolean isVertical, boolean isReverse) {
        //RecyclerView需要设置样式,其实就是设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器来控制
        //设置水平还是垂直
        layoutManager.setOrientation(isVertical ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL);
        //设置标准(正向)还是反向的
        layoutManager.setReverseLayout(isReverse);

        mList.setLayoutManager(layoutManager);
        //创建适配器
        mAdapter = new ListViewAdapter(mData);
        //设置到RecyclerView里头
        mList.setAdapter(mAdapter);

        //初始化事件
        initListener();
    }
}
