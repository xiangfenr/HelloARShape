package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.ar.sceneform.ux.ArFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.sphere).setOnClickListener(this);
        findViewById(R.id.cylinder).setOnClickListener(this);
        findViewById(R.id.cube).setOnClickListener(this);
        findViewById(R.id.texture).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sphere:
                startARactivity(1);
                break;
            case R.id.cylinder:
                startARactivity(2);
                break;
            case R.id.cube:
                startARactivity(3);
                break;
            case R.id.texture:
                startARactivity(4);
                break;
        }
    }

    private void startARactivity(int position) {
        Intent intent = new Intent(this, ARActivity.class);
        intent.putExtra("order", position);
        startActivity(intent);
    }
}
