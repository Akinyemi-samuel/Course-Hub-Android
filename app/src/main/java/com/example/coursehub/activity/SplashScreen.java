package com.example.coursehub.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.coursehub.MainActivity;
import com.example.coursehub.R;
import com.example.coursehub.databinding.ActivitySplashScreenBinding;
import com.example.coursehub.service.Pair;
import com.example.coursehub.service.UserService;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {

    ActivitySplashScreenBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        token = sharedPreferences.getString("token", null);


        TranslateAnimation animation = new TranslateAnimation(-1000, 0, 0, 0);
        TranslateAnimation animation2 = new TranslateAnimation(1000, 0, 0, 0);
        animation.setDuration(1000); // Animation duration in milliseconds
        animation2.setDuration(1200); // Animation duration in milliseconds
        binding.img.setAnimation(animation);
        binding.txt.setAnimation(animation2);
        // Start the animation
        binding.img.startAnimation(animation);
        binding.txt.startAnimation(animation2);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.img.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.txt.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        //creates a timer of 3 seconds before moving to next activity
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (token == null || token.isEmpty()) {
                    startActivity(new Intent(getApplicationContext(), Wellcome.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    finish();
                }else{
                    new UserService().execute(new Pair<>(getApplicationContext(), token, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            editor.clear().commit();
                            startActivity(new Intent(getApplicationContext(), Wellcome.class));
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                            finish();
                        }
                    }));
                }

            }
        }, 3000);

    }
}