package com.example.coursehub.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Response;
import com.example.coursehub.MainActivity;
import com.example.coursehub.R;
import com.example.coursehub.databinding.ActivityLoginBinding;
import com.example.coursehub.room.viewmodel.UserViewModel;
import com.example.coursehub.service.UserService;
import com.example.coursehub.service.ValidateInputField;
import com.example.coursehub.validations.IsPasswordValid;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    SharedPreferences sharedPreferences;

    UserViewModel userViewModel;
    SharedPreferences.Editor editor;
    private UserService userService;
    private final ValidateInputField validateInputField = new ValidateInputField();
    boolean passwordVisible;
    CallbackManager callbackManager;
    String otpEmail;
    EditText getEmailOtp;
    BottomSheetDialog dialogOTP;
    BottomSheetDialog dialogVerifyOTP;

    BottomSheetDialog dialogChangePassword;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userService = new UserService(getApplicationContext());

        // initiates facebook login
        RelativeLayout facebookBtn = findViewById(R.id.facebookBtn);
        initiatesFacebookLogin();

        dialogOTP = new BottomSheetDialog(this);
        dialogVerifyOTP = new BottomSheetDialog(this);
        dialogChangePassword = new BottomSheetDialog(this);

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

        binding.loginbtn.setOnClickListener(n -> {
            final String email = validateInputField.apply(binding.email);
            final String password = validateInputField.apply(binding.password);
            binding.errormsg.setVisibility(View.VISIBLE);

            if (email.isEmpty() || password.isEmpty()) {
                binding.errormsg.setText("Empty field");
                return;
            } else {
                proceedLogin();
            }
        });

        createBottomSheetDialogOTP();


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


    private void createBottomSheetDialogOTP() {
        View view = getLayoutInflater().inflate(R.layout.forgot_password_email_check_dialog, null, false);

        ImageView close = view.findViewById(R.id.txt);
        Button getOTP = view.findViewById(R.id.loginbtn);

        // close the dialog box
        close.setOnClickListener(n -> {
            dialogOTP.setDismissWithAnimation(true);
            dialogOTP.dismiss();
        });

        // get the OTP CODE sent to your provided email address
        getOTP.setOnClickListener(n -> {
            getEmailOtp = view.findViewById(R.id.emailOtp);

            otpEmail = validateInputField.apply(getEmailOtp);

            userService.getOtPForPasswordForgot(otpEmail, this, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Toast.makeText(Login.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                    createBottomSheetDialogVerifyOTP();
                    dialogVerifyOTP.show();
                    dialogOTP.dismiss();
                }
            });
        });


        dialogOTP.setContentView(view);
        dialogOTP.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogOTP.getEdgeToEdgeEnabled();
        dialogOTP.setCancelable(false);

    }

    private void createBottomSheetDialogVerifyOTP() {
        View view = getLayoutInflater().inflate(R.layout.forgot_password_otp_verification_dialog, null, false);

        EditText otp1 = view.findViewById(R.id.firstotp);
        EditText otp2 = view.findViewById(R.id.secondotp);
        EditText otp3 = view.findViewById(R.id.thirdotp);
        EditText otp4 = view.findViewById(R.id.fourthotp);
        ImageView close = view.findViewById(R.id.txt);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView resendCode = view.findViewById(R.id.resend_code);
        Button verifyBtn = view.findViewById(R.id.verifybtn);


        // close the dialog box
        close.setOnClickListener(n -> {
            dialogVerifyOTP.setDismissWithAnimation(true);
            dialogVerifyOTP.dismiss();
        });

        // resend the OTP CODE
        resendCode.setOnClickListener(n -> {

            otpEmail = validateInputField.apply(getEmailOtp);

            userService.getOtPForPasswordForgot(otpEmail, this, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Toast.makeText(Login.this, "OTP Re-Sent successfully", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // verify if the OTP CODE is correct with the one sent to the user in email
        verifyBtn.setOnClickListener(n -> {
            final String OTP_CODE = validateInputField.apply(otp1) + validateInputField.apply(otp2) + validateInputField.apply(otp3) + validateInputField.apply(otp4);
            otpEmail = validateInputField.apply(getEmailOtp);

            userService.verifyOtPForPasswordForgot(OTP_CODE, otpEmail, this, s -> {
                Toast.makeText(Login.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                createBottomSheetDialogChangePassword();
                dialogChangePassword.show();
                dialogVerifyOTP.dismiss();
            });

        });

        dialogVerifyOTP.setContentView(view);
        dialogVerifyOTP.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogVerifyOTP.getEdgeToEdgeEnabled();
        dialogVerifyOTP.setCancelable(false);

    }

    private void createBottomSheetDialogChangePassword() {
        View view = getLayoutInflater().inflate(R.layout.forgot_password_change_password_dialog, null, false);

        EditText password1 = view.findViewById(R.id.password);
        EditText password2 = view.findViewById(R.id.password_confirm);

        ImageView close = view.findViewById(R.id.txt);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView errorMsg = view.findViewById(R.id.errormsg);
        Button changePasswordBtn = view.findViewById(R.id.change_password_btn);


        // close the dialog box
        close.setOnClickListener(n -> {
            dialogChangePassword.setDismissWithAnimation(true);
            dialogChangePassword.dismiss();
        });

        // verify if the OTP CODE is correct with the one sent to the user in email
        changePasswordBtn.setOnClickListener(n -> {
            final String password = validateInputField.apply(password1);
            final String confirmPassword = validateInputField.apply(password2);
            otpEmail = validateInputField.apply(getEmailOtp);

            if (!password.equals(confirmPassword)) {
                errorMsg.setText("Password do not match");
                return;
            }

            if (!IsPasswordValid.isPasswordValid(password).isEmpty()) {
                String err = IsPasswordValid.isPasswordValid(password);
                errorMsg.setText(err);
                return;
            }

            userService.changePasswordForPasswordForgot(password, otpEmail, this, s -> {
                Toast.makeText(Login.this, "Password Updated Successfully", Toast.LENGTH_LONG).show();
                dialogChangePassword.dismiss();
            });

        });

        dialogChangePassword.setContentView(view);
        dialogChangePassword.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogChangePassword.getEdgeToEdgeEnabled();
        dialogChangePassword.setCancelable(false);

    }

    public void proceedLogin() {

        try {
            userService.UserLogin(binding.email, binding.password, this, binding, new Response.Listener<JSONObject>() {
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

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToRegisteration(View view) {
        startActivity(new Intent(getApplicationContext(), Registration.class));
    }

    public void forgotPassword(View view) {
        dialogOTP.show();
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
                        Toast.makeText(Login.this, "cancelled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(Login.this, "facebook error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void goBack(View view) {
        onBackPressed();
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

        userService.FacebookLogin(jsonObject, this, binding, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String token = jsonObject.getString("token");
                    editor.putString("token", token).commit();
                    System.out.println(token);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }
}