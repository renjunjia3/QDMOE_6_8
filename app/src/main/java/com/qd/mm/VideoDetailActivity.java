package com.qd.mm;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aitangba.swipeback.SwipeBackActivity;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.qd.mm.adapter.IndexItemAdapter;
import com.qd.mm.adapter.VideoDetailRecommendHengAdapter;
import com.qd.mm.app.App;
import com.qd.mm.bean.CheckOrderInfo;
import com.qd.mm.bean.VideoInfo;
import com.qd.mm.bean.VideoRelateResultInfo;
import com.qd.mm.config.PageConfig;
import com.qd.mm.event.CloseVideoDetailEvent;
import com.qd.mm.ui.dialog.OpenVipNoticeDialog;
import com.qd.mm.ui.view.CustomeGridView;
import com.qd.mm.util.API;
import com.qd.mm.util.DialogUtil;
import com.qd.mm.util.NetWorkUtils;
import com.qd.mm.util.SharedPreferencesUtil;
import com.qd.mm.video.JCFullScreenActivity;
import com.qd.mm.video.VideoConfig;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import wiki.scene.statuslib.StatusViewLayout;

/**
 * Case By:视频详情
 * package:com.cyldf.cyldfxv
 * Author：scene on 2017/4/13 10:02
 */
public class VideoDetailActivity extends SwipeBackActivity {

