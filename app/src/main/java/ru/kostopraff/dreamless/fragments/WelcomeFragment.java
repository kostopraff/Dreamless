package ru.kostopraff.dreamless.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import ru.kostopraff.dreamless.DreamPicsum;
import ru.kostopraff.dreamless.R;
import ru.kostopraff.dreamless.fragments.MainFragment;

public class WelcomeFragment extends Fragment {

    public WelcomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.welcome_fragment, container, false);
        final ImageView imageView = view.findViewById(R.id.welcome_image);
        final View.OnClickListener def = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_DREAM_SETTINGS));
            }
        };
        View.OnClickListener welcome = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = view.findViewById(R.id.welcome_text);
                textView.setVisibility(View.INVISIBLE);
                ObjectAnimator.ofFloat(imageView, imageView.SCALE_Y, 1f, 0.7f).start();
                ObjectAnimator.ofFloat(imageView, imageView.SCALE_X, 1f, 0.7f).start();
                getFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.bottom_replace, new MainFragment())
                        .commit();
                imageView.setOnClickListener(def);
            }
        };
        imageView.setOnClickListener(welcome);

        return view;
    }
}
