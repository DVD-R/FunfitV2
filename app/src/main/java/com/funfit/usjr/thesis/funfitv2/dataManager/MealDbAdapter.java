package com.funfit.usjr.thesis.funfitv2.dataManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.funfit.usjr.thesis.funfitv2.model.Meal;

import java.util.ArrayList;

/**
 * Created by victor on 2/2/2016.
 */
public class MealDbAdapter {

    private Context context;
    MealDbHelper mealDbHelper;
    private SQLiteDatabase sql;

    public MealDbAdapter(Context context){
        this.context = context;
        mealDbHelper = new MealDbHelper(context);
    }

    public MealDbAdapter openDb(){
        sql = mealDbHelper.getWritableDatabase();
        return this;
    }

    public void closeDb(){ mealDbHelper.close();}


    public long saveMeal(ArrayList<Meal> mealArrayList){

        ContentValues contentValues = null;
        for (Meal meal: mealArrayList){
            contentValues = new ContentValues();
            contentValues.put(mealDbHelper.mMEALID, meal.getMeal_id());
            contentValues.put(mealDbHelper.mNAME, meal.getmName());
            contentValues.put(mealDbHelper.mFAT, meal.getFat());
            contentValues.put(mealDbHelper.mCHOLESTEROL, meal.getCholesterol());
            contentValues.put(mealDbHelper.mSODIUM, meal.getSodium());
            contentValues.put(mealDbHelper.mCARBOHYDRATE, meal.getCarbohydrate());
            contentValues.put(mealDbHelper.mPROTEIN, meal.getProtein());
            contentValues.put(mealDbHelper.mCALORIES, meal.getCalories());
            contentValues.put(mealDbHelper.mTIME, meal.getmTime());
        }

        return sql.insert(mealDbHelper.mTABLE_NAME, null, contentValues);
    }


    static class MealDbHelper extends SQLiteOpenHelper{

        private static final String DATABASENAME = "MEAL_DB";
        private static final int DATABASE_VERSION = 1;
        private static final String mMEALID = "_id";
        private static final String mNAME = "mName";
        private static final String mFAT = "mFat";
        private static final String mCHOLESTEROL = "mCholesterol";
        private static final String mSODIUM = "mSodium";
        private static final String mCARBOHYDRATE = "mCarbohydrate";
        private static final String mCALORIES = "mCalories";
        private static final String mPROTEIN = "mProtein";
        private static final String mTABLE_NAME = "food";
        private static final String mTIME = "mTime";
        private static final String CREATE_TABLE_CLIENT = "CREATE TABLE " + mTABLE_NAME + "(" +mMEALID+
                " INTEGER(12) NOT NULL, " +mFAT+ " VARCHAR(25) NOT NULL, " +mNAME+ " VARCHAR(25) NOT NULL, " +mCHOLESTEROL+ " VARCHAR(25) NOT NULL, " +mSODIUM+
                " VARCHAR(25) NOT NULL, " +mCARBOHYDRATE+ " VARCHAR(25) NOT NULL, " +mPROTEIN+ " VARCHAR(25) NOT NULL, " +mCALORIES+ " VARCHAR(25) NOT NULL, " +mTIME+ " VARCHAR(25) NOT NULL);";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " +mTABLE_NAME;

        public MealDbHelper(Context context){
            super(context, DATABASENAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                Log.i("Oncreate", "Database Created");
                db.execSQL(CREATE_TABLE_CLIENT);
            }catch (Exception e){
                Log.e("Meal Db onCreate DB", String.valueOf(e));
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Log.i("OnUpgrage", "Database Updated");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e){
                Log.e("Meal Db onUpgrade Error", String.valueOf(e));
            }
        }
    }
}