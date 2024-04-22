package com.example.campusdiscovery;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 */
public class RSVPListFragment extends Fragment {
    private RSVPListAdapter adapter;
    public RSVPListFragment() {
        //setRSVPFilter(RSVPStatus.WILL_ATTEND);
    }

    public void setRSVPFilter(RSVPStatus state) {
        ArrayList<Map.Entry<User,RSVPStatus>> filtered = new ArrayList<>();
        for (Map.Entry<User, RSVPStatus> x : EventsFragment.getContextEvent().getRSVPList().entrySet()) {
            if (x.getValue() == state) {
                filtered.add(x);
            }
        }
        if (adapter == null) {
            adapter = new RSVPListAdapter(getActivity(), filtered);
        } else {
            adapter.clear();
            adapter.addAll(filtered);
            adapter.notifyDataSetChanged();
        }
        adapter.setHost(EventsFragment.getContextEvent().getHost() == LoginActivity.getCurrentUser());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewMain = inflater.inflate(R.layout.fragment_rsvp_list_list, container, false);

        // Set the adapter
        ListView recyclerView = (ListView) viewMain.findViewById(R.id.list_rsvp);
        setRSVPFilter(RSVPStatus.WILL_ATTEND);
        recyclerView.setAdapter(adapter);

        // Add radio buttons for filtering (choosing which kind of RSVP state to show)
        Context context = viewMain.getContext();
        LinearLayout linearLayout = (LinearLayout) viewMain.findViewById(R.id.radioFilterLayout);
        RadioGroup group = new RadioGroup(context);
        for (int i = 0; i < RSVPStatus.values().length; i++) {
            RadioButton btn = new RadioButton(context);
            btn.setText(RSVPStatus.values()[i].toString());
            btn.setTag(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((RadioButton)view).isChecked()) {
                        setRSVPFilter(RSVPStatus.values()[(int)view.getTag()]);
                    }
                }
            });
            group.addView(btn);
            if (i == 0) {
                btn.setChecked(true);
            }
        }
        linearLayout.addView(group);

        return viewMain;
    }
}