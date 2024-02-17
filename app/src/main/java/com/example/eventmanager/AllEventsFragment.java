package com.example.eventmanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;

public class AllEventsFragment extends Fragment {

    private ListView listViewEvents;
    private DatabaseHandler databaseHandler;

    public AllEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allevents, container, false);

        listViewEvents = view.findViewById(R.id.listViewEvents);
        databaseHandler = new DatabaseHandler(requireContext());

        // Fetch data from the database
        Cursor cursor = databaseHandler.getAllEventsCursor();

        // columns to display in the ListView
        String[] columns = {databaseHandler.getEventNameKey(), databaseHandler.getEventKey(), databaseHandler.getDateKey()};

        //views to bind the data
        int[] toViews = {R.id.txtEventNameLi, R.id.txtEventLi, R.id.txtDateLi};

        // SimpleCursorAdapter to populate the ListView
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                requireContext(),
                R.layout.list_item_event,
                cursor,
                columns,
                toViews,
                0
        );

        // Set adapter for the ListView
        listViewEvents.setAdapter(cursorAdapter);

        return view;
    }
}
