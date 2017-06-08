package com.qd.mm.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Channel的间隔
 * Created by scene on 2017/3/14.
 */

public class ActorItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public ActorItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.bottom = space;
        //获取childPosition
        int childLayoutPosition = parent.getChildLayoutPosition(view);

        //设置每一行第一个的左间距
        if (childLayoutPosition % 2 == 0) {
            outRect.left = space;
            outRect.right = space / 2;
        }
        //设置每行最后一个的右间距
        if (childLayoutPosition % 2 == 1) {
            outRect.right = space;
            outRect.left = space / 2;
        }
        if (childLayoutPosition < 2) {
            outRect.top = space;
        }
    }

}
