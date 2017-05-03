package com.im.chat.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.im.chat.R;
import com.im.chat.adapter.HeaderListAdapter;
import com.im.chat.engine.AppEngine;
import com.im.chat.model.BaseBean;
import com.im.chat.model.NotifyListModel;
import com.im.chat.service.RequestLogout;
import com.im.chat.util.Utils;
import com.im.chat.view.RefreshableRecyclerView;
import com.im.chat.viewholder.NotifyItemHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 通告页
 * Created by xiaochj on 14-9-17.
 */
public class NotificationFragment extends BaseFragment{

  @Bind(R.id.notify_pullrefresh)
  protected SwipeRefreshLayout refreshLayout;
  @Bind(R.id.notify_view)
  protected RecyclerView recyclerView;
  protected LinearLayoutManager layoutManager;
  HeaderListAdapter<NotifyListModel> notificationAdapter;
  int totalItem = RefreshableRecyclerView.DEFAULT_PAGE_SIZE;//总共多少个item

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(com.im.chat.R.layout.notify_fragment, container, false);
    ButterKnife.bind(this, view);
    layoutManager = new LinearLayoutManager(getActivity());
    notificationAdapter = new HeaderListAdapter<>(NotifyItemHolder.class);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(notificationAdapter);
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        getNotify();
      }
    });
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    headerLayout.showTitle(com.im.chat.R.string.notification_title);
    getNotify();
  }

  private void getNotify(){
    refreshLayout.setRefreshing(false);
    //调用retrofit自己的服务器接口
    AppEngine.getInstance().getAppService().getNotifyList(/*skip/limit+1,limit*/1,-1).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseBean<List<NotifyListModel>>>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {

      }

      @Override public void onNext(BaseBean<List<NotifyListModel>> notifyListBean) {
        if(notifyListBean.getCode() == 1) {
          if(notifyListBean.getData() != null && notifyListBean.getTotal() > 0) {
            totalItem = notifyListBean.getTotal();
            ArrayList<NotifyListModel> list = new ArrayList<>();
            List<NotifyListModel> notifyListModelList = notifyListBean.getData();
            notificationAdapter.setDataList(notifyListModelList);
            notificationAdapter.notifyDataSetChanged();
          }
        }else if(notifyListBean.getCode() == 400){
          Utils.showInfoDialog(getActivity(), getString(R.string.sso_tip), new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              RequestLogout.getInstance().logoutApp(getContext());
            }
          });
        }
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  //@Override
  //public void onLoad(int skip, int limit, boolean isRefresh) {
  //  if(skip + limit <= totalItem) {
  //    loadData(skip, limit, isRefresh);
  //  }
  //}
}
