package com.example.campusdiscovery;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 */
public class InviteeListFragment extends Fragment {
    private InviteeListAdapter adapter;
    public InviteeListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewMain = inflater.inflate(R.layout.fragment_invitee_list_list, container, false);

        // Set the adapter
        ListView recyclerView = (ListView) viewMain.findViewById(R.id.list_invitee);
        adapter = new InviteeListAdapter(getActivity(), EventsFragment.getContextEvent().getInvitees());
        recyclerView.setAdapter(adapter);

        EditText toBeInvited = (EditText) viewMain.findViewById(R.id.editTextInvitee);
        Button inviteButton = (Button) viewMain.findViewById(R.id.inviteButton);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User u = Database.getDB().findUserByUsername(toBeInvited.getText().toString());
                if (u == null) {
                    Toast.makeText(getContext(), "User not found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (EventsFragment.getContextEvent().invite(u, LoginActivity.getCurrentUser())) {
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "User invited successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "User not invited due to an error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RadioButton rbInviteOnly = (RadioButton) viewMain.findViewById(R.id.radioInviteOnly);
        RadioButton rbAnyoneCanInvite = (RadioButton) viewMain.findViewById(R.id.radioAnyoneCanRSVP);

        rbInviteOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbInviteOnly.isChecked()) {
                    if (EventsFragment.getContextEvent().setInviteOnly(true, LoginActivity.getCurrentUser())) {
                        Toast.makeText(getContext(), "Set to invite only successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                        if (EventsFragment.getContextEvent().isInviteOnly()) {
                            rbAnyoneCanInvite.setChecked(false);
                            rbInviteOnly.setChecked(true);
                        } else {
                            rbAnyoneCanInvite.setChecked(true);
                            rbInviteOnly.setChecked(false);
                        }
                    }
                }
            }
        });

        rbAnyoneCanInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbAnyoneCanInvite.isChecked()) {
                    if (EventsFragment.getContextEvent().setInviteOnly(false, LoginActivity.getCurrentUser())) {
                        Toast.makeText(getContext(), "Set to anyone can join successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                        if (EventsFragment.getContextEvent().isInviteOnly()) {
                            rbAnyoneCanInvite.setChecked(false);
                            rbInviteOnly.setChecked(true);
                        } else {
                            rbAnyoneCanInvite.setChecked(true);
                            rbInviteOnly.setChecked(false);
                        }
                    }
                }
            }
        });

        if (EventsFragment.getContextEvent().isInviteOnly()) {
            rbAnyoneCanInvite.setChecked(false);
            rbInviteOnly.setChecked(true);
        } else {
            rbAnyoneCanInvite.setChecked(true);
            rbInviteOnly.setChecked(false);
        }


        return viewMain;
    }
}