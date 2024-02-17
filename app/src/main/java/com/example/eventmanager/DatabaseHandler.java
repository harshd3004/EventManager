package com.example.eventmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EventManagerDB";
    private static final String TABLE_EVENTS = "events";

    private static final String KEY_ID = "id";
    private static final String KEY_EVENT_NAME = "event_name";
    private static final String KEY_EVENT = "event";
    private static final String KEY_DATE = "event_date";
    private static final String KEY_LOCATION = "event_location";
    private static final String KEY_DESCRIPTION = "event_description";

    // SQL query with _id as an alias for the primary key column
    private static final String SELECT_ALL_EVENTS = "SELECT " +
            KEY_ID + " AS _id, " +
            KEY_EVENT_NAME + ", " +
            KEY_EVENT + ", " +
            KEY_DATE + " " +
            "FROM " + TABLE_EVENTS;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EVENT_NAME + " TEXT,"
                + KEY_EVENT + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public void addEvent(EventData event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, event.getEventName());
        values.put(KEY_EVENT, event.getEvent());
        values.put(KEY_DATE, event.getEventDate());
        values.put(KEY_LOCATION, event.getEventLocation());
        values.put(KEY_DESCRIPTION, event.getEventDescription());

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }




    // Fetch all events cursor
    public Cursor getAllEventsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(SELECT_ALL_EVENTS, null);
    }


    // Getter methods for accessing private keys
    public String getEventNameKey() {
        return KEY_EVENT_NAME;
    }

    public String getEventKey() {
        return KEY_EVENT;
    }

    public String getDateKey() {
        return KEY_DATE;
    }


}

