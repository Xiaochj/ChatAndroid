package com.im.chat.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.im.chat.App;
import com.im.chat.R;
import com.im.chat.adapter.HeaderListAdapter;
import com.im.chat.engine.AppEngine;
import com.im.chat.model.LeanchatUser;
import com.im.chat.model.NotifyItemBean;
import com.im.chat.model.NotifyListBean;
import com.im.chat.model.NotifyListModel;
import com.im.chat.model.NotifyListRequestBean;
import com.im.chat.service.PreferenceMap;
import com.im.chat.util.Constants;
import com.im.chat.util.LogUtils;
import com.im.chat.util.UserCacheUtils;
import com.im.chat.view.RefreshableRecyclerView;
import com.im.chat.viewholder.NotifyItemHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 通告页
 * Created by xiaochj on 14-9-17.
 */
public class NotificationFragment extends BaseFragment {
  @Bind(R.id.notify_pullrefresh)
  protected SwipeRefreshLayout refreshLayout;

  @Bind(R.id.notify_view)
  protected RefreshableRecyclerView recyclerView;

  protected LinearLayoutManager layoutManager;

  HeaderListAdapter<LeanchatUser> notificationAdapter;
  int orderType;
  PreferenceMap preferenceMap;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(com.im.chat.R.layout.notify_fragment, container, false);
    ButterKnife.bind(this, view);

    layoutManager = new LinearLayoutManager(getActivity());
    notificationAdapter = new HeaderListAdapter<>(NotifyItemHolder.class);
    recyclerView.setOnLoadDataListener(new RefreshableRecyclerView.OnLoadDataListener() {
      @Override
      public void onLoad(int skip, int limit, boolean isRefresh) {
//        loadMoreNotificationData(skip, limit, isRefresh);
        loadData(skip,limit,isRefresh);
      }
    });
    recyclerView.setRelationSwipeLayout(refreshLayout);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(notificationAdapter);
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
//    preferenceMap = PreferenceMap.getCurUserPrefDao(getActivity());
//    orderType = preferenceMap.getNearbyOrder();
    headerLayout.showTitle(com.im.chat.R.string.notification_title);
    recyclerView.refreshData();
  }

  private void loadData(int skip,int limit,boolean isRefresh){
    //调用retrofit自己的服务器接口
    //NotifyListRequestBean notifyListRequestBean = new NotifyListRequestBean(skip,limit);
    AppEngine.getInstance().getAppService().getNotifyList(skip,limit).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<NotifyListBean>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {

      }

      @Override public void onNext(NotifyListBean notifyListBean) {
        if(notifyListBean.getStatus() == 1) {
          if(notifyListBean.getData() != null) {
            ArrayList<NotifyItemBean> list = new ArrayList<>();
            List<NotifyListModel> notifyListModelList = notifyListBean.getData();
            for (NotifyListModel notifyListModel : notifyListModelList) {
              NotifyItemBean notifyItemBean =
                  new NotifyItemBean("", notifyListModel.getName(),
                      notifyListModel.getDescription(), notifyListModel.getCreate_time(),notifyListModel.getUrl());
              list.add(notifyItemBean);
            }
            recyclerView.setLoadComplete(list.toArray(), isRefresh);
          }
        }
      }
    });
  }

  ///**
  // * 加载数据
  // * @param skip
  // * @param limit
  // * @param isRefresh
  // */
  //private void loadMoreNotificationData(final int skip, final int limit, final boolean isRefresh) {
  //  PreferenceMap preferenceMap = PreferenceMap.getCurUserPrefDao(App.ctx);
  //  AVGeoPoint geoPoint = preferenceMap.getLocation();
  //  if (geoPoint == null) {
  //    LogUtils.i("geo point is null");
  //    return;
  //  }
  //  AVQuery<LeanchatUser> q = LeanchatUser.getQuery(LeanchatUser.class);
  //  LeanchatUser user = LeanchatUser.getCurrentUser();
  //  q.whereNotEqualTo(Constants.OBJECT_ID, user.getObjectId());
  //  if (orderType == Constants.ORDER_DISTANCE) {
  //    q.whereNear(LeanchatUser.LOCATION, geoPoint);
  //  } else {
  //    q.orderByDescending(Constants.UPDATED_AT);
  //  }
  //  q.skip(skip);
  //  q.limit(limit);
  //  q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
  //  q.findInBackground(new FindCallback<LeanchatUser>() {
  //    @Override
  //    public void done(List<LeanchatUser> list, AVException e) {
  //      UserCacheUtils.cacheUsers(list);
  //      recyclerView.setLoadComplete(list.toArray(), isRefresh);
  //    }
  //  });
  //}

  @Override
  public void onDestroy() {
    super.onDestroy();
//    preferenceMap.setNearbyOrder(orderType);
  }
}
