package com.example.campusdiscovery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class YourEventsFragment extends Fragment {

    public YourEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_your_events, container, false);
        ListView lv = (ListView) v.findViewById(R.id.listViewYourEvents);
        ArrayList<Event> eventsRSVPedTo = new ArrayList<>();
        for (Event curr : Database.getDB().getEventList()) {
            if (curr.getRSVPList().containsKey(LoginActivity.getCurrentUser()) && curr.getRSVPList().get(LoginActivity.getCurrentUser()) != RSVPStatus.WILL_NOT_ATTEND) {
                eventsRSVPedTo.add(curr);
            }
        }
        YourEventsAdapter yea = new YourEventsAdapter(getActivity(), eventsRSVPedTo);
        lv.setAdapter(yea);
        return v;
    }
}