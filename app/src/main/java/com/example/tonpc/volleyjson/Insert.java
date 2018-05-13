package com.example.tonpc.volleyjson;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Insert extends AppCompatActivity implements View.OnClickListener {
    String url = "http://192.168.1.10:8081/VolleyPHP/insert.php";
    String nameString, addressString;
    EditText e_name, e_address;
    ProgressDialog PD;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        e_name = (EditText) findViewById(R.id.e_name);
        e_address = (EditText) findViewById(R.id.e_address);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                PD.show();
                nameString = e_name.getText().toString();
                addressString = e_address.getText().toString();
                StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PD.dismiss();
                        e_name.setText("");
                        e_address.setText("");
                        Toast.makeText(getApplicationContext(), "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PD.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed!!", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("name", nameString);
                        params.put("address", addressString);
                        return params;
                    }
                };
                MyApplication.getInstance().addToReqQueue(postRequest);
                break;
        }

    }
}
