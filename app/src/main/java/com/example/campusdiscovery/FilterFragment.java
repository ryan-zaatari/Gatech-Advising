package com.example.campusdiscovery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilterFragment extends Fragment {

    private static boolean enableNameFilter;
    private static boolean enableHostFilter;
    private static boolean enableLocationFilter;
    private static String hostFilter = "";
    private static String locationFilter = "";
    private static String nameFilter = "";

    public static List<Event> getFilteredEventList() {
        ArrayList<Event> ret = new ArrayList<>();
        for (Event curr : Database.getDB().getEventList()) {
            if (enableNameFilter) {
                if (!curr.getTitle().toLowerCase().startsWith(nameFilter.toLowerCase())) {
                    continue;
                }
            }
            if (enableHostFilter) {
                if (!curr.getHost().getName().equals(hostFilter)) {
                    continue;
                }
            }
            if (enableLocationFilter) {
                if (!curr.getLocation().equals(locationFilter)) {
                    continue;
                }
            }
            ret.add(curr);
        }
        return ret;
    }

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_filter, container, false);
        CheckBox chkNameFilter = (CheckBox) v.findViewById(R.id.chkEnableNameFilter);
        CheckBox chkHostFilter = (CheckBox) v.findViewById(R.id.chkBoxHostFilter);
        CheckBox chkLocationFilter = (CheckBox) v.findViewById(R.id.chkBoxLocationFilter);
        EditText editTextNameFilter = (EditText) v.findViewById(R.id.editTextNameFilter);
        EditText editTextHostFilter = (EditText) v.findViewById(R.id.editTextHostFilter);
        EditText editTextLocationFilter = (EditText) v.findViewById(R.id.editTextLocationFilter);
        if (enableNameFilter) {
            chkNameFilter.setChecked(true);
        }
        if (enableHostFilter) {
            chkHostFilter.setChecked(true);
        }
        if (enableLocationFilter) {
            chkLocationFilter.setChecked(true);
        }
        editTextNameFilter.setText(nameFilter);
        editTextHostFilter.setText(hostFilter);
        editTextLocationFilter.setText(locationFilter);
        chkNameFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                enableNameFilter = b;
            }
        });
        chkHostFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                enableHostFilter = b;
            }
        });
        chkLocationFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                enableLocationFilter = b;
            }
        });
        editTextHostFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                hostFilter = editable.toString();
            }
        });
        editTextLocationFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                locationFilter = editable.toString();
            }
        });
        editTextNameFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                nameFilter = editable.toString();
            }
        });
        return v;
    }
}