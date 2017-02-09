package com.im.chat.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.im.chat.adapter.HeaderListAdapter;
import com.im.chat.model.LeanchatUser;
import com.im.chat.service.PreferenceMap;
import com.im.chat.util.Constants;
import com.im.chat.util.LogUtils;
import com.im.chat.util.UserCacheUtils;
import com.im.chat.view.RefreshableRecyclerView;
import com.im.chat.viewholder.NotificationItemHolder;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lzw on 14-9-17.
 */
public class NotificationFragment extends BaseFragment {

  private final SortDialogListener distanceListener = new SortDialogListener(Constants.ORDER_DISTANCE);
  private final SortDialogListener updatedAtListener = new SortDialogListener(Constants.ORDER_UPDATED_AT);

  @Bind(com.im.chat.R.id.fragment_near_srl_pullrefresh)
  protected SwipeRefreshLayout refreshLayout;

  @Bind(com.im.chat.R.id.fragment_near_srl_view)
  protected RefreshableRecyclerView recyclerView;

  protected LinearLayoutManager layoutManager;

  HeaderListAdapter<LeanchatUser> notificationAdapter;
  int orderType;
  PreferenceMap preferenceMap;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(com.im.chat.R.layout.notification_fragment, container, false);
    ButterKnife.bind(this, view);

    layoutManager = new LinearLayoutManager(getActivity());
    notificationAdapter = new HeaderListAdapter<>(NotificationItemHolder.class);
    recyclerView.setOnLoadDataListener(new RefreshableRecyclerView.OnLoadDataListener() {
      @Override
      public void onLoad(int skip, int limit, boolean isRefresh) {
        loadMoreNotificationData(skip, limit, isRefresh);
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
    preferenceMap = PreferenceMap.getCurUserPrefDao(getActivity());
    orderType = preferenceMap.getNearbyOrder();
    headerLayout.showTitle(com.im.chat.R.string.notification_title);
    headerLayout.showRightImageButton(com.im.chat.R.drawable.nearby_order, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(com.im.chat.R.string.notification_fragment_sort).setPositiveButton(com.im.chat.R.string.notification_fragment_loginTime,
          updatedAtListener).setNegativeButton(com.im.chat.R.string.notification_fragment_distance, distanceListener).show();
      }
    });
    recyclerView.refreshData();
  }

  /**
   * 加载数据
   * @param skip
   * @param limit
   * @param isRefresh
   */
  private void loadMoreNotificationData(final int skip, final int limit, final boolean isRefresh) {
    PreferenceMap preferenceMap = PreferenceMap.getCurUserPrefDao(App.ctx);
    AVGeoPoint geoPoint = preferenceMap.getLocation();
    if (geoPoint == null) {
      LogUtils.i("geo point is null");
      return;
    }
    AVQuery<LeanchatUser> q = LeanchatUser.getQuery(LeanchatUser.class);
    LeanchatUser user = LeanchatUser.getCurrentUser();
    q.whereNotEqualTo(Constants.OBJECT_ID, user.getObjectId());
    if (orderType == Constants.ORDER_DISTANCE) {
      q.whereNear(LeanchatUser.LOCATION, geoPoint);
    } else {
      q.orderByDescending(Constants.UPDATED_AT);
    }
    q.skip(skip);
    q.limit(limit);
    q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    q.findInBackground(new FindCallback<LeanchatUser>() {
      @Override
      public void done(List<LeanchatUser> list, AVException e) {
        UserCacheUtils.cacheUsers(list);
        recyclerView.setLoadComplete(list.toArray(), isRefresh);
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    preferenceMap.setNearbyOrder(orderType);
  }

  public class SortDialogListener implements DialogInterface.OnClickListener {
    int orderType;

    public SortDialogListener(int orderType) {
      this.orderType = orderType;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
      NotificationFragment.this.orderType = orderType;
      recyclerView.refreshData();
    }
  }
}
