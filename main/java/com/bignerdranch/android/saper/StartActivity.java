package com.bignerdranch.android.saper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class StartActivity extends AppCompatActivity {

    Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        buttonStart = (Button)findViewById(R.id.buttStart);
    }
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId()) {
            case  R.id.buttStart:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            break;
            case R.id.buttEnd:
                finish();
                break;
        }
    }
}
