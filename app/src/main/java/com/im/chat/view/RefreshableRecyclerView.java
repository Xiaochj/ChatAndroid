package com.im.chat.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.im.chat.adapter.HeaderListAdapter;

import java.util.Arrays;

/**
 * Created by wli on 15/12/7.
 * 支持下拉刷新以及上滑加载更多的 RecyclerView
 *
 * 下拉刷新需要配合 SwipeRefreshLayout 使用，需要在初始化 RefreshableRecyclerView后
 * 调用 setRelationSwipeLayout 来设置关联
 *
 * 因为上拉加载需要有 footer，所以需要配合 HeaderListAdapter 使用
 */
public class RefreshableRecyclerView extends RecyclerView {

  public static final int DEFAULT_PAGE_SIZE = 10;//每页加载的item数
  public static int STATUS_NORMAL = 0;//普通状态
  public static int STATUS_LAOD_MORE = 2;//上拉加载

  public int currentScrollState = RecyclerView.SCROLL_STATE_IDLE;//滑动状态，默认停止
  private int pageSize = DEFAULT_PAGE_SIZE;//分页大小
  private int loadStatus = STATUS_NORMAL;//load状态，默认是普通状态
  public boolean enableLoadMore = true;//是否允许加载更多

  private SwipeRefreshLayout swipeRefreshLayout;
  private LoadMoreFooterView loadMoreFooterView;
  private OnLoadDataListener onLoadDataListener;

  public RefreshableRecyclerView(Context context) {
    super(context);
    initView();
  }

  public RefreshableRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  public RefreshableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initView();
  }

  /**
   * 设置关联的 SwipeRefreshLayout， 下拉刷新时使用
   */
  public void setRelationSwipeLayout(SwipeRefreshLayout relationSwipeLayout) {
    swipeRefreshLayout = relationSwipeLayout;
    if (null != swipeRefreshLayout) {
      swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override public void onRefresh() {
          startRefresh();
        }
      });
    } else {
      throw new IllegalArgumentException("SwipeRefreshLayout can not be null");
    }
  }

  /**
   * RefreshableRecyclerView 需要配合 HeaderListAdapter 使用
   */
  @Override public void setAdapter(Adapter adapter) {
    super.setAdapter(adapter);
    if (null != adapter) {
      if (adapter instanceof HeaderListAdapter) {
        ((HeaderListAdapter) adapter).setFooterView(loadMoreFooterView);
      } else {
        throw new IllegalArgumentException("adapter should be HeaderListAdapter");
      }
    } else {
      throw new IllegalArgumentException("adapter can not be null");
    }
  }

  @Override public HeaderListAdapter getAdapter() {
    return (HeaderListAdapter) super.getAdapter();
  }

  /**
   * 设置加载页的大小，默认为 DEFAULT_PAGE_SIZE
   */
  public void setPageNum(int pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * 初始化加载数据
   */
  public void initData() {
    startRefresh();
  }

  public void setOnLoadDataListener(OnLoadDataListener loadDataListener) {
    onLoadDataListener = loadDataListener;
  }

  private void initView() {
    loadMoreFooterView = new LoadMoreFooterView(getContext());
    //点击加载更多
    loadMoreFooterView.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        //如果允许加载并且当前状态正常
        if (enableLoadMore && STATUS_LAOD_MORE != getLoadStatus()) {
          //加载
          startLoad();
        }
      }
    });
    //滑动
    this.addOnScrollListener(new OnScrollListener() {
      int lastVisibleItem = 0;
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //如果允许加载并且当前状态正常
        if (enableLoadMore && STATUS_LAOD_MORE != getLoadStatus() ) {
          LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
          lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        }
      }

      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        currentScrollState = newState;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem) >= totalItemCount - 1)) {
          startLoad();
        }
      }
    });
  }

  /**
   * 下拉刷新
   */
  private void startRefresh() {
    if (null != onLoadDataListener) {
      onLoadDataListener.onLoad(0, pageSize, true);
    }
  }

  /**
   * 开始加载
   */
  private void startLoad() {
    if (STATUS_LAOD_MORE != getLoadStatus()) {
      HeaderListAdapter adapter = getAdapter();
      if (null != onLoadDataListener && null != adapter) {
        //设置加载状态
        setLoadStatus(STATUS_LAOD_MORE);
        //加载更多
        onLoadDataListener.onLoad(adapter.getDataList().size(), pageSize, false);
      } else {
        //设置普通状态
        setLoadStatus(STATUS_NORMAL);
      }
    }
  }

  /**
   * 设置是否可用上滑加载
   */
  public void setEnableLoadMore(boolean enable) {
    enableLoadMore = enable;
  }

  /**
   * 设置load状态
   */
  private void setLoadStatus(int status) {
    loadStatus = status;
    loadMoreFooterView.onLoadStatusChanged(status);
  }

//  /**
//   * 设置刷新完毕
//   */
//  public void setLoadComplete() {
//    setLoadStatus(STATUS_NORMAL);
//    if (null != swipeRefreshLayout) {
//      swipeRefreshLayout.setRefreshing(false);
//    }
//  }

  public int getLoadStatus() {
    return loadStatus;
  }

  /**
   * 设置刷新完毕，如果 isRefresh 为 true，则清空所有数据，设置为 datas
   * 如果 isReresh 为 false，则把 datas 叠加到现有数据中
   */
  public void setLoadComplete(Object[] datas, boolean isRefresh) {
    setLoadStatus(STATUS_NORMAL);
    HeaderListAdapter adapter = getAdapter();
    if (null != adapter) {
      if (isRefresh) {
        adapter.setDataList(Arrays.asList(datas));
        adapter.notifyDataSetChanged();
        if (null != swipeRefreshLayout) {
          swipeRefreshLayout.setRefreshing(false);
        }
      } else {
        adapter.addDataList(Arrays.asList(datas));
        adapter.notifyDataSetChanged();
      }
    }
  }

  public interface OnLoadDataListener {
    public void onLoad(int skip, int limit, boolean isRefresh);
  }
}
