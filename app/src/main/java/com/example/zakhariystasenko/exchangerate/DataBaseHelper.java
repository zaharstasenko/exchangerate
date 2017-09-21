package com.example.zakhariystasenko.exchangerate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

class DataBaseHelper extends SQLiteOpenHelper {
    private static final String KEY_ID = "_id";
    private static final String DATABASE_NAME = "MyCurrencyDatabase";
    private static final String TABLE_CURRENCY = "MyTableCurrency";

    private static final String KEY_NAME = "txt";
    private static final String KEY_CUR_ID = "cc";
    private static final String KEY_EXCHANGE_DATE = "exchangedate";
    private static final String KEY_R = "r030";
    private static final String KEY_RATE = "rate";

    DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_CURRENCY + "(" + KEY_ID
                + " text, " + KEY_NAME + " text, " + KEY_CUR_ID + " text, "
                + KEY_EXCHANGE_DATE + " text, " + KEY_R + " text, " + KEY_RATE + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    void writeToDatabase(ArrayList<Map<String, Object>> data) {
        SQLiteDatabase database = this.getWritableDatabase();

        int index = 0;

        for (Map<String, Object> map : data) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseHelper.KEY_ID, index + "");

            for (String key : map.keySet()) {
                contentValues.put(key, map.get(key).toString());
            }

            database.insert(DataBaseHelper.TABLE_CURRENCY, null, contentValues);
        }

        this.close();
    }

    ArrayList<Currency> getCurrentRateFromDataBase() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DataBaseHelper.TABLE_CURRENCY, null, null, null, null, null, null);

        String currentDate = DateConverter.getDateDD_MM_YYYY();


        ArrayList<Currency> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NAME);
            int rateIndex = cursor.getColumnIndex(DataBaseHelper.KEY_RATE);
            int currencyIdIndex = cursor.getColumnIndex(DataBaseHelper.KEY_CUR_ID);

            do {
                int dateIndex = cursor.getColumnIndex(DataBaseHelper.KEY_EXCHANGE_DATE);
                Log.d("Database...",cursor.getString(dateIndex));

                if (cursor.getString(dateIndex).equals(currentDate)) {
                    Currency currency = new Currency();

                    currency.setCurrencyName(cursor.getString(nameIndex));
                    currency.setCurrencyId(cursor.getString(currencyIdIndex));
                    currency.setCurrencyRate(Double.parseDouble(cursor.getString(rateIndex)));

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

    private ArrayList<String> getMissingDates(ArrayList<String> requiredDates){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DataBaseHelper.TABLE_CURRENCY, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_EXCHANGE_DATE));

                if (requiredDates.contains(date)){
                    requiredDates.remove(date);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        return requiredDates;
    }
}
