package com.example.zakhariystasenko.exchangerate.data_management;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.zakhariystasenko.exchangerate.data_management.data_models.Currency;
import com.example.zakhariystasenko.exchangerate.data_management.data_models.DailyExchangeRate;
import com.example.zakhariystasenko.exchangerate.utils.DatabaseMissingDataException;
import com.example.zakhariystasenko.exchangerate.utils.MyDate;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class DataBaseHelper extends SQLiteOpenHelper implements IDatabase {
    private static final String DATABASE_NAME = "CurrencyDatabase";
    private static final String TABLE_CURRENCY = "TableCurrency";

    private static final String KEY_NAME = "txt";
    private static final String KEY_CUR_ID = "cc";
    private static final String KEY_EXCHANGE_DATE = "exchangedate";
    private static final String KEY_RATE = "rate";

    private static ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_CURRENCY + "(" + KEY_NAME + " text, " +
                KEY_CUR_ID + " text, " + KEY_EXCHANGE_DATE + " text, " + KEY_RATE + " real" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    private ArrayList<Currency> searchByDate(MyDate date) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DataBaseHelper.TABLE_CURRENCY, null, null, null, null, null, null);

        String currentDate = date.getDateForDatabase();
        ArrayList<Currency> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NAME);
            int rateIndex = cursor.getColumnIndex(DataBaseHelper.KEY_RATE);
            int currencyIdIndex = cursor.getColumnIndex(DataBaseHelper.KEY_CUR_ID);

            do {
                int dateIndex = cursor.getColumnIndex(DataBaseHelper.KEY_EXCHANGE_DATE);

                if (cursor.getString(dateIndex).equals(currentDate)) {
                    Currency currency = new Currency();
                    currency.setCurrencyName(cursor.getString(nameIndex));
                    currency.setCurrencyId(cursor.getString(currencyIdIndex));
                    currency.setCurrencyRate(cursor.getDouble(rateIndex));

                    data.add(currency);
                }
            } while (cursor.moveToNext());

            cursor.close();

            if (data.size() == 0) {
                return null;
            }

        } else {
            cursor.close();
            return null;
        }

        return data;
    }

    @Override
    public Single<DailyExchangeRate> exchangeRateForDate(final MyDate date) {
        return Single.create(new SingleOnSubscribe<DailyExchangeRate>() {
            @Override
            public void subscribe(final SingleEmitter<DailyExchangeRate> e) throws Exception {
                mExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Currency> currencies = searchByDate(date);

                        if (currencies != null) {
                            e.onSuccess(new DailyExchangeRate(date, currencies));
                        } else {
                            Log.d("Check", "Need download");
                            e.onError(new DatabaseMissingDataException());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void writeExchangeRateForDate(final MyDate date, final DailyExchangeRate model) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase database = getWritableDatabase();

                for (Currency item : model.getCurrencies()) {
                    database.insert(DataBaseHelper.TABLE_CURRENCY, null, getContent(item));
                }

                close();
            }
        });
    }

    private ContentValues getContent(Currency data) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBaseHelper.KEY_CUR_ID, data.getCurrencyId());
        contentValues.put(DataBaseHelper.KEY_RATE, data.getCurrencyRate());
        contentValues.put(DataBaseHelper.KEY_NAME, data.getCurrencyName());
        contentValues.put(DataBaseHelper.KEY_EXCHANGE_DATE, data.getExchangeDate());

        return contentValues;
    }
}
