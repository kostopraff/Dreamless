package ru.kostopraff.dreamless.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.leanback.app.ErrorSupportFragment;

import java.util.Objects;

import ru.kostopraff.dreamless.R;

public class ErrorFragment extends ErrorSupportFragment {
    private static final String TAG = ErrorFragment.class.getSimpleName();
    private static final boolean TRANSLUCENT = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setErrorContent();
    }

    private void setErrorContent() {
        setImageDrawable(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.lb_ic_sad_cloud));
        setMessage(getResources().getString(R.string.error_fragment_message));
        setDefaultBackground(TRANSLUCENT);
        setButtonText(getResources().getString(R.string.dismiss_error));
        setButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Objects.requireNonNull(getFragmentManager()).beginTransaction().remove(ErrorFragment.this).commit();
            }
        });
    }
}
