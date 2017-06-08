package com.mzhguqvn.mzhguq;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.mzhguqvn.mzhguq.app.App;
import com.mzhguqvn.mzhguq.bean.UserInfo;
import com.mzhguqvn.mzhguq.util.API;
import com.mzhguqvn.mzhguq.util.NetWorkUtils;
import com.mzhguqvn.mzhguq.util.SharedPreferencesUtil;
import com.mzhguqvn.mzhguq.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 启动页
 */
public class LuncherActivity extends AppCompatActivity {

    private long loginTime = 0L;
    private int retryTime = 0;
    private static final int TIME = 2000;

    private ImageView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //解决重复启动的问题
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_luncher);
        //申请权限---内存读写权限
        loginTime = System.currentTimeMillis();
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            applyExternalPer();
        } else {
            loginAndRegister();
        }
        //ToastUtils.getInstance(LuncherActivity.this).showToast("渠道：" + App.CHANNEL_ID);
        loading = (ImageView) findViewById(R.id.loading);
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            loading.startAnimation(operatingAnim);
        }
    }

    private void applyExternalPer() {

        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .setDeniedMessage("需要获取手机状态权限，以作为您的唯一标识").build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        loginAndRegister();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        finish();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void loginAndRegister() {
        if (!NetWorkUtils.isNetworkConnected(LuncherActivity.this)) {
            ToastUtils.getInstance(LuncherActivity.this).showToast("请检查网络连接");
            finish();
            return;
        }

        retryTime++;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        App.IMEI = tm.getDeviceId();
        if (App.IMEI.isEmpty()) {
            App.IMEI = SharedPreferencesUtil.getString(LuncherActivity.this, "IMEI", "");
            if (App.IMEI.isEmpty()) {
                String imei = createRandom(false, 20);
                SharedPreferencesUtil.putString(LuncherActivity.this, "IMEI", imei);
                App.IMEI = imei;
            }
        }

        HashMap<String, String> params = API.createParams();
        params.put("device", Build.BRAND);
        params.put("system", Build.VERSION.SDK);
        OkHttpUtils.get().url(API.URL_PRE + API.LOGIN_REGISTER).params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        e.printStackTrace();
                        if (retryTime < 3) {
                            loginAndRegister();
                        } else {
                            ToastUtils.getInstance(LuncherActivity.this).showToast("请检查网络连接");
                            ActivityCompat.finishAffinity(LuncherActivity.this);
                        }
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        try {
                            UserInfo info = JSON.parseObject(s, UserInfo.class);
                            App.user_id = info.getUser_id();
                            App.role = info.getRole();

                            SharedPreferencesUtil.putInt(LuncherActivity.this, App.USERID_KEY, App.user_id);
                            SharedPreferencesUtil.putInt(LuncherActivity.this, App.ROLE_KEY, App.role);

                            SharedPreferencesUtil.putLong(LuncherActivity.this, App.LAST_LOGIN_TIME, System.currentTimeMillis());
                            if (System.currentTimeMillis() - loginTime < TIME) {
                                // 已经获取到权限了
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(LuncherActivity.this, MainActivity.class));
                                        ActivityCompat.finishAffinity(LuncherActivity.this);
                                    }
                                }, TIME - (System.currentTimeMillis() - loginTime));
                            } else {
                                startActivity(new Intent(LuncherActivity.this, MainActivity.class));
                                ActivityCompat.finishAffinity(LuncherActivity.this);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            loginAndRegister();
                        }
                    }
                });
    }

    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 20) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

    @Override
    protected void onDestroy() {
        try {
            loading.clearAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            super.onDestroy();
        }
    }
}
