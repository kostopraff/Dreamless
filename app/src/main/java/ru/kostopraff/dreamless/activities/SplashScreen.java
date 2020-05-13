package ru.kostopraff.dreamless.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, MainActivity.class));
        finish();

        /*Intent intentDream = new Intent(Intent.ACTION_MAIN);
        intentDream.setClassName("com.android.systemui", "com.android.systemui.Somnambulator");
        startActivity(intentDream);*/
    }
}
