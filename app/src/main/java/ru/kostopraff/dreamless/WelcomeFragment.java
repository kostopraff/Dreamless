package ru.kostopraff.dreamless;

import android.animation.LayoutTransition;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment {

    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.welcome_fragment, container, false);
        final ImageView imageView = view.findViewById(R.id.welcome_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = view.findViewById(R.id.welcome_text);
                textView.setVisibility(-1);
                imageView.setClickable(false);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setScaleX((float) 0.7);
                imageView.setScaleY((float) 0.7);
                getFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.bottom_replace, new MainFragment())
                        .commit();
            }
        });

        return view;
    }
}