    public static final String ARG_VIDEO_INFO = "arg_video_info";
    public static final String ARG_IS_ENTER_FROM = "is_enter_from";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.detail_player)
    ImageView detailPlayer;
    @BindView(R.id.play_count)
    TextView playCount;
    @BindView(R.id.aboutCommendTextView)
    TextView aboutCommendTextView;
    @BindView(R.id.aboutCommendGridView)
    CustomeGridView aboutCommendGridView;
    @BindView(R.id.aboutCommendGridView2)
    CustomeGridView aboutCommendGridView2;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.statusViewLayout)
    StatusViewLayout statusViewLayout;


    private Unbinder unbinder;

    private VideoInfo videoInfo;
    private int isEnterFrom = 0;

    //相关推荐
    private List<VideoInfo> videoRelateList = new ArrayList<>();
    private IndexItemAdapter videoRelateAdapter;
    private List<VideoInfo> videoRelateList1 = new ArrayList<>();
    private VideoDetailRecommendHengAdapter videoDetailRecommendHengAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.fragment_video_detail);
        registerBoradcastReceiver();
        unbinder = ButterKnife.bind(this);
        videoInfo = (VideoInfo) getIntent().getSerializableExtra(ARG_VIDEO_INFO);
        isEnterFrom = getIntent().getIntExtra(ARG_IS_ENTER_FROM, 0);

        initToolbarNav(toolbar);
        initView();
        MainActivity.upLoadPageInfo(PageConfig.VIDEO_DETAIL_POSITION_ID, videoInfo.getVideo_id(), 0);
    }


    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        toolbarTitle.setText(videoInfo.getTitle());
        playCount.setText(videoInfo.getHits() + "次访问");
        Glide.with(this).load(videoInfo.getThumb_heng()).asBitmap().centerCrop().placeholder(R.drawable.bg_loading).error(R.drawable.bg_error).into(detailPlayer);
        //相关推荐
        videoRelateAdapter = new IndexItemAdapter(VideoDetailActivity.this, videoRelateList);
        aboutCommendGridView.setAdapter(videoRelateAdapter);
        aboutCommendGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (isEnterFrom == PageConfig.TRY_SEE_POSITOTN_ID && App.role == 0) {
                    //试看区进来的但是不是会员
                    DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false,
                            "该片为15元区视频，请开通15元区后继续观看", true, videoInfo.getVideo_id(), PageConfig.VIDEO_DETAIL_POSITION_ID);
                } else if (isEnterFrom == PageConfig.GLOD_POSITOTN_ID && App.role == 0) {
                    //黄金区进来的但是不是会员
                    DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false,
                            "该片为15元区视频，请开通15元区后继续观看", true, videoInfo.getVideo_id(),
                            PageConfig.VIDEO_DETAIL_POSITION_ID);
                } else {
                    Intent intent = new Intent(VideoDetailActivity.this, VideoDetailActivity.class);
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, videoRelateList.get(position));
                    intent.putExtra(VideoDetailActivity.ARG_IS_ENTER_FROM, isEnterFrom == PageConfig.TRY_SEE_POSITOTN_ID ? PageConfig.GLOD_POSITOTN_ID : isEnterFrom);
                    startActivity(intent);
                    finish();
                }

            }
        });
        videoDetailRecommendHengAdapter = new VideoDetailRecommendHengAdapter(VideoDetailActivity.this, videoRelateList1);
        aboutCommendGridView2.setAdapter(videoDetailRecommendHengAdapter);
        aboutCommendGridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEnterFrom == PageConfig.TRY_SEE_POSITOTN_ID && App.role == 0) {
                    //试看区进来的但是不是会员
                    DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false,
                            "该片为15元区视频，请开通15元区后继续观看", true, videoInfo.getVideo_id(), PageConfig.VIDEO_DETAIL_POSITION_ID);
                } else if (isEnterFrom == PageConfig.GLOD_POSITOTN_ID && App.role == 0) {
                    //黄金区进来的但是不是会员
                    DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false,
                            "该片为15元区视频，请开通15元区后继续观看", true, videoInfo.getVideo_id(),
                            PageConfig.VIDEO_DETAIL_POSITION_ID);
                } else {
                    Intent intent = new Intent(VideoDetailActivity.this, VideoDetailActivity.class);
                    intent.putExtra(VideoDetailActivity.ARG_VIDEO_INFO, videoRelateList.get(position));
                    intent.putExtra(VideoDetailActivity.ARG_IS_ENTER_FROM, isEnterFrom == PageConfig.TRY_SEE_POSITOTN_ID ? PageConfig.GLOD_POSITOTN_ID : isEnterFrom);
                    startActivity(intent);
                    finish();
                }
            }
        });
        getRecomendVideo();

        progressDialog = new ProgressDialog(VideoDetailActivity.this);
        progressDialog.setMessage("正在获取支付结果...");
    }


    @OnClick(R.id.play_video)
    public void onClickPlayVideo() {
        if (isEnterFrom == PageConfig.TRY_SEE_POSITOTN_ID && App.role == 0 && App.tryCount >= VideoConfig.TRY_COUNT_TIME) {
            //游客试看区进来的没有试看次数
            DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false,
                    "游客只能试看" + VideoConfig.TRY_COUNT_TIME + "次，请开通15元区或30元区继续观看", true, videoInfo.getVideo_id(),
                    PageConfig.VIDEO_DETAIL_POSITION_ID);
        } else if (isEnterFrom == PageConfig.GLOD_POSITOTN_ID && App.role == 0) {
            //试看区进来的但是不是会员
            DialogUtil.getInstance().showSubmitDialog(VideoDetailActivity.this, false,
                    "该片为15元区视频，请开通15元区后继续观看", true, videoInfo.getVideo_id(),
                    PageConfig.VIDEO_DETAIL_POSITION_ID);
        } else {
            App.tryCount += 1;
            SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.TRY_COUNT_KEY, App.tryCount);
            Intent intent = new Intent(VideoDetailActivity.this, JCFullScreenActivity.class);
            intent.putExtra(JCFullScreenActivity.PARAM_VIDEO_INFO, videoInfo);
            startActivityForResult(intent, 101);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            DialogUtil.getInstance().showGoldVipDialog(VideoDetailActivity.this, videoInfo.getVideo_id(), PageConfig.VIDEO_DETAIL_POSITION_ID);
        }
    }

    /**
     * 获取相关推荐
     */
    private RequestCall recommendRequestCall;

    public void getRecomendVideo() {
        statusViewLayout.showLoading();
        if (NetWorkUtils.isNetworkConnected(VideoDetailActivity.this)) {
            HashMap<String, String> params = API.createParams();
            params.put("video_id", String.valueOf(videoInfo.getVideo_id()));
            params.put("layout_id", String.valueOf(isEnterFrom));
            recommendRequestCall = OkHttpUtils.get().url(API.URL_PRE + API.VIDEO_RELATED).params(params).build();
            recommendRequestCall.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    try {
                        if (!isFinishing()) {
                            statusViewLayout.showContent();
                            aboutCommendTextView.setVisibility(View.GONE);
                            aboutCommendGridView.setVisibility(View.GONE);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void onResponse(String s, int i) {
                    try {
                        VideoRelateResultInfo relateResultInfo = JSON.parseObject(s, VideoRelateResultInfo.class);

                        videoRelateList.clear();
                        videoRelateList.addAll(relateResultInfo.getData());
                        videoRelateAdapter.notifyDataSetChanged();
                        if (videoRelateList.size() > 0) {
                            aboutCommendTextView.setVisibility(View.VISIBLE);
                            aboutCommendGridView.setVisibility(View.VISIBLE);
                        } else {
                            aboutCommendTextView.setVisibility(View.GONE);
                            aboutCommendGridView.setVisibility(View.GONE);
                        }
                        if (videoRelateList.size() > 3) {
                            videoRelateList1.clear();
                            for (int j = 3; j < videoRelateList.size(); j++) {
                                videoRelateList1.add(videoRelateList.get(j));
                            }
                            videoDetailRecommendHengAdapter.notifyDataSetChanged();
                            videoRelateList.clear();
                            for (int j = 0; j < 3; j++) {
                                videoRelateList.add(relateResultInfo.getData().get(j));
                            }
                            videoRelateAdapter.notifyDataSetChanged();
                        }
                        statusViewLayout.showContent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            statusViewLayout.showNetError(onClickRetryListener);
        }
    }

    View.OnClickListener onClickRetryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getRecomendVideo();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (App.isNeedCheckOrder && App.orderIdInt != 0) {
            checkOrder();
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onDestroy() {
        if (requestCall != null) {
            requestCall.cancel();
        }
        if (recommendRequestCall != null) {
            recommendRequestCall.cancel();
        }
        DialogUtil.getInstance().cancelAllDialog();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        unRegisterBoradcastReceiver();
        super.onDestroy();
    }

    @Subscribe
    public void closeVideoDetail(CloseVideoDetailEvent closeVideoDetailEvent) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 检查是否成功
     */
    private RequestCall requestCall;
    private ProgressDialog progressDialog;

    private void checkOrder() {
        if (progressDialog != null) {
            progressDialog.show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = API.createParams();
                params.put("order_id", App.orderIdInt + "");
                App.isNeedCheckOrder = false;
                App.orderIdInt = 0;
                requestCall = OkHttpUtils.get().url(API.URL_PRE + API.CHECK_ORDER).params(params).build();
                requestCall.execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        e.printStackTrace();
                        DialogUtil.getInstance().showCustomSubmitDialog(VideoDetailActivity.this, "如遇微信不能支付，请使用支付宝支付");
                        if (progressDialog != null && progressDialog.isShowing()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            CheckOrderInfo checkOrderInfo = JSON.parseObject(s, CheckOrderInfo.class);
                            if (checkOrderInfo.isStatus()) {
                                MainActivity.onPaySuccess();
                                MainActivity.isNeedChangeTab = true;
                                String message1 = "";
                                App.role = checkOrderInfo.getRole();
                                SharedPreferencesUtil.putInt(VideoDetailActivity.this, App.ROLE_KEY, App.role);
                                switch (App.role) {
                                    case 1:
                                    case 2:
                                        message1 = "15元区";
                                        break;
                                    case 3:
                                    case 4:
                                        message1 = "30元区";
                                        break;
                                    default:
                                        break;
                                }
                                OpenVipNoticeDialog openVipNoticeDialog1 = DialogUtil.getInstance().showOpenVipNoticeDialog(VideoDetailActivity.this, message1);
                                openVipNoticeDialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        closeVideoDetail(new CloseVideoDetailEvent());
                                    }
                                });
                            } else {
                                DialogUtil.getInstance().showCustomSubmitDialog(VideoDetailActivity.this, "如遇微信不能支付，请使用支付宝支付");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            DialogUtil.getInstance().showCustomSubmitDialog(VideoDetailActivity.this, "如遇微信不能支付，请使用支付宝支付");
                        }
                    }
                });
            }
        }, 1000);

    }


    public static final String ACTION_NAME_VIDEODETAILACTIVITY_CHECK_ORDER = "action_name_VideoDetailActivity_check_order";
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME_VIDEODETAILACTIVITY_CHECK_ORDER)) {
                checkOrder();
            }
        }
    };

    private void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME_VIDEODETAILACTIVITY_CHECK_ORDER);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private void unRegisterBoradcastReceiver() {
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }
}
