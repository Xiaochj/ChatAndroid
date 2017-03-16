package com.im.chat.viewholder;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.im.chat.R;
import com.im.chat.activity.NotifyDetailActivity;
import com.im.chat.model.NotifyItemBean;
import com.squareup.picasso.Picasso;

import cn.leancloud.chatkit.view.RoundImageView;
import cn.leancloud.chatkit.viewholder.LCIMCommonViewHolder;

/**
 * 通告的viewholder
 * Created by xiaochj on 2017/3/8.
 */

public class NotifyItemHolder extends LCIMCommonViewHolder<NotifyItemBean> {

    cn.leancloud.chatkit.view.RoundImageView mImageView;
    TextView mTitleTv,mContentTv,mTimeTv;

    public NotifyItemHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.notify_item);
        initView(context);
    }

    private void initView(Context context){
        mImageView = (RoundImageView)itemView.findViewById(R.id.notify_item_img);
        mTitleTv = (TextView)itemView.findViewById(R.id.notify_item_title);
        mContentTv = (TextView)itemView.findViewById(R.id.notify_item_content);
        mTimeTv = (TextView)itemView.findViewById(R.id.notify_item_time);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NotifyDetailActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void bindData(NotifyItemBean notifyItemBean) {
        if(notifyItemBean != null) {
            if(!"".equals(notifyItemBean.getImgUrl())) {
                Picasso.with(getContext()).load(notifyItemBean.getImgUrl()).into(mImageView);
            }
            mTitleTv.setText(notifyItemBean.getTitle());
            mTimeTv.setText(notifyItemBean.getTime());
            mContentTv.setText(notifyItemBean.getContent());
        }

    }

    @Override
    public void setData(NotifyItemBean notifyItemBean) {
        super.setData(notifyItemBean);
    }

    public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<NotifyItemHolder>() {
    @Override
    public NotifyItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
      return new NotifyItemHolder(parent.getContext(), parent);
    }
  };
}
