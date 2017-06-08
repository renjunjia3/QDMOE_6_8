package com.qd.mm.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qd.mm.R;

/**
 * Case By:查看大图
 * package:com.mzhguqvn.mzhguq.ui.dialog
 * Author：scene on 2017/5/22 18:06
 */

public class BigImageDialog extends Dialog {
    private ImageView imageView;
    private String url = "";

    public BigImageDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    public BigImageDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView();
    }

    /**
     * Case By:
     * Author: scene on 2017/5/22 18:08
     * 初始化view
     */
    private void initView() {
        setContentView(R.layout.dialog_big_image);
        imageView = (ImageView) findViewById(R.id.image);
        if (!TextUtils.isEmpty(url))
            Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    public void setUrl(String url) {
        if (!this.url.equals(url)) {
            this.url = url;
            if (!TextUtils.isEmpty(url)) {
                Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            }
        }

    }
}
