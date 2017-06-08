package com.qd.mm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qd.mm.R;
import com.qd.mm.base.BaseFragment;
import com.qd.mm.event.StartBrotherEvent;
import com.qd.mm.event.TabSelectedEvent;
import com.qd.mm.ui.fragment.channel.ChannelFragment;
import com.qd.mm.ui.fragment.mine.HotLineFragment;
import com.qd.mm.ui.fragment.mine.MineFragment;
import com.qd.mm.ui.fragment.vip.GlodVip1Fragment;
import com.qd.mm.ui.fragment.vip.TrySeeFragment;
import com.qd.mm.ui.view.BottomBar;
import com.qd.mm.ui.view.BottomBarTab;
import com.qd.mm.util.API;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Case By: 主Fragment
 * package:com.hfaufhreu.hjfeuio.ui.fragment
 * Author：scene on 2017/4/18 9:06
 */

public class MainFragment extends BaseFragment {

    public static final int TAB_1 = 0;
    public static final int TAB_2 = 1;
    public static final int TAB_3 = 2;
    public static final int TAB_4 = 3;

    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.to_user)
    ImageView toUser;

    private List<SupportFragment> fragments = new ArrayList<>();
    private List<String> tabNames = new ArrayList<>();

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (savedInstanceState == null || tabNames.size() == 0 || fragments.size() == 0) {
            tabNames.clear();
            fragments.clear();

            fragments.add(TrySeeFragment.newInstance());
            fragments.add(GlodVip1Fragment.newInstance());
            fragments.add(ChannelFragment.newInstance());
            fragments.add(MineFragment.newInstance());

            tabNames.add(getString(R.string.tab_try_see));
            tabNames.add(getString(R.string.tab_15));
            tabNames.add(getString(R.string.tab_30));
            tabNames.add(getString(R.string.tab_mine));


            loadMultipleRootFragment(R.id.fl_container, TAB_1,
                    fragments.get(TAB_1),
                    fragments.get(TAB_2),
                    fragments.get(TAB_3),
                    fragments.get(TAB_4));

        } else {
            fragments.add(findChildFragment(TrySeeFragment.class));
            fragments.add(findChildFragment(GlodVip1Fragment.class));
            fragments.add(findChildFragment(ChannelFragment.class));
            fragments.add(findChildFragment(MineFragment.class));
        }
        try {
            name.setText(tabNames.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView();
        return view;
    }

    private void initView() {
        EventBus.getDefault().register(this);
        toUser.setImageResource(R.drawable.ic_toolbar_vip_try_see);
        mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_try_see_d, R.drawable.ic_bottom_bar_try_see_s, tabNames.get(0)));
        mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_glod_d, R.drawable.ic_bottom_bar_glod_s, tabNames.get(1)));
        mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_diamond_d, R.drawable.ic_bottom_bar_diamond_s, tabNames.get(2)));
        mBottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.ic_bottom_bar_mine_d, R.drawable.ic_bottom_bar_mine_s, tabNames.get(3)));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                hideSoftInput();
                showHideFragment(fragments.get(position), fragments.get(prePosition));
                name.setText(tabNames.get(position));
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                //目前这个不用也可以，作用是可以在切换到fragment的时候做一些处理比如可以让列表回到顶部
                EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @OnClick(R.id.complaint)
    public void onClickComplaint() {
        EventBus.getDefault().post(new StartBrotherEvent(HotLineFragment.newInstance()));
    }

    @OnClick(R.id.to_user)
    public void onClickTop() {
        if (fragments.get(fragments.size() - 1) instanceof MineFragment) {
            mBottomBar.setCurrentItem(fragments.size() - 1);
        }
    }

    /**
     * 弹出支付窗口之后调用
     */
    public static void clickWantPay() {
        HashMap<String, String> params = API.createParams();
        OkHttpUtils.get().url(API.URL_PRE + API.PAY_CLICK).params(params).build().execute(null);
    }

}
