package com.example.asus.veterineradmin.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.asus.veterineradmin.Fragments.HomeFragment;
import com.example.asus.veterineradmin.R;
import com.example.asus.veterineradmin.Utils.ChangeFragments;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChangeFragments changeFragments=new ChangeFragments(MainActivity.this);
        changeFragments.change(new HomeFragment());
    }
}
