package com.im.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.im.chat.R;

import com.im.chat.friends.ContactPersonInfoActivity;
import com.im.chat.model.ContactListModel;
import com.im.chat.util.ChatConstants;
import com.im.chat.util.ChatUserProvider;
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
    private List<String> mList = new ArrayList<String>();
    private String[] mStrArray;
    private ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_search_layout);
        mQuitTv.setOnClickListener(this);
        for(ContactListModel contactListModel : ChatUserProvider.getInstance().getAllUsers()){
            mList.add(contactListModel.getName());
        }
        //list转array
        mStrArray = mList.toArray(new String[mList.size()]);
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,mStrArray);
        mListView.setAdapter(mArrayAdapter);
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
                    mArrayAdapter.getFilter().filter(newText);
                    //mListView.setFilterText(newText);
                }else{
                    mArrayAdapter.getFilter().filter(null);
                    //mListView.clearTextFilter();
                }
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                String str = textView.getText().toString();
                for(ContactListModel contactListModel : ChatUserProvider.getInstance().getAllUsers()){
                    if(contactListModel.getName().toString().equals(str)){
                        Intent intent = new Intent(SearchActivity.this, ContactPersonInfoActivity.class);
                        intent.putExtra(ChatConstants.CONTACT_USER, contactListModel);
                        startActivity(intent);
                        return;
                    }
                }
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
