package ru.kostopraff.dreamless;

import android.os.Build;
import android.service.dreams.DreamService;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

public class PhotoDream extends DreamService {

    private ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFullscreen(true);
        setInteractive(false);

        imageView = new ImageView(this);
        setContentView(imageView);
        imageView.setImageResource(R.drawable.test_squirrel);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

}
