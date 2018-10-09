package aacom.wangke.wkappw.testmvp;


/**
 * Mvp  Presenter 实现类，负责把 model 的 数据 传给 view
 */
public class TestPresenterImpl extends TestContract.TestPresenter {

    @Override
    public void requestSD() {
        model.getDataFromSD(new TestContract.Callback() {
            @Override
            public void onResult(String text) {
                view.updateUISD(text);

            }
        });
    }

    @Override
    public   void requestNet() {
        model.getDataFromNet(new TestContract.Callback() {
            @Override
            public void onResult(String text) {
                view.updateUINet(text);
            }
        });
    }


    @Override
    public TestContract.TestModel createModel() {
        return new TestModelImpl();
    }
}
