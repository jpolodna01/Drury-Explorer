package edu.drury.mcs.icarus.druryexplorer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Chick on 10/26/2014.
 */
public class DataManager extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "hallManager",
        TABLE_HALL = "Hall",
        KEY_ID ="id",
        KEY_NAME = "name",
        KEY_YEAR = "year",
        KEY_HISTORY = "history";

    public DataManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE" + TABLE_HALL + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_YEAR + " TEXT," + KEY_HISTORY + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HALL);

        onCreate(db);
    }

    public void createHall(Halls hall)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, hall.getName());
        values.put(KEY_YEAR, hall.getYear());
        values.put(KEY_HISTORY, hall.getHistory());

        db.insert(TABLE_HALL, null, values);
        db.close();
    }

    public Halls getHall(int id)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_HALL, new String[] { KEY_ID, KEY_NAME, KEY_YEAR, KEY_HISTORY }, KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null);

        if(cursor !=null)
            cursor.moveToFirst();

        Halls hall = new Halls(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return hall;
    }

    public void deleteHall(Halls hall)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_HALL, KEY_ID + "=?", new String[] {String.valueOf(hall.getID())});
        db.close();
    }

    public int getHallCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HALL, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateHall(Halls hall)
    {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, hall.getName());
        values.put(KEY_YEAR, hall.getYear());
        values.put(KEY_HISTORY, hall.getHistory());

        return db.update(TABLE_HALL, values, KEY_ID + "=?", new String[] { String.valueOf(hall.getID()) });
    }

    public List<Halls> getAllHalls()
    {
        List<Halls> halls = new ArrayList<Halls>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM" + TABLE_HALL, null);

        if(cursor.moveToFirst())
        {
            do{
                Halls hall = new Halls(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));

                halls.add(hall);
            }

            while(cursor.moveToNext());
        }
        return halls;
    }
}
