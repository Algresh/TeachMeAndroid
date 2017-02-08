package ru.tulupov.alex.teachme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.tulupov.alex.teachme.models.City;
import ru.tulupov.alex.teachme.models.PriceList;
import ru.tulupov.alex.teachme.models.Subject;
import ru.tulupov.alex.teachme.models.Teacher;

import static ru.tulupov.alex.teachme.database.DataBaseHelper.*;


public class DataBaseManager {

    private SQLiteDatabase sqLiteDatabase;

    public DataBaseManager(Context context) {
        DataBaseHelper mDatabaseHelper = new DataBaseHelper(context);
        sqLiteDatabase = mDatabaseHelper.getReadableDatabase();

    }

    public void closeDatabase() {
        sqLiteDatabase.close();
    }

    public boolean teacherIsFavorite(int id) {
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE_TEACHER, null,
                TEACHER_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        int count = cursor.getCount();

        return count != 0;
    }

    public void deleteFromFavorite (int id) {
        sqLiteDatabase.beginTransaction();

        sqLiteDatabase.delete(DATABASE_TABLE_TEACHER, TEACHER_ID + "=?",
                new String[]{String.valueOf(id)});

        sqLiteDatabase.delete(DATABASE_TABLE_PRICE_LIST, PRICE_LIST_TEACHER_ID + "=?",
                new String[]{String.valueOf(id)});

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void saveFavoriteTeacherToDB(Teacher teacher) {
        ContentValues cv = new ContentValues();

        cv.put(TEACHER_ID, teacher.getId());
        cv.put(TEACHER_FIRST_NAME, teacher.getFirstName());
        cv.put(TEACHER_LAST_NAME, teacher.getLastName());
        cv.put(TEACHER_FATHER_NAME, teacher.getFatherName());
        cv.put(TEACHER_PHOTO, teacher.getPhoto());
        cv.put(TEACHER_PHONE, teacher.getPhoneNumber());
        cv.put(TEACHER_BIRTH_DATE, teacher.getBirthDate());
        cv.put(TEACHER_OKRUG, teacher.getOkrug());
        cv.put(TEACHER_DISTRICT, teacher.getDistrict());
        cv.put(TEACHER_DESCRIPTION, teacher.getDescription());
        cv.put(TEACHER_LEAVE_HOUSE, teacher.isLeaveHome());
        cv.put(TEACHER_SUBWAYS, teacher.getSubways());
        cv.put(TEACHER_EMAIL, teacher.getEmail());
        cv.put(TEACHER_CITY_TITLE, teacher.getCity().getTitle());
        cv.put(TEACHER_CITY_HAS_SUBWAY, teacher.getCity().isHasSubway());
        cv.put(TEACHER_CITY_ID, teacher.getCity().getId());

        long idTeacher = sqLiteDatabase.insert(DATABASE_TABLE_TEACHER, null, cv);
        cv.clear();

        for (PriceList priceList : teacher.getPriceLists()) {
            cv.put(PRICE_LIST_ID, priceList.getId());
            cv.put(PRICE_LIST_SUBJECT_TITLE, priceList.getSubject().getTitle());
            cv.put(PRICE_LIST_EXPERIENCE, priceList.getExperience());
            cv.put(PRICE_LIST_PRICE, priceList.getPrice());
            cv.put(PRICE_LIST_TEACHER_ID, idTeacher);
            sqLiteDatabase.insert(DATABASE_TABLE_PRICE_LIST, null, cv);

            cv.clear();
        }
    }

    public List<Teacher> getFavoriteTeachers () {

        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE_TEACHER, null, null, null, null, null, null);

        List<Teacher> teacherList = new ArrayList<>();
        if (cursor.moveToFirst()) {

            do {
                int[] arrInt = new int[4];
                String[] arrStr = new String[12];

                arrInt[0] = cursor.getInt(cursor.getColumnIndex(TEACHER_ID));
                arrStr[0] = cursor.getString(cursor.getColumnIndex(TEACHER_FIRST_NAME));
                arrStr[1] = cursor.getString(cursor.getColumnIndex(TEACHER_LAST_NAME));
                arrStr[2] = cursor.getString(cursor.getColumnIndex(TEACHER_FATHER_NAME));
                arrStr[3] = cursor.getString(cursor.getColumnIndex(TEACHER_PHOTO));
                arrStr[4] = cursor.getString(cursor.getColumnIndex(TEACHER_PHONE));
                arrStr[5]= cursor.getString(cursor.getColumnIndex(TEACHER_BIRTH_DATE));
                arrStr[6] = cursor.getString(cursor.getColumnIndex(TEACHER_OKRUG));
                arrStr[7] = cursor.getString(cursor.getColumnIndex(TEACHER_DISTRICT));
                arrStr[8] = cursor.getString(cursor.getColumnIndex(TEACHER_DESCRIPTION));
                arrInt[1] = cursor.getInt(cursor.getColumnIndex(TEACHER_LEAVE_HOUSE));
                arrStr[9] = cursor.getString(cursor.getColumnIndex(TEACHER_SUBWAYS));
                arrStr[10] = cursor.getString(cursor.getColumnIndex(TEACHER_EMAIL));
                arrStr[11] = cursor.getString(cursor.getColumnIndex(TEACHER_CITY_TITLE));
                arrInt[2] = cursor.getInt(cursor.getColumnIndex(TEACHER_CITY_HAS_SUBWAY));
                arrInt[3] = cursor.getInt(cursor.getColumnIndex(TEACHER_CITY_ID));

                Cursor cursorPL = sqLiteDatabase.query(DATABASE_TABLE_PRICE_LIST, null, PRICE_LIST_TEACHER_ID + "=?",
                        new String[]{String.valueOf(arrInt[0])}, null, null, null);

                List<PriceList> priceLists = new ArrayList<>();
                if (cursorPL.moveToFirst()) {
                    do {
                        int id = cursorPL.getInt(cursorPL.getColumnIndex(PRICE_LIST_ID));
                        int exp = cursorPL.getInt(cursorPL.getColumnIndex(PRICE_LIST_EXPERIENCE));
                        int price = cursorPL.getInt(cursorPL.getColumnIndex(PRICE_LIST_PRICE));
                        String sbj = cursorPL.getString(cursorPL.getColumnIndex(PRICE_LIST_SUBJECT_TITLE));
                        priceLists.add(wrapPriceList(id, exp, price, sbj));
                    } while (cursorPL.moveToNext());
                }

                teacherList.add(wrapTeacher(arrInt, arrStr, priceLists));
            } while (cursor.moveToNext());
        }

        return teacherList;
    }

    protected PriceList wrapPriceList (int id, int exp, int price, String sbj) {
        PriceList priceList = new PriceList();

        priceList.setId(id);
        priceList.setExperience(String.valueOf(exp));
        priceList.setPrice(price);
        priceList.setSubject(new Subject(sbj));

        return priceList;
    }


    protected Teacher wrapTeacher(int[] arrInt, String[] arr, List<PriceList> list) {

        Teacher teacher = new Teacher();

        teacher.setId(arrInt[0]);
        teacher.setFirstName(arr[0]);
        teacher.setLastName(arr[1]);
        teacher.setFatherName(arr[2]);
        teacher.setPhoto(arr[3]);
        teacher.setPhoneNumber(arr[4]);
        teacher.setBirthDate(arr[5]);
        teacher.setOkrug(arr[6]);
        teacher.setDistrict(arr[7]);
        teacher.setDescription(arr[8]);
        teacher.setLeaveHome(arrInt[1] != 0);
        teacher.setSubways(arr[9]);
        teacher.setEmail(arr[10]);
        teacher.setCity(new City(arrInt[2], arr[11], arrInt[3] != 0));
        teacher.setPriceLists(list);
        teacher.setFavorite(true);

        return teacher;
    }


}
