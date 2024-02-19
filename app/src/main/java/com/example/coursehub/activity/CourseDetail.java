package com.example.coursehub.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.coursehub.R;
import com.example.coursehub.adapter.ReviewAdapter;
import com.example.coursehub.databinding.ActivityCourseDetailBinding;
import com.example.coursehub.room.entities.Booking;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.entities.Reviews;
import com.example.coursehub.room.entities.WishList;
import com.example.coursehub.room.viewmodel.BookingViewModel;
import com.example.coursehub.room.viewmodel.CourseViewModel;
import com.example.coursehub.room.viewmodel.ReviewViewModel;
import com.example.coursehub.room.viewmodel.WishListViewModel;
import com.example.coursehub.service.LoadingDialog;
import com.example.coursehub.service.NetworkUtils;
import com.example.coursehub.service.Pair;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CourseDetail extends AppCompatActivity {

    ActivityCourseDetailBinding binding;

    String Publishablekey = "pk_test_51OjmCdEY3VTC05Kfld1VBV9ciXaGz3TuS1OHEleknQIpY1OJ9Hn1sjyP44yAknNUQfq7fYVgFPVa2cqovlCSmUpx0043EaJYTs";
    String SecretKey = "sk_test_51OjmCdEY3VTC05KfcFv4GAkUqpZePRCzfz5TAqoBeUGIbVrTB84hplVDaIJwt0CxZvjSoaQ95xgLqLjMENHgyQSS00DCu0dnnK";

    String CustomerId;
    String ephericalKey;
    String ClientSecret;

    PaymentSheet paymentSheet;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ReviewAdapter reviewAdapter;

    String token;
    String userId;

    Long courseId;
    CourseViewModel courseViewModel;
    Double price;
    WishListViewModel wishListViewModel;
    BookingViewModel bookingViewModel;

    ReviewViewModel reviewViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        token = sharedPreferences.getString("token", null);
        userId = sharedPreferences.getString("id", "0");

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        wishListViewModel = new ViewModelProvider(this).get(WishListViewModel.class);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        courseId = getIntent().getLongExtra("courseId", 0);

        reviewAdapter = new ReviewAdapter(getApplicationContext(), reviewViewModel, this);



        PaymentConfiguration.init(this, Publishablekey);

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

            onPaymentResult(paymentSheetResult);
        });

        new StripePayment().execute();


        courseViewModel.getCourseById(courseId).observe(this, new Observer<Course>() {
            @Override
            public void onChanged(Course course) {

                Glide.with(getApplicationContext()).load(course.getImageUrl())
                        .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(10)))
                        .into(binding.courseImg);
                binding.description.setText(course.getDescription());
                binding.instructor.setText(course.getInstructor());
                binding.duration.setText(String.format("%s Weeks", course.getDuration()));
                price = course.getPrice();


                String COUNTRY = "US";
                String LANGUAGE = "en";
                String formattedPrice = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(price);
                binding.price.setText(formattedPrice);
                binding.nameOfCourse.setText(course.getName());
                binding.location.setText(course.getLocation());

            }
        });

        binding.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isConnected = NetworkUtils.isNetworkAvailable(getApplicationContext());
                if (!isConnected) {
                    Toast.makeText(getApplicationContext(), "No internet Connection available", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (token == null || token.isEmpty()) {
                    signInDialog();
                    return;
                }



                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        paymentFlow();
                    }
                }, 3000);


            }
        });



        reviewViewModel.getReviewsById(courseId).observe(this, new Observer<List<Reviews>>() {
            @Override
            public void onChanged(List<Reviews> reviews) {

                reviewAdapter.setCategories(reviews);
                binding.reviewCategoryRecyclerview.setAdapter(reviewAdapter);
                binding.reviewCategoryRecyclerview.setHasFixedSize(true);

                if (reviews == null || reviews.isEmpty()) {
                    binding.rating.setText(0);
                    return;
                }

                int totalRatings = 0;
                int sumOfRatings = 0;

                for (Reviews rating : reviews) {
                    sumOfRatings += rating.getRating();
                    totalRatings++;
                }

                double t = (double) sumOfRatings / totalRatings;
                int filledStars = (int) t;
                String a = String.valueOf(t);
                binding.rating.setText(a);
                binding.reviewRating.setText(a);


                for (int i = 0; i < filledStars; i++) {
                    addStar(binding.starsLayout, R.drawable.star); // Add filled stars
                }

            }
        });

        wishListViewModel.isCourseInWishlistCheck(new WishList(Long.parseLong(userId), courseId)).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Drawable expectedDrawable = getResources().getDrawable(R.drawable.favorite); // Replace with your expected image resource

                    // Get the Drawable currently set to the ImageView
                    Drawable actualDrawable = binding.wishlist.getDrawable();

                    // Compare the Drawables
                    if (actualDrawable != null && actualDrawable.getConstantState().equals(expectedDrawable.getConstantState())) {
                        // The image displayed in the ImageView matches the expected image
                        // Your code when condition is true
                    } else {
                        binding.wishlist.setImageDrawable(expectedDrawable);
                    }
                }
            }
        });


        // add course to wishlist by userId
        binding.wishlist.setOnClickListener(n -> {
            boolean isConnected = NetworkUtils.isNetworkAvailable(getApplicationContext());
            if (!isConnected) {
                Toast.makeText(this, "No internet Connection available", Toast.LENGTH_SHORT).show();
                return;
            }

            if (token == null || token.isEmpty()) {
                signInDialog();
                return;
            }

            wishListViewModel.insert(new WishList(Long.parseLong(userId), courseId), getApplicationContext(), this);

        });

    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {

        if (paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();
            Date currentDate = new Date();
            long currentTimeMillis = currentDate.getTime();
            bookingViewModel.insert(new Booking(Long.parseLong(userId), courseId, currentTimeMillis, price), getApplicationContext(), this);
        }else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            // Payment was canceled by the user
            Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            // Payment failed due to an error
            PaymentSheetResult.Failed failedResult = (PaymentSheetResult.Failed) paymentSheetResult;
            String errorMessage = failedResult.getError().getLocalizedMessage();
            Toast.makeText(this, "Payment failed: ", Toast.LENGTH_SHORT).show();
        } else {
            // Unknown result type
            Toast.makeText(this, "Unknown payment result", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBack(View view) {
        onBackPressed();
    }

    private void paymentFlow() {

        paymentSheet.presentWithPaymentIntent(ClientSecret, new PaymentSheet.Configuration("CourseHub", new PaymentSheet.CustomerConfiguration(
                CustomerId,
                ephericalKey
        )));
    }


    public void signInDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" Please sign in to continue...");
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


    private void addStar(LinearLayout layout, int drawableId) {
        ImageView star = new ImageView(this);
        star.setImageResource(drawableId);
        layout.addView(star);
    }



    public void myMethod(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject object = new JSONObject(s);

                            CustomerId = object.getString("id");

                            //Toast.makeText(CourseDetail.this, CustomerId, Toast.LENGTH_SHORT).show();

                            getEmphericalKey();


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(CourseDetail.this, volleyError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                return header;


            }
        };


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void getEmphericalKey() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject object = new JSONObject(s);

                            ephericalKey = object.getString("id");

                            //Toast.makeText(CourseDetail.this, CustomerId, Toast.LENGTH_SHORT).show();

                            getClientSecret(CustomerId, ephericalKey);


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(CourseDetail.this, volleyError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                header.put("Stripe-Version", "2023-10-16");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                return params;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void getClientSecret(String customerId, String ephericalKey) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject object = new JSONObject(s);


                            ClientSecret = object.getString("client_secret");

                            //  Toast.makeText(CourseDetail.this, ClientSecret, Toast.LENGTH_SHORT).show();




                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                String errorMessage = "Error occurred: " + volleyError.getLocalizedMessage();
                Toast.makeText(CourseDetail.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);

                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                params.put("amount", "1000"+"00");
                params.put("currency", "USD");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }


   public class StripePayment extends AsyncTask<Void, Void, Void>{

       @Override
       protected Void doInBackground(Void... voids) {
           myMethod();
           return null;
       }
   }

}