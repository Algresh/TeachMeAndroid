package ru.tulupov.alex.teachme.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "teachme.db";

    public static final String DATABASE_TABLE_TEACHER = "teacher";

    public static final String TEACHER_ID = "id";
    public static final String TEACHER_FIRST_NAME = "first_name";
    public static final String TEACHER_LAST_NAME= "last_name";
    public static final String TEACHER_FATHER_NAME = "father_name";
    public static final String TEACHER_PHOTO = "photo";
    public static final String TEACHER_PHONE = "phone";
    public static final String TEACHER_BIRTH_DATE = "birth_date";
    public static final String TEACHER_OKRUG = "okrug";
    public static final String TEACHER_DISTRICT = "district";
    public static final String TEACHER_DESCRIPTION = "description";
    public static final String TEACHER_LEAVE_HOUSE = "leave_house";
    public static final String TEACHER_SUBWAYS = "subways";
    public static final String TEACHER_EMAIL = "email";
    public static final String TEACHER_CITY_TITLE = "city_title";
    public static final String TEACHER_CITY_HAS_SUBWAY = "city_has_subway";
    public static final String TEACHER_CITY_ID = "city_id";

    private static final String DATABASE_CREATE_TABLE_TEACHER = "create table " + DATABASE_TABLE_TEACHER + "(" +
            TEACHER_ID  + " integer primary key autoincrement," +
            TEACHER_FIRST_NAME  + " text," +
            TEACHER_LAST_NAME  + " text," +
            TEACHER_FATHER_NAME  + " text," +
            TEACHER_PHOTO  + " text," +
            TEACHER_PHONE  + " text," +
            TEACHER_BIRTH_DATE  + " text," +
            TEACHER_OKRUG  + " text," +
            TEACHER_DISTRICT  + " text," +
            TEACHER_DESCRIPTION  + " text," +
            TEACHER_LEAVE_HOUSE  + " integer," +
            TEACHER_SUBWAYS  + " text," +
            TEACHER_EMAIL  + " text," +
            TEACHER_CITY_TITLE  + " text," +
            TEACHER_CITY_HAS_SUBWAY  + " integer," +
            TEACHER_CITY_ID  + " integer" +
            ");";


    public static final String DATABASE_TABLE_PRICE_LIST = "price_list";

    public static final String PRICE_LIST_ID = "id";
    public static final String PRICE_LIST_SUBJECT_TITLE = "subject_title";
    public static final String PRICE_LIST_EXPERIENCE = "experience";
    public static final String PRICE_LIST_PRICE = "price";
    public static final String PRICE_LIST_TEACHER_ID = "teacher_id";

    private static final String DATABASE_CREATE_TABLE_PRICE_LIST = "create table " + DATABASE_TABLE_PRICE_LIST + "(" +
            PRICE_LIST_ID  + " integer primary key autoincrement," +
            PRICE_LIST_SUBJECT_TITLE  + " text," +
            PRICE_LIST_EXPERIENCE  + " integer," +
            PRICE_LIST_PRICE  + " integer," +
            PRICE_LIST_TEACHER_ID  + " integer" +
            ");";



    public DataBaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_TABLE_TEACHER);
        sqLiteDatabase.execSQL(DATABASE_CREATE_TABLE_PRICE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_CREATE_TABLE_TEACHER);
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_CREATE_TABLE_PRICE_LIST);

        onCreate(sqLiteDatabase);
    }
}
