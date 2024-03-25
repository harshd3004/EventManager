package com.example.eventmanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrganizeFragment extends Fragment {

    private EditText txtEventName;
    private AutoCompleteTextView txtEvent;
    private EditText txtLocation, txtDescription, txtGroupLink;
    private Button btnSubmit,btnPickDate,btnPickTime;
    private Calendar selectedDateAndTime;
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
        txtGroupLink = view.findViewById(R.id.txtWhatsAppLink);
        btnSubmit = view.findViewById(R.id.btnSubmitEvent);
        btnPickDate = view.findViewById(R.id.btnPickDate);
        btnPickTime = view.findViewById(R.id.btnPickTime);

        databaseHandler = new DatabaseHandler(requireContext());

        selectedDateAndTime = Calendar.getInstance();

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from UI
                String eventName = txtEventName.getText().toString();
                String additionalInfo = txtEvent.getText().toString();
                String location = txtLocation.getText().toString();
                String description = txtDescription.getText().toString();
                String groupLink = txtGroupLink.getText().toString();
                // Format date and time
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String formattedDateAndTime = dateFormat.format(selectedDateAndTime.getTime());

                // Create an EventData object
                EventData eventData = new EventData(eventName, additionalInfo, formattedDateAndTime, location, description,groupLink);

                // Add the event to the database
                //databaseHandler.addEvent(eventData); //sqllite
                databaseHandler.addEventFB(eventData);//firebase
                clearInputs();
                Toast.makeText(requireContext(), "Event added successfully!", Toast.LENGTH_LONG).show();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDateAndTime.set(Calendar.YEAR, year);
                selectedDateAndTime.set(Calendar.MONTH, monthOfYear);
                selectedDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTimeButtons();
            }
        };

        new DatePickerDialog(
                requireContext(),
                dateSetListener,
                selectedDateAndTime.get(Calendar.YEAR),
                selectedDateAndTime.get(Calendar.MONTH),
                selectedDateAndTime.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateAndTime.set(Calendar.MINUTE, minute);
                updateDateTimeButtons();
            }
        };

        new TimePickerDialog(
                requireContext(),
                timeSetListener,
                selectedDateAndTime.get(Calendar.HOUR_OF_DAY),
                selectedDateAndTime.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(requireContext())
        ).show();
    }

    private void updateDateTimeButtons() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDateAndTime.getTime());
        btnPickDate.setText(formattedDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String formattedTime = timeFormat.format(selectedDateAndTime.getTime());
        btnPickTime.setText(formattedTime);
    }

    private void clearInputs(){
        txtEventName.setText("");
        txtEvent.setText("");
        txtLocation.setText("");
        txtDescription.setText("");
        txtGroupLink.setText("");
        btnPickDate.setText("Pick Date");
        btnPickTime.setText("Pick Time");
    }
}