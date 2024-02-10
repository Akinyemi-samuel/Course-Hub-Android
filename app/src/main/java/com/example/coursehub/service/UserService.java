package com.example.coursehub.service;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coursehub.databinding.ActivityLoginBinding;
import com.example.coursehub.databinding.ActivityRegistrationBinding;
import com.example.coursehub.environemnt.Environment;
import com.example.coursehub.room.entities.Course;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserService extends AsyncTask<Pair<Context, String, Response.Listener<JSONObject> , Response.ErrorListener  >, Void, Void> {

    private final ValidateInputField validateInputField = new ValidateInputField();


    public void RegisterUser(Context context, JSONObject jsonObject, ActivityRegistrationBinding binding, Dialog registrationDialog) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.createLoadingDialgg();
        loadingDialog.OpenlogoutDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final String url = Environment.getBaseUrl()+"auth";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e(TAG, "Registered user:{}  ");
                Toast.makeText(context, "Registration Successful", Toast.LENGTH_LONG).show();
                loadingDialog.dismissDialog();
                registrationDialog.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String responseString = new String(networkResponse.data, StandardCharsets.UTF_8);
                    Log.e("ErrorResponse", "Response data: " + responseString);
                    binding.errormsg.setText(responseString);
                    loadingDialog.dismissDialog();
                    return;
                }
                binding.errormsg.setText("An Error has occurred");
                Log.e(TAG, "Volley Error: " + volleyError.getMessage());
                loadingDialog.dismissDialog();
                return;
            }
        });

        requestQueue.add(jsonObjectRequest);

    }


    public void UserLogin(EditText em, EditText pw, Context context, ActivityLoginBinding binding, Response.Listener<JSONObject> response ){

        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.createLoadingDialgg();
        loadingDialog.OpenlogoutDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

       final String url = Environment.getBaseUrl()+"auth/login";

        final String email = validateInputField.apply(em);
        final String password = validateInputField.apply(pw);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                response.onResponse(jsonObject);
                Log.e("LOGIN TAG", "Login successful:{}  ");
                Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show();
                loadingDialog.dismissDialog();
                return;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String responseString = new String(networkResponse.data, StandardCharsets.UTF_8);
                    Log.e("ErrorResponse", "Response data: " + responseString);
                    binding.errormsg.setText(responseString);
                    loadingDialog.dismissDialog();
                    return;
                }
                binding.errormsg.setText("An Error has occurred");
                Log.e("LOGIN ERROR ", Objects.requireNonNull(volleyError.getMessage()));
                loadingDialog.dismissDialog();
                return;
            }
        });

        requestQueue.add(jsonObjectRequest);
    }



    public void getOtPForPasswordForgot(String email, Context context, Response.Listener<String> response){

        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.createLoadingDialgg();
        loadingDialog.OpenlogoutDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final String url = Environment.getBaseUrl()+"user/passwordreset/" + email + "/verify";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                response.onResponse(s);
                Log.e("OTP TAG", "OTP sent successful:{}  ");
                loadingDialog.dismissDialog();
                return;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String responseString = new String(networkResponse.data, StandardCharsets.UTF_8);
                    Log.e("ErrorResponse", "Response data: " + responseString);
                    Toast.makeText(context, responseString, Toast.LENGTH_LONG).show();
                    loadingDialog.dismissDialog();
                    return;
                }
                Toast.makeText(context, "An error has occurred", Toast.LENGTH_LONG).show();
                Log.e("LOGIN ERROR ", Objects.requireNonNull(volleyError.getMessage()));
                loadingDialog.dismissDialog();
                return;
            }
        });

        requestQueue.add(stringRequest);
    }


    public void verifyOtPForPasswordForgot(String token, String email, Context context, Response.Listener<String> response){

        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.createLoadingDialgg();
        loadingDialog.OpenlogoutDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final String url = Environment.getBaseUrl()+"user/passwordreset/" + email + "/" + token;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, s -> {
            response.onResponse(s);
            Log.e("OTP TAG", "OTP verified successful:{}  ");
            loadingDialog.dismissDialog();
            return;
        }, volleyError -> {
            NetworkResponse networkResponse = volleyError.networkResponse;
            if (networkResponse != null && networkResponse.data != null) {
                String responseString = new String(networkResponse.data, StandardCharsets.UTF_8);
                Log.e("ErrorResponse", "Response data: " + responseString);
                Toast.makeText(context, responseString, Toast.LENGTH_LONG).show();
                loadingDialog.dismissDialog();
                return;
            }
            Toast.makeText(context, "An error has occurred", Toast.LENGTH_LONG).show();
            Log.e("LOGIN ERROR ", Objects.requireNonNull(volleyError.getMessage()));
            loadingDialog.dismissDialog();
            return;
        });

        requestQueue.add(stringRequest);
    }


    public void changePasswordForPasswordForgot(String password, String email, Context context, Response.Listener<String> response){

        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.createLoadingDialgg();
        loadingDialog.OpenlogoutDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final String url = Environment.getBaseUrl()+"user/passwordreset/" + email + "/" + password + "/p";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, s -> {
            response.onResponse(s);
            Log.e("OTP TAG", "Password Updated successful:{}  ");
            loadingDialog.dismissDialog();
            return;
        }, volleyError -> {
            NetworkResponse networkResponse = volleyError.networkResponse;
            if (networkResponse != null && networkResponse.data != null) {
                String responseString = new String(networkResponse.data, StandardCharsets.UTF_8);
                Log.e("ErrorResponse", "Response data: " + responseString);
                Toast.makeText(context, responseString, Toast.LENGTH_LONG).show();
                loadingDialog.dismissDialog();
                return;
            }
            Toast.makeText(context, "An error has occurred", Toast.LENGTH_LONG).show();
            Log.e("PASSWORD CHANGE ERROR ", Objects.requireNonNull(volleyError.getMessage()));
            loadingDialog.dismissDialog();
            return;
        });

        requestQueue.add(stringRequest);
    }

    private void getUserDetails(Context context, String token, Response.Listener<JSONObject> response, Response.ErrorListener err) {

       final String userDetailsUrl = Environment.getBaseUrl() + "auth/userdetails";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, userDetailsUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                response.onResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                err.onErrorResponse(volleyError);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;

            }
        };
        requestQueue.add(jsonObjectRequest);
    }



    @Override
    protected Void doInBackground(Pair<Context, String, Response.Listener<JSONObject>, Response.ErrorListener>... pairs) {
        Context context = pairs[0].first;
        String token = pairs[0].second;
        Response.Listener<JSONObject> response = pairs[0].third;
        Response.ErrorListener err = pairs[0].fourth;
        getUserDetails(context, token, response ,err);
        return null;
    }
}
