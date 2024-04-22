package com.example.campusdiscovery;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusdiscovery.databinding.FragmentRsvpListBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RSVPListAdapter extends ArrayAdapter<Map.Entry<User,RSVPStatus>> {
    private boolean isHost = false;
    public RSVPListAdapter(@NonNull Activity context, ArrayList<Map.Entry<User,RSVPStatus>> rsvpList) {
        super(context, 0, rsvpList);
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Map.Entry<User,RSVPStatus> currRSVPState = getItem(position);
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View rowView=inflater.inflate(R.layout.fragment_rsvp_list, null,true);

        TextView statusText = (TextView) rowView.findViewById(R.id.rsvpStatusText);
        TextView userText = (TextView) rowView.findViewById(R.id.rsvpUserText);
        statusText.setText(" Response: " + currRSVPState.getValue().toString());
        userText.setText("User: " + currRSVPState.getKey().getName());
        Button removeButton = (Button) rowView.findViewById(R.id.rsvpRemoveButton);
        if (LoginActivity.getCurrentUser() != currRSVPState.getKey() && !isHost && LoginActivity.getCurrentUser().getUserType() != UserType.Administrator) {
            removeButton.setVisibility(View.GONE);
            removeButton.setHeight(0);
        }
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EventsFragment.getContextEvent().removeRSVP(currRSVPState.getKey(), LoginActivity.getCurrentUser())){
                    Toast.makeText(getContext(), "RSVP Removed Successfully!", Toast.LENGTH_SHORT).show();
                    remove(currRSVPState);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Can't Remove RSVP.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  rowView;
    }
}