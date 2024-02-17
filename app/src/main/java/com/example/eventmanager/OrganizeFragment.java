package com.example.eventmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class OrganizeFragment extends Fragment {

    private EditText txtEventName;
    private AutoCompleteTextView txtEvent;
    private EditText txtLocation;
    private EditText txtDescription;
    private Button btnSubmit;

    private DatabaseHandler databaseHandler;


    public OrganizeFragment() {
        // empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_organize, container, false);

        txtEventName = view.findViewById(R.id.txtEventName);
        txtEvent = view.findViewById(R.id.actvEvent);
        txtLocation = view.findViewById(R.id.txtLocation);
        txtDescription = view.findViewById(R.id.txtDescription);
        btnSubmit = view.findViewById(R.id.btnSubmitEvent);

        databaseHandler = new DatabaseHandler(requireContext());

        btnSubmit.setOnClickListener(v -> {
            // Get data from UI
            String eventName = txtEventName.getText().toString();
            String additionalInfo = txtEvent.getText().toString();
            String location = txtLocation.getText().toString();
            String description = txtDescription.getText().toString();
            //  date and time handling to be added here

            // Create an EventData object
            EventData eventData = new EventData(eventName, additionalInfo, "", location, description);

            // Add the event to the database
            databaseHandler.addEvent(eventData);

            Toast.makeText(requireContext(), "Event added successfully!", Toast.LENGTH_LONG).show();
        });
        // Inflate the layout for this fragment
        return view;
    }
}