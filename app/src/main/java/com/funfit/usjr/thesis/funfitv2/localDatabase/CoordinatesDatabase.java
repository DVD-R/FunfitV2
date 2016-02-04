package com.funfit.usjr.thesis.funfitv2.localDatabase;
import com.google.android.gms.maps.model.LatLng;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ocabafox on 2/2/2016.
 */
public class CoordinatesDatabase {

    CoordinatesHelper coordinatesHelper;
    public Context context;
    public ContentResolver cr;
    private SQLiteDatabase db;

    //constructor
    public CoordinatesDatabase(Context context){
        this.context = context;
    }

    //open DB
    public CoordinatesDatabase open(){
        db = coordinatesHelper.getWritableDatabase();
        return this;
    }

    //close DB
    public void close(){
        coordinatesHelper.close();
    }

    //addRoute
    public long addRoute(String coordList ){
        ContentValues contentValues = new ContentValues();
        contentValues.put(coordinatesHelper.COORDINATES, coordList);
        return db.insert(CoordinatesHelper.TABLE_NAME, null, contentValues);
    }



    static class CoordinatesHelper extends SQLiteOpenHelper{

        private static final String DATABASE_NAME = "FunFitDatabase";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_NAME = "coordinates";

        private static final String CID = "_id";
        private static final String COORDINATES = "coordinates";

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME+ "(" + CID + " INTEGER PRIMARY KEY," + COORDINATES
                + " VARCHAR(100) NOT NULL)";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
                + TABLE_NAME;

        Context context;
        public CoordinatesHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }
}
