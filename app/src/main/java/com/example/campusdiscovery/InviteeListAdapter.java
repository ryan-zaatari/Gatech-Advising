package com.example.campusdiscovery;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InviteeListAdapter extends ArrayAdapter<User> {
    private boolean isHost = false;
    public InviteeListAdapter(@NonNull Activity context, List<User> inviteeList) {
        super(context, 0, inviteeList);
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User currUser = getItem(position);
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View rowView=inflater.inflate(R.layout.fragment_invitee_list, null,true);

        TextView userText = (TextView) rowView.findViewById(R.id.inviteeUserText);
        userText.setText("User: " + currUser.getName());
        Button removeButton = (Button) rowView.findViewById(R.id.inviteeRemoveButton);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EventsFragment.getContextEvent().cancelInvitation(currUser, LoginActivity.getCurrentUser())) {
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "User uninvited Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to remove invitation", Toast.LENGTH_SHORT).show();

                }
            }
        });
        return  rowView;
    }
}