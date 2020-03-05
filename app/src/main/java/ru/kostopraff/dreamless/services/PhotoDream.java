package ru.kostopraff.dreamless.services;

import android.os.Build;
import android.service.dreams.DreamService;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import ru.kostopraff.dreamless.R;

public class PhotoDream extends DreamService {

    private ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFullscreen(true);
        setInteractive(false);
        setContentView(R.layout.dream_photo);

        imageView = findViewById(R.id.dream_photo);
        imageView.setImageResource(R.drawable.test_squirrel);
    }

}
