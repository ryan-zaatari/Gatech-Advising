package com.example.campusdiscovery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.example.campusdiscovery.databinding.FragmentEventsBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;

    private static Event contextEvent;

    public static Event getContextEvent() {
        return contextEvent;
    }

    public static void setContextEvent(Event contextEvent) {
        EventsFragment.contextEvent = contextEvent;
    }

    int currPage = 1;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.

        String name = "";
        String type = "";
        if (LoginActivity.getCurrentUser() != null) {
            name = LoginActivity.getCurrentUser().getName();
            type = LoginActivity.getCurrentUser().getUserType().toString();
            binding.textViewName.setText("Name: " + name);
            binding.textViewType.setText("Type: " + type);
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }


        binding.buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContextEvent(null);
                NavHostFragment.findNavController(EventsFragment.this)
                        .navigate(R.id.action_SecondFragment_to_create_edit_event, null);
            }
        });
        binding.editTextNumber.setText("1");
        binding.editTextNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int i = Integer.parseInt(editable.toString());
                    int maxpage = Math.max((int)Math.ceil((double) FilterFragment.getFilteredEventList().size() / 10), 1);
                    if (i > maxpage || i < 1) {
                         binding.editTextNumber.setText(currPage + "");
                    } else {
                        currPage = i;
                        setAdapter();
                    }
                }
                catch (Exception e) {
                    binding.editTextNumber.setText(currPage + "");
                }
            }
        });
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maxpage = (int)Math.ceil((double) FilterFragment.getFilteredEventList().size() / 10);
                if (currPage+1 <= maxpage) {
                    currPage++;
                    binding.editTextNumber.setText(currPage + "");
                    setAdapter();
                }
            }
        });
        binding.buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currPage-1 > 0) {
                    currPage--;
                    binding.editTextNumber.setText(currPage + "");
                    setAdapter();
                }
            }
        });
        binding.buttonShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EventsFragment.this)
                        .navigate(R.id.action_SecondFragment_to_mapFragment, null);
            }
        });
        binding.buttonYourEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EventsFragment.this)
                        .navigate(R.id.action_SecondFragment_to_yourEventsFragment, null);
            }
        });
        binding.buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EventsFragment.this)
                        .navigate(R.id.action_SecondFragment_to_filterFragment, null);
            }
        });
        setAdapter();
    }

    public void setAdapter() {
        ArrayList<Event> events = new ArrayList<>();
        List<Event> filteredEvents = FilterFragment.getFilteredEventList();
        for (int i = (currPage - 1) * 10; i < currPage * 10 && i < filteredEvents.size(); i++) {
            events.add(filteredEvents.get(i));
        }
        EventsAdapter adapter = new EventsAdapter(this.getActivity(), this, events);
        binding.eventsListView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}