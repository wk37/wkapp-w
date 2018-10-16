package aacom.wangke.wkappw.login;

import android.content.Intent;
import android.os.Bundle;

import com.wangke.core.basemvp.BaseMVPActivity;

import aacom.wangke.wkappw.main.MainActivity;
import aacom.wangke.wkappw.testmvp.TestContract;
import aacom.wangke.wkappw.testmvp.TestPresenterImpl;

public class LoginActivity extends BaseMVPActivity<TestContract.TestPresenter> implements TestContract.TestView {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    @Override
    public TestContract.TestPresenter createPresenter() {
        return new TestPresenterImpl();
    }

    @Override
    public void updateUISD(String text) {

    }

    @Override
    public void updateUINet(String text) {


    }
}
