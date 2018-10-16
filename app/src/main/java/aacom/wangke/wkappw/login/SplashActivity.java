package aacom.wangke.wkappw.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wangke.core.basemvp.BaseMVPActivity;
import com.wangke.core.basemvp.BasePresenter;
import com.wangke.core.basemvp.BaseView;
import com.wangke.core.utils.AppUtils;
import com.wangke.core.utils.LSSpUtil;
import com.wangke.core.utils.LogUtil;

import aacom.wangke.wkappw.main.MainActivity;
import aacom.wangke.wkappw.R;
import aacom.wangke.wkappw.SPConstants;


/**
 * Created by wk on 2018/10/11.
 */

public class SplashActivity extends BaseMVPActivity<BasePresenter> implements BaseView, View.OnClickListener {


    private RelativeLayout mActivitySignMain;
    private ImageView mImgAd;
    private Button mSpJumpBtn;

    private boolean hasNewGuide = false;  //相对上一版，是否有更新 引导图，有，设置该值 为true
    private boolean isLogin;            //是否登录过
    private boolean mustReLogin = false;      // 是否要求用户重新登录
    private boolean isUpdateAppVersionCode = false;      // 是否 需要写入 当前版本号
    private int appVersionCode;
    public Handler handler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_activity_splash);
        initView();
        initData();
    }

    public void initView() {
        mActivitySignMain = (RelativeLayout) findViewById(R.id.activity_sign_main);
        mImgAd = (ImageView) findViewById(R.id.img_ad);
        mSpJumpBtn = (Button) findViewById(R.id.sp_jump_btn);
//        mImgAd.setOnClickListener(this);
//        mSpJumpBtn.setOnClickListener(this);
    }

    public void initData() {
        handler = new Handler();
        String token = (String) LSSpUtil.get(SPConstants.SP_USER_LOGIN_TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            token = "Bearer " + token;
        } else {
            token = "Bearer";
        }
        checkToGo();


    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("-->onResume:", getClass().getSimpleName());

    }

    private void checkToGo() {
        // 获取当前版本号
        appVersionCode = AppUtils.getAppVersionCode();
        int spVersionCode = (int) LSSpUtil.get(SPConstants.SP_VERSION_CODE, 0);
        isLogin = (boolean) LSSpUtil.get(SPConstants.SP_IS_LOGIN, false);

        if (spVersionCode < appVersionCode) {  // 新安装APP  /  覆盖安装
            isUpdateAppVersionCode = true;
            if (mustReLogin) {//覆盖安装时，如果新版要求重新登录，先将登录状态置否
                LSSpUtil.put(SPConstants.SP_IS_LOGIN, false);
                isLogin = false;
            }
            if ((spVersionCode == 0) || hasNewGuide) {
                toWelcomeActivity();     // 为 0 ，即新安装的 APP ，直接 去欢迎界面
            } else {
                toLoginOrMainActivity(mustReLogin, 1000);
            }
        } else {     // 正常启动

            toLoginOrMainActivity(false, 0);

        }
    }


    /**
     * 去登录还是 主页
     *
     * @param mustLogin 是否强制登录
     * @param loadTime  延时时间，初始化需要时间
     */
    private void toLoginOrMainActivity(boolean mustLogin, int loadTime) {
        if (mustLogin || !isLogin) {
            // 必须重新登录 或者  没登录过
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else {
            // 跳转到 主页
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
//                    Routers.open(SplashActivity.this, "wkapp://MainActivity");
                    finish();
                }
            }, loadTime);
        }
    }

    //去欢迎界面 ， 延时操作是为了 让APP初始化，也可以不延时
    private void toWelcomeActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isUpdateAppVersionCode) {// 只在 新安装APP 和 覆盖安装APP 时，写入 1 次 版本号
            LSSpUtil.put(SPConstants.SP_VERSION_CODE, appVersionCode);
        }
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sp_jump_btn) {
            toLoginOrMainActivity(false, 0);
        }
    }


}
