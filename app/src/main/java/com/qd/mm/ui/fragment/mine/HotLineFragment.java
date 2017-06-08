package com.qd.mm.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qd.mm.MainActivity;
import com.qd.mm.R;
import com.qd.mm.base.BaseBackFragment;
import com.qd.mm.config.PageConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 投诉热线
 * Created by Administrator on 2017/3/16.
 */

public class HotLineFragment extends BaseBackFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    public static HotLineFragment newInstance() {
        HotLineFragment fragment = new HotLineFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_hot_line, container, false);
        ButterKnife.bind(this, view);
        toolbarTitle.setText("求片/投诉/客服");
        initToolbarNav(toolbar);
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        MainActivity.upLoadPageInfo(PageConfig.REBACK_POSITOTN_ID,0,0);
    }
}
