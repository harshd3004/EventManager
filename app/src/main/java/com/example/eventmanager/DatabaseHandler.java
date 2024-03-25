package com.example.eventmanager;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
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
    private static final String KEY_FORM_LINK = "formlink";

    Context context;

    // SQL query with _id as an alias for the primary key column
    private static final String SELECT_ALL_EVENTS = "SELECT " +
            KEY_ID + " AS _id, " +
            KEY_EVENT_NAME + ", " +
            KEY_EVENT + ", " +
            KEY_DATE + ", " +
            KEY_LOCATION + ", " +
            KEY_DESCRIPTION + ", " +
            KEY_GROUP_LINK + ", " +
            KEY_FORM_LINK +
            " FROM " + TABLE_EVENTS;

    // SQL query with _id as an alias for the primary key column for the students table

    private static final String SELECT_EVENT_BY_ID = "SELECT * FROM " + TABLE_EVENTS +
            " WHERE " + KEY_ID + " = ?";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    //

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
                + KEY_FORM_LINK + " TEXT" + ")";
        db.execSQL(CREATE_EVENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }


    // Fetch all events cursor
    public Cursor getAllEventsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(SELECT_ALL_EVENTS, null);
    }

    //fetch all events from FB


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
                KEY_FORM_LINK +
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
                KEY_FORM_LINK +
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
        return KEY_FORM_LINK;
    }


    //Below section for Firebase
    public void addEventFB(EventData event){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("EventsData");
        String key = eventsRef.push().getKey();
        event.setFbId(key);
        eventsRef.child(key).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Firebase","Inserted event data");
            }
        });
        showEventKeyDialogue(key);
    }

    private void showEventKeyDialogue(String key) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.evekey_dialogue);
        TextView message =  dialog.findViewById(R.id.msg);
        message.setText("Importatnt! \n Copy this Event key for further access to your event");
        EditText etkey = dialog.findViewById(R.id.key);
        etkey.setText(key);
        Button btn = dialog.findViewById(R.id.btnAction);
        btn.setText("Copy");
        dialog.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EventKey", key);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Event ID copied to clipboard", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

    }

    public void addParticipant(String eveKey,ParticipantData participant){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference participantsRef = database.getReference("Participants");
        String ekey = eveKey;

        DatabaseReference eveRef =  participantsRef.child(ekey);

        String pkey = eveRef.push().getKey();

        eveRef.child(pkey).setValue(participant).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Firebase","Inserted participant data");
            }
        });

    };


    //method


    public ArrayList<ParticipantData> getAllParticipantList(String eveKey){
        ArrayList<ParticipantData> participants = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference participantRef = database.getReference("Participants").child(eveKey);

        participantRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ParticipantData participant = dataSnapshot.getValue(ParticipantData.class);
                    participants.add(participant);
                }
                Log.d("Firebase","Retrived participants : "+participants.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase","Error while retrieving");
            }
        });
        return participants;
    }


    // Method to get event details by eventId
//    public void getEventDetails(String eventId) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference eventsRef = database.getReference("EventsData");
//        final EventData[] eventData = {new EventData()};
//        Log.d("Firebase","Id : "+eventId);
//        eventsRef.child(eventId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                eventData[0] = snapshot.getValue(EventData.class);
//                Log.d("Firebase","Single event data received");
//                new EventRegisterActivity().showEventData(eventData[0]);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("Firebase","Error while retrieving");
//            }
//        });
//    }
}