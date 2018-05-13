package com.example.tonpc.volleyjson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_insert, btn_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_insert = (Button) findViewById(R.id.btn_insert);
        btn_view = (Button) findViewById(R.id.btn_view);
        btn_view.setOnClickListener(this);
        btn_insert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_insert:
                Intent loginInsert = new Intent(MainActivity.this, Insert.class);
                startActivity(loginInsert);
                break;
            case R.id.btn_view:
                //Toast.makeText(getApplicationContext(), "Under construction",Toast.LENGTH_LONG).show();
                Intent loginView = new Intent(MainActivity.this, ViewList.class);
                startActivity(loginView);
                break;
        }

    }
}
