package ru.kostopraff.dreamless.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import ru.kostopraff.dreamless.R;

public class SambaFragment extends Fragment {

    public SambaFragment(){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.samba_fragment, container, false);
        ImageView image = view.findViewById(R.id.img);
        return view;
    }


}
