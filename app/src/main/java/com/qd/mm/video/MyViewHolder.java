package com.qd.mm.video;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qd.mm.R;

import master.flame.danmaku.danmaku.model.android.ViewCacheStuffer;

public class MyViewHolder extends ViewCacheStuffer.ViewHolder {

    public final ImageView mIcon;
    public final TextView mText;

    public MyViewHolder(View itemView) {
        super(itemView);
        mIcon = (ImageView) itemView.findViewById(R.id.icon);
        mText = (TextView) itemView.findViewById(R.id.text);
    }

}