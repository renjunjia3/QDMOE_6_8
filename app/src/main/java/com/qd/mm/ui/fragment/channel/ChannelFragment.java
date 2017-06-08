package com.qd.mm.ui.fragment.channel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qd.mm.MainActivity;
import com.qd.mm.R;
import com.qd.mm.adapter.ChannelAdapter;
import com.qd.mm.app.App;
import com.qd.mm.base.BaseMainFragment;
import com.qd.mm.bean.ChannelResultInfo;
import com.qd.mm.config.PageConfig;
import com.qd.mm.event.StartBrotherEvent;
import com.qd.mm.itemdecoration.ActorItemDecoration;
import com.qd.mm.listener.OnTaskFinishedListener;
import com.qd.mm.pull_loadmore.PtrClassicFrameLayout;
import com.qd.mm.pull_loadmore.PtrDefaultHandler;
import com.qd.mm.pull_loadmore.PtrFrameLayout;
import com.qd.mm.task.ChannelTask;
import com.qd.mm.util.API;
import com.qd.mm.util.DialogUtil;
import com.qd.mm.util.NetWorkUtils;
import com.qd.mm.util.ScreenUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:频道
 * package:com.mzhguqvn.mzhguq.ui.fragment.channel
 * Author：scene on 2017/6/1 12:06
 */

public class ChannelFragment extends BaseMainFragment implements ChannelAdapter.OnClickChannelItemListener {
    private static final String TAG = "ChannelFragment";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;

    private ChannelAdapter adapter;
    private List<ChannelResultInfo.DataBean> list;

    public static ChannelFragment newInstance() {
        return new ChannelFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        MainActivity.upLoadPageInfo(PageConfig.CHANNEL_POSITION_ID, 0, 0);
        initView();
        getdata(true);
    }

    private void initView() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getdata(false);
            }
        });

        list = new ArrayList<>();
        adapter = new ChannelAdapter(getContext(), list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int space = ScreenUtils.instance(getContext()).dp2px(5);
        recyclerView.addItemDecoration(new ActorItemDecoration(space));
        recyclerView.setAdapter(adapter);
        adapter.setOnClickChannelItemListener(this);
    }

    private void getdata(final boolean isShowLoading) {
        HashMap<String, String> params = API.createParams();
        OkHttpUtils.get().url(API.URL_PRE + API.CHANNEL).params(params).tag(TAG).build().execute(new StringCallback() {
            @Override
            public void onBefore(Request request, int id) {
                if (!NetWorkUtils.isNetworkConnected(getContext())) {
                    showStatus(3);
                    OkHttpUtils.getInstance().cancelTag(TAG);
                } else {
                    if (isShowLoading) {
                        statusViewLayout.showLoading();
                    }
                }
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    Log.e(TAG, s);
                    new ChannelTask(getContext(), new OnTaskFinishedListener<ChannelResultInfo>() {
                        @Override
                        public void onTaskFinished(ChannelResultInfo data) {
                            if (data.isStatus()) {
                                if (data.getData() != null && data.getData().size() > 0) {
                                    showStatus(1);
                                    list.clear();
                                    list.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    showStatus(4);
                                }
                            } else {
                                showStatus(2);
                            }
                        }
                    }, s).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } catch (Exception e) {
                    e.printStackTrace();
                    showStatus(2);
                }
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                showStatus(2);
            }
        });
    }

    View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getdata(true);
        }
    };

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(TAG);
        super.onDestroyView();
    }

    private void showStatus(int status) {
        switch (status) {
            case 1:
                //正常
                statusViewLayout.showContent();
                break;
            case 2:
                //失败
                statusViewLayout.showFailed(retryListener);
                break;
            case 3:
                //无网
                statusViewLayout.showNetError(retryListener);
                break;
            case 4:
                //无内容
                statusViewLayout.showNone(retryListener);
                break;
        }
        ptrLayout.refreshComplete();
    }

    @Override
    public void onClickChannelItem(int position) {
        if (App.role < 2) {
            DialogUtil.getInstance().showSubmitDialog(getContext(), false, "该栏目为30元区专享，请先开通30元区", true, PageConfig.CHANNEL_POSITION_ID);
        } else {
            EventBus.getDefault().post(new StartBrotherEvent(ChannelDetailFragment.newInstance(list.get(position).getId(), list.get(position).getTitle())));
        }
    }
}
