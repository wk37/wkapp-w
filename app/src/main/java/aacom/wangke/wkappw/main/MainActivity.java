package aacom.wangke.wkappw.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wangke.core.basemvp.BaseMVPActivity;
import com.wangke.core.retrofit.BaseRetrofit;

import aacom.wangke.wkappw.R;
import aacom.wangke.wkappw.http.UrlConstants;
import aacom.wangke.wkappw.testmvp.TestContract;
import aacom.wangke.wkappw.testmvp.TestPresenterImpl;

public class MainActivity extends BaseMVPActivity<TestContract.TestPresenter>implements TestContract.TestView {

    private Button mBtnSd;
    private Button mBtnNet;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        mBtnSd = findViewById(R.id.btn_sd);
        mBtnNet = findViewById(R.id.btn_net);
        mBtnSd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BaseRetrofit.resetBaseUrl(UrlConstants.DEV_ENV);

                presenter.requestSD();
            }
        });
        mBtnNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseRetrofit.resetBaseUrl(UrlConstants.PRE_PUBLISH_ENV);

                presenter.requestNet();

            }
        });

    }

    @Override
    public TestContract.TestPresenter createPresenter() {
        return new TestPresenterImpl();
    }

    @Override
    public void updateUISD(String text) {
        textView.setText(text);
    }

    @Override
    public void updateUINet(String text) {
        textView.setText(text);

    }
}
