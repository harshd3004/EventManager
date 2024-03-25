package com.example.eventmanager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private ListView upcomingEventsListView,featuredEventsListView;
    private DatabaseHandler databaseHandler;
    private EventListAdapter upcomingAdapter;
//    ,featuredAdapter
    private ArrayList<EventData> uevents,fevents;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        upcomingEventsListView = view.findViewById(R.id.liUpcomingEvents);
//        featuredEventsListView = view.findViewById(R.id.liFeaturedEvents);
        databaseHandler = new DatabaseHandler(requireContext());
        databaseHandler.deleteExpiredEvents();

        // Fetch upcoming events from the database
        uevents = new ArrayList<>();
        fevents = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("EventsData");

        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int eventsAdded = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EventData event = dataSnapshot.getValue(EventData.class);
                    if (isEventAfterTenDate(event)) {
                        uevents.add(event);
                    }
                    if(eventsAdded<3){
                        fevents.add(event);
                        eventsAdded++;
                    }
                }

                Log.d("Firebase", "Retrieved upcoming events: " + uevents.size());
                upcomingAdapter.notifyDataSetChanged();
//                featuredAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", "Error while retrieving");
            }
        });
        upcomingAdapter = new EventListAdapter(getContext(), uevents);
        upcomingEventsListView.setAdapter(upcomingAdapter);
//        featuredAdapter = new EventListAdapter(getContext(), fevents);
//        featuredEventsListView.setAdapter(featuredAdapter);

        upcomingEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the event ID from the invisible TextView
                TextView eveIdTextView = view.findViewById(R.id.eveIdLi);
                String eventId = eveIdTextView.getText().toString();

                // Create an intent to open EventRegisterActivity
                Intent intent = new Intent(requireContext(), EventRegisterActivity.class);
                intent.putExtra("eventId", eventId);

                // Start the activity
                startActivity(intent);
            }
        });

//        featuredEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                // Get the event ID from the invisible TextView
//                TextView eveIdTextView = view.findViewById(R.id.eveIdLi);
//                String eventId = eveIdTextView.getText().toString();
//
//                // Create an intent to open EventRegisterActivity
//                Intent intent = new Intent(requireContext(), EventRegisterActivity.class);
//                intent.putExtra("eventId", eventId);
//
//                // Start the activity
//                startActivity(intent);
//            }
//        });
        return view;
    }


    private boolean isEventAfterTenDate(EventData event) {
        // Get the current date and time
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DATE, 5);
        // Parse the event date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date eventDate = dateFormat.parse(event.getEventDate());
            // Compare event date with current date
            return eventDate != null && eventDate.after(currentDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
