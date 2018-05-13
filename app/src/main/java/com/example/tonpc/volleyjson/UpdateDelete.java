package com.example.tonpc.volleyjson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateDelete extends AppCompatActivity implements View.OnClickListener {
    EditText edit_name, edit_address;
    String idString, nameString, addressString;
    Button btn_edit, btn_delete;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        PD = new ProgressDialog(this);
        PD.setMessage("please wait.....");
        PD.setCancelable(false);
        edit_name = findViewById(R.id.edit_name);
        edit_address = findViewById(R.id.edit_address);
        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        Intent i = getIntent();
        HashMap<String, String> itemID = (HashMap<String, String>) i.getSerializableExtra("id");
        HashMap<String, String> itemNOME = (HashMap<String, String>) i.getSerializableExtra("name");
        HashMap<String, String> itemCOGNOME = (HashMap<String, String>) i.getSerializableExtra("address");
        idString = itemID.get(ViewList.ITEM_ID);
        nameString = itemNOME.get(ViewList.ITEM_NAME);
        addressString = itemCOGNOME.get(ViewList.ITEM_ADDRESS);
        Toast.makeText(getApplicationContext(), addressString, Toast.LENGTH_SHORT).show();
        edit_name.setText(nameString);
        edit_address.setText(addressString);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit:
                PD.show();
                String name = edit_name.getText().toString();
                String address = edit_address.getText().toString();
                //non tutti gli indirizzi"ip" sono uguali, per sapere il proprio indirizzo ip basta aprire "msdos", scrivere "ipconfig" clikkare "Invio"
                //euscirà il vostro indirizzo ip attuale
                String update_url = "http://192.168.1.12:8081/VolleyPHP/update.php";
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idString);
                params.put("name", name);
                params.put("address", address);
                RequestQueue requestQueue = Volley.newRequestQueue(UpdateDelete.this);
                CustomRequest update_request = new CustomRequest(update_url,
                        params, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), response.names().toString(), Toast.LENGTH_SHORT).show();
                        try {

                            //Toast.makeText(getApplicationContext(), obj.names().get(1).toString(), Toast.LENGTH_SHORT).show();
                            int success = response.getInt("success");
                            if (success == 1) {
                                PD.dismiss();
                                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                // redirect to readdata
                                MoveToReadData();
                            } else {
                                PD.dismiss();
                                Toast.makeText(getApplicationContext(), "failed to update", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            PD.dismiss();
                            Toast.makeText(getApplicationContext(), "erreur", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError response) {
                        Toast.makeText(getApplicationContext(), "fatal", Toast.LENGTH_SHORT).show();
                        PD.dismiss();
                    }
                }){

                };
                // Adding request to request queue
                MyApplication.getInstance().addToReqQueue(update_request);
                //requestQueue.add(update_request);
                break;
            case R.id.btn_delete:
                PD.show();
                //non tutti gli indirizzi"ip" sono uguali, per sapere il proprio indirizzo ip basta aprire "msdos", scrivere "ipconfig" clikkare "Invio"
                //euscirà il vostro indirizzo ip attuale
                String delete_url = "http://192.168.1.10:8081/VolleyPHP/delete.php?id=" + idString;
                JsonObjectRequest delete_request = new JsonObjectRequest(delete_url, null, new Response.Listener<JSONObject>() {
                    //JsonObjectRequest delete_request = new JsonObjectRequest(delete_url,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt("success");
                            if (success == 1) {
                                PD.dismiss();
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                // redirect to readdata
                                MoveToReadData();
                            } else {
                                PD.dismiss();
                                Toast.makeText(getApplicationContext(), "Failed!!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                // Adding request to request queue
                MyApplication.getInstance().addToReqQueue(delete_request);
                break;
        }
    }

    private void MoveToReadData() {
        Intent read_intent = new Intent(UpdateDelete.this, ViewList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(read_intent);
    }
}
