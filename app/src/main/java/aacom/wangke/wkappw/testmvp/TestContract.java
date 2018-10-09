package aacom.wangke.wkappw.testmvp;

import com.wangke.core.basemvp.BaseModel;
import com.wangke.core.basemvp.BasePresenter;
import com.wangke.core.basemvp.BaseView;

/**
 * 契约类，一目了然 model、view、presetner 之间的 关系
 */

public interface TestContract {

    interface Callback {
        void onResult(String text);
    }


    interface TestModel extends BaseModel {
        void getDataFromSD(Callback callback1);

        void getDataFromNet(Callback callback2);
    }

    interface TestView extends BaseView {
        void updateUISD(String text);

        void updateUINet(String text);
    }

    abstract class TestPresenter extends BasePresenter<TestView, TestModel> {
        public abstract void requestSD();
        public abstract void requestNet();

    }


}
