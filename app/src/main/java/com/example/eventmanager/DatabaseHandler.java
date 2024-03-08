package com.example.eventmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "EventManagerDB";

    //Event Table
    private static final String TABLE_EVENTS = "events";
    private static final String KEY_ID = "id";
    private static final String KEY_EVENT_NAME = "event_name";
    private static final String KEY_EVENT = "event";
    private static final String KEY_DATE = "event_date";
    private static final String KEY_LOCATION = "event_location";
    private static final String KEY_DESCRIPTION = "event_description";
    private static final String KEY_GROUP_LINK = "grouplink";
    private static final String KEY_PARTICIPANTS = "participants";

    // Students Table
    private static final String TABLE_STUDENTS = "students";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NAME = "name";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_COLLEGE = "college";
    private static final String KEY_COURSE = "course";

    // SQL query with _id as an alias for the primary key column
    private static final String SELECT_ALL_EVENTS = "SELECT " +
            KEY_ID + " AS _id, " +
            KEY_EVENT_NAME + ", " +
            KEY_EVENT + ", " +
            KEY_DATE + ", " +
            KEY_LOCATION + ", " +
            KEY_DESCRIPTION + ", " +
            KEY_GROUP_LINK + ", " +
            KEY_PARTICIPANTS +
            " FROM " + TABLE_EVENTS;

    // SQL query with _id as an alias for the primary key column for the students table
    private static final String SELECT_ALL_STUDENTS = "SELECT " +
            KEY_ID + " AS _id, " +
            KEY_USER_ID + ", " +
            KEY_PASSWORD + ", " +
            KEY_NAME + ", " +
            KEY_CONTACT + ", " +
            KEY_COLLEGE + ", " +
            KEY_COURSE +
            " FROM " + TABLE_STUDENTS;

    private static final String SELECT_EVENT_BY_ID = "SELECT * FROM " + TABLE_EVENTS +
            " WHERE " + KEY_ID + " = ?";

    private static final String SELECT_STUDENT_BY_ID = "SELECT * FROM " + TABLE_STUDENTS +
            " WHERE " + KEY_ID + " = ?";

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
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_GROUP_LINK + " TEXT,"
                + KEY_PARTICIPANTS + " TEXT" + ")";
        db.execSQL(CREATE_EVENTS_TABLE);

        String CREATE_STUDENTS_TABLE = "CREATE TABLE " + TABLE_STUDENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_ID + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_CONTACT + " TEXT,"
                + KEY_COLLEGE + " TEXT,"
                + KEY_COURSE + " TEXT" + ")";
        db.execSQL(CREATE_STUDENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
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
        values.put(KEY_GROUP_LINK, event.getGroupLink());
        values.put(KEY_PARTICIPANTS, event.getParticipants());

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    // Method to get event details by eventId
    public EventData getEventDetails(int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        EventData eventData = null;

        String[] selectionArgs = {String.valueOf(eventId)};

        Cursor cursor = db.rawQuery(SELECT_EVENT_BY_ID, selectionArgs);

        if (cursor.moveToFirst()) {
            String eventName = cursor.getString(1);
            String event = cursor.getString(2);
            String date = cursor.getString(3);
            String location = cursor.getString(4);
            String description = cursor.getString(5);
            String groupLink = cursor.getString(6);
            String participants = cursor.getString(7);

            eventData = new EventData(eventName, event, date, location, description, groupLink, participants);

            cursor.close();
        }

        return eventData;
    }

    // Fetch all events cursor
    public Cursor getAllEventsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(SELECT_ALL_EVENTS, null);
    }

    public Cursor getAllUpcomingEventsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Get today date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);

        // Get upcoming events greater than 10 days from today
        String query = "SELECT " +
                KEY_ID + " AS _id, " +
                KEY_EVENT_NAME + ", " +
                KEY_EVENT + ", " +
                KEY_DATE + ", " +
                KEY_LOCATION + ", " +
                KEY_DESCRIPTION + ", " +
                KEY_GROUP_LINK + ", " +
                KEY_PARTICIPANTS +
                " FROM " + TABLE_EVENTS +
                " WHERE date(" + KEY_DATE + ") > date('" + formattedDate + "', '+10 days')";

        return db.rawQuery(query, null);
    }

    public Cursor getFeaturedEventsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get the first three events as featured events
        String query = "SELECT " +
                KEY_ID + " AS _id, " +
                KEY_EVENT_NAME + ", " +
                KEY_EVENT + ", " +
                KEY_DATE + ", " +
                KEY_LOCATION + ", " +
                KEY_DESCRIPTION + ", " +
                KEY_GROUP_LINK + ", " +
                KEY_PARTICIPANTS +
                " FROM " + TABLE_EVENTS +
                " ORDER BY " + KEY_DATE + " ASC" +
                " LIMIT 3";

        return db.rawQuery(query, null);
    }

    public void deleteExpiredEvents() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get today's date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);

        // Delete events where the date is less than or equal to today
        db.delete(TABLE_EVENTS, KEY_DATE + " <= date('" + formattedDate + "')", null);

        db.close();
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

    public String getIdKey() {
        return KEY_ID;
    }

    public String getGroupLinkKey() {
        return KEY_GROUP_LINK;
    }

    public String getParticipantsKey() {
        return KEY_PARTICIPANTS;
    }
}


