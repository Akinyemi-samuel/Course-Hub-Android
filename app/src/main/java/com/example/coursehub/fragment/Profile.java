package com.example.coursehub.fragment;


import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.coursehub.R;
import com.example.coursehub.activity.Login;
import com.example.coursehub.activity.Notification;
import com.example.coursehub.activity.Wellcome;
import com.example.coursehub.databinding.FragmentProfileBinding;


public class Profile extends Fragment {

    FragmentProfileBinding binding;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater);
        // Inflate the layout for this fragment

        sharedPreferences = requireActivity().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String firstName = sharedPreferences.getString("firstName", null);
        String lastName = sharedPreferences.getString("lastName", null);
        String email = sharedPreferences.getString("email", null);

        binding.settingsName.setText(firstName + " " + lastName);
        binding.settingsEmail.setText(email);

        binding.notification.setOnClickListener(n ->{
            startActivity(new Intent(getContext(), Notification.class));
        });

        binding.wishlist.setOnClickListener(n ->{
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new WishList()).commit();
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("You are about to log out.");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.clear().commit();
                        Intent intent = new Intent(getContext(), Login.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });


        assert binding.contact != null;
        binding.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmail("akinyemisamuelayo@gmail.com");
            }
        });

        binding.about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openWebsite("https://github.com/Akinyemi-samuel");
            }
        });



        return binding.getRoot();
    }

    private void openWebsite(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Handle the case where no web browser app is available
            Toast.makeText(getContext(), "No web browser app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void composeEmail(String recipient) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + recipient)); // only email apps should handle this
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Handle the case where no email app is available
            Toast.makeText(getContext(), "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
}