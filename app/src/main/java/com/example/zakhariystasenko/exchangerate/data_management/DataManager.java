package com.example.zakhariystasenko.exchangerate.data_management;

import com.example.zakhariystasenko.exchangerate.graph.GraphViewActivity;
import com.example.zakhariystasenko.exchangerate.rate.Currency;
import com.example.zakhariystasenko.exchangerate.rate.RateViewActivity;
import com.example.zakhariystasenko.exchangerate.utils.DateManager;
import com.example.zakhariystasenko.exchangerate.utils.SimpleObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DataManager implements DataBaseHelper.DatabaseCallback {
    private Map<String, Double> mDataForPeriod = new HashMap<>();
    private String mCurrencyRequested;
    private final int PERIOD = 30;

    private DayRequestCallback mDayRequestCallback;
    private PeriodRequestCallback mPeriodRequestCallback;

    private DataBaseHelper mDataBaseHelper;
    private ExchangeRateDownloader mDownloader;
    private Disposable mApiCall;

    private Map<String,Boolean> mRunningActivities = initActivityMap();
    private static boolean mDataIsWriting = false;

    public DataManager(DataBaseHelper dataBaseHelper, ExchangeRateDownloader downloader) {
        mDataBaseHelper = dataBaseHelper;
        mDownloader = downloader;
    }

    private Map<String,Boolean> initActivityMap(){
        Map<String,Boolean> res = new HashMap<>();

        res.put(RateViewActivity.class.getSimpleName(),false);
        res.put(GraphViewActivity.class.getSimpleName(),false);

        return res;
    }

    private void downloadExchangeRateData() {
        mDownloader.getCurrentData(DateManager.getDateYYYYMMDD())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createSingleDownloadObserver());
    }

    private void downloadExchangeRateData(List<String> dates) {
        Observable.fromIterable(dates)
                .flatMap(new Function<String, ObservableSource<ArrayList<Currency>>>() {
                    @Override
                    public ObservableSource<ArrayList<Currency>> apply(String s) throws Exception {
                        return mDownloader.getCurrentData(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createPeriodDownloadObserver());
    }

    private Observer<ArrayList<Currency>> createSingleDownloadObserver() {
        return new SimpleObserver<ArrayList<Currency>>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
                mApiCall = mDisposable;
            }

            @Override
            public void onNext(ArrayList<Currency> value) {
                mDayRequestCallback.getData(value);
                mDataBaseHelper.requestWriteToDatabase(value);

                mDisposable.dispose();
            }
        };
    }

    private Observer<ArrayList<Currency>> createPeriodDownloadObserver() {
        return new SimpleObserver<ArrayList<Currency>>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
                mApiCall = mDisposable;
            }

            @Override
            public void onNext(ArrayList<Currency> value) {
                mDataIsWriting = true;
                mDataBaseHelper.requestWriteToDatabase(value);

                for (Currency currency : value) {
                    if (currency.getCurrencyId().equals(mCurrencyRequested)) {
                        mDataForPeriod.put(currency.getExchangeDate(),
                                currency.getCurrencyRate());
                        break;
                    }
                }
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mPeriodRequestCallback.getData(parseData(mDataForPeriod));
                mDisposable.dispose();
            }
        };
    }

    public void requestDataForDay(DayRequestCallback callback){
        mDayRequestCallback = callback;

        ArrayList<Currency> data = mDataBaseHelper.getCurrentRateFromDataBase();

        if (data != null) {
            mDayRequestCallback.getData(data);
        } else {
            downloadExchangeRateData();
        }
    }

    public void requestDataForPeriod(String requestedCurrency, PeriodRequestCallback callback) {
        mCurrencyRequested = requestedCurrency;
        mPeriodRequestCallback = callback;

        mDataForPeriod.clear();
        mDataForPeriod = mDataBaseHelper.getDataForPeriod(requestedCurrency);

        if (mDataForPeriod.size() == PERIOD) {
            mPeriodRequestCallback.getData(parseData(mDataForPeriod));
        } else {
            if (!mDataIsWriting) {
                List<String> missingDates = DateManager.getMissingDates(mDataForPeriod.keySet());
                downloadExchangeRateData(missingDates);
            } else {
                mDataBaseHelper.setCallback(this);
            }
        }
    }

    public interface DayRequestCallback {
        void getData(ArrayList<Currency> data);
    }

    public interface PeriodRequestCallback {
        void getData(Map<Integer, Double> data);
    }

    @Override
    public void onAllWriteFinished() {
        mDataIsWriting = false;
        mPeriodRequestCallback.getData(
                parseData(mDataBaseHelper.getDataForPeriod(mCurrencyRequested)));
    }

    private Map<Integer, Double> parseData(Map<String, Double> data) {
        Map<Integer, Double> res = new TreeMap<>();

        for (String key : data.keySet()) {
            res.put(Integer.parseInt(DateManager.getDateYYYYMMDD(key)), data.get(key));
        }

        return res;
    }

    public void notifyActivityStateChange(String activityName, Boolean isRunning){
        mRunningActivities.remove(activityName);
        mRunningActivities.put(activityName,isRunning);
    }

    public void disposeApiCalls() {
        boolean needDispose = true;

        for (Boolean activityIsRunning : mRunningActivities.values()){
            if (activityIsRunning){
                needDispose = false;
            }
        }

        if (needDispose) {
            if (mApiCall != null) {
                mApiCall.dispose();
            }
        }
    }
}
