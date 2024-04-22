package com.example.campusdiscovery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

public class CreateOrEditFragment extends Fragment {

    public CreateOrEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean isEditing = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button saveButton = (Button)view.findViewById(R.id.buttonAddCreate);
        EditText desc = (EditText)view.findViewById(R.id.editTextDescription);
        EditText date = (EditText)view.findViewById(R.id.editTextDate);
        EditText title = (EditText)view.findViewById(R.id.editTextTitle);
        Spinner loc = (Spinner)view.findViewById(R.id.editTextLocation);
        loc.setAdapter(new ArrayAdapter<String>(getActivity(), android.support.design.R.layout.support_simple_spinner_dropdown_item, new ArrayList<>(Database.getDB().getLocationList().keySet())));
        Event event = EventsFragment.getContextEvent();
        loc.setSelection(0);
        if (event != null) {
            isEditing = true;
            desc.setText(event.getDescription());
            date.setText(event.getTime());
            title.setText(event.getTitle());
            int i = 0;
            for (String curr : Database.getDB().getLocationList().keySet()) {
                if (curr.equals(event.getLocation())) {
                    loc.setSelection(i);
                    break;
                }
                i++;
            }
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditing) {
                    boolean timeUpdated = event.setTime(date.getText().toString(), LoginActivity.getCurrentUser());
                    if (!timeUpdated) {
                        Toast.makeText(getContext(), "Unauthorized!", Toast.LENGTH_SHORT).show();
                    } else {
                        event.setTitle(title.getText().toString(), LoginActivity.getCurrentUser());
                        event.setDescription(desc.getText().toString(), LoginActivity.getCurrentUser());
                        event.setLocation(loc.getSelectedItem().toString(), LoginActivity.getCurrentUser());
                        Toast.makeText(getContext(), "Event Updated!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Event newEvent = new Event(title.getText().toString(), LoginActivity.getCurrentUser(), desc.getText().toString(), loc.getSelectedItem().toString(), date.getText().toString());
                    if (Database.getDB().createEvent(newEvent)) {
                        Toast.makeText(getContext(), "Event Created!", Toast.LENGTH_SHORT).show();
                    }
                }
                NavHostFragment.findNavController(CreateOrEditFragment.this).popBackStack();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_edit_event, container, false);
    }
}