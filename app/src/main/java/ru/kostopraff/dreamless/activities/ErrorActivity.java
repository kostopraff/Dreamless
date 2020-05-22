package ru.kostopraff.dreamless.activities;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import ru.kostopraff.dreamless.R;
import ru.kostopraff.dreamless.fragments.ErrorFragment;

public class ErrorActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.main_browse_fragment, new ErrorFragment()).commit();
    }
}
