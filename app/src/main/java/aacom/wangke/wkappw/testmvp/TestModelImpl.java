package aacom.wangke.wkappw.testmvp;



public class TestModelImpl implements TestContract.TestModel {


    @Override
    public void getDataFromSD(TestContract.Callback callback1) {
        callback1.onResult("getDataFromSD");
    }

    @Override
    public void getDataFromNet(TestContract.Callback callback2) {
        callback2.onResult("getDataFromNet");

    }
}
