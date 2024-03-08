package com.example.eventmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class AllEventsFragment extends Fragment {

    private ListView listViewEvents;
    private DatabaseHandler databaseHandler;
    Cursor cursor;

    public AllEventsFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allevents, container, false);

        listViewEvents = view.findViewById(R.id.listViewEvents);
        databaseHandler = new DatabaseHandler(requireContext());

        databaseHandler.deleteExpiredEvents();
        // Fetch data from the database
        cursor = databaseHandler.getAllEventsCursor();

        EventCursorAdapter cursorAdapter = new EventCursorAdapter(
                requireContext(),
                cursor,
                0
        );

        // Set adapter for the ListView
        listViewEvents.setAdapter(cursorAdapter);

        // Set item click listener
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the event ID from the invisible TextView
                TextView eveIdTextView = view.findViewById(R.id.eveId);
                int eventId = Integer.parseInt(eveIdTextView.getText().toString());

                // Create an intent to open EventRegisterActivity
                Intent intent = new Intent(requireContext(), EventRegisterActivity.class);
                intent.putExtra("eventId", eventId);

                Toast.makeText(getActivity().getApplicationContext(), "Starting intent", Toast.LENGTH_LONG).show();
                // Start the activity
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
