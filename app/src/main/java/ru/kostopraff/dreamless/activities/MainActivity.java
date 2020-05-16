package ru.kostopraff.dreamless.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.connection.Connection;

import java.io.IOException;

import ru.kostopraff.dreamless.R;

public class MainActivity extends FragmentActivity {
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("Dreamless", Context.MODE_PRIVATE);
        if(!preferences.getBoolean("MASTER", false)){
            startActivity(new Intent(getApplicationContext(), MasterSetupActivity.class));
        }
        setContentView(R.layout.activity_main);
    }
}
