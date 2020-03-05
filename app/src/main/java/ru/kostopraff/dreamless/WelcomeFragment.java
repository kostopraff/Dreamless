package ru.kostopraff.dreamless;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeFragment extends Fragment {

    public WelcomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.welcome_fragment, container, false);
        final ImageView imageView = view.findViewById(R.id.welcome_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = view.findViewById(R.id.welcome_text);
                textView.setVisibility(View.INVISIBLE);
                imageView.setClickable(false);
                imageView.setFocusable(false);
                ObjectAnimator.ofFloat(imageView, imageView.SCALE_Y, 1f, 0.7f).start();
                ObjectAnimator.ofFloat(imageView, imageView.SCALE_X, 1f, 0.7f).start();
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
