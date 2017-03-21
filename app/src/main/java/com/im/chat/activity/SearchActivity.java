package com.im.chat.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.im.chat.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * 搜索页
 * Created by xiaochj on 2017/3/9.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.contact_searchView)
    SearchView mSearchView;
    @Bind(R.id.quit_layout)
    TextView mQuitTv;
    @Bind(R.id.contact_listView)
    ListView mListView;
    private List<String> mList = new ArrayList<String>(Arrays.asList("aaa", "bbb", "ccc", "airsaid"));
    private String[] mStrArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_search_layout);
        mQuitTv.setOnClickListener(this);
        //list转array
        mStrArray = mList.toArray(new String[mList.size()]);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrArray));
        mListView.setTextFilterEnabled(true);
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.quit_layout:
                finish();
                break;
        }
    }
}
