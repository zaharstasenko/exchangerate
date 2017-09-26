package com.example.zakhariystasenko.exchangerate;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;

class DataWriterAsyncTask<T> extends AsyncTask<ArrayList<T>, Void, Void> {
    private DataWriterCallback mCallback;
    private DataBaseHelper mDatabaseHelper;

    DataWriterAsyncTask(DataBaseHelper databaseHelper, DataWriterCallback callback){
        mCallback = callback;
        mDatabaseHelper = databaseHelper;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mCallback.onCurrentWriteFinished();
    }

    @Override
    protected Void doInBackground(ArrayList<T>... data) {
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        for (T item : data[0]) {
            database.insert(DataBaseHelper.TABLE_CURRENCY, null, mCallback.getContent(item));
        }

        mDatabaseHelper.close();
        return null;
    }

    interface DataWriterCallback<T> {
        void onCurrentWriteFinished();
        ContentValues getContent(T data);
    }

}
