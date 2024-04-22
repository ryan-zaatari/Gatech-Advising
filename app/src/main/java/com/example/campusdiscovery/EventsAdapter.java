package com.example.campusdiscovery;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {
    private final EventsFragment eventsFragment;
    public EventsAdapter(@NonNull Activity context, EventsFragment eventsFragment, List<Event> eventList) {
        super(context, 0, eventList);
        this.eventsFragment = eventsFragment;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event currEvent = getItem(position);
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View rowView=inflater.inflate(R.layout.eventsview, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.eventTitle);
        TextView hostText = (TextView) rowView.findViewById(R.id.eventHost);
        TextView dateText = (TextView) rowView.findViewById(R.id.eventDate);
        TextView descriptionText = (TextView) rowView.findViewById(R.id.eventDescription);
        TextView locationText = (TextView) rowView.findViewById(R.id.eventLocation);
        TextView headcountText = (TextView) rowView.findViewById(R.id.eventHeadCount);
        TextView capacityText = (TextView) rowView.findViewById(R.id.eventCapacity);
        Button editButton = (Button) rowView.findViewById(R.id.buttonEdit);
        Button deleteButton = (Button) rowView.findViewById(R.id.buttonDelete);
        Button RSVPButton = (Button) rowView.findViewById(R.id.buttonRSVP);
        Button RSVPListButton = (Button) rowView.findViewById(R.id.buttonRSVPList);
        Button inviteeListButton = (Button) rowView.findViewById(R.id.buttonInviteeList);

        titleText.setText(currEvent.getTitle());
        hostText.setText("Hosted by: " + currEvent.getHost().getName());
        dateText.setText("On " + currEvent.getTime().toString());
        locationText.setText("At " + currEvent.getLocation().toString());
        descriptionText.setText("Description:\n" + currEvent.getDescription());
        capacityText.setText("Max Capacity: " + currEvent.getCapacity());
        headcountText.setText("Attending Count: " + currEvent.getAttendingCount());


        editButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        editButton.setHeight(0);
        deleteButton.setHeight(0);
        RSVPButton.setVisibility(View.GONE);
        RSVPListButton.setVisibility(View.GONE);
        RSVPButton.setHeight(0);
        RSVPListButton.setHeight(0);
        inviteeListButton.setVisibility(View.GONE);
        inviteeListButton.setHeight(0);

        hostText.setVisibility(View.GONE);
        dateText.setVisibility(View.GONE);
        descriptionText.setVisibility(View.GONE);
        locationText.setVisibility(View.GONE);
        capacityText.setVisibility(View.GONE);
        headcountText.setVisibility(View.GONE);
        capacityText.setHeight(0);
        headcountText.setHeight(0);
        locationText.setHeight(0);
        descriptionText.setHeight(0);
        dateText.setHeight(0);
        hostText.setHeight(0);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventsFragment.setContextEvent(currEvent);
                NavHostFragment.findNavController(eventsFragment)
                        .navigate(R.id.action_SecondFragment_to_create_edit_event, null);
            }
        });
        RSVPListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventsFragment.setContextEvent(currEvent);
                NavHostFragment.findNavController(eventsFragment)
                        .navigate(R.id.action_SecondFragment_to_RSVPListFragment, null);
            }
        });
        inviteeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventsFragment.setContextEvent(currEvent);
                NavHostFragment.findNavController(eventsFragment)
                        .navigate(R.id.action_SecondFragment_to_inviteeListFragment, null);
            }
        });
        RSVPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] options = new String[RSVPStatus.values().length + 1];
                for (int i = 0; i < RSVPStatus.values().length; i++) {
                    options[i] = RSVPStatus.values()[i].toString();
                }
                options[RSVPStatus.values().length] = "REMOVE_RSVP";
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("RSVP Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == options.length - 1) {
                            if (currEvent.removeRSVP(LoginActivity.getCurrentUser(), LoginActivity.getCurrentUser())){
                                Toast.makeText(getContext(), "RSVP Removed Successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Can't Remove RSVP as you don't have a previous RSVP entry.", Toast.LENGTH_LONG).show();
                            }
                            headcountText.setText("Attending Count: " + currEvent.getAttendingCount());
                            return;
                        }
                        RSVPStatus status = RSVPStatus.values()[which];
                        if (currEvent.RSVP(status, LoginActivity.getCurrentUser())){
                            Toast.makeText(getContext(), "RSVP Successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Can't RSVP, either because you're uninvited or because the event reached maximum capacity.", Toast.LENGTH_LONG).show();
                        }
                        headcountText.setText("Attending Count: " + currEvent.getAttendingCount());
                    }
                });
                builder.show();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Warning")
                        .setMessage("Do you really want to delete this event?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (Database.getDB().deleteEvent(currEvent, LoginActivity.getCurrentUser())) {
                                    Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                                    eventsFragment.setAdapter();
                                } else {
                                    Toast.makeText(getContext(), "Failed to delete!", Toast.LENGTH_SHORT).show();
                                }
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hostText.getVisibility() == View.GONE) {
                    if (Database.getDB().canEditEvent(currEvent, LoginActivity.getCurrentUser())) {
                        editButton.setVisibility(View.VISIBLE);
                        deleteButton.setVisibility(View.VISIBLE);
                        inviteeListButton.setVisibility(View.VISIBLE);
                        inviteeListButton.setHeight(40);

                        editButton.setHeight(40);
                        deleteButton.setHeight(40);
                    }

                    RSVPButton.setVisibility(View.VISIBLE);
                    RSVPListButton.setVisibility(View.VISIBLE);
                    RSVPButton.setHeight(40);
                    RSVPListButton.setHeight(40);

                    hostText.setVisibility(View.VISIBLE);
                    dateText.setVisibility(View.VISIBLE);
                    capacityText.setVisibility(View.VISIBLE);
                    headcountText.setVisibility(View.VISIBLE);
                    descriptionText.setVisibility(View.VISIBLE);
                    locationText.setVisibility(View.VISIBLE);
                    hostText.setHeight(100);
                    dateText.setHeight(100);
                    capacityText.setHeight(100);
                    headcountText.setHeight(100);
                    locationText.setHeight(100);
                    descriptionText.setHeight(300);

                } else {
                    editButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                    editButton.setHeight(0);
                    deleteButton.setHeight(0);
                    RSVPButton.setVisibility(View.GONE);
                    RSVPListButton.setVisibility(View.GONE);
                    RSVPButton.setHeight(0);
                    RSVPListButton.setHeight(0);
                    inviteeListButton.setVisibility(View.GONE);
                    inviteeListButton.setHeight(0);

                    hostText.setVisibility(View.GONE);
                    dateText.setVisibility(View.GONE);
                    descriptionText.setVisibility(View.GONE);
                    locationText.setVisibility(View.GONE);
                    capacityText.setVisibility(View.GONE);
                    headcountText.setVisibility(View.GONE);
                    capacityText.setHeight(0);
                    headcountText.setHeight(0);
                    locationText.setHeight(0);
                    descriptionText.setHeight(0);
                    dateText.setHeight(0);
                    hostText.setHeight(0);
                }

            }
        });

        return rowView;
    }
}
