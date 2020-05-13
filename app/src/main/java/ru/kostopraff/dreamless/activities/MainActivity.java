package ru.kostopraff.dreamless.activities;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import ru.kostopraff.dreamless.R;

public class MainActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
