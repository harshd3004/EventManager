package com.example.eventmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllEventsFragment extends Fragment {

    private ListView listViewEvents;
    private DatabaseHandler databaseHandler;
    Cursor cursor;
    ArrayList<EventData> events;
    EventListAdapter adapter;


    public AllEventsFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allevents, container, false);

        listViewEvents = view.findViewById(R.id.listViewEvents);
        databaseHandler = new DatabaseHandler(requireContext());

        databaseHandler.deleteExpiredEvents();

        //Fetch from FB
        events = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("EventsData");

        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    EventData event = dataSnapshot.getValue(EventData.class);
                    events.add(event);
                }

                Log.d("Firebase","Retrived events : "+events.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase","Error while retrieving");
            }
        });
        adapter = new EventListAdapter(getContext(), events);
        listViewEvents.setAdapter(adapter);

        // Set item click listener
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
