package com.example.aitendance;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
private EditText editTextUsername, editTextPassword, editTextEmail, editTextMobile;
private Button buttonRegister;
private ProgressDialog progressDialog;
private TextView textViewLogin;
private static final String TAG = "MainActivity";
private TextView dob;
private Spinner departmentspinner;
private DatePickerDialog.OnDateSetListener DobListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    editTextEmail = (EditText) findViewById(R.id.editTextEmail);
    editTextUsername = (EditText) findViewById(R.id.editTextUsername);
    editTextPassword = (EditText) findViewById(R.id.editTextPassword);
    editTextMobile = (EditText) findViewById(R.id.editTextMobile);
    textViewLogin = (TextView) findViewById(R.id.textViewLogin);
    buttonRegister = (Button) findViewById(R.id.buttonRegister);
    departmentspinner = (Spinner) findViewById(R.id.departmentspinner);
    dob = (TextView) findViewById(R.id.dob);
    progressDialog = new ProgressDialog(this);
    buttonRegister.setOnClickListener(this);
    textViewLogin.setOnClickListener(this);
    dob.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    MainActivity.this,
                    android.R.style.Theme_DeviceDefault_Dialog_MinWidth
                    , DobListener,
                    year, month, day
            );
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    });
    DobListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Log.d(TAG, "onDateSet : mm/dd//yyyy  :" + month + " / " + dayOfMonth + " / " + year);
            month++;
            String date = dayOfMonth +"/"+ month +"/"+ year;
        dob.setText(date);
        }
    };
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1
        , getResources().getStringArray(R.array.department));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentspinner.setAdapter(myAdapter);
    }
    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String pass = editTextPassword.getText().toString().trim();
        final String mobile = editTextMobile.getText().toString().trim();
        final String date = dob.getText().toString().trim();
        final String department =departmentspinner.getSelectedItem().toString().trim();
        progressDialog.setMessage("Registering User");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), "Error is ::"+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", pass);
                params.put("email", email);
                params.put("date", date);
                params.put("department", department);
                params.put("mobile", mobile);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
    @Override
    public void onClick(View view) {
        if(view == buttonRegister)
        { registerUser();
    }
        if(view == textViewLogin){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
