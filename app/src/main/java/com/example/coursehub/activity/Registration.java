package com.example.coursehub.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.example.coursehub.MainActivity;
import com.example.coursehub.R;
import com.example.coursehub.databinding.ActivityRegistrationBinding;
import com.example.coursehub.service.NetworkUtils;
import com.example.coursehub.service.UserService;
import com.example.coursehub.service.ValidateInputField;
import com.example.coursehub.validations.EmailValidator;
import com.example.coursehub.validations.IsPasswordValid;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Registration extends AppCompatActivity {

    ValidateInputField validateInputField = new ValidateInputField();
    ActivityRegistrationBinding binding;
    CallbackManager callbackManager;
    boolean passwordVisible;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private UserService userService;
    Dialog dialog;
    Button okBtn;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userService = new UserService(getApplicationContext());

        // initiates facebook login
        RelativeLayout facebookBtn = findViewById(R.id.facebookBtn);
        initiatesFacebookLogin();


        //Setting up registration confirmation dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.successful_registration_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.card_curve));
        dialog.setCancelable(false);
        okBtn = dialog.findViewById(R.id.okbtn);


        binding.registerBtn.setOnClickListener(n -> {
            final String fname = validateInputField.apply(binding.fname);
            final String lname = validateInputField.apply(binding.lname);
            final String email = validateInputField.apply(binding.email);
            final String password = validateInputField.apply(binding.password);
            binding.errormsg.setVisibility(View.VISIBLE);

            if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                binding.errormsg.setText("Empty Fields.");
                return;
            }

            if (!EmailValidator.isEmailValid(email)) {
                binding.errormsg.setText("Invalid email format.");
                return;
            }


            if (!IsPasswordValid.isPasswordValid(password).isEmpty()) {
                String err = IsPasswordValid.isPasswordValid(password);
                binding.errormsg.setText(err);
                return;
            }

            proceedRegistration();


        });

        // success dialog button
        okBtn.setOnClickListener(n -> {
            dialog.dismiss();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        // toggle password visibility
        binding.password.setOnTouchListener((view, motionEvent) -> {
            final int Right = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= binding.password.getRight() - binding.password.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = binding.password.getSelectionEnd();
                    if (passwordVisible) {
                        binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye_off, 0);
                        binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye, 0);
                        binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    binding.password.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        // authenticate the user facebook account using the login manager
        facebookBtn.setOnClickListener(n -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void proceedRegistration() {
        final String firstname = validateInputField.apply(binding.fname);
        final String lastName = validateInputField.apply(binding.lname);
        final String email = validateInputField.apply(binding.email);
        final String password = validateInputField.apply(binding.password);


        boolean isConnected = NetworkUtils.isNetworkAvailable(getApplicationContext());
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), "No internet Connection available", Toast.LENGTH_SHORT).show();
            return;
        }


        JSONObject registrationData = new JSONObject();
        try {

            registrationData.put("firstName", firstname);
            registrationData.put("lastName", lastName);
            registrationData.put("email", email);
            registrationData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        UserService registrationService = new UserService(getApplicationContext());
        registrationService.RegisterUser(this, registrationData, binding, dialog);
    }


    public void goToLogin(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public void initiatesFacebookLogin() {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        AccessToken accessToken = loginResult.getAccessToken();

                        GraphRequest request = GraphRequest.newMeRequest(
                                accessToken,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code

                                        try {

                                            String firstName = object.getString("first_name");
                                            String lastName = object.getString("last_name");
                                            String user_email = object.getString("email");
                                            proceedFacebokLogin(firstName, lastName, user_email);

                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name, last_name ,email");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(Registration.this, "cancelled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(Registration.this, "facebook error", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public void proceedFacebokLogin(String fName, String lName, String email) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("firstName", fName);
            jsonObject.put("lastName", lName);
            jsonObject.put("email", email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        userService.FacebookLogin(jsonObject, this, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String token = jsonObject.getString("token");
                    editor.putString("token", token).commit();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

}