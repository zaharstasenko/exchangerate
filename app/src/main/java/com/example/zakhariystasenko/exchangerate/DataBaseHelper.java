package com.example.zakhariystasenko.exchangerate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class DataBaseHelper extends SQLiteOpenHelper implements DataWriterAsyncTask.DataWriterCallback<Currency> {
    private static final String DATABASE_NAME = "CurrencyDatabase";
    static final String TABLE_CURRENCY = "TableCurrency";

    private static final String KEY_NAME = "txt";
    private static final String KEY_CUR_ID = "cc";
    private static final String KEY_EXCHANGE_DATE = "exchangedate";
    private static final String KEY_RATE = "rate";

    private DataWriterAsyncTask<Currency> mDataWriterAsyncTask = null;
    private ArrayList<ArrayList<Currency>> mDataForWriting = new ArrayList<>();

    private static DatabaseCallback mDatabaseCallback;

    DataBaseHelper(Context context) {
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

    ArrayList<Currency> getCurrentRateFromDataBase() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DataBaseHelper.TABLE_CURRENCY, null, null, null, null, null, null);

        String currentDate = DateManager.getDateDD_MM_YYYY();
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

    Map<String, Double> getDataForPeriod(String currencyId) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DataBaseHelper.TABLE_CURRENCY, null, null, null, null, null, null);

        ArrayList<String> requiredDates = DateManager.getLast30DatesDD_MM_YYYY();
        Map<String, Double> res = new HashMap<>();

        if (cursor.moveToFirst()) {
            int dateIndex = cursor.getColumnIndex(DataBaseHelper.KEY_EXCHANGE_DATE);
            int idIndex = cursor.getColumnIndex(DataBaseHelper.KEY_CUR_ID);
            int rateIndex = cursor.getColumnIndex(DataBaseHelper.KEY_RATE);

            do {
                String date = cursor.getString(dateIndex);
                String id = cursor.getString(idIndex);

                if (requiredDates.contains(date) && currencyId.equals(id)) {
                    res.put(date, cursor.getDouble(rateIndex));
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        return res;
    }

    void requestWriteToDatabase(ArrayList<Currency> data) {
        mDataForWriting.add(data);

        if (mDataWriterAsyncTask == null) {
            mDataWriterAsyncTask = new DataWriterAsyncTask<>(this, this);
            mDataWriterAsyncTask.execute(mDataForWriting.get(0));
            mDataForWriting.remove(0);
        }
    }

    @Override
    public void onCurrentWriteFinished() {
        if (mDataForWriting.size() > 0) {
            mDataWriterAsyncTask = new DataWriterAsyncTask<>(this, this);
            mDataWriterAsyncTask.execute(mDataForWriting.get(0));
            mDataForWriting.remove(0);
        } else {
            mDataWriterAsyncTask = null;

            if (mDatabaseCallback != null) {
                mDatabaseCallback.onAllWriteFinished();
            }
        }
    }

    @Override
    public ContentValues getContent(Currency data) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBaseHelper.KEY_CUR_ID, data.getCurrencyId());
        contentValues.put(DataBaseHelper.KEY_RATE, data.getCurrencyRate());
        contentValues.put(DataBaseHelper.KEY_NAME, data.getCurrencyName());
        contentValues.put(DataBaseHelper.KEY_EXCHANGE_DATE, data.getExchangeDate());

        return contentValues;
    }

    interface DatabaseCallback {
        void onAllWriteFinished();
    }

    void setCallback(DatabaseCallback callback) {
        mDatabaseCallback = callback;
    }


}
