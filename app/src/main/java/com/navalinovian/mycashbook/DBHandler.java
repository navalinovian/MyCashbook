package com.navalinovian.mycashbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "mycashbook";

    // below int is our database version
    private static final int DB_VERSION = 2;

    private static final String TABLE_USER = "user";
    private static final String USER_ID_COL = "person_id";
    private static final String USERNAME_COL = "username";
    private static final String PASSWORD_COL = "password";

    private static final String TABLE_REPORT = "report";
    private static final String REPORT_ID_COL = "report_id";
    private static final String REPORT_CODE_COL = "report_code";
    private static final String AMOUNT_COL = "amount";
    private static final String REPORT_DATE_COL = "report_date";
    private static final String DESCRIPTION_COL = "description";
    private static final String UID_REPORT_COL = "uid_fk";


    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_table_user = "CREATE TABLE " + TABLE_USER + " ("
                + USER_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USERNAME_COL + " TEXT NOT NULL,"
                + PASSWORD_COL + " TEXT NOT NULL)";

        String create_table_report = "CREATE TABLE " + TABLE_REPORT + " ("
                + REPORT_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + REPORT_CODE_COL + " INTEGER NOT NULL,"
                + AMOUNT_COL + " TEXT NOT NULL,"
                + REPORT_DATE_COL + " TEXT NOT NULL,"
                + DESCRIPTION_COL + " TEXT NOT NULL,"
                + UID_REPORT_COL + " INTEGER, "
                + "FOREIGN KEY ("+UID_REPORT_COL+") REFERENCES "+TABLE_USER+"("+ USER_ID_COL +"));";

        db.execSQL(create_table_user);
        db.execSQL(create_table_report);

        ContentValues values = new ContentValues();
        values.put(USERNAME_COL, "user");
        values.put(PASSWORD_COL, "user");
        db.insert(TABLE_USER, null, values);
    }

    public void addNewUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USERNAME_COL, username);
        values.put(PASSWORD_COL, password);

        db.insert(TABLE_USER, null, values);

        db.close();
    }

    public void addReport(Integer report_code, String amount, String report_date, String description, Integer uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(REPORT_CODE_COL, report_code);
        values.put(AMOUNT_COL, amount);
        values.put(REPORT_DATE_COL, report_date);
        values.put(DESCRIPTION_COL, description);
        values.put(UID_REPORT_COL, uid);

        db.insert(TABLE_REPORT, null, values);
        db.close();


    }

    public ArrayList<Cashflow> getAllCashflow(Integer uid){
        String userid = String.valueOf(uid);
        Log.i("uid adalah", userid);
        ArrayList<Cashflow> results = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.query(TABLE_REPORT, new String[]
                            { REPORT_ID_COL, REPORT_CODE_COL , AMOUNT_COL, REPORT_DATE_COL, DESCRIPTION_COL, UID_REPORT_COL },
                    UID_REPORT_COL + "=?", new String[] { userid }, null, null, REPORT_DATE_COL);

            try{
                if (!cursor.moveToFirst()){
                    System.out.println("gagal");
                    return null;
                }
                do {
                    results.add(new Cashflow(cursor.getInt(cursor.getColumnIndex("report_code")),
                            cursor.getString(cursor.getColumnIndex("amount")),
                            cursor.getString(cursor.getColumnIndex("report_date")),
                            cursor.getString(cursor.getColumnIndex("description"))));
                }while (cursor.moveToNext());
            }finally {
                cursor.close();
            }
        } finally {
            db.close();
        }
        return results;
    }


    public boolean authenticate(String username, String password){
        boolean auth=false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]
                        { USER_ID_COL, USERNAME_COL , PASSWORD_COL },
                USERNAME_COL + "=?", new String[] { username }, null, null, null);
        if( cursor.moveToFirst() && cursor.getCount() > 0)
        {
            Log.i("info",cursor.getString(cursor.getColumnIndex("password")));
            if (cursor.getString(cursor.getColumnIndex("password")).equals(password)){
                auth = true;
            }else{
                auth = false;
            }
        }else{
            auth = false;
        }
        cursor.close();
        db.close();
        return auth;
    }

    public boolean authenticate(Integer person_id, String password){
        boolean auth;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ USERNAME_COL+", "+PASSWORD_COL+" FROM " + TABLE_USER
                + " WHERE "+ USER_ID_COL +" = ?", new String[] {person_id+""});
        if( cursor.moveToFirst() && cursor.getCount() > 0)
        {
            Log.i("info",cursor.getString(cursor.getColumnIndex("password")));
            if (cursor.getString(cursor.getColumnIndex("password")).equals(password)){
                auth = true;
            }else{
                auth = false;
            }
        }else{
            auth = false;
        }
        cursor.close();
        db.close();
        return auth;
    }
    public int getUserId(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]
                        { USER_ID_COL },
                USERNAME_COL + "=?", new String[] { username }, null, null, null);
        if( cursor.moveToFirst() && cursor.getCount() > 0) {
            db.close();
            return cursor.getInt(cursor.getColumnIndex("person_id"));
        }else{
            db.close();
            return -1;
        }
    }

    public String getSumAmount(Integer uid, Integer report_code){
        String total="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + AMOUNT_COL + ") AS Total"
                +" FROM " + TABLE_REPORT + " WHERE "+ UID_REPORT_COL +"" +
                " = "+ uid +" AND "+ REPORT_CODE_COL +" = "+ report_code , null);
        if (cursor.moveToFirst()){
            total = cursor.getString(cursor.getColumnIndex("Total"));
        }
        db.close();
        return total;
    }

    public void changePassword(Integer person_id, String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PASSWORD_COL, newPassword);

        db.update(TABLE_USER, values, "person_id =?", new String[]{person_id.toString()} );

        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT);
        onCreate(db);
    }
}
