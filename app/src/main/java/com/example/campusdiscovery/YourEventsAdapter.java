package com.example.campusdiscovery;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class YourEventsAdapter extends ArrayAdapter<Event> {
    List<Event> events;
    public YourEventsAdapter(@NonNull Activity context, List<Event> eventList) {
        super(context, 0, eventList);
        this.events = eventList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event currEvent = getItem(position);
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View rowView=inflater.inflate(R.layout.fragment_your_events_list_item, null,true);

        TextView descText = (TextView) rowView.findViewById(R.id.txtViewYourEventDescription);
        TextView conflictText = (TextView) rowView.findViewById(R.id.txtViewYourEventConflict);

        descText.setText(currEvent.getTitle() + "\n On " + currEvent.getTime());
        conflictText.setText("");

        for (Event curr : this.events) {
            if (curr != currEvent) {
                if (curr.getTime().equals(currEvent.getTime())) {
                    if (!conflictText.getText().equals("")) {
                        conflictText.setText(conflictText.getText() + ", ");
                    } else {
                        conflictText.setText("Has conflict with events: ");
                    }
                    conflictText.setText(conflictText.getText() + curr.getTitle());
                }
            }
        }

        if (conflictText.getText().equals("")) {
            conflictText.setVisibility(View.GONE);
        }

        return rowView;
    }
}
