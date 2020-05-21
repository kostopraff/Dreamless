package ru.kostopraff.dreamless.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import ru.kostopraff.dreamless.R;

public class MainActivity extends FragmentActivity {
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("Dreamless", Context.MODE_PRIVATE);
        if(!preferences.getBoolean("MASTER", false)){
            ComponentName name = new ComponentName("com.android.tv.settings",
                    "com.android.tv.settings.device.display.daydream.DaydreamActivity");
            Intent i=new Intent(Intent.ACTION_MAIN)
                    .addCategory(Intent.CATEGORY_LAUNCHER)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    .setComponent(name);;
            startActivity(i);
        }
        setContentView(R.layout.activity_main);
    }
}
