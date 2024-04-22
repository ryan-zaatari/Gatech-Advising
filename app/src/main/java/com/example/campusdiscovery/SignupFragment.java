package com.example.campusdiscovery;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import com.example.campusdiscovery.databinding.FragmentSignupBinding;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // NavHostFragment.findNavController(SignupFragment.this).navigate(R.id.action_FirstFragment_to_mapFragment, null);
        if (LoginActivity.getCurrentUser() != null) {
            NavHostFragment.findNavController(SignupFragment.this)
                    .navigate(R.id.action_FirstFragment_to_SecondFragment, null);
            return;
        }
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.nameEditText.getText().toString();
                String pass = binding.passwordEditText.getText().toString();
                String type = binding.typeSpinner.getSelectedItem().toString();
                if (name.isEmpty()) {
                    Snackbar.make(view, "Name cannot be empty", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if (name.trim().isEmpty()) {
                    Snackbar.make(view, "Name cannot be composed of whitespace only", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    User user = new User(name,pass,UserType.valueOf(type));
                    if (!Database.getDB().addUser(user)) {
                        Snackbar.make(view, "Error saving to database", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }

                    LoginActivity.setCurrentUser(user);

                    NavHostFragment.findNavController(SignupFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment, null);

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}