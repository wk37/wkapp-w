package aacom.wangke.wkappw.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wangke.core.basemvp.BaseMVPActivity;
import com.wangke.core.basemvp.BasePresenter;
import com.wangke.core.basemvp.BaseView;

import aacom.wangke.wkappw.R;
import aacom.wangke.wkappw.testmvp.TestContract;
import aacom.wangke.wkappw.views.SimpleGuideBanner;

public class WelcomeActivity extends BaseMVPActivity<BasePresenter> implements BaseView {

    private SimpleGuideBanner mBanner;
    private TextView mTvJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_activity_welcome);

        initView();
        initData();

    }

    @Override
    public TestContract.TestPresenter createPresenter() {
        return null;
    }


    public void initView() {
        mBanner = (SimpleGuideBanner) findViewById(R.id.banner);
        mTvJump = (TextView) findViewById(R.id.tv_jump);
    }

    public void initData() {
        mBanner.addItemData(R.drawable.ic_launcher);
        mBanner.addItemData(R.drawable.ic_launcher);
        mBanner.addItemData(R.drawable.ic_launcher);
        mBanner.addItemData(R.drawable.ic_launcher);
        mBanner.showBanner();
        mBanner.setIndicatorHeight(100);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBanner.setCloseBanner(true);
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        mTvJump.setOnClickListener(listener);
        mBanner.setOutSideOnClickListener(listener);
    }

}
