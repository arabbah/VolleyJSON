package com.example.tonpc.volleyjson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewList extends AppCompatActivity {
    String url = "http://192.168.1.10:8081/VolleyPHP/viewList.php";
    ArrayList<HashMap<String, String>> array_list;
    ProgressDialog PD;
    SimpleAdapter adapter;
    ListView listview = null;

    // JSON Node names
    public static final String ITEM_ID = "id";
    public static final String ITEM_NAME = "name";
    public static final String ITEM_ADDRESS= "address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        listview = (ListView) findViewById(R.id.listview_01);
        array_list = new ArrayList<HashMap<String, String>>();

        ReadDataFromDB();
        
    }

    private void ReadDataFromDB() {
        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.show();
        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        JSONArray ja = response.getJSONArray("orders");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jobj = ja.getJSONObject(i);
                            HashMap<String, String> item = new HashMap<String, String>();
                            item.put("image", Integer.toString(R.drawable.fleche));
                            item.put(ITEM_ID, jobj.getString(ITEM_ID));
                            item.put(ITEM_NAME, jobj.getString(ITEM_NAME));
                            item.put(ITEM_ADDRESS, jobj.getString(ITEM_ADDRESS));
                            array_list.add(item);
                        } // for loop ends
                        String[] from = {"image",ITEM_ID, ITEM_NAME, ITEM_ADDRESS};
                        int[] to = {R.id.imageView1,R.id.item_id ,R.id.item_name, R.id.item_address};
                        adapter = new SimpleAdapter(getApplicationContext(), array_list, R.layout.row_items, from, to);
                        listview.setAdapter(adapter);
                        listview.setOnItemClickListener(new ListitemClickListener());
                        PD.dismiss();
                    } // if ends
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
            }
        });
        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(jreq);
    }
    //On List Item Click move to UpdateDelete Activity
    class ListitemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intentEditDelete = new Intent(ViewList.this, UpdateDelete.class);
            String idString = String.valueOf(adapter.getItemId(position));
            String nameString = String.valueOf(array_list.get(position));
            String addressString = String.valueOf(array_list.get(position));
            intentEditDelete.putExtra("id", array_list.get(position));
            intentEditDelete.putExtra("name", array_list.get(position));
            intentEditDelete.putExtra("address", array_list.get(position));
            startActivity(intentEditDelete);
        }
    }
    public void addData(View view) {
        Intent add_intent = new Intent(ViewList.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(add_intent);
    }
}