package com.example.coursehub;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.coursehub.activity.Login;
import com.example.coursehub.databinding.ActivityMainBinding;
import com.example.coursehub.fragment.Home;
import com.example.coursehub.fragment.Profile;
import com.example.coursehub.fragment.Search;
import com.example.coursehub.fragment.WishList;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", null);

        //sets the default fragment to show on startup
        replaceFragment(new Home());

        if (getIntent().getBooleanExtra("openSearchFragment", false)) {
            // Set the search fragment as selected
            binding.bottomNavigationView.setSelectedItemId(R.id.search);
            replaceFragment(new Search());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.home) {
                    replaceFragment(new Home());
                } else if (item.getItemId() == R.id.search) {
                    replaceFragment(new Search());
                } else if (item.getItemId() == R.id.wishlist) {
                    replaceFragment(new WishList());
                } else if (item.getItemId() == R.id.profile) {
                    if (token == null || token.isEmpty()){
                        AlertDialog();
                    }else {
                        replaceFragment(new Profile());
                    }

                }
                return true;
            }
        });


    }

    public void AlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" Please sign in to continue");
        builder.setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), Login.class));
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

    // method to replace activity background with specified fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment).commit();

    }
}