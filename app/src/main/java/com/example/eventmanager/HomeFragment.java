package com.example.eventmanager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
public class HomeFragment extends Fragment {

    private ListView upcomingEventsListView;
    private DatabaseHandler databaseHandler;
    private EventCursorAdapter cursorAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        upcomingEventsListView = view.findViewById(R.id.recyclerViewUpcomingEvents);
        databaseHandler = new DatabaseHandler(requireContext());

        // Fetch upcoming events from the database
        Cursor cursor = databaseHandler.getAllUpcomingEventsCursor();

        // Set up the adapter for the ListView using EventCursorAdapter
        cursorAdapter = new EventCursorAdapter(
                requireContext(),
                cursor,
                0
        );

        // Set adapter for the ListView
        upcomingEventsListView.setAdapter(cursorAdapter);

        // Add any other necessary UI updates or actions related to upcoming events

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Close the cursor when the fragment is paused
        if (cursorAdapter != null) {
            cursorAdapter.changeCursor(null);
        }
    }
}
