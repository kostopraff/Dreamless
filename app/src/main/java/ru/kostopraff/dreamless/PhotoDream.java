package ru.kostopraff.dreamless;

import android.graphics.Color;
import android.service.dreams.DreamService;
import android.widget.TextView;

public class PhotoDream extends DreamService {

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFullscreen(true);
        setInteractive(false);

        TextView txtView = new TextView(this);
        setContentView(txtView);
        txtView.setText("Hello DayDream world!");
        txtView.setTextColor(Color.rgb(184, 245, 0));
        txtView.setTextSize(30);
    }
    
}
