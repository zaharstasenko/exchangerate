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

class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CurrencyDatabase";
    private static final String TABLE_CURRENCY = "TableCurrency";

    private static final String KEY_NAME = "txt";
    private static final String KEY_CUR_ID = "cc";
    private static final String KEY_EXCHANGE_DATE = "exchangedate";
    private static final String KEY_RATE = "rate";

    DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("Check","Database on create");
        sqLiteDatabase.execSQL("create table " + TABLE_CURRENCY + "(" + KEY_NAME + " text, " +
                KEY_CUR_ID + " text, " + KEY_EXCHANGE_DATE + " text, " + KEY_RATE + " real" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    void writeToDatabase(ArrayList<Currency> data) {
        SQLiteDatabase database = this.getWritableDatabase();

        for (Currency currency : data) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataBaseHelper.KEY_CUR_ID, currency.getCurrencyId());
            contentValues.put(DataBaseHelper.KEY_RATE, currency.getCurrencyRate());
            contentValues.put(DataBaseHelper.KEY_NAME, currency.getCurrencyName());
            contentValues.put(DataBaseHelper.KEY_EXCHANGE_DATE,currency.getExchangeDate());

            database.insert(DataBaseHelper.TABLE_CURRENCY, null, contentValues);
        }

        this.close();
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

    Map<String,Double> getDataForPeriod(String currencyId){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DataBaseHelper.TABLE_CURRENCY, null, null, null, null, null, null);

        ArrayList<String> requiredDates = DateManager.getLast30DatesDD_MM_YYYY();
        Map<String,Double> res = new HashMap<>();

        if (cursor.moveToFirst()) {
            int dateIndex = cursor.getColumnIndex(DataBaseHelper.KEY_EXCHANGE_DATE);
            int idIndex = cursor.getColumnIndex(DataBaseHelper.KEY_CUR_ID);
            int rateIndex = cursor.getColumnIndex(DataBaseHelper.KEY_RATE);

            do {
                String date = cursor.getString(dateIndex);
                String id = cursor.getString(idIndex);

                if (requiredDates.contains(date) && currencyId.equals(id)){
                    res.put(date,cursor.getDouble(rateIndex));
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        return res;
    }
}
